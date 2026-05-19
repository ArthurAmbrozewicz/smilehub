package com.smilehub.smilehub.config;

import com.smilehub.smilehub.services.PerfilService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class PerfilDataInitializer implements CommandLineRunner {

    private final PerfilService perfilService;

    public PerfilDataInitializer(PerfilService perfilService) {
        this.perfilService = perfilService;
    }

    @Override
    public void run(String... args) {
        perfilService.inicializarPerfisPadrao();
    }
}
