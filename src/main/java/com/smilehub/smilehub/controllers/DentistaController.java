package com.smilehub.smilehub.controllers;

import com.smilehub.smilehub.dto.DentistaEditarDTO;
import com.smilehub.smilehub.dto.DentistaRequestDTO;
import com.smilehub.smilehub.dto.DentistaResponseDTO;
import com.smilehub.smilehub.dto.EspecialidadeResponseDTO;
import com.smilehub.smilehub.services.DentistaService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dentistas")
public class DentistaController {

    private final DentistaService dentistaService;

    public DentistaController(DentistaService dentistaService) {
        this.dentistaService = dentistaService;
    }

    @PostMapping
    public ResponseEntity<DentistaResponseDTO> criar(@RequestBody DentistaRequestDTO request) {
        DentistaResponseDTO dentista = dentistaService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(dentista);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DentistaResponseDTO> editar(
            @PathVariable Long id,
            @RequestBody DentistaEditarDTO request
    ) {
        return ResponseEntity.ok(dentistaService.editar(id, request));
    }

    @PostMapping("/{id}/especialidades/{idEspecialidade}")
    public ResponseEntity<Void> adicionarEspecialidade(
            @PathVariable Long id,
            @PathVariable Long idEspecialidade
    ) {
        dentistaService.adicionarEspecialidade(id, idEspecialidade);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}/especialidades/{idEspecialidade}")
    public ResponseEntity<Void> removerEspecialidade(
            @PathVariable Long id,
            @PathVariable Long idEspecialidade
    ) {
        dentistaService.removerEspecialidade(id, idEspecialidade);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/especialidades")
    public ResponseEntity<List<EspecialidadeResponseDTO>> listarEspecialidades(@PathVariable Long id) {
        return ResponseEntity.ok(dentistaService.listarEspecialidades(id));
    }

    @GetMapping
    public ResponseEntity<List<DentistaResponseDTO>> listar() {
        return ResponseEntity.ok(dentistaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DentistaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(dentistaService.buscarPorId(id));
    }
}
