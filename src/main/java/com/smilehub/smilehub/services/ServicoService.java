package com.smilehub.smilehub.services;

import com.smilehub.smilehub.dto.ServicoMaterialItemDTO;
import com.smilehub.smilehub.dto.ServicoRequestDTO;
import com.smilehub.smilehub.dto.ServicoResponseDTO;
import com.smilehub.smilehub.dto.ServicoUpdateDTO;
import com.smilehub.smilehub.entities.Material;
import com.smilehub.smilehub.entities.Servico;
import com.smilehub.smilehub.entities.ServicoMaterial;
import com.smilehub.smilehub.entities.Usuario;
import com.smilehub.smilehub.exception.BusinessException;
import com.smilehub.smilehub.exception.ResourceNotFoundException;
import com.smilehub.smilehub.repositories.MaterialRepository;
import com.smilehub.smilehub.repositories.ServicoMaterialRepository;
import com.smilehub.smilehub.repositories.ServicoRepository;
import com.smilehub.smilehub.repositories.UsuarioRepository;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServicoService {

    private final ServicoRepository servicoRepository;
    private final ServicoMaterialRepository servicoMaterialRepository;
    private final MaterialRepository materialRepository;
    private final UsuarioRepository usuarioRepository;

    public ServicoService(
            ServicoRepository servicoRepository,
            ServicoMaterialRepository servicoMaterialRepository,
            MaterialRepository materialRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.servicoRepository = servicoRepository;
        this.servicoMaterialRepository = servicoMaterialRepository;
        this.materialRepository = materialRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public ServicoResponseDTO criar(ServicoRequestDTO request) {
        validarNome(request.nome());
        validarValor(request.valor());

        if (servicoRepository.existsByNome(request.nome())) {
            throw new BusinessException("Serviço já cadastrado");
        }

        Usuario usuario = buscarUsuarioAutenticado();
        boolean ativo = request.ativo() != null ? request.ativo() : true;
        Servico servico = new Servico(request.nome(), request.valor(), usuario);
        servico.setAtivo(ativo);

        Servico salvo = servicoRepository.save(servico);
        salvarMateriais(salvo, request.materiais());

        return montarResponse(salvo);
    }

    @Transactional
    public ServicoResponseDTO editar(Long id, ServicoUpdateDTO request) {
        validarNome(request.nome());
        validarValor(request.valor());

        Servico servico = buscarEntidadePorId(id);

        if (servicoRepository.existsByNomeAndIdNot(request.nome(), id)) {
            throw new BusinessException("Serviço já cadastrado");
        }

        servico.setNome(request.nome());
        servico.setValor(request.valor());
        if (request.ativo() != null) {
            servico.setAtivo(request.ativo());
        }

        Servico salvo = servicoRepository.save(servico);
        sincronizarMateriais(salvo, request.materiais());

        return montarResponse(salvo);
    }

    @Transactional
    public void desativar(Long id) {
        Servico servico = buscarEntidadePorId(id);

        if (!servico.isAtivo()) {
            throw new BusinessException("Serviço já está inativo");
        }

        servico.setAtivo(false);
        servicoRepository.save(servico);
    }

    @Transactional(readOnly = true)
    public List<ServicoResponseDTO> listar() {
        return servicoRepository.findAll().stream()
                .map(this::montarResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ServicoResponseDTO buscarPorId(Long id) {
        return montarResponse(buscarEntidadePorId(id));
    }

    @Transactional(readOnly = true)
    public Servico buscarEntidadePorId(Long id) {
        return servicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado: " + id));
    }

    private ServicoResponseDTO montarResponse(Servico servico) {
        List<ServicoMaterial> materiais = servicoMaterialRepository.findAllByServicoId(servico.getId());
        return ServicoResponseDTO.from(servico, materiais);
    }

    private void sincronizarMateriais(Servico servico, List<ServicoMaterialItemDTO> materiais) {
        servicoMaterialRepository.deleteByServicoId(servico.getId());
        salvarMateriais(servico, materiais);
    }

    private void salvarMateriais(Servico servico, List<ServicoMaterialItemDTO> materiais) {
        if (materiais == null || materiais.isEmpty()) {
            return;
        }

        validarMateriais(materiais);

        for (ServicoMaterialItemDTO item : materiais) {
            Material material = materialRepository.findById(item.materialId())
                    .orElseThrow(() -> new ResourceNotFoundException("Material não encontrado: " + item.materialId()));

            if (!material.isAtivo()) {
                throw new BusinessException("Material inativo: " + material.getNome());
            }

            servicoMaterialRepository.save(new ServicoMaterial(servico, material, item.quantidade()));
        }
    }

    private void validarMateriais(List<ServicoMaterialItemDTO> materiais) {
        Set<Long> idsMateriais = new HashSet<>();

        for (ServicoMaterialItemDTO item : materiais) {
            if (item.materialId() == null) {
                throw new BusinessException("Material é obrigatório");
            }

            if (!idsMateriais.add(item.materialId())) {
                throw new BusinessException("Material duplicado no serviço");
            }

            if (item.quantidade() == null || item.quantidade() <= 0) {
                throw new BusinessException("Quantidade do material deve ser maior que zero");
            }
        }
    }

    private Usuario buscarUsuarioAutenticado() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BusinessException("Usuário não autenticado");
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof String email) || email.isBlank()) {
            throw new BusinessException("Usuário não autenticado");
        }

        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }

    private void validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new BusinessException("Nome é obrigatório");
        }
    }

    private void validarValor(BigDecimal valor) {
        if (valor == null) {
            throw new BusinessException("Valor é obrigatório");
        }

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Valor deve ser maior que zero");
        }
    }
}
