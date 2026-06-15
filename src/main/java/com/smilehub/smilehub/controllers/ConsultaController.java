package com.smilehub.smilehub.controllers;

import com.smilehub.smilehub.dto.CancelarConsultaDTO;
import com.smilehub.smilehub.dto.ConsultaEditarDTO;
import com.smilehub.smilehub.dto.ConsultaRequestDTO;
import com.smilehub.smilehub.dto.ConsultaResponseDTO;
import com.smilehub.smilehub.services.ConsultaService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/consultas")
public class ConsultaController {

    private final ConsultaService consultaService;

    public ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @PostMapping
    public ResponseEntity<ConsultaResponseDTO> criar(@RequestBody ConsultaRequestDTO request) {
        ConsultaResponseDTO consulta = consultaService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(consulta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConsultaResponseDTO> editar(
            @PathVariable Long id,
            @RequestBody ConsultaEditarDTO request
    ) {
        return ResponseEntity.ok(consultaService.editar(id, request));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<ConsultaResponseDTO> cancelar(
            @PathVariable Long id,
            @RequestBody CancelarConsultaDTO request
    ) {
        return ResponseEntity.ok(consultaService.cancelar(id, request));
    }

    @PatchMapping("/{id}/finalizar")
    public ResponseEntity<ConsultaResponseDTO> finalizar(@PathVariable Long id) {
        return ResponseEntity.ok(consultaService.finalizar(id));
    }

    @GetMapping
    public ResponseEntity<List<ConsultaResponseDTO>> listar() {
        return ResponseEntity.ok(consultaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(consultaService.buscarPorId(id));
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<ConsultaResponseDTO>> listarPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(consultaService.listarPorPaciente(pacienteId));
    }

    @GetMapping("/dentista/{dentistaId}")
    public ResponseEntity<List<ConsultaResponseDTO>> listarPorDentista(@PathVariable Long dentistaId) {
        return ResponseEntity.ok(consultaService.listarPorDentista(dentistaId));
    }
}
