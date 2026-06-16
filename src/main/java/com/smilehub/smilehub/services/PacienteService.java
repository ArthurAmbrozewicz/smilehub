package com.smilehub.smilehub.services;

import com.smilehub.smilehub.dto.PacienteEditarDTO;
import com.smilehub.smilehub.dto.PacienteRequestDTO;
import com.smilehub.smilehub.dto.PacienteResponseDTO;
import com.smilehub.smilehub.entities.Paciente;
import com.smilehub.smilehub.entities.Usuario;
import com.smilehub.smilehub.exception.BusinessException;
import com.smilehub.smilehub.exception.ResourceNotFoundException;
import com.smilehub.smilehub.repositories.PacienteRepository;
import com.smilehub.smilehub.repositories.UsuarioRepository;
import com.smilehub.smilehub.util.UsuarioDtypeUtil;
import java.util.List;
import java.util.UUID;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PacienteService {

    private static final String DTYPE_ADMINISTRADOR = "ADMINISTRADOR";
    private static final String DTYPE_DENTISTA = "DENTISTA";

    private final PacienteRepository pacienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public PacienteService(
            PacienteRepository pacienteRepository,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.pacienteRepository = pacienteRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public PacienteResponseDTO criar(PacienteRequestDTO request) {
        validarAcessoCriar();
        validarRequest(request);

        if (usuarioRepository.existsByEmail(request.email())) {
            throw new BusinessException("E-mail já cadastrado");
        }
        if (pacienteRepository.existsByCpf(request.cpf())) {
            throw new BusinessException("CPF já cadastrado");
        }

        boolean ativo = request.ativo() != null ? request.ativo() : true;
        String senhaInterna = passwordEncoder.encode(UUID.randomUUID().toString());

        Paciente paciente = new Paciente(
                request.nome(),
                request.email(),
                senhaInterna,
                ativo,
                request.cpf(),
                request.telefone()
        );

        return PacienteResponseDTO.from(pacienteRepository.save(paciente));
    }

    @Transactional
    public PacienteResponseDTO editar(Long id, PacienteEditarDTO request) {
        validarAcessoEditar(id);
        validarEditarRequest(request);

        Paciente paciente = buscarEntidadePorId(id);

        if (usuarioRepository.existsByEmailAndIdNot(request.email(), id)) {
            throw new BusinessException("E-mail já cadastrado");
        }
        if (pacienteRepository.existsByCpfAndIdNot(request.cpf(), id)) {
            throw new BusinessException("CPF já cadastrado");
        }

        paciente.setNome(request.nome());
        paciente.setEmail(request.email());
        paciente.setCpf(request.cpf());
        paciente.setTelefone(request.telefone());
        if (request.ativo() != null) {
            paciente.setAtivo(request.ativo());
        }

        return PacienteResponseDTO.from(pacienteRepository.save(paciente));
    }

    @Transactional(readOnly = true)
    public List<PacienteResponseDTO> listar() {
        validarAcessoListar();

        return pacienteRepository.findAll().stream()
                .map(PacienteResponseDTO::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public PacienteResponseDTO buscarPorId(Long id) {
        validarAcessoBuscarPorId(id);
        return PacienteResponseDTO.from(buscarEntidadePorId(id));
    }

    @Transactional(readOnly = true)
    public Paciente buscarEntidadePorId(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado: " + id));
    }

    private void validarAcessoCriar() {
        String dtype = resolverDtypeUsuarioLogado();

        if (DTYPE_ADMINISTRADOR.equals(dtype) || DTYPE_DENTISTA.equals(dtype)) {
            return;
        }

        throw new BusinessException("Acesso negado");
    }

    private void validarAcessoEditar(Long id) {
        if (!DTYPE_ADMINISTRADOR.equals(resolverDtypeUsuarioLogado())) {
            throw new BusinessException("Acesso negado");
        }
    }

    private void validarAcessoListar() {
        String dtype = resolverDtypeUsuarioLogado();

        if (DTYPE_ADMINISTRADOR.equals(dtype) || DTYPE_DENTISTA.equals(dtype)) {
            return;
        }

        throw new BusinessException("Acesso negado");
    }

    private void validarAcessoBuscarPorId(Long id) {
        String dtype = resolverDtypeUsuarioLogado();

        if (DTYPE_ADMINISTRADOR.equals(dtype) || DTYPE_DENTISTA.equals(dtype)) {
            return;
        }

        throw new BusinessException("Acesso negado");
    }

    private String resolverDtypeUsuarioLogado() {
        return UsuarioDtypeUtil.resolverDtype(buscarUsuarioAutenticado());
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

    private void validarRequest(PacienteRequestDTO request) {
        if (request.nome() == null || request.nome().isBlank()) {
            throw new BusinessException("Nome é obrigatório");
        }
        if (request.email() == null || request.email().isBlank()) {
            throw new BusinessException("E-mail é obrigatório");
        }
        if (request.cpf() == null || request.cpf().isBlank()) {
            throw new BusinessException("CPF é obrigatório");
        }
        if (request.telefone() == null || request.telefone().isBlank()) {
            throw new BusinessException("Telefone é obrigatório");
        }
    }

    private void validarEditarRequest(PacienteEditarDTO request) {
        if (request.nome() == null || request.nome().isBlank()) {
            throw new BusinessException("Nome é obrigatório");
        }
        if (request.email() == null || request.email().isBlank()) {
            throw new BusinessException("E-mail é obrigatório");
        }
        if (request.cpf() == null || request.cpf().isBlank()) {
            throw new BusinessException("CPF é obrigatório");
        }
        if (request.telefone() == null || request.telefone().isBlank()) {
            throw new BusinessException("Telefone é obrigatório");
        }
    }
}
