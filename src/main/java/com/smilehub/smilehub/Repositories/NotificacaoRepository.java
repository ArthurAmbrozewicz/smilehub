package com.smilehub.smilehub.repositories;

import com.smilehub.smilehub.entities.Notificacao;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {

    List<Notificacao> findByUsuarioIdOrderByDataDesc(Long usuarioId);

    long countByUsuarioIdAndLidaFalse(Long usuarioId);
}
