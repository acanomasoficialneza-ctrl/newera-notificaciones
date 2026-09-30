package com.newera.notificaciones.services;

import com.newera.notificaciones.models.AlertaCampanita;
import com.newera.notificaciones.models.NotificacionRequest;
import com.newera.notificaciones.repositories.AlertaCampanitaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificacionService {

    private final AlertaCampanitaRepository alertaRepository;

    public void procesarNotificacion(NotificacionRequest request) {
        // 1. Guardar en Base de Datos (Campanita)
        if (request.getIdUsuarioDestino() != null) {
            AlertaCampanita alerta = new AlertaCampanita();
            alerta.setIdUsuarioDestino(request.getIdUsuarioDestino());
            alerta.setTitulo(request.getTitulo());
            alerta.setMensaje(request.getMensaje());
            alerta.setLeido(false);
            alerta.setFechaCreacion(LocalDateTime.now());
            alertaRepository.save(alerta);
        }
    }

    public void marcarComoLeida(Integer idAlerta) {
        alertaRepository.findById(idAlerta).ifPresent(alerta -> {
            alerta.setLeido(true);
            alertaRepository.save(alerta);
        });
    }
}
