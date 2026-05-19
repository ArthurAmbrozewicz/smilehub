package com.smilehub.smilehub.services;

import com.smilehub.smilehub.dto.PerfilResponseDTO;
import com.smilehub.smilehub.entities.Perfil;
import com.smilehub.smilehub.exception.ResourceNotFoundException;
import com.smilehub.smilehub.repositories.PerfilRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PerfilService {

    private static final List<String> PERFIS_PADRAO = List.of("ADMIN", "DENTISTA", "PACIENTE");

    private final PerfilRepository perfilRepository;

    public PerfilService(PerfilRepository perfilRepository) {
        this.perfilRepository = perfilRepository;
    }

    @Transactional
    public void inicializarPerfisPadrao() {
        PERFIS_PADRAO.forEach(nome -> {
            if (!perfilRepository.existsByNome(nome)) {
                perfilRepository.save(new Perfil(nome));
            }
        });
    }

    @Transactional(readOnly = true)
    public List<PerfilResponseDTO> listar() {
        return perfilRepository.findAll().stream()
                .map(PerfilResponseDTO::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public PerfilResponseDTO buscarPorId(Long id) {
        return PerfilResponseDTO.from(buscarEntidadePorId(id));
    }

    @Transactional(readOnly = true)
    public Perfil buscarEntidadePorId(Long id) {
        return perfilRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil não encontrado: " + id));
    }
}
