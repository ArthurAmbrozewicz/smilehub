package com.smilehub.smilehub.services;

import com.smilehub.smilehub.dto.MaterialReporDTO;
import com.smilehub.smilehub.dto.MaterialRequestDTO;
import com.smilehub.smilehub.dto.MaterialResponseDTO;
import com.smilehub.smilehub.dto.MaterialUpdateDTO;
import com.smilehub.smilehub.entities.Material;
import com.smilehub.smilehub.entities.Usuario;
import com.smilehub.smilehub.exception.BusinessException;
import com.smilehub.smilehub.exception.ResourceNotFoundException;
import com.smilehub.smilehub.repositories.MaterialRepository;
import com.smilehub.smilehub.repositories.UsuarioRepository;
import com.smilehub.smilehub.util.UsuarioDtypeUtil;
import java.util.List;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MaterialService {

    private static final String DTYPE_ADMINISTRADOR = "ADMINISTRADOR";

    private final MaterialRepository materialRepository;
    private final UsuarioRepository usuarioRepository;

    public MaterialService(MaterialRepository materialRepository, UsuarioRepository usuarioRepository) {
        this.materialRepository = materialRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public MaterialResponseDTO criar(MaterialRequestDTO request) {
        validarAcessoAdmin();
        validarNome(request.nome());
        validarQuantidade(request.quantidade());

        if (materialRepository.existsByNome(request.nome())) {
            throw new BusinessException("Material já cadastrado");
        }

        Usuario usuario = buscarUsuarioAutenticado();
        boolean ativo = request.ativo() != null ? request.ativo() : true;
        Material material = new Material(request.nome(), request.quantidade(), usuario);
        material.setAtivo(ativo);

        return MaterialResponseDTO.from(materialRepository.save(material));
    }

    @Transactional
    public MaterialResponseDTO editar(Long id, MaterialUpdateDTO request) {
        validarAcessoAdmin();
        validarNome(request.nome());
        validarQuantidade(request.quantidade());

        Material material = buscarEntidadePorId(id);

        if (materialRepository.existsByNomeAndIdNot(request.nome(), id)) {
            throw new BusinessException("Material já cadastrado");
        }

        material.setNome(request.nome());

        int novaQuantidade = request.quantidade();
        if (novaQuantidade > material.getQuantidadeInicial()) {
            material.setQuantidadeInicial(novaQuantidade);
            material.setAlertaEstoqueBaixo(false);
        }

        material.setQuantidade(novaQuantidade);

        if (request.ativo() != null) {
            material.setAtivo(request.ativo());
        }

        atualizarFlagEstoqueBaixo(material);

        return MaterialResponseDTO.from(materialRepository.save(material));
    }

    @Transactional
    public MaterialResponseDTO reporEstoque(Long id, MaterialReporDTO request) {
        validarAcessoAdmin();
        validarQuantidadeRepor(request.quantidade());

        Material material = buscarEntidadePorId(id);

        if (!material.isAtivo()) {
            throw new BusinessException("Material inativo");
        }

        int novaQuantidade = material.getQuantidade() + request.quantidade();

        if (novaQuantidade > material.getQuantidadeInicial()) {
            material.setQuantidadeInicial(novaQuantidade);
        }

        material.setQuantidade(novaQuantidade);
        atualizarFlagEstoqueBaixo(material);

        return MaterialResponseDTO.from(materialRepository.save(material));
    }

    @Transactional
    public void desativar(Long id) {
        validarAcessoAdmin();
        Material material = buscarEntidadePorId(id);

        if (!material.isAtivo()) {
            throw new BusinessException("Material já está inativo");
        }

        material.setAtivo(false);
        materialRepository.save(material);
    }

    @Transactional(readOnly = true)
    public List<MaterialResponseDTO> listar() {
        validarAcessoAdmin();
        return materialRepository.findAll().stream()
                .map(MaterialResponseDTO::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public MaterialResponseDTO buscarPorId(Long id) {
        validarAcessoAdmin();
        return MaterialResponseDTO.from(buscarEntidadePorId(id));
    }

    @Transactional(readOnly = true)
    public Material buscarEntidadePorId(Long id) {
        return materialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Material não encontrado: " + id));
    }

    void atualizarFlagEstoqueBaixo(Material material) {
        if (!EstoqueService.estaComEstoqueBaixo(material)) {
            material.setAlertaEstoqueBaixo(false);
        }
    }

    private void validarAcessoAdmin() {
        if (!DTYPE_ADMINISTRADOR.equals(UsuarioDtypeUtil.resolverDtype(buscarUsuarioAutenticado()))) {
            throw new BusinessException("Acesso negado");
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

    private void validarQuantidadeRepor(Integer quantidade) {
        if (quantidade == null) {
            throw new BusinessException("Quantidade é obrigatória");
        }

        if (quantidade <= 0) {
            throw new BusinessException("Quantidade deve ser maior que zero");
        }
    }

    private void validarQuantidade(Integer quantidade) {
        if (quantidade == null) {
            throw new BusinessException("Quantidade é obrigatória");
        }

        if (quantidade < 0) {
            throw new BusinessException("Quantidade não pode ser negativa");
        }
    }
}
