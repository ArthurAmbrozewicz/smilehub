package com.smilehub.smilehub.services;

import com.smilehub.smilehub.dto.MedicamentoItemDTO;
import com.smilehub.smilehub.dto.PrescricaoRequestDTO;
import com.smilehub.smilehub.dto.PrescricaoResponseDTO;
import com.smilehub.smilehub.entities.Consulta;
import com.smilehub.smilehub.entities.Medicamento;
import com.smilehub.smilehub.entities.Prescricao;
import com.smilehub.smilehub.entities.Usuario;
import com.smilehub.smilehub.exception.BusinessException;
import com.smilehub.smilehub.exception.ResourceNotFoundException;
import com.smilehub.smilehub.repositories.ConsultaRepository;
import com.smilehub.smilehub.repositories.MedicamentoRepository;
import com.smilehub.smilehub.repositories.PrescricaoRepository;
import com.smilehub.smilehub.repositories.UsuarioRepository;
import com.smilehub.smilehub.util.UsuarioDtypeUtil;
import java.util.List;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PrescricaoService {

    private static final String DTYPE_ADMINISTRADOR = "ADMINISTRADOR";
    private static final String DTYPE_DENTISTA = "DENTISTA";
    private static final String STATUS_AGENDADA = "AGENDADA";
    private static final String STATUS_FINALIZADA = "FINALIZADA";

    private final PrescricaoRepository prescricaoRepository;
    private final MedicamentoRepository medicamentoRepository;
    private final ConsultaRepository consultaRepository;
    private final ConsultaService consultaService;
    private final PacienteService pacienteService;
    private final UsuarioRepository usuarioRepository;

    public PrescricaoService(
            PrescricaoRepository prescricaoRepository,
            MedicamentoRepository medicamentoRepository,
            ConsultaRepository consultaRepository,
            ConsultaService consultaService,
            PacienteService pacienteService,
            UsuarioRepository usuarioRepository
    ) {
        this.prescricaoRepository = prescricaoRepository;
        this.medicamentoRepository = medicamentoRepository;
        this.consultaRepository = consultaRepository;
        this.consultaService = consultaService;
        this.pacienteService = pacienteService;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<PrescricaoResponseDTO> listarPorPacienteId(Long pacienteId) {
        validarAcessoListar();
        pacienteService.buscarEntidadePorId(pacienteId);

        return prescricaoRepository.findAllByPacienteIdOrderByDataEmissaoDesc(pacienteId).stream()
                .map(this::montarResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PrescricaoResponseDTO buscarPorConsultaId(Long consultaId) {
        validarAcessoConsulta(consultaId);

        Prescricao prescricao = prescricaoRepository.findByConsultaId(consultaId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Prescrição não encontrada para a consulta: " + consultaId
                ));

        return montarResponse(prescricao);
    }

    @Transactional
    public PrescricaoResponseDTO salvar(PrescricaoRequestDTO request) {
        validarRequest(request);
        Consulta consulta = consultaRepository.findById(request.consultaId())
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada: " + request.consultaId()));
        validarAcessoConsulta(consulta);

        if (!STATUS_AGENDADA.equals(consulta.getStatus()) && !STATUS_FINALIZADA.equals(consulta.getStatus())) {
            throw new BusinessException("Não é possível prescrever para consulta cancelada");
        }

        Prescricao prescricao = prescricaoRepository.findByConsultaId(consulta.getId())
                .orElseGet(() -> new Prescricao(consulta, request.observacoes()));

        prescricao.setObservacoes(request.observacoes());
        prescricao = prescricaoRepository.save(prescricao);

        medicamentoRepository.deleteByPrescricaoId(prescricao.getId());

        for (MedicamentoItemDTO item : request.medicamentos()) {
            validarMedicamento(item);
            medicamentoRepository.save(new Medicamento(
                    prescricao,
                    item.nome().trim(),
                    item.dosagem().trim(),
                    item.frequencia().trim(),
                    item.duracao().trim()
            ));
        }

        if (STATUS_AGENDADA.equals(consulta.getStatus())) {
            consulta.setStatus(STATUS_FINALIZADA);
            consultaRepository.save(consulta);
        }

        return montarResponse(prescricao);
    }

    private PrescricaoResponseDTO montarResponse(Prescricao prescricao) {
        List<Medicamento> medicamentos = medicamentoRepository.findAllByPrescricaoId(prescricao.getId());
        return PrescricaoResponseDTO.from(prescricao, medicamentos);
    }

    private void validarAcessoListar() {
        Usuario usuario = buscarUsuarioAutenticado();
        String dtype = UsuarioDtypeUtil.resolverDtype(usuario);

        if (DTYPE_ADMINISTRADOR.equals(dtype) || DTYPE_DENTISTA.equals(dtype)) {
            return;
        }

        throw new BusinessException("Acesso negado");
    }

    private void validarAcessoConsulta(Long consultaId) {
        Consulta consulta = consultaService.buscarEntidadePorId(consultaId);
        validarAcessoConsulta(consulta);
    }

    private void validarAcessoConsulta(Consulta consulta) {
        Usuario usuario = buscarUsuarioAutenticado();
        String dtype = UsuarioDtypeUtil.resolverDtype(usuario);

        if (DTYPE_ADMINISTRADOR.equals(dtype)) {
            return;
        }

        if (DTYPE_DENTISTA.equals(dtype) && usuario.getId().equals(consulta.getDentista().getId())) {
            return;
        }

        throw new BusinessException("Acesso negado");
    }

    private void validarRequest(PrescricaoRequestDTO request) {
        if (request == null) {
            throw new BusinessException("Dados da prescrição são obrigatórios");
        }
        if (request.consultaId() == null) {
            throw new BusinessException("Consulta é obrigatória");
        }
        if (request.medicamentos() == null || request.medicamentos().isEmpty()) {
            throw new BusinessException("Informe ao menos um medicamento");
        }
    }

    private void validarMedicamento(MedicamentoItemDTO item) {
        if (item.nome() == null || item.nome().isBlank()) {
            throw new BusinessException("Nome do medicamento é obrigatório");
        }
        if (item.dosagem() == null || item.dosagem().isBlank()) {
            throw new BusinessException("Dosagem é obrigatória");
        }
        if (item.frequencia() == null || item.frequencia().isBlank()) {
            throw new BusinessException("Frequência é obrigatória");
        }
        if (item.duracao() == null || item.duracao().isBlank()) {
            throw new BusinessException("Duração é obrigatória");
        }
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
}
