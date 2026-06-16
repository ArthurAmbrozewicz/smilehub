package com.smilehub.smilehub.services;

import com.smilehub.smilehub.dto.NotificacaoEventoDTO;
import com.smilehub.smilehub.dto.NotificacaoResponseDTO;
import com.smilehub.smilehub.dto.NotificacaoResumoDTO;
import com.smilehub.smilehub.entities.Administrador;
import com.smilehub.smilehub.entities.Consulta;
import com.smilehub.smilehub.entities.Notificacao;
import com.smilehub.smilehub.entities.Usuario;
import com.smilehub.smilehub.exception.BusinessException;
import com.smilehub.smilehub.exception.ResourceNotFoundException;
import com.smilehub.smilehub.repositories.AdministradorRepository;
import com.smilehub.smilehub.repositories.NotificacaoRepository;
import com.smilehub.smilehub.repositories.UsuarioRepository;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class NotificacaoService {

    private static final DateTimeFormatter FORMATO_DATA_HORA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");

    private final NotificacaoRepository notificacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AdministradorRepository administradorRepository;
    private final NotificacaoSseService notificacaoSseService;

    public NotificacaoService(
            NotificacaoRepository notificacaoRepository,
            UsuarioRepository usuarioRepository,
            AdministradorRepository administradorRepository,
            NotificacaoSseService notificacaoSseService
    ) {
        this.notificacaoRepository = notificacaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.administradorRepository = administradorRepository;
        this.notificacaoSseService = notificacaoSseService;
    }

    @Transactional
    public void notificarConsultaCriada(Consulta consulta) {
        String descricao = montarDescricaoConsultaCriada(consulta);
        Set<Long> destinatarios = resolverDestinatariosConsultaCriada(consulta);

        for (Long destinatarioId : destinatarios) {
            Usuario destinatario = usuarioRepository.findById(destinatarioId)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + destinatarioId));

            Notificacao salva = notificacaoRepository.save(new Notificacao(descricao, destinatario));
            long naoLidas = notificacaoRepository.countByUsuarioIdAndLidaFalse(destinatarioId);

            notificacaoSseService.publicar(
                    destinatarioId,
                    new NotificacaoEventoDTO(naoLidas, NotificacaoResponseDTO.from(salva))
            );
        }
    }

    @Transactional(readOnly = true)
    public SseEmitter abrirStream() {
        Usuario usuario = buscarUsuarioAutenticado();
        NotificacaoResumoDTO resumo = new NotificacaoResumoDTO(
                notificacaoRepository.countByUsuarioIdAndLidaFalse(usuario.getId())
        );
        return notificacaoSseService.conectar(usuario.getId(), resumo);
    }

    @Transactional(readOnly = true)
    public List<NotificacaoResponseDTO> listarDoUsuarioLogado() {
        Usuario usuario = buscarUsuarioAutenticado();
        return notificacaoRepository.findByUsuarioIdOrderByDataDesc(usuario.getId()).stream()
                .map(NotificacaoResponseDTO::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public NotificacaoResumoDTO resumoDoUsuarioLogado() {
        Usuario usuario = buscarUsuarioAutenticado();
        long naoLidas = notificacaoRepository.countByUsuarioIdAndLidaFalse(usuario.getId());
        return new NotificacaoResumoDTO(naoLidas);
    }

    @Transactional
    public NotificacaoResponseDTO marcarComoLida(Long id) {
        Usuario usuario = buscarUsuarioAutenticado();
        Notificacao notificacao = buscarNotificacaoDoUsuario(id, usuario.getId());
        notificacao.setLida(true);
        return NotificacaoResponseDTO.from(notificacaoRepository.save(notificacao));
    }

    @Transactional
    public void marcarTodasComoLidas() {
        Usuario usuario = buscarUsuarioAutenticado();
        List<Notificacao> notificacoes = notificacaoRepository.findByUsuarioIdOrderByDataDesc(usuario.getId());

        for (Notificacao notificacao : notificacoes) {
            if (!notificacao.isLida()) {
                notificacao.setLida(true);
            }
        }

        notificacaoRepository.saveAll(notificacoes);
    }

    private Notificacao buscarNotificacaoDoUsuario(Long id, Long usuarioId) {
        Notificacao notificacao = notificacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notificação não encontrada: " + id));

        if (!notificacao.getUsuario().getId().equals(usuarioId)) {
            throw new BusinessException("Notificação não pertence ao usuário autenticado");
        }

        return notificacao;
    }

    private Set<Long> resolverDestinatariosConsultaCriada(Consulta consulta) {
        Set<Long> destinatarios = new HashSet<>();
        Long criadorId = consulta.getUsuario().getId();
        Long dentistaId = consulta.getDentista().getId();

        if (!criadorId.equals(dentistaId)) {
            destinatarios.add(dentistaId);
        }

        administradorRepository.findAll().stream()
                .filter(Administrador::isAtivo)
                .map(Administrador::getId)
                .filter(id -> !id.equals(criadorId))
                .forEach(destinatarios::add);

        return destinatarios;
    }

    private String montarDescricaoConsultaCriada(Consulta consulta) {
        String nomeUsuario = consulta.getUsuario().getNome();
        String nomePaciente = consulta.getPaciente().getNome();
        String dataHorario = consulta.getDataInicio().format(FORMATO_DATA_HORA);

        return String.format(
                "O usuário %s marcou uma consulta com o paciente %s em %s.",
                nomeUsuario,
                nomePaciente,
                dataHorario
        );
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
}
