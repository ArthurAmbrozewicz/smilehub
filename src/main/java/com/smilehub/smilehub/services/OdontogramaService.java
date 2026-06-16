package com.smilehub.smilehub.services;

import com.smilehub.smilehub.dto.DenteItemDTO;
import com.smilehub.smilehub.dto.OdontogramaResponseDTO;
import com.smilehub.smilehub.dto.OdontogramaSalvarDTO;
import com.smilehub.smilehub.entities.Dentista;
import com.smilehub.smilehub.entities.Dente;
import com.smilehub.smilehub.entities.Odontograma;
import com.smilehub.smilehub.entities.Paciente;
import com.smilehub.smilehub.entities.Usuario;
import com.smilehub.smilehub.exception.BusinessException;
import com.smilehub.smilehub.exception.ResourceNotFoundException;
import com.smilehub.smilehub.repositories.DenteRepository;
import com.smilehub.smilehub.repositories.OdontogramaRepository;
import com.smilehub.smilehub.repositories.UsuarioRepository;
import com.smilehub.smilehub.util.UsuarioDtypeUtil;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OdontogramaService {

    private static final String DTYPE_ADMINISTRADOR = "ADMINISTRADOR";
    private static final String DTYPE_DENTISTA = "DENTISTA";
    private static final String STATUS_HIGIDO = "higido";

    private final OdontogramaRepository odontogramaRepository;
    private final DenteRepository denteRepository;
    private final PacienteService pacienteService;
    private final DentistaService dentistaService;
    private final UsuarioRepository usuarioRepository;

    public OdontogramaService(
            OdontogramaRepository odontogramaRepository,
            DenteRepository denteRepository,
            PacienteService pacienteService,
            DentistaService dentistaService,
            UsuarioRepository usuarioRepository
    ) {
        this.odontogramaRepository = odontogramaRepository;
        this.denteRepository = denteRepository;
        this.pacienteService = pacienteService;
        this.dentistaService = dentistaService;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public OdontogramaResponseDTO buscarPorPacienteId(Long pacienteId) {
        validarAcesso();
        pacienteService.buscarEntidadePorId(pacienteId);

        Odontograma odontograma = odontogramaRepository.findByPacienteId(pacienteId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Odontograma não encontrado para o paciente: " + pacienteId
                ));

        return montarResponse(odontograma);
    }

    @Transactional
    public OdontogramaResponseDTO salvarPorPacienteId(Long pacienteId, OdontogramaSalvarDTO request) {
        validarAcesso();
        validarRequest(request);

        Paciente paciente = pacienteService.buscarEntidadePorId(pacienteId);
        Dentista dentista = resolverDentistaResponsavel();

        Odontograma odontograma = odontogramaRepository.findByPacienteId(pacienteId)
                .orElseGet(() -> new Odontograma(paciente, dentista, request.observacoes()));

        odontograma.setDentista(dentista);
        odontograma.setObservacoes(request.observacoes());
        odontograma.setDataUltimaAlteracao(LocalDateTime.now());

        odontograma = odontogramaRepository.save(odontograma);
        final Long odontogramaId = odontograma.getId();

        List<Dente> dentesExistentes = denteRepository.findAllByOdontogramaId(odontogramaId);
        Map<Integer, Dente> dentesPorNumero = dentesExistentes.stream()
                .collect(Collectors.toMap(Dente::getNumero, Function.identity(), (atual, duplicado) -> atual));

        Set<Integer> numerosMantidos = new HashSet<>();

        for (DenteItemDTO item : request.dentes()) {
            if (item.numero() == null) {
                throw new BusinessException("Número do dente é obrigatório");
            }

            String status = item.status() == null || item.status().isBlank()
                    ? STATUS_HIGIDO
                    : item.status().trim();

            String observacoes = item.observacoes() == null ? "" : item.observacoes().trim();

            if (STATUS_HIGIDO.equals(status) && observacoes.isEmpty()) {
                continue;
            }

            numerosMantidos.add(item.numero());

            Dente denteExistente = dentesPorNumero.get(item.numero());
            if (denteExistente != null) {
                denteExistente.setStatus(status);
                denteExistente.setObservacoes(observacoes);
                denteRepository.save(denteExistente);
                continue;
            }

            denteRepository.save(new Dente(odontograma, item.numero(), status, observacoes));
        }

        for (Dente denteExistente : dentesExistentes) {
            if (!numerosMantidos.contains(denteExistente.getNumero())) {
                denteRepository.delete(denteExistente);
            }
        }

        return montarResponse(odontograma);
    }

    private OdontogramaResponseDTO montarResponse(Odontograma odontograma) {
        List<Dente> dentes = denteRepository.findAllByOdontogramaId(odontograma.getId());
        return OdontogramaResponseDTO.from(odontograma, dentes);
    }

    private Dentista resolverDentistaResponsavel() {
        Usuario usuario = buscarUsuarioAutenticado();
        String dtype = UsuarioDtypeUtil.resolverDtype(usuario);

        if (DTYPE_DENTISTA.equals(dtype)) {
            return dentistaService.buscarEntidadePorId(usuario.getId());
        }

        if (DTYPE_ADMINISTRADOR.equals(dtype)) {
            throw new BusinessException("Administrador não pode salvar odontograma sem perfil de dentista");
        }

        throw new BusinessException("Acesso negado");
    }

    private void validarAcesso() {
        String dtype = UsuarioDtypeUtil.resolverDtype(buscarUsuarioAutenticado());

        if (DTYPE_ADMINISTRADOR.equals(dtype) || DTYPE_DENTISTA.equals(dtype)) {
            return;
        }

        throw new BusinessException("Acesso negado");
    }

    private void validarRequest(OdontogramaSalvarDTO request) {
        if (request == null || request.dentes() == null) {
            throw new BusinessException("Lista de dentes é obrigatória");
        }

        Set<Integer> numeros = new HashSet<>();
        for (DenteItemDTO item : request.dentes()) {
            if (item.numero() == null) {
                continue;
            }
            if (!numeros.add(item.numero())) {
                throw new BusinessException("Dente duplicado: " + item.numero());
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
}
