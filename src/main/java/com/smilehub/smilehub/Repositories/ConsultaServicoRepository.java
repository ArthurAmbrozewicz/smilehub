package com.smilehub.smilehub.repositories;

import com.smilehub.smilehub.entities.Consulta;
import com.smilehub.smilehub.entities.ConsultaServico;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultaServicoRepository extends JpaRepository<ConsultaServico, Long> {

    List<ConsultaServico> findAllByConsulta(Consulta consulta);

    List<ConsultaServico> findAllByConsultaId(Long consultaId);

    boolean existsByConsultaIdAndServicoId(Long consultaId, Long servicoId);

    void deleteByConsultaId(Long consultaId);
}
