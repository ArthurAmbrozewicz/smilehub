package com.smilehub.smilehub.controllers;

import com.smilehub.smilehub.dto.MaterialReporDTO;
import com.smilehub.smilehub.dto.MaterialRequestDTO;
import com.smilehub.smilehub.dto.MaterialResponseDTO;
import com.smilehub.smilehub.dto.MaterialUpdateDTO;
import com.smilehub.smilehub.services.MaterialService;
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
@RequestMapping("/api/materiais")
public class MaterialController {

    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @PostMapping
    public ResponseEntity<MaterialResponseDTO> criar(@RequestBody MaterialRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(materialService.criar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaterialResponseDTO> editar(
            @PathVariable Long id,
            @RequestBody MaterialUpdateDTO request
    ) {
        return ResponseEntity.ok(materialService.editar(id, request));
    }

    @PatchMapping("/{id}/repor")
    public ResponseEntity<MaterialResponseDTO> reporEstoque(
            @PathVariable Long id,
            @RequestBody MaterialReporDTO request
    ) {
        return ResponseEntity.ok(materialService.reporEstoque(id, request));
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        materialService.desativar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<MaterialResponseDTO>> listar() {
        return ResponseEntity.ok(materialService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaterialResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(materialService.buscarPorId(id));
    }
}
