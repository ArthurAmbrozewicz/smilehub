package com.smilehub.smilehub.repositories;

import com.smilehub.smilehub.entities.Paciente;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    boolean existsByCpf(String cpf);

    boolean existsByCpfAndIdNot(String cpf, Long id);

    List<Paciente> findAllByAtivoTrue();
}
