package com.smilehub.smilehub.controllers;

import com.smilehub.smilehub.dto.MotivoCancelamentoRequestDTO;
import com.smilehub.smilehub.dto.MotivoCancelamentoResponseDTO;
import com.smilehub.smilehub.services.MotivoCancelamentoService;
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
@RequestMapping("/api/motivos-cancelamento")
public class MotivoCancelamentoController {

    private final MotivoCancelamentoService motivoCancelamentoService;

    public MotivoCancelamentoController(MotivoCancelamentoService motivoCancelamentoService) {
        this.motivoCancelamentoService = motivoCancelamentoService;
    }

    @PostMapping
    public ResponseEntity<MotivoCancelamentoResponseDTO> criar(@RequestBody MotivoCancelamentoRequestDTO request) {
        MotivoCancelamentoResponseDTO motivo = motivoCancelamentoService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(motivo);
    }

    @GetMapping
    public ResponseEntity<List<MotivoCancelamentoResponseDTO>> listar() {
        return ResponseEntity.ok(motivoCancelamentoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MotivoCancelamentoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(motivoCancelamentoService.buscarPorId(id));
    }
}
