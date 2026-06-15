package com.smilehub.smilehub.repositories;

import com.smilehub.smilehub.entities.Especialidade;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EspecialidadeRepository extends JpaRepository<Especialidade, Long> {

    boolean existsByNome(String nome);

    boolean existsByNomeAndIdNot(String nome, Long id);

    List<Especialidade> findAllByAtivoTrue();
}
