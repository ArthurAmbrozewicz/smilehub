package com.smilehub.smilehub.config;

import com.smilehub.smilehub.entities.Administrador;
import com.smilehub.smilehub.repositories.AdministradorRepository;
import com.smilehub.smilehub.repositories.UsuarioRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInitializer implements ApplicationRunner {

    private static final String ADMIN_EMAIL = "admin@smilehub.com";

    private final UsuarioRepository usuarioRepository;
    private final AdministradorRepository administradorRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(
            UsuarioRepository usuarioRepository,
            AdministradorRepository administradorRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.usuarioRepository = usuarioRepository;
        this.administradorRepository = administradorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (usuarioRepository.existsByEmail(ADMIN_EMAIL)) {
            return;
        }

        Administrador administrador = new Administrador(
                "Administrador",
                ADMIN_EMAIL,
                passwordEncoder.encode("admin123"),
                true
        );

        administradorRepository.save(administrador);
    }
}
