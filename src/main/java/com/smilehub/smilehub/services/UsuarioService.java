package com.smilehub.smilehub.services;

import com.smilehub.smilehub.dto.UsuarioRequestDTO;
import com.smilehub.smilehub.dto.UsuarioResponseDTO;
import com.smilehub.smilehub.entities.Perfil;
import com.smilehub.smilehub.entities.Usuario;
import com.smilehub.smilehub.exception.BusinessException;
import com.smilehub.smilehub.exception.ResourceNotFoundException;
import com.smilehub.smilehub.repositories.UsuarioRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PerfilService perfilService;

    public UsuarioService(UsuarioRepository usuarioRepository, PerfilService perfilService) {
        this.usuarioRepository = usuarioRepository;
        this.perfilService = perfilService;
    }

    @Transactional
    public UsuarioResponseDTO criar(UsuarioRequestDTO request) {
        validarRequest(request);

        if (usuarioRepository.existsByEmail(request.email())) {
            throw new BusinessException("E-mail já cadastrado");
        }
        if (usuarioRepository.existsByCpf(request.cpf())) {
            throw new BusinessException("CPF já cadastrado");
        }

        Perfil perfil = perfilService.buscarEntidadePorId(request.perfilId());

        Usuario usuario = new Usuario(
                request.nome(),
                request.email(),
                request.cpf(),
                request.senha(),
                perfil
        );

        return UsuarioResponseDTO.from(usuarioRepository.save(usuario));
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listar() {
        return usuarioRepository.findAll().stream()
                .map(UsuarioResponseDTO::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO buscarPorId(Long id) {
        return UsuarioResponseDTO.from(buscarEntidadePorId(id));
    }

    @Transactional(readOnly = true)
    public Usuario buscarEntidadePorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + id));
    }

    private void validarRequest(UsuarioRequestDTO request) {
        if (request.nome() == null || request.nome().isBlank()) {
            throw new BusinessException("Nome é obrigatório");
        }
        if (request.email() == null || request.email().isBlank()) {
            throw new BusinessException("E-mail é obrigatório");
        }
        if (request.cpf() == null || request.cpf().isBlank()) {
            throw new BusinessException("CPF é obrigatório");
        }
        if (request.senha() == null || request.senha().isBlank()) {
            throw new BusinessException("Senha é obrigatória");
        }
        if (request.perfilId() == null) {
            throw new BusinessException("Perfil é obrigatório");
        }
    }
}
