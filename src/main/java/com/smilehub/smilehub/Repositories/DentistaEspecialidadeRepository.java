package com.smilehub.smilehub.repositories;

import com.smilehub.smilehub.entities.DentistaEspecialidade;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DentistaEspecialidadeRepository extends JpaRepository<DentistaEspecialidade, Long> {

    boolean existsByDentistaIdAndEspecialidadeId(Long dentistaId, Long especialidadeId);

    List<DentistaEspecialidade> findByDentistaId(Long dentistaId);
}
