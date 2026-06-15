package com.smilehub.smilehub.repositories;

import com.smilehub.smilehub.entities.Dentista;
import com.smilehub.smilehub.entities.DentistaEspecialidade;
import com.smilehub.smilehub.entities.Especialidade;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DentistaEspecialidadeRepository extends JpaRepository<DentistaEspecialidade, Long> {

    boolean existsByDentistaIdAndEspecialidadeId(Long dentistaId, Long especialidadeId);

    boolean existsByDentistaAndEspecialidade(Dentista dentista, Especialidade especialidade);

    List<DentistaEspecialidade> findByDentistaId(Long dentistaId);

    List<DentistaEspecialidade> findAllByDentista(Dentista dentista);
}
