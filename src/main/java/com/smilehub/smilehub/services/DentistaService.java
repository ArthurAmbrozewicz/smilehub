package com.smilehub.smilehub.services;

import com.smilehub.smilehub.dto.DentistaEditarDTO;
import com.smilehub.smilehub.dto.DentistaRequestDTO;
import com.smilehub.smilehub.dto.DentistaResponseDTO;
import com.smilehub.smilehub.dto.EspecialidadeResumoDTO;
import com.smilehub.smilehub.dto.EspecialidadeResponseDTO;
import com.smilehub.smilehub.entities.Dentista;
import com.smilehub.smilehub.entities.DentistaEspecialidade;
import com.smilehub.smilehub.entities.Especialidade;
import com.smilehub.smilehub.exception.BusinessException;
import com.smilehub.smilehub.exception.ResourceNotFoundException;
import com.smilehub.smilehub.repositories.DentistaEspecialidadeRepository;
import com.smilehub.smilehub.repositories.DentistaRepository;
import com.smilehub.smilehub.repositories.UsuarioRepository;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DentistaService {

    private final DentistaRepository dentistaRepository;
    private final UsuarioRepository usuarioRepository;
    private final DentistaEspecialidadeRepository dentistaEspecialidadeRepository;
    private final EspecialidadeService especialidadeService;
    private final PasswordEncoder passwordEncoder;

    public DentistaService(
            DentistaRepository dentistaRepository,
            UsuarioRepository usuarioRepository,
            DentistaEspecialidadeRepository dentistaEspecialidadeRepository,
            EspecialidadeService especialidadeService,
            PasswordEncoder passwordEncoder
    ) {
        this.dentistaRepository = dentistaRepository;
        this.usuarioRepository = usuarioRepository;
        this.dentistaEspecialidadeRepository = dentistaEspecialidadeRepository;
        this.especialidadeService = especialidadeService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public DentistaResponseDTO criar(DentistaRequestDTO request) {
        validarRequest(request);

        if (usuarioRepository.existsByEmail(request.email())) {
            throw new BusinessException("E-mail já cadastrado");
        }
        if (dentistaRepository.existsByCpf(request.cpf())) {
            throw new BusinessException("CPF já cadastrado");
        }
        if (dentistaRepository.existsByCro(request.cro())) {
            throw new BusinessException("CRO já cadastrado");
        }

        boolean ativo = request.ativo() != null ? request.ativo() : true;
        String senhaCriptografada = passwordEncoder.encode(request.senha());

        Dentista dentista = new Dentista(
                request.nome(),
                request.email(),
                senhaCriptografada,
                ativo,
                request.cpf(),
                request.cro()
        );

        return DentistaResponseDTO.from(dentistaRepository.save(dentista));
    }

    @Transactional
    public DentistaResponseDTO editar(Long id, DentistaEditarDTO request) {
        validarEditarRequest(request);

        Dentista dentista = buscarEntidadePorId(id);

        if (usuarioRepository.existsByEmailAndIdNot(request.email(), id)) {
            throw new BusinessException("E-mail já cadastrado");
        }
        if (dentistaRepository.existsByCpfAndIdNot(request.cpf(), id)) {
            throw new BusinessException("CPF já cadastrado");
        }

        dentista.setNome(request.nome());
        dentista.setEmail(request.email());
        dentista.setCpf(request.cpf());
        dentista.setCro(request.cro());
        if (request.senha() != null && !request.senha().isBlank()) {
            dentista.setSenha(passwordEncoder.encode(request.senha()));
        }
        if (request.ativo() != null) {
            dentista.setAtivo(request.ativo());
        }

        return DentistaResponseDTO.from(dentistaRepository.save(dentista));
    }

    @Transactional
    public void adicionarEspecialidade(Long dentistaId, Long especialidadeId) {
        Dentista dentista = buscarEntidadePorId(dentistaId);
        Especialidade especialidade = especialidadeService.buscarEntidadePorId(especialidadeId);

        if (dentistaEspecialidadeRepository.existsByDentistaAndEspecialidade(dentista, especialidade)) {
            throw new BusinessException("Dentista já possui esta especialidade");
        }

        dentistaEspecialidadeRepository.save(new DentistaEspecialidade(dentista, especialidade));
    }

    @Transactional
    public void removerEspecialidade(Long dentistaId, Long especialidadeId) {
        Dentista dentista = buscarEntidadePorId(dentistaId);
        Especialidade especialidade = especialidadeService.buscarEntidadePorId(especialidadeId);

        DentistaEspecialidade vinculo = dentistaEspecialidadeRepository.findAllByDentista(dentista).stream()
                .filter(v -> v.getEspecialidade().getId().equals(especialidade.getId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Vínculo não encontrado"));

        dentistaEspecialidadeRepository.delete(vinculo);
    }

    @Transactional(readOnly = true)
    public List<EspecialidadeResponseDTO> listarEspecialidades(Long dentistaId) {
        Dentista dentista = buscarEntidadePorId(dentistaId);

        return dentistaEspecialidadeRepository.findAllByDentista(dentista).stream()
                .map(DentistaEspecialidade::getEspecialidade)
                .map(EspecialidadeResponseDTO::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<DentistaResponseDTO> listar() {
        return dentistaRepository.findAll().stream()
                .map(this::mapearComEspecialidades)
                .toList();
    }

    @Transactional(readOnly = true)
    public DentistaResponseDTO buscarPorId(Long id) {
        Dentista dentista = buscarEntidadePorId(id);
        return mapearComEspecialidades(dentista);
    }

    @Transactional(readOnly = true)
    public Dentista buscarEntidadePorId(Long id) {
        return dentistaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dentista não encontrado: " + id));
    }

    private DentistaResponseDTO mapearComEspecialidades(Dentista dentista) {
        List<EspecialidadeResumoDTO> especialidades = dentistaEspecialidadeRepository.findAllByDentista(dentista).stream()
                .map(v -> EspecialidadeResumoDTO.from(v.getEspecialidade()))
                .toList();

        return DentistaResponseDTO.from(dentista, especialidades);
    }

    private void validarRequest(DentistaRequestDTO request) {
        if (request.nome() == null || request.nome().isBlank()) {
            throw new BusinessException("Nome é obrigatório");
        }
        if (request.email() == null || request.email().isBlank()) {
            throw new BusinessException("E-mail é obrigatório");
        }
        if (request.senha() == null || request.senha().isBlank()) {
            throw new BusinessException("Senha é obrigatória");
        }
        if (request.cpf() == null || request.cpf().isBlank()) {
            throw new BusinessException("CPF é obrigatório");
        }
        if (request.cro() == null || request.cro().isBlank()) {
            throw new BusinessException("CRO é obrigatório");
        }
    }

    private void validarEditarRequest(DentistaEditarDTO request) {
        if (request.nome() == null || request.nome().isBlank()) {
            throw new BusinessException("Nome é obrigatório");
        }
        if (request.email() == null || request.email().isBlank()) {
            throw new BusinessException("E-mail é obrigatório");
        }
        if (request.cpf() == null || request.cpf().isBlank()) {
            throw new BusinessException("CPF é obrigatório");
        }
        if (request.cro() == null || request.cro().isBlank()) {
            throw new BusinessException("CRO é obrigatório");
        }
    }
}
