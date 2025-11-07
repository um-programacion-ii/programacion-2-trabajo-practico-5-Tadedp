package com.example.sistemaempleados.repositories.proyecto;

import com.example.sistemaempleados.models.Proyecto;
import com.example.sistemaempleados.repositories.ProyectoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public abstract class ProyectoRepositoryTest {
    @Autowired
    private ProyectoRepository proyectoRepository;

    private Proyecto proyecto;

    @BeforeEach
    void setUp() {
        proyecto = new Proyecto(
                null,
                "Capacitación y Desarrollo",
                "Organización de cursos, talleres y formaciones",
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31),
                new HashSet<>()
        );
    }

    @Test
    void saveProyecto() {
        Proyecto savedProyecto = proyectoRepository.save(proyecto);

        assertNotNull(savedProyecto);
        assertEquals("Capacitación y Desarrollo", savedProyecto.getNombre());
    }

    @Test
    void updateProyecto() {
        Proyecto savedProyecto = proyectoRepository.save(proyecto);
        savedProyecto.setNombre("Capacitaciones y Desarrollos");
        Proyecto updatedProyecto = proyectoRepository.save(savedProyecto);

        assertNotNull(updatedProyecto);
        assertEquals("Capacitaciones y Desarrollos", updatedProyecto.getNombre());
    }

    @Test
    void deleteByIdProyecto() {
        Proyecto savedProyecto = proyectoRepository.save(proyecto);
        proyectoRepository.deleteById(savedProyecto.getId());

        assertFalse(proyectoRepository.findById(savedProyecto.getId()).isPresent());
    }

    @Test
    void findByIdProyecto() {
        Proyecto savedProyecto = proyectoRepository.save(proyecto);
        Optional<Proyecto> foundProyecto = proyectoRepository.findById(savedProyecto.getId());

        assertEquals("Capacitación y Desarrollo", foundProyecto.get().getNombre());
    }

    @Test
    void findAllProyecto() {
        Proyecto savedProyecto = proyectoRepository.save(proyecto);
        List<Proyecto> foundProyectos = proyectoRepository.findAll();

        assertEquals(1, foundProyectos.size());
        assertEquals(proyecto, foundProyectos.getFirst());
    }

    @Test
    void findByFechaFinAfterProyecto() {
        Proyecto savedProyecto = proyectoRepository.save(proyecto);
        List<Proyecto> foundProyectos = proyectoRepository.findByFechaFinAfter(LocalDate.now());

        assertEquals(1, foundProyectos.size());
        assertEquals(proyecto, foundProyectos.getFirst());
    }
}
