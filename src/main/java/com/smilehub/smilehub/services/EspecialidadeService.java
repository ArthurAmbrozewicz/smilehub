package com.smilehub.smilehub.services;

import com.smilehub.smilehub.dto.EspecialidadeRequestDTO;
import com.smilehub.smilehub.dto.EspecialidadeResponseDTO;
import com.smilehub.smilehub.dto.EspecialidadeUpdateDTO;
import com.smilehub.smilehub.entities.Especialidade;
import com.smilehub.smilehub.entities.Usuario;
import com.smilehub.smilehub.exception.BusinessException;
import com.smilehub.smilehub.exception.ResourceNotFoundException;
import com.smilehub.smilehub.repositories.EspecialidadeRepository;
import com.smilehub.smilehub.repositories.UsuarioRepository;
import java.util.List;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EspecialidadeService {

    private final EspecialidadeRepository especialidadeRepository;
    private final UsuarioRepository usuarioRepository;

    public EspecialidadeService(
            EspecialidadeRepository especialidadeRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.especialidadeRepository = especialidadeRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public EspecialidadeResponseDTO criar(EspecialidadeRequestDTO request) {
        validarNome(request.nome());

        if (especialidadeRepository.existsByNome(request.nome())) {
            throw new BusinessException("Especialidade já cadastrada");
        }

        Usuario usuario = buscarUsuarioAutenticado();
        boolean ativo = request.ativo() != null ? request.ativo() : true;
        Especialidade especialidade = new Especialidade(request.nome(), usuario);
        especialidade.setAtivo(ativo);

        return EspecialidadeResponseDTO.from(especialidadeRepository.save(especialidade));
    }

    @Transactional
    public EspecialidadeResponseDTO editar(Long id, EspecialidadeUpdateDTO request) {
        validarNome(request.nome());

        Especialidade especialidade = buscarEntidadePorId(id);

        if (especialidadeRepository.existsByNomeAndIdNot(request.nome(), id)) {
            throw new BusinessException("Especialidade já cadastrada");
        }

        especialidade.setNome(request.nome());
        if (request.ativo() != null) {
            especialidade.setAtivo(request.ativo());
        }
        return EspecialidadeResponseDTO.from(especialidadeRepository.save(especialidade));
    }

    @Transactional
    public void desativar(Long id) {
        Especialidade especialidade = buscarEntidadePorId(id);

        if (!especialidade.isAtivo()) {
            throw new BusinessException("Especialidade já está inativa");
        }

        especialidade.setAtivo(false);
        especialidadeRepository.save(especialidade);
    }

    @Transactional(readOnly = true)
    public List<EspecialidadeResponseDTO> listar() {
        return especialidadeRepository.findAll().stream()
                .map(EspecialidadeResponseDTO::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public EspecialidadeResponseDTO buscarPorId(Long id) {
        return EspecialidadeResponseDTO.from(buscarEntidadePorId(id));
    }

    @Transactional(readOnly = true)
    public Especialidade buscarEntidadePorId(Long id) {
        return especialidadeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Especialidade não encontrada: " + id));
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

    private void validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new BusinessException("Nome é obrigatório");
        }
    }
}
