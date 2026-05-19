package com.smilehub.smilehub.controllers;

import com.smilehub.smilehub.dto.EspecialidadeRequestDTO;
import com.smilehub.smilehub.dto.EspecialidadeResponseDTO;
import com.smilehub.smilehub.services.EspecialidadeService;
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
@RequestMapping("/api/especialidades")
public class EspecialidadeController {

    private final EspecialidadeService especialidadeService;

    public EspecialidadeController(EspecialidadeService especialidadeService) {
        this.especialidadeService = especialidadeService;
    }

    @PostMapping
    public ResponseEntity<EspecialidadeResponseDTO> criar(@RequestBody EspecialidadeRequestDTO request) {
        EspecialidadeResponseDTO especialidade = especialidadeService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(especialidade);
    }

    @GetMapping
    public ResponseEntity<List<EspecialidadeResponseDTO>> listar() {
        return ResponseEntity.ok(especialidadeService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EspecialidadeResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(especialidadeService.buscarPorId(id));
    }
}
