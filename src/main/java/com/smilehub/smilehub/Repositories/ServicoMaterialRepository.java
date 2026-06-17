package com.smilehub.smilehub.repositories;

import com.smilehub.smilehub.entities.ServicoMaterial;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicoMaterialRepository extends JpaRepository<ServicoMaterial, Long> {

    List<ServicoMaterial> findAllByServicoId(Long servicoId);

    void deleteByServicoId(Long servicoId);
}
