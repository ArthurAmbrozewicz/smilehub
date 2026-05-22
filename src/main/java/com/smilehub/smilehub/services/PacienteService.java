package com.smilehub.smilehub.services;

import com.smilehub.smilehub.dto.PacienteRequestDTO;
import com.smilehub.smilehub.dto.PacienteResponseDTO;
import com.smilehub.smilehub.entities.Ativo;
import com.smilehub.smilehub.entities.Paciente;
import com.smilehub.smilehub.exception.BusinessException;
import com.smilehub.smilehub.exception.ResourceNotFoundException;
import com.smilehub.smilehub.repositories.PacienteRepository;
import com.smilehub.smilehub.repositories.UsuarioRepository;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PacienteService {

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
        validarRequest(request);

        if (usuarioRepository.existsByEmail(request.email())) {
            throw new BusinessException("E-mail já cadastrado");
        }
        if (pacienteRepository.existsByCpf(request.cpf())) {
            throw new BusinessException("CPF já cadastrado");
        }

        Ativo ativo = request.ativo() != null ? request.ativo() : Ativo.S;
        String senhaCriptografada = passwordEncoder.encode(request.senha());

        Paciente paciente = new Paciente(
                request.nome(),
                request.email(),
                senhaCriptografada,
                ativo,
                request.cpf(),
                request.telefone()
        );

        return PacienteResponseDTO.from(pacienteRepository.save(paciente));
    }

    @Transactional(readOnly = true)
    public List<PacienteResponseDTO> listar() {
        return pacienteRepository.findAll().stream()
                .map(PacienteResponseDTO::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public PacienteResponseDTO buscarPorId(Long id) {
        return PacienteResponseDTO.from(buscarEntidadePorId(id));
    }

    @Transactional(readOnly = true)
    public Paciente buscarEntidadePorId(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado: " + id));
    }

    private void validarRequest(PacienteRequestDTO request) {
        if (request.nome() == null || request.nome().isBlank()) {
            throw new BusinessException("Nome é obrigatório");
        }
        if (request.email() == null || request.email().isBlank()) {
            throw new BusinessException("E-mail é obrigatório");
        }
        if (request.senha() == null || request.senha().isBlank()) {
            throw new BusinessException("Senha é obrigatória");
        }
        if (request.cpf() == null || request.cpf().isBlank()) {
            throw new BusinessException("CPF é obrigatório");
        }
        if (request.telefone() == null || request.telefone().isBlank()) {
            throw new BusinessException("Telefone é obrigatório");
        }
        if (request.ativo() != null && request.ativo() != Ativo.S && request.ativo() != Ativo.N) {
            throw new BusinessException("Ativo deve ser S ou N");
        }
    }
}
