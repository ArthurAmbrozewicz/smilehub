package com.smilehub.smilehub.controllers;

import com.smilehub.smilehub.dto.OdontogramaResponseDTO;
import com.smilehub.smilehub.dto.OdontogramaSalvarDTO;
import com.smilehub.smilehub.services.OdontogramaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/odontogramas")
public class OdontogramaController {

    private final OdontogramaService odontogramaService;

    public OdontogramaController(OdontogramaService odontogramaService) {
        this.odontogramaService = odontogramaService;
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<OdontogramaResponseDTO> buscarPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(odontogramaService.buscarPorPacienteId(pacienteId));
    }

    @PutMapping("/paciente/{pacienteId}")
    public ResponseEntity<OdontogramaResponseDTO> salvarPorPaciente(
            @PathVariable Long pacienteId,
            @RequestBody OdontogramaSalvarDTO request
    ) {
        return ResponseEntity.ok(odontogramaService.salvarPorPacienteId(pacienteId, request));
    }
}
