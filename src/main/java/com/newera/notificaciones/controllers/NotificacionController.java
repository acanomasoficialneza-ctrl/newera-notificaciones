package com.newera.notificaciones.controllers;

import com.newera.notificaciones.models.AlertaCampanita;
import com.newera.notificaciones.models.NotificacionRequest;
import com.newera.notificaciones.repositories.AlertaCampanitaRepository;
import com.newera.notificaciones.services.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notificaciones")
@RequiredArgsConstructor
public class NotificacionController {

    private final NotificacionService notificacionService;
    private final AlertaCampanitaRepository alertaRepository;

    @GetMapping(value = "/stream/{idUsuario}", produces = org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE)
    public org.springframework.web.servlet.mvc.method.annotation.SseEmitter streamNotificaciones(@PathVariable Integer idUsuario) {
        return notificacionService.subscribe(idUsuario);
    }

    @PostMapping("/enviar")
    public ResponseEntity<String> enviarNotificacion(@RequestBody NotificacionRequest request) {
        notificacionService.procesarNotificacion(request);
        return ResponseEntity.ok("Notificación procesada exitosamente");
    }

    @GetMapping("/campanita/{idUsuario}")
    public ResponseEntity<List<AlertaCampanita>> getNotificacionesCampanita(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(alertaRepository.findByIdUsuarioDestinoOrderByFechaCreacionDesc(idUsuario));
    }

    @PutMapping("/campanita/{idAlerta}/leer")
    public ResponseEntity<String> marcarComoLeida(@PathVariable Integer idAlerta) {
        notificacionService.marcarComoLeida(idAlerta);
        return ResponseEntity.ok("Notificación marcada como leída");
    }
}
