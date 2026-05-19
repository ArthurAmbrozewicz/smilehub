package com.smilehub.smilehub.services;

import com.smilehub.smilehub.dto.DentistaRequestDTO;
import com.smilehub.smilehub.dto.DentistaResponseDTO;
import com.smilehub.smilehub.entities.Ativo;
import com.smilehub.smilehub.entities.Dentista;
import com.smilehub.smilehub.entities.Usuario;
import com.smilehub.smilehub.exception.BusinessException;
import com.smilehub.smilehub.exception.ResourceNotFoundException;
import com.smilehub.smilehub.repositories.DentistaRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DentistaService {

    private final DentistaRepository dentistaRepository;
    private final UsuarioService usuarioService;

    public DentistaService(DentistaRepository dentistaRepository, UsuarioService usuarioService) {
        this.dentistaRepository = dentistaRepository;
        this.usuarioService = usuarioService;
    }

    @Transactional
    public DentistaResponseDTO criar(DentistaRequestDTO request) {
        validarRequest(request);

        if (dentistaRepository.existsByEmail(request.email())) {
            throw new BusinessException("E-mail já cadastrado");
        }
        if (dentistaRepository.existsByCpf(request.cpf())) {
            throw new BusinessException("CPF já cadastrado");
        }
        if (dentistaRepository.existsByCro(request.cro())) {
            throw new BusinessException("CRO já cadastrado");
        }

        Usuario usuario = usuarioService.buscarEntidadePorId(request.usuarioId());
        Ativo ativo = request.ativo() != null ? request.ativo() : Ativo.S;

        Dentista dentista = new Dentista(
                request.nome(),
                request.cpf(),
                request.email(),
                request.cro(),
                usuario,
                ativo
        );

        return DentistaResponseDTO.from(dentistaRepository.save(dentista));
    }

    @Transactional(readOnly = true)
    public List<DentistaResponseDTO> listar() {
        return dentistaRepository.findAll().stream()
                .map(DentistaResponseDTO::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public DentistaResponseDTO buscarPorId(Long id) {
        return DentistaResponseDTO.from(buscarEntidadePorId(id));
    }

    @Transactional(readOnly = true)
    public Dentista buscarEntidadePorId(Long id) {
        return dentistaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dentista não encontrado: " + id));
    }

    private void validarRequest(DentistaRequestDTO request) {
        if (request.nome() == null || request.nome().isBlank()) {
            throw new BusinessException("Nome é obrigatório");
        }
        if (request.cpf() == null || request.cpf().isBlank()) {
            throw new BusinessException("CPF é obrigatório");
        }
        if (request.email() == null || request.email().isBlank()) {
            throw new BusinessException("E-mail é obrigatório");
        }
        if (request.cro() == null || request.cro().isBlank()) {
            throw new BusinessException("CRO é obrigatório");
        }
        if (request.usuarioId() == null) {
            throw new BusinessException("Usuário criador é obrigatório");
        }
        if (request.ativo() != null && request.ativo() != Ativo.S && request.ativo() != Ativo.N) {
            throw new BusinessException("Ativo deve ser S ou N");
        }
    }
}
