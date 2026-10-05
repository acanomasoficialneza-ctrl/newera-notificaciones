package com.newera.notificaciones.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "alertas_campanita")
public class AlertaCampanita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alerta")
    private Integer idAlerta;

    @Column(name = "id_usuario_destino")
    private Integer idUsuarioDestino;

    @Column(name = "titulo", length = 100, nullable = false)
    private String titulo;

    @Column(name = "mensaje", columnDefinition = "TEXT", nullable = false)
    private String mensaje;

    @Column(name = "tipo", length = 50)
    private String tipo;

    @Column(name = "leido")
    private Boolean leido;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
}
