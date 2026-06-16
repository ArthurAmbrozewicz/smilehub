package com.smilehub.smilehub.repositories;

import com.smilehub.smilehub.entities.Odontograma;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OdontogramaRepository extends JpaRepository<Odontograma, Long> {

    Optional<Odontograma> findByPacienteId(Long pacienteId);
}
