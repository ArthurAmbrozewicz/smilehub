package com.smilehub.smilehub.services;

import com.smilehub.smilehub.dto.NotificacaoEventoDTO;
import com.smilehub.smilehub.dto.NotificacaoResumoDTO;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class NotificacaoSseService {

    private static final long TIMEOUT_MS = 30L * 60L * 1000L;

    private final CopyOnWriteArrayList<ConexaoSse> conexoes = new CopyOnWriteArrayList<>();

    public SseEmitter conectar(Long usuarioId, NotificacaoResumoDTO resumoInicial) {
        SseEmitter emitter = new SseEmitter(TIMEOUT_MS);
        ConexaoSse conexao = new ConexaoSse(usuarioId, emitter);

        conexoes.add(conexao);

        emitter.onCompletion(() -> remover(conexao));
        emitter.onTimeout(() -> remover(conexao));
        emitter.onError(ex -> remover(conexao));

        enviarEvento(emitter, "conectado", resumoInicial);

        return emitter;
    }

    public void publicar(Long usuarioId, NotificacaoEventoDTO evento) {
        for (ConexaoSse conexao : conexoes) {
            if (!conexao.usuarioId().equals(usuarioId)) {
                continue;
            }

            enviarEvento(conexao.emitter(), "atualizacao", evento);
        }
    }

    private void enviarEvento(SseEmitter emitter, String nome, Object dados) {
        try {
            emitter.send(SseEmitter.event().name(nome).data(dados));
        } catch (IOException | IllegalStateException ex) {
            conexoes.removeIf(conexao -> conexao.emitter() == emitter);
            emitter.complete();
        }
    }

    private void remover(ConexaoSse conexao) {
        conexoes.remove(conexao);
        conexao.emitter().complete();
    }

    private record ConexaoSse(Long usuarioId, SseEmitter emitter) {
    }
}
