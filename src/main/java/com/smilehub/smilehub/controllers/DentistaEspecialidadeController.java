package com.smilehub.smilehub.controllers;

import com.smilehub.smilehub.dto.DentistaEspecialidadeRequestDTO;
import com.smilehub.smilehub.dto.DentistaEspecialidadeResponseDTO;
import com.smilehub.smilehub.services.DentistaEspecialidadeService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dentista-especialidades")
public class DentistaEspecialidadeController {

    private final DentistaEspecialidadeService dentistaEspecialidadeService;

    public DentistaEspecialidadeController(DentistaEspecialidadeService dentistaEspecialidadeService) {
        this.dentistaEspecialidadeService = dentistaEspecialidadeService;
    }

    @PostMapping
    public ResponseEntity<DentistaEspecialidadeResponseDTO> vincular(
            @RequestBody DentistaEspecialidadeRequestDTO request
    ) {
        DentistaEspecialidadeResponseDTO vinculo = dentistaEspecialidadeService.vincular(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(vinculo);
    }

    @GetMapping
    public ResponseEntity<List<DentistaEspecialidadeResponseDTO>> listar() {
        return ResponseEntity.ok(dentistaEspecialidadeService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DentistaEspecialidadeResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(dentistaEspecialidadeService.buscarPorId(id));
    }

    @GetMapping("/dentista/{dentistaId}")
    public ResponseEntity<List<DentistaEspecialidadeResponseDTO>> listarPorDentista(
            @PathVariable Long dentistaId
    ) {
        return ResponseEntity.ok(dentistaEspecialidadeService.listarPorDentista(dentistaId));
    }
}
