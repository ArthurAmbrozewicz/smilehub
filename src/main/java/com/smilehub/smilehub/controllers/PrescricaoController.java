package com.smilehub.smilehub.controllers;

import com.smilehub.smilehub.dto.PrescricaoRequestDTO;
import com.smilehub.smilehub.dto.PrescricaoResponseDTO;
import com.smilehub.smilehub.services.PrescricaoService;
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
@RequestMapping("/api/prescricoes")
public class PrescricaoController {

    private final PrescricaoService prescricaoService;

    public PrescricaoController(PrescricaoService prescricaoService) {
        this.prescricaoService = prescricaoService;
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<PrescricaoResponseDTO>> listarPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(prescricaoService.listarPorPacienteId(pacienteId));
    }

    @GetMapping("/consulta/{consultaId}")
    public ResponseEntity<PrescricaoResponseDTO> buscarPorConsulta(@PathVariable Long consultaId) {
        return ResponseEntity.ok(prescricaoService.buscarPorConsultaId(consultaId));
    }

    @PostMapping
    public ResponseEntity<PrescricaoResponseDTO> salvar(@RequestBody PrescricaoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(prescricaoService.salvar(request));
    }
}
