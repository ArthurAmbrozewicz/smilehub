package com.smilehub.smilehub.repositories;

import com.smilehub.smilehub.entities.MotivoCancelamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MotivoCancelamentoRepository extends JpaRepository<MotivoCancelamento, Long> {

    boolean existsByDescricao(String descricao);
}
