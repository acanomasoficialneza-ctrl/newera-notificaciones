package com.newera.notificaciones.models;

import lombok.Data;

@Data
public class NotificacionRequest {
    private Integer idUsuarioDestino;
    private String correoDestino;
    private String titulo;
    private String mensaje;
}
