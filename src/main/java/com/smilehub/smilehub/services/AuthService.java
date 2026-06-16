package com.smilehub.smilehub.services;

import com.smilehub.smilehub.dto.LoginRequestDTO;
import com.smilehub.smilehub.dto.LoginResponseDTO;
import com.smilehub.smilehub.entities.Usuario;
import com.smilehub.smilehub.exception.BusinessException;
import com.smilehub.smilehub.repositories.UsuarioRepository;
import com.smilehub.smilehub.security.JwtService;
import com.smilehub.smilehub.util.UsuarioDtypeUtil;
import java.time.LocalDateTime;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public LoginResponseDTO login(LoginRequestDTO request) {
        validarRequest(request);

        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new BusinessException("E-mail ou senha inválidos"));

        if (!passwordEncoder.matches(request.senha(), usuario.getSenha())) {
            throw new BusinessException("E-mail ou senha inválidos");
        }

        if (!usuario.isAtivo()) {
            throw new BusinessException("Usuário inativo");
        }

        String dtype = UsuarioDtypeUtil.resolverDtype(usuario);
        if ("PACIENTE".equals(dtype)) {
            throw new BusinessException("Pacientes não possuem acesso ao sistema");
        }

        usuario.setUltimoLogin(LocalDateTime.now());
        usuarioRepository.save(usuario);

        String token = jwtService.gerarToken(
                usuario.getId(),
                usuario.getEmail(),
                usuario.getNome(),
                dtype
        );

        return new LoginResponseDTO(
                token,
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                dtype
        );
    }

    private void validarRequest(LoginRequestDTO request) {
        if (request.email() == null || request.email().isBlank()) {
            throw new BusinessException("E-mail é obrigatório");
        }
        if (request.senha() == null || request.senha().isBlank()) {
            throw new BusinessException("Senha é obrigatória");
        }
    }
}
