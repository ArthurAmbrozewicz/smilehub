package com.smilehub.smilehub.controllers;

import com.smilehub.smilehub.dto.ServicoRequestDTO;
import com.smilehub.smilehub.dto.ServicoResponseDTO;
import com.smilehub.smilehub.dto.ServicoUpdateDTO;
import com.smilehub.smilehub.services.ServicoService;
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
@RequestMapping("/api/servicos")
public class ServicoController {

    private final ServicoService servicoService;

    public ServicoController(ServicoService servicoService) {
        this.servicoService = servicoService;
    }

    @PostMapping
    public ResponseEntity<ServicoResponseDTO> criar(@RequestBody ServicoRequestDTO request) {
        ServicoResponseDTO servico = servicoService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(servico);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicoResponseDTO> editar(
            @PathVariable Long id,
            @RequestBody ServicoUpdateDTO request
    ) {
        return ResponseEntity.ok(servicoService.editar(id, request));
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        servicoService.desativar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ServicoResponseDTO>> listar() {
        return ResponseEntity.ok(servicoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(servicoService.buscarPorId(id));
    }
}
