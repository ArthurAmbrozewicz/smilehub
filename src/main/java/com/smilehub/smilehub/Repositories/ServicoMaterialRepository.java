package com.smilehub.smilehub.repositories;

import com.smilehub.smilehub.entities.ServicoMaterial;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ServicoMaterialRepository extends JpaRepository<ServicoMaterial, Long> {

    List<ServicoMaterial> findAllByServicoId(Long servicoId);

    @Modifying
    @Query("DELETE FROM ServicoMaterial sm WHERE sm.servico.id = :servicoId")
    void deleteByServicoId(Long servicoId);
}
