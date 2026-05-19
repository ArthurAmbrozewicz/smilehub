package com.smilehub.smilehub.services;

import com.smilehub.smilehub.dto.MotivoCancelamentoRequestDTO;
import com.smilehub.smilehub.dto.MotivoCancelamentoResponseDTO;
import com.smilehub.smilehub.entities.MotivoCancelamento;
import com.smilehub.smilehub.entities.Usuario;
import com.smilehub.smilehub.exception.BusinessException;
import com.smilehub.smilehub.exception.ResourceNotFoundException;
import com.smilehub.smilehub.repositories.MotivoCancelamentoRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MotivoCancelamentoService {

    private final MotivoCancelamentoRepository motivoCancelamentoRepository;
    private final UsuarioService usuarioService;

    public MotivoCancelamentoService(
            MotivoCancelamentoRepository motivoCancelamentoRepository,
            UsuarioService usuarioService
    ) {
        this.motivoCancelamentoRepository = motivoCancelamentoRepository;
        this.usuarioService = usuarioService;
    }

    @Transactional
    public MotivoCancelamentoResponseDTO criar(MotivoCancelamentoRequestDTO request) {
        validarRequest(request);

        if (motivoCancelamentoRepository.existsByDescricao(request.descricao())) {
            throw new BusinessException("Motivo de cancelamento já cadastrado");
        }

        Usuario usuario = usuarioService.buscarEntidadePorId(request.usuarioCriacaoId());

        MotivoCancelamento motivo = new MotivoCancelamento(request.descricao(), usuario);

        return MotivoCancelamentoResponseDTO.from(motivoCancelamentoRepository.save(motivo));
    }

    @Transactional(readOnly = true)
    public List<MotivoCancelamentoResponseDTO> listar() {
        return motivoCancelamentoRepository.findAll().stream()
                .map(MotivoCancelamentoResponseDTO::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public MotivoCancelamentoResponseDTO buscarPorId(Long id) {
        return MotivoCancelamentoResponseDTO.from(buscarEntidadePorId(id));
    }

    @Transactional(readOnly = true)
    public MotivoCancelamento buscarEntidadePorId(Long id) {
        return motivoCancelamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Motivo de cancelamento não encontrado: " + id));
    }

    private void validarRequest(MotivoCancelamentoRequestDTO request) {
        if (request.descricao() == null || request.descricao().isBlank()) {
            throw new BusinessException("Descrição é obrigatória");
        }
        if (request.usuarioCriacaoId() == null) {
            throw new BusinessException("Usuário criador é obrigatório");
        }
    }
}
