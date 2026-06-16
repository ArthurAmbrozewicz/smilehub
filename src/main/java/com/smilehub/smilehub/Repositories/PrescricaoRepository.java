package com.smilehub.smilehub.repositories;

import com.smilehub.smilehub.entities.Prescricao;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PrescricaoRepository extends JpaRepository<Prescricao, Long> {

    Optional<Prescricao> findByConsultaId(Long consultaId);

    @Query("""
            SELECT p FROM Prescricao p
            JOIN p.consulta c
            WHERE c.paciente.id = :pacienteId
            ORDER BY p.dataEmissao DESC
            """)
    List<Prescricao> findAllByPacienteIdOrderByDataEmissaoDesc(@Param("pacienteId") Long pacienteId);
}
