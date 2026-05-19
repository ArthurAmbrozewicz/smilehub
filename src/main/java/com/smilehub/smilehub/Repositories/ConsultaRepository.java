package com.smilehub.smilehub.repositories;

import com.smilehub.smilehub.entities.Consulta;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    List<Consulta> findByPacienteId(Long pacienteId);

    List<Consulta> findByDentistaId(Long dentistaId);
}
