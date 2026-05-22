package com.smilehub.smilehub.services;

import com.smilehub.smilehub.dto.AdministradorRequestDTO;
import com.smilehub.smilehub.dto.AdministradorResponseDTO;
import com.smilehub.smilehub.entities.Administrador;
import com.smilehub.smilehub.entities.Ativo;
import com.smilehub.smilehub.exception.BusinessException;
import com.smilehub.smilehub.exception.ResourceNotFoundException;
import com.smilehub.smilehub.repositories.AdministradorRepository;
import com.smilehub.smilehub.repositories.UsuarioRepository;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdministradorService {

    private final AdministradorRepository administradorRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AdministradorService(
            AdministradorRepository administradorRepository,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.administradorRepository = administradorRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public AdministradorResponseDTO criar(AdministradorRequestDTO request) {
        validarRequest(request);

        if (usuarioRepository.existsByEmail(request.email())) {
            throw new BusinessException("E-mail já cadastrado");
        }

        Ativo ativo = request.ativo() != null ? request.ativo() : Ativo.S;
        String senhaCriptografada = passwordEncoder.encode(request.senha());

        Administrador administrador = new Administrador(
                request.nome(),
                request.email(),
                senhaCriptografada,
                ativo
        );

        return AdministradorResponseDTO.from(administradorRepository.save(administrador));
    }

    @Transactional(readOnly = true)
    public List<AdministradorResponseDTO> listar() {
        return administradorRepository.findAll().stream()
                .map(AdministradorResponseDTO::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public AdministradorResponseDTO buscarPorId(Long id) {
        return AdministradorResponseDTO.from(buscarEntidadePorId(id));
    }

    @Transactional(readOnly = true)
    public Administrador buscarEntidadePorId(Long id) {
        return administradorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Administrador não encontrado: " + id));
    }

    private void validarRequest(AdministradorRequestDTO request) {
        if (request.nome() == null || request.nome().isBlank()) {
            throw new BusinessException("Nome é obrigatório");
        }
        if (request.email() == null || request.email().isBlank()) {
            throw new BusinessException("E-mail é obrigatório");
        }
        if (request.senha() == null || request.senha().isBlank()) {
            throw new BusinessException("Senha é obrigatória");
        }
        if (request.ativo() != null && request.ativo() != Ativo.S && request.ativo() != Ativo.N) {
            throw new BusinessException("Ativo deve ser S ou N");
        }
    }
}
