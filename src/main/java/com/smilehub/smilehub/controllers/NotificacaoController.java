package com.smilehub.smilehub.controllers;

import com.smilehub.smilehub.dto.NotificacaoResponseDTO;
import com.smilehub.smilehub.dto.NotificacaoResumoDTO;
import com.smilehub.smilehub.services.NotificacaoService;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/notificacoes")
public class NotificacaoController {

    private final NotificacaoService notificacaoService;

    public NotificacaoController(NotificacaoService notificacaoService) {
        this.notificacaoService = notificacaoService;
    }

    @GetMapping
    public ResponseEntity<List<NotificacaoResponseDTO>> listarDoUsuarioLogado() {
        return ResponseEntity.ok(notificacaoService.listarDoUsuarioLogado());
    }

    @GetMapping("/resumo")
    public ResponseEntity<NotificacaoResumoDTO> resumoDoUsuarioLogado() {
        return ResponseEntity.ok(notificacaoService.resumoDoUsuarioLogado());
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamNotificacoes() {
        return notificacaoService.abrirStream();
    }

    @PatchMapping("/{id}/lida")
    public ResponseEntity<NotificacaoResponseDTO> marcarComoLida(@PathVariable Long id) {
        return ResponseEntity.ok(notificacaoService.marcarComoLida(id));
    }

    @PatchMapping("/marcar-todas-lidas")
    public ResponseEntity<Void> marcarTodasComoLidas() {
        notificacaoService.marcarTodasComoLidas();
        return ResponseEntity.noContent().build();
    }
}
