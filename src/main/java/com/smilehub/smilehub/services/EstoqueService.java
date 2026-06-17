package com.smilehub.smilehub.services;

import com.smilehub.smilehub.entities.Consulta;
import com.smilehub.smilehub.entities.ConsultaServico;
import com.smilehub.smilehub.entities.Material;
import com.smilehub.smilehub.entities.ServicoMaterial;
import com.smilehub.smilehub.exception.BusinessException;
import com.smilehub.smilehub.repositories.ConsultaServicoRepository;
import com.smilehub.smilehub.repositories.MaterialRepository;
import com.smilehub.smilehub.repositories.ServicoMaterialRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EstoqueService {

    private final MaterialRepository materialRepository;
    private final ServicoMaterialRepository servicoMaterialRepository;
    private final ConsultaServicoRepository consultaServicoRepository;
    private final NotificacaoService notificacaoService;

    public EstoqueService(
            MaterialRepository materialRepository,
            ServicoMaterialRepository servicoMaterialRepository,
            ConsultaServicoRepository consultaServicoRepository,
            NotificacaoService notificacaoService
    ) {
        this.materialRepository = materialRepository;
        this.servicoMaterialRepository = servicoMaterialRepository;
        this.consultaServicoRepository = consultaServicoRepository;
        this.notificacaoService = notificacaoService;
    }

    @Transactional
    public void debitarConsulta(Consulta consulta) {
        Map<Long, Integer> consumo = calcularConsumoConsulta(consulta.getId());
        validarDisponibilidade(consumo);
        aplicarMovimentacao(consumo, -1);
    }

    @Transactional
    public void creditarConsulta(Consulta consulta) {
        Map<Long, Integer> consumo = calcularConsumoConsulta(consulta.getId());
        aplicarMovimentacao(consumo, 1);
    }

    static boolean estaComEstoqueBaixo(Material material) {
        if (material.getQuantidadeInicial() <= 0) {
            return false;
        }

        double limite = material.getQuantidadeInicial() * 0.10;
        return material.getQuantidade() <= limite;
    }

    private Map<Long, Integer> calcularConsumoConsulta(Long consultaId) {
        List<ConsultaServico> servicosConsulta = consultaServicoRepository.findAllByConsultaId(consultaId);
        Map<Long, Integer> consumo = new HashMap<>();

        for (ConsultaServico itemConsulta : servicosConsulta) {
            List<ServicoMaterial> materiaisServico = servicoMaterialRepository.findAllByServicoId(
                    itemConsulta.getServico().getId()
            );

            for (ServicoMaterial item : materiaisServico) {
                Long materialId = item.getMaterial().getId();
                consumo.merge(materialId, item.getQuantidade(), Integer::sum);
            }
        }

        return consumo;
    }

    private void validarDisponibilidade(Map<Long, Integer> consumo) {
        for (Map.Entry<Long, Integer> entry : consumo.entrySet()) {
            Material material = materialRepository.findById(entry.getKey())
                    .orElseThrow(() -> new BusinessException("Material não encontrado"));

            if (!material.isAtivo()) {
                throw new BusinessException("Material inativo: " + material.getNome());
            }

            if (material.getQuantidade() < entry.getValue()) {
                throw new BusinessException(
                        "Estoque insuficiente de \"" + material.getNome() + "\". Disponível: "
                                + material.getQuantidade() + ", necessário: " + entry.getValue()
                );
            }
        }
    }

    private void aplicarMovimentacao(Map<Long, Integer> consumo, int direcao) {
        for (Map.Entry<Long, Integer> entry : consumo.entrySet()) {
            Material material = materialRepository.findById(entry.getKey())
                    .orElseThrow(() -> new BusinessException("Material não encontrado"));

            int novaQuantidade = material.getQuantidade() + (entry.getValue() * direcao);

            if (novaQuantidade < 0) {
                throw new BusinessException("Estoque insuficiente de \"" + material.getNome() + "\"");
            }

            material.setQuantidade(novaQuantidade);

            if (!estaComEstoqueBaixo(material)) {
                material.setAlertaEstoqueBaixo(false);
            }

            Material salvo = materialRepository.save(material);

            if (estaComEstoqueBaixo(salvo) && !salvo.isAlertaEstoqueBaixo()) {
                salvo.setAlertaEstoqueBaixo(true);
                materialRepository.save(salvo);
                notificacaoService.notificarEstoqueBaixo(salvo);
            }
        }
    }
}
