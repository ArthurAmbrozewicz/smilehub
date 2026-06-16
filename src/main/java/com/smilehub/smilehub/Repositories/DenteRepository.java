package com.smilehub.smilehub.repositories;

import com.smilehub.smilehub.entities.Dente;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DenteRepository extends JpaRepository<Dente, Long> {

    @Query("SELECT d FROM Dente d WHERE d.odontograma.id = :odontogramaId")
    List<Dente> findAllByOdontogramaId(@Param("odontogramaId") Long odontogramaId);

    @Query("SELECT d FROM Dente d WHERE d.odontograma.id = :odontogramaId AND d.numero = :numero")
    Optional<Dente> findByOdontogramaIdAndNumero(
            @Param("odontogramaId") Long odontogramaId,
            @Param("numero") Integer numero
    );
}
