package com.example.sistemaempleados.repositories;

import com.example.sistemaempleados.models.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {
    List<Proyecto> findByFechaFinAfter(LocalDate fechaActual);
}
