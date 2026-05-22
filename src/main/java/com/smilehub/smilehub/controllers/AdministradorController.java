package com.smilehub.smilehub.controllers;

import com.smilehub.smilehub.dto.AdministradorRequestDTO;
import com.smilehub.smilehub.dto.AdministradorResponseDTO;
import com.smilehub.smilehub.services.AdministradorService;
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
@RequestMapping("/api/administradores")
public class AdministradorController {

    private final AdministradorService administradorService;

    public AdministradorController(AdministradorService administradorService) {
        this.administradorService = administradorService;
    }

    @PostMapping
    public ResponseEntity<AdministradorResponseDTO> criar(@RequestBody AdministradorRequestDTO request) {
        AdministradorResponseDTO administrador = administradorService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(administrador);
    }

    @GetMapping
    public ResponseEntity<List<AdministradorResponseDTO>> listar() {
        return ResponseEntity.ok(administradorService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdministradorResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(administradorService.buscarPorId(id));
    }
}
