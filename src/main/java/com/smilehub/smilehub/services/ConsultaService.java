package com.smilehub.smilehub.services;

import com.smilehub.smilehub.dto.ConsultaRequestDTO;
import com.smilehub.smilehub.dto.ConsultaResponseDTO;
import com.smilehub.smilehub.entities.Consulta;
import com.smilehub.smilehub.entities.Dentista;
import com.smilehub.smilehub.entities.MotivoCancelamento;
import com.smilehub.smilehub.entities.Paciente;
import com.smilehub.smilehub.entities.Usuario;
import com.smilehub.smilehub.exception.BusinessException;
import com.smilehub.smilehub.exception.ResourceNotFoundException;
import com.smilehub.smilehub.repositories.ConsultaRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final PacienteService pacienteService;
    private final DentistaService dentistaService;
    private final UsuarioService usuarioService;
    private final MotivoCancelamentoService motivoCancelamentoService;

    public ConsultaService(
            ConsultaRepository consultaRepository,
            PacienteService pacienteService,
            DentistaService dentistaService,
            UsuarioService usuarioService,
            MotivoCancelamentoService motivoCancelamentoService
    ) {
        this.consultaRepository = consultaRepository;
        this.pacienteService = pacienteService;
        this.dentistaService = dentistaService;
        this.usuarioService = usuarioService;
        this.motivoCancelamentoService = motivoCancelamentoService;
    }

    @Transactional
    public ConsultaResponseDTO criar(ConsultaRequestDTO request) {
        validarRequest(request);

        Paciente paciente = pacienteService.buscarEntidadePorId(request.pacienteId());
        Dentista dentista = dentistaService.buscarEntidadePorId(request.dentistaId());
        Usuario usuario = usuarioService.buscarEntidadePorId(request.usuarioId());

        MotivoCancelamento motivo = null;
        if (request.motivoCancelamentoId() != null) {
            motivo = motivoCancelamentoService.buscarEntidadePorId(request.motivoCancelamentoId());
        }

        String status = request.status() != null && !request.status().isBlank()
                ? request.status()
                : "AGENDADA";

        Consulta consulta = new Consulta(
                paciente,
                dentista,
                usuario,
                motivo,
                request.descricao(),
                request.dataInicio(),
                request.dataFim(),
                status
        );

        return ConsultaResponseDTO.from(consultaRepository.save(consulta));
    }

    @Transactional(readOnly = true)
    public List<ConsultaResponseDTO> listar() {
        return consultaRepository.findAll().stream()
                .map(ConsultaResponseDTO::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ConsultaResponseDTO buscarPorId(Long id) {
        return ConsultaResponseDTO.from(buscarEntidadePorId(id));
    }

    @Transactional(readOnly = true)
    public List<ConsultaResponseDTO> listarPorPaciente(Long pacienteId) {
        pacienteService.buscarEntidadePorId(pacienteId);
        return consultaRepository.findByPacienteId(pacienteId).stream()
                .map(ConsultaResponseDTO::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ConsultaResponseDTO> listarPorDentista(Long dentistaId) {
        dentistaService.buscarEntidadePorId(dentistaId);
        return consultaRepository.findByDentistaId(dentistaId).stream()
                .map(ConsultaResponseDTO::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public Consulta buscarEntidadePorId(Long id) {
        return consultaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada: " + id));
    }

    private void validarRequest(ConsultaRequestDTO request) {
        if (request.pacienteId() == null) {
            throw new BusinessException("Paciente é obrigatório");
        }
        if (request.dentistaId() == null) {
            throw new BusinessException("Dentista é obrigatório");
        }
        if (request.usuarioId() == null) {
            throw new BusinessException("Usuário é obrigatório");
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
        if (request.dataFim().isBefore(request.dataInicio()) || request.dataFim().isEqual(request.dataInicio())) {
            throw new BusinessException("Data de fim deve ser posterior à data de início");
        }
        if (request.status() != null && request.status().equalsIgnoreCase("CANCELADA")
                && request.motivoCancelamentoId() == null) {
            throw new BusinessException("Motivo de cancelamento é obrigatório para consultas canceladas");
        }
    }
}
