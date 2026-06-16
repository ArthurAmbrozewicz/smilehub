package com.smilehub.smilehub.util;

import com.smilehub.smilehub.entities.Administrador;
import com.smilehub.smilehub.entities.Dentista;
import com.smilehub.smilehub.entities.Paciente;
import com.smilehub.smilehub.entities.Usuario;
import org.hibernate.Hibernate;

public final class UsuarioDtypeUtil {

    private UsuarioDtypeUtil() {
    }

    public static String resolverDtype(Usuario usuario) {
        if (usuario instanceof Administrador) {
            return "ADMINISTRADOR";
        }
        if (usuario instanceof Paciente) {
            return "PACIENTE";
        }
        if (usuario instanceof Dentista) {
            return "DENTISTA";
        }

        String className = Hibernate.getClass(usuario).getSimpleName();
        return switch (className) {
            case "Administrador" -> "ADMINISTRADOR";
            case "Dentista" -> "DENTISTA";
            case "Paciente" -> "PACIENTE";
            default -> "USUARIO";
        };
    }
}
