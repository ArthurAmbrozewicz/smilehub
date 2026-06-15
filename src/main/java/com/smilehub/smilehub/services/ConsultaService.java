package com.smilehub.smilehub.services;

import com.smilehub.smilehub.dto.CancelarConsultaDTO;
import com.smilehub.smilehub.dto.ConsultaEditarDTO;
import com.smilehub.smilehub.dto.ConsultaRequestDTO;
import com.smilehub.smilehub.dto.ConsultaResponseDTO;
import com.smilehub.smilehub.dto.ConsultaServicoItemDTO;
import com.smilehub.smilehub.entities.Consulta;
import com.smilehub.smilehub.entities.ConsultaServico;
import com.smilehub.smilehub.entities.Dentista;
import com.smilehub.smilehub.entities.MotivoCancelamento;
import com.smilehub.smilehub.entities.Paciente;
import com.smilehub.smilehub.entities.Servico;
import com.smilehub.smilehub.entities.Usuario;
import com.smilehub.smilehub.exception.BusinessException;
import com.smilehub.smilehub.exception.ResourceNotFoundException;
import com.smilehub.smilehub.repositories.ConsultaRepository;
import com.smilehub.smilehub.repositories.ConsultaServicoRepository;
import com.smilehub.smilehub.repositories.UsuarioRepository;
import com.smilehub.smilehub.util.UsuarioDtypeUtil;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConsultaService {

    private static final String STATUS_AGENDADA = "AGENDADA";
    private static final String STATUS_CANCELADA = "CANCELADA";
    private static final String STATUS_FINALIZADA = "FINALIZADA";

    private static final String DTYPE_ADMINISTRADOR = "ADMINISTRADOR";
    private static final String DTYPE_DENTISTA = "DENTISTA";

    private final ConsultaRepository consultaRepository;
    private final ConsultaServicoRepository consultaServicoRepository;
    private final PacienteService pacienteService;
    private final DentistaService dentistaService;
    private final ServicoService servicoService;
    private final UsuarioRepository usuarioRepository;
    private final MotivoCancelamentoService motivoCancelamentoService;

    public ConsultaService(
            ConsultaRepository consultaRepository,
            ConsultaServicoRepository consultaServicoRepository,
            PacienteService pacienteService,
            DentistaService dentistaService,
            ServicoService servicoService,
            UsuarioRepository usuarioRepository,
            MotivoCancelamentoService motivoCancelamentoService
    ) {
        this.consultaRepository = consultaRepository;
        this.consultaServicoRepository = consultaServicoRepository;
        this.pacienteService = pacienteService;
        this.dentistaService = dentistaService;
        this.servicoService = servicoService;
        this.usuarioRepository = usuarioRepository;
        this.motivoCancelamentoService = motivoCancelamentoService;
    }

    @Transactional
    public ConsultaResponseDTO criar(ConsultaRequestDTO request) {
        validarRequest(request);
        validarDatas(request.dataInicio(), request.dataFim());

        if (request.dataInicio().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Data de início não pode ser no passado");
        }

        Paciente paciente = pacienteService.buscarEntidadePorId(request.pacienteId());
        Dentista dentista = dentistaService.buscarEntidadePorId(request.dentistaId());
        Usuario usuario = buscarUsuarioAutenticado();

        if (consultaRepository.existsConflitoHorarioDentista(
                dentista.getId(),
                request.dataInicio(),
                request.dataFim()
        )) {
            throw new BusinessException("Dentista já possui consulta agendada neste horário");
        }

        Consulta consulta = new Consulta(
                paciente,
                dentista,
                usuario,
                null,
                request.descricao(),
                request.dataInicio(),
                request.dataFim(),
                STATUS_AGENDADA
        );

        Consulta consultaSalva = consultaRepository.save(consulta);
        salvarServicos(consultaSalva, request.servicos());

        return montarResponse(consultaSalva);
    }

    @Transactional
    public ConsultaResponseDTO editar(Long id, ConsultaEditarDTO request) {
        validarEditarRequest(request);
        validarDatas(request.dataInicio(), request.dataFim());

        Consulta consulta = buscarEntidadePorId(id);

        if (!STATUS_AGENDADA.equals(consulta.getStatus())) {
            throw new BusinessException("Apenas consultas agendadas podem ser editadas");
        }

        consulta.setDescricao(request.descricao());
        consulta.setDataInicio(request.dataInicio());
        consulta.setDataFim(request.dataFim());

        Consulta consultaSalva = consultaRepository.save(consulta);
        sincronizarServicos(consultaSalva, request.servicos());

        return montarResponse(consultaSalva);
    }

    @Transactional
    public ConsultaResponseDTO cancelar(Long id, CancelarConsultaDTO request) {
        if (request.motivoCancelamentoId() == null) {
            throw new BusinessException("Motivo de cancelamento é obrigatório");
        }

        Consulta consulta = buscarEntidadePorId(id);

        if (STATUS_CANCELADA.equals(consulta.getStatus())) {
            throw new BusinessException("Consulta já está cancelada");
        }
        if (STATUS_FINALIZADA.equals(consulta.getStatus())) {
            throw new BusinessException("Consulta finalizada não pode ser cancelada");
        }

        MotivoCancelamento motivo = motivoCancelamentoService.buscarEntidadePorId(request.motivoCancelamentoId());

        consulta.setStatus(STATUS_CANCELADA);
        consulta.setMotivoCancelamento(motivo);

        return montarResponse(consultaRepository.save(consulta));
    }

    @Transactional
    public ConsultaResponseDTO finalizar(Long id) {
        Consulta consulta = buscarEntidadePorId(id);

        if (!STATUS_AGENDADA.equals(consulta.getStatus())) {
            throw new BusinessException("Apenas consultas agendadas podem ser finalizadas");
        }

        consulta.setStatus(STATUS_FINALIZADA);

        return montarResponse(consultaRepository.save(consulta));
    }

    @Transactional(readOnly = true)
    public List<ConsultaResponseDTO> listar() {
        Usuario usuarioLogado = buscarUsuarioAutenticado();
        String dtype = UsuarioDtypeUtil.resolverDtype(usuarioLogado);

        if (DTYPE_ADMINISTRADOR.equals(dtype)) {
            return consultaRepository.findAll().stream()
                    .map(this::montarResponse)
                    .toList();
        }
        if (DTYPE_DENTISTA.equals(dtype)) {
            return consultaRepository.findByDentistaId(usuarioLogado.getId()).stream()
                    .map(this::montarResponse)
                    .toList();
        }

        throw new BusinessException("Acesso negado");
    }

    @Transactional(readOnly = true)
    public ConsultaResponseDTO buscarPorId(Long id) {
        return montarResponse(buscarEntidadePorId(id));
    }

    @Transactional(readOnly = true)
    public List<ConsultaResponseDTO> listarPorPaciente(Long pacienteId) {
        validarAcessoListarPorPaciente(pacienteId);
        pacienteService.buscarEntidadePorId(pacienteId);

        return consultaRepository.findByPacienteId(pacienteId).stream()
                .map(this::montarResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ConsultaResponseDTO> listarPorDentista(Long dentistaId) {
        validarAcessoListarPorDentista(dentistaId);
        dentistaService.buscarEntidadePorId(dentistaId);

        return consultaRepository.findByDentistaId(dentistaId).stream()
                .map(this::montarResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Consulta buscarEntidadePorId(Long id) {
        return consultaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada: " + id));
    }

    private ConsultaResponseDTO montarResponse(Consulta consulta) {
        List<ConsultaServico> servicos = consultaServicoRepository.findAllByConsultaId(consulta.getId());
        return ConsultaResponseDTO.from(consulta, servicos);
    }

    private void salvarServicos(Consulta consulta, List<ConsultaServicoItemDTO> servicos) {
        if (servicos == null || servicos.isEmpty()) {
            return;
        }

        validarServicos(servicos);

        for (ConsultaServicoItemDTO item : servicos) {
            Servico servico = servicoService.buscarEntidadePorId(item.servicoId());

            if (!servico.isAtivo()) {
                throw new BusinessException("Serviço inativo: " + servico.getNome());
            }

            consultaServicoRepository.save(new ConsultaServico(consulta, servico, item.valor()));
        }
    }

    private void sincronizarServicos(Consulta consulta, List<ConsultaServicoItemDTO> servicos) {
        consultaServicoRepository.deleteByConsultaId(consulta.getId());
        salvarServicos(consulta, servicos);
    }

    private void validarServicos(List<ConsultaServicoItemDTO> servicos) {
        Set<Long> idsServicos = new HashSet<>();

        for (ConsultaServicoItemDTO item : servicos) {
            if (item.servicoId() == null) {
                throw new BusinessException("Serviço é obrigatório");
            }

            if (!idsServicos.add(item.servicoId())) {
                throw new BusinessException("Serviço duplicado na consulta");
            }

            validarValorServico(item.valor());
        }
    }

    private void validarValorServico(BigDecimal valor) {
        if (valor == null) {
            throw new BusinessException("Valor do serviço é obrigatório");
        }

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Valor do serviço deve ser maior que zero");
        }
    }

    private void validarAcessoListarPorPaciente(Long pacienteId) {
        String dtype = UsuarioDtypeUtil.resolverDtype(buscarUsuarioAutenticado());

        if (DTYPE_ADMINISTRADOR.equals(dtype) || DTYPE_DENTISTA.equals(dtype)) {
            return;
        }

        throw new BusinessException("Acesso negado");
    }

    private void validarAcessoListarPorDentista(Long dentistaId) {
        Usuario usuarioLogado = buscarUsuarioAutenticado();
        String dtype = UsuarioDtypeUtil.resolverDtype(usuarioLogado);

        if (DTYPE_ADMINISTRADOR.equals(dtype)) {
            return;
        }
        if (DTYPE_DENTISTA.equals(dtype) && usuarioLogado.getId().equals(dentistaId)) {
            return;
        }

        throw new BusinessException("Acesso negado");
    }

    private Usuario buscarUsuarioAutenticado() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BusinessException("Usuário não autenticado");
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof String email) || email.isBlank()) {
            throw new BusinessException("Usuário não autenticado");
        }

        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }

    private void validarRequest(ConsultaRequestDTO request) {
        if (request.pacienteId() == null) {
            throw new BusinessException("Paciente é obrigatório");
        }
        if (request.dentistaId() == null) {
            throw new BusinessException("Dentista é obrigatório");
        }
        if (request.descricao() == null || request.descricao().isBlank()) {
            throw new BusinessException("Descrição é obrigatória");
        }
        if (request.dataInicio() == null) {
            throw new BusinessException("Data de início é obrigatória");
        }
        if (request.dataFim() == null) {
            throw new BusinessException("Data de fim é obrigatória");
        }
    }

    private void validarEditarRequest(ConsultaEditarDTO request) {
        if (request.descricao() == null || request.descricao().isBlank()) {
            throw new BusinessException("Descrição é obrigatória");
        }
        if (request.dataInicio() == null) {
            throw new BusinessException("Data de início é obrigatória");
        }
        if (request.dataFim() == null) {
            throw new BusinessException("Data de fim é obrigatória");
        }
    }

    private void validarDatas(LocalDateTime dataInicio, LocalDateTime dataFim) {
        if (dataFim.isBefore(dataInicio) || dataFim.isEqual(dataInicio)) {
            throw new BusinessException("Data de fim deve ser posterior à data de início");
        }
    }
}
