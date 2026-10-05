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

    private final java.util.Map<Integer, org.springframework.web.servlet.mvc.method.annotation.SseEmitter> notificationEmitters = new java.util.concurrent.ConcurrentHashMap<>();

    public org.springframework.web.servlet.mvc.method.annotation.SseEmitter subscribe(Integer idUsuario) {
        org.springframework.web.servlet.mvc.method.annotation.SseEmitter emitter = new org.springframework.web.servlet.mvc.method.annotation.SseEmitter(3600000L); // 1 hora
        notificationEmitters.put(idUsuario, emitter);

        Runnable cleanup = () -> notificationEmitters.remove(idUsuario);
        emitter.onCompletion(cleanup);
        emitter.onTimeout(cleanup);
        emitter.onError(e -> cleanup.run());

        // Enviar estado inicial inmediatamente
        pushToUser(idUsuario);

        return emitter;
    }

    private void pushToUser(Integer idUsuario) {
        org.springframework.web.servlet.mvc.method.annotation.SseEmitter emitter = notificationEmitters.get(idUsuario);
        if (emitter != null) {
            try {
                emitter.send(org.springframework.web.servlet.mvc.method.annotation.SseEmitter.event()
                        .name("notifications-update")
                        .data(alertaRepository.findByIdUsuarioDestinoOrderByFechaCreacionDesc(idUsuario)));
            } catch (Exception e) {
                notificationEmitters.remove(idUsuario);
            }
        }
    }

    public void procesarNotificacion(NotificacionRequest request) {
        // 1. Guardar en Base de Datos (Campanita)
        if (request.getIdUsuarioDestino() != null) {
            AlertaCampanita alerta = new AlertaCampanita();
            alerta.setIdUsuarioDestino(request.getIdUsuarioDestino());
            alerta.setTitulo(request.getTitulo());
            alerta.setMensaje(request.getMensaje());
            alerta.setTipo(request.getTipo() != null ? request.getTipo() : "INFO");
            alerta.setLeido(false);
            alerta.setFechaCreacion(LocalDateTime.now());
            alertaRepository.save(alerta);

            // 2. Empujar por SSE
            pushToUser(request.getIdUsuarioDestino());
        }
    }

    public void marcarComoLeida(Integer idAlerta) {
        alertaRepository.findById(idAlerta).ifPresent(alerta -> {
            alerta.setLeido(true);
            alertaRepository.save(alerta);
            
            // Empujar por SSE
            pushToUser(alerta.getIdUsuarioDestino());
        });
    }
}
