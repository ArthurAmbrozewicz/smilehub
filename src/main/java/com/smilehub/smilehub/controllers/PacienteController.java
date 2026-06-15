package com.smilehub.smilehub.controllers;

import com.smilehub.smilehub.dto.PacienteEditarDTO;
import com.smilehub.smilehub.dto.PacienteRequestDTO;
import com.smilehub.smilehub.dto.PacienteResponseDTO;
import com.smilehub.smilehub.services.PacienteService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @PostMapping
    public ResponseEntity<PacienteResponseDTO> criar(@RequestBody PacienteRequestDTO request) {
        PacienteResponseDTO paciente = pacienteService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(paciente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PacienteResponseDTO> editar(
            @PathVariable Long id,
            @RequestBody PacienteEditarDTO request
    ) {
        return ResponseEntity.ok(pacienteService.editar(id, request));
    }

    @GetMapping
    public ResponseEntity<List<PacienteResponseDTO>> listar() {
        return ResponseEntity.ok(pacienteService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pacienteService.buscarPorId(id));
    }
}
