package com.smilehub.smilehub.repositories;

import com.smilehub.smilehub.entities.Medicamento;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface MedicamentoRepository extends JpaRepository<Medicamento, Long> {

    List<Medicamento> findAllByPrescricaoId(Long prescricaoId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    void deleteByPrescricaoId(Long prescricaoId);
}
