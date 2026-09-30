package com.newera.notificaciones.repositories;

import com.newera.notificaciones.models.AlertaCampanita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertaCampanitaRepository extends JpaRepository<AlertaCampanita, Integer> {
    List<AlertaCampanita> findByIdUsuarioDestinoOrderByFechaCreacionDesc(Integer idUsuarioDestino);
}
