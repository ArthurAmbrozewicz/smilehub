package com.smilehub.smilehub.repositories;

import com.smilehub.smilehub.entities.Consulta;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    List<Consulta> findByPacienteId(Long pacienteId);

    List<Consulta> findByDentistaId(Long dentistaId);

    List<Consulta> findByPacienteIdAndStatus(Long pacienteId, String status);

    List<Consulta> findByDentistaIdAndStatus(Long dentistaId, String status);

    @Query("""
            SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END
            FROM Consulta c
            WHERE c.dentista.id = :dentistaId
            AND c.status = 'AGENDADA'
            AND c.dataInicio < :dataFim
            AND c.dataFim > :dataInicio
            """)
    boolean existsConflitoHorarioDentista(
            @Param("dentistaId") Long dentistaId,
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim
    );
}
