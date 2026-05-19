package com.smilehub.smilehub.services;

import com.smilehub.smilehub.dto.DentistaEspecialidadeRequestDTO;
import com.smilehub.smilehub.dto.DentistaEspecialidadeResponseDTO;
import com.smilehub.smilehub.entities.Dentista;
import com.smilehub.smilehub.entities.DentistaEspecialidade;
import com.smilehub.smilehub.entities.Especialidade;
import com.smilehub.smilehub.exception.BusinessException;
import com.smilehub.smilehub.exception.ResourceNotFoundException;
import com.smilehub.smilehub.repositories.DentistaEspecialidadeRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DentistaEspecialidadeService {

    private final DentistaEspecialidadeRepository dentistaEspecialidadeRepository;
    private final DentistaService dentistaService;
    private final EspecialidadeService especialidadeService;

    public DentistaEspecialidadeService(
            DentistaEspecialidadeRepository dentistaEspecialidadeRepository,
            DentistaService dentistaService,
            EspecialidadeService especialidadeService
    ) {
        this.dentistaEspecialidadeRepository = dentistaEspecialidadeRepository;
        this.dentistaService = dentistaService;
        this.especialidadeService = especialidadeService;
    }

    @Transactional
    public DentistaEspecialidadeResponseDTO vincular(DentistaEspecialidadeRequestDTO request) {
        validarRequest(request);

        if (dentistaEspecialidadeRepository.existsByDentistaIdAndEspecialidadeId(
                request.dentistaId(),
                request.especialidadeId()
        )) {
            throw new BusinessException("Dentista já possui esta especialidade");
        }

        Dentista dentista = dentistaService.buscarEntidadePorId(request.dentistaId());
        Especialidade especialidade = especialidadeService.buscarEntidadePorId(request.especialidadeId());

        DentistaEspecialidade vinculo = new DentistaEspecialidade(dentista, especialidade);

        return DentistaEspecialidadeResponseDTO.from(dentistaEspecialidadeRepository.save(vinculo));
    }

    @Transactional(readOnly = true)
    public List<DentistaEspecialidadeResponseDTO> listar() {
        return dentistaEspecialidadeRepository.findAll().stream()
                .map(DentistaEspecialidadeResponseDTO::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public DentistaEspecialidadeResponseDTO buscarPorId(Long id) {
        return DentistaEspecialidadeResponseDTO.from(buscarEntidadePorId(id));
    }

    @Transactional(readOnly = true)
    public List<DentistaEspecialidadeResponseDTO> listarPorDentista(Long dentistaId) {
        dentistaService.buscarEntidadePorId(dentistaId);
        return dentistaEspecialidadeRepository.findByDentistaId(dentistaId).stream()
                .map(DentistaEspecialidadeResponseDTO::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public DentistaEspecialidade buscarEntidadePorId(Long id) {
        return dentistaEspecialidadeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vínculo não encontrado: " + id));
    }

    private void validarRequest(DentistaEspecialidadeRequestDTO request) {
        if (request.dentistaId() == null) {
            throw new BusinessException("Dentista é obrigatório");
        }
        if (request.especialidadeId() == null) {
            throw new BusinessException("Especialidade é obrigatória");
        }
    }
}
