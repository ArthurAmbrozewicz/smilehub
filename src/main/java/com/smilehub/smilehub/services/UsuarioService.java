package com.smilehub.smilehub.services;

import com.smilehub.smilehub.dto.AlterarSenhaDTO;
import com.smilehub.smilehub.dto.UsuarioResponseDTO;
import com.smilehub.smilehub.entities.Usuario;
import com.smilehub.smilehub.exception.BusinessException;
import com.smilehub.smilehub.exception.ResourceNotFoundException;
import com.smilehub.smilehub.repositories.UsuarioRepository;
import com.smilehub.smilehub.util.UsuarioDtypeUtil;
import java.util.List;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    private static final String DTYPE_ADMINISTRADOR = "ADMINISTRADOR";

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void alterarSenha(Long id, AlterarSenhaDTO request) {
        validarAlterarSenhaRequest(request);

        Usuario usuario = buscarEntidadePorId(id);
        Usuario usuarioLogado = buscarUsuarioAutenticado();
        String dtypeLogado = UsuarioDtypeUtil.resolverDtype(usuarioLogado);

        if (!DTYPE_ADMINISTRADOR.equals(dtypeLogado)) {
            if (request.senhaAtual() == null || request.senhaAtual().isBlank()) {
                throw new BusinessException("Senha atual é obrigatória");
            }
            if (!passwordEncoder.matches(request.senhaAtual(), usuario.getSenha())) {
                throw new BusinessException("Senha atual inválida");
            }
        }

        usuario.setSenha(passwordEncoder.encode(request.novaSenha()));
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void desativar(Long id) {
        Usuario usuario = buscarEntidadePorId(id);
        Usuario usuarioLogado = buscarUsuarioAutenticado();
        String dtypeLogado = UsuarioDtypeUtil.resolverDtype(usuarioLogado);

        if (!DTYPE_ADMINISTRADOR.equals(dtypeLogado) && !usuarioLogado.getId().equals(id)) {
            throw new BusinessException("Acesso negado");
        }

        if (!usuario.isAtivo()) {
            throw new BusinessException("Usuário já está inativo");
        }

        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
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
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }

    @Transactional(readOnly = true)
    public Usuario buscarEntidadePorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + id));
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

        return buscarPorEmail(email);
    }

    private void validarAlterarSenhaRequest(AlterarSenhaDTO request) {
        if (request.novaSenha() == null || request.novaSenha().isBlank()) {
            throw new BusinessException("Nova senha é obrigatória");
        }
    }
}
