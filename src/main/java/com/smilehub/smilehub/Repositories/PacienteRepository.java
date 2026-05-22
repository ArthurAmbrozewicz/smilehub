package com.smilehub.smilehub.repositories;

import com.smilehub.smilehub.entities.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    boolean existsByCpf(String cpf);
}
