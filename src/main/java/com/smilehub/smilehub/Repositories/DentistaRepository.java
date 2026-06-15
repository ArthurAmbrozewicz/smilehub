package com.smilehub.smilehub.repositories;

import com.smilehub.smilehub.entities.Dentista;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DentistaRepository extends JpaRepository<Dentista, Long> {

    boolean existsByCpf(String cpf);

    boolean existsByCpfAndIdNot(String cpf, Long id);

    boolean existsByCro(String cro);

    List<Dentista> findAllByAtivoTrue();
}
