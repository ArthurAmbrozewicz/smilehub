package com.smilehub.smilehub.services;

import com.smilehub.smilehub.dto.ServicoRequestDTO;
import com.smilehub.smilehub.dto.ServicoResponseDTO;
import com.smilehub.smilehub.dto.ServicoUpdateDTO;
import com.smilehub.smilehub.entities.Servico;
import com.smilehub.smilehub.entities.Usuario;
import com.smilehub.smilehub.exception.BusinessException;
import com.smilehub.smilehub.exception.ResourceNotFoundException;
import com.smilehub.smilehub.repositories.ServicoRepository;
import com.smilehub.smilehub.repositories.UsuarioRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServicoService {

    private final ServicoRepository servicoRepository;
    private final UsuarioRepository usuarioRepository;

    public ServicoService(ServicoRepository servicoRepository, UsuarioRepository usuarioRepository) {
        this.servicoRepository = servicoRepository;
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

        return ServicoResponseDTO.from(servicoRepository.save(servico));
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

        return ServicoResponseDTO.from(servicoRepository.save(servico));
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
                .map(ServicoResponseDTO::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ServicoResponseDTO buscarPorId(Long id) {
        return ServicoResponseDTO.from(buscarEntidadePorId(id));
    }

    @Transactional(readOnly = true)
    public Servico buscarEntidadePorId(Long id) {
        return servicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado: " + id));
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
