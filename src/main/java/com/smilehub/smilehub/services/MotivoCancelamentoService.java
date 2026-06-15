package com.smilehub.smilehub.services;

import com.smilehub.smilehub.dto.MotivoCancelamentoRequestDTO;
import com.smilehub.smilehub.dto.MotivoCancelamentoResponseDTO;
import com.smilehub.smilehub.entities.MotivoCancelamento;
import com.smilehub.smilehub.entities.Usuario;
import com.smilehub.smilehub.exception.BusinessException;
import com.smilehub.smilehub.exception.ResourceNotFoundException;
import com.smilehub.smilehub.repositories.MotivoCancelamentoRepository;
import com.smilehub.smilehub.repositories.UsuarioRepository;
import java.util.List;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MotivoCancelamentoService {

    private final MotivoCancelamentoRepository motivoCancelamentoRepository;
    private final UsuarioRepository usuarioRepository;

    public MotivoCancelamentoService(
            MotivoCancelamentoRepository motivoCancelamentoRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.motivoCancelamentoRepository = motivoCancelamentoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public MotivoCancelamentoResponseDTO criar(MotivoCancelamentoRequestDTO request) {
        validarRequest(request);

        if (motivoCancelamentoRepository.existsByDescricao(request.descricao())) {
            throw new BusinessException("Motivo de cancelamento já cadastrado");
        }

        Usuario usuario = buscarUsuarioAutenticado();

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

    private void validarRequest(MotivoCancelamentoRequestDTO request) {
        if (request.descricao() == null || request.descricao().isBlank()) {
            throw new BusinessException("Descrição é obrigatória");
        }
    }
}
