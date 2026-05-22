package com.smilehub.smilehub.repositories;

import com.smilehub.smilehub.entities.Dentista;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DentistaRepository extends JpaRepository<Dentista, Long> {

    boolean existsByCpf(String cpf);

    boolean existsByCro(String cro);
}
