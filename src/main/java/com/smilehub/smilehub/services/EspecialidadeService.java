package com.smilehub.smilehub.services;

import com.smilehub.smilehub.dto.EspecialidadeRequestDTO;
import com.smilehub.smilehub.dto.EspecialidadeResponseDTO;
import com.smilehub.smilehub.entities.Especialidade;
import com.smilehub.smilehub.entities.Usuario;
import com.smilehub.smilehub.exception.BusinessException;
import com.smilehub.smilehub.exception.ResourceNotFoundException;
import com.smilehub.smilehub.repositories.EspecialidadeRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EspecialidadeService {

    private final EspecialidadeRepository especialidadeRepository;
    private final UsuarioService usuarioService;

    public EspecialidadeService(
            EspecialidadeRepository especialidadeRepository,
            UsuarioService usuarioService
    ) {
        this.especialidadeRepository = especialidadeRepository;
        this.usuarioService = usuarioService;
    }

    @Transactional
    public EspecialidadeResponseDTO criar(EspecialidadeRequestDTO request) {
        validarRequest(request);

        if (especialidadeRepository.existsByNome(request.nome())) {
            throw new BusinessException("Especialidade já cadastrada");
        }

        Usuario usuario = usuarioService.buscarEntidadePorId(request.usuarioId());

        Especialidade especialidade = new Especialidade(request.nome(), usuario);

        return EspecialidadeResponseDTO.from(especialidadeRepository.save(especialidade));
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

    private void validarRequest(EspecialidadeRequestDTO request) {
        if (request.nome() == null || request.nome().isBlank()) {
            throw new BusinessException("Nome é obrigatório");
        }
        if (request.usuarioId() == null) {
            throw new BusinessException("Usuário criador é obrigatório");
        }
    }
}
