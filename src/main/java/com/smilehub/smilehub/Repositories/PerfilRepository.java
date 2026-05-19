package com.smilehub.smilehub.repositories;

import com.smilehub.smilehub.entities.Perfil;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {

    Optional<Perfil> findByNome(String nome);

    boolean existsByNome(String nome);
}
