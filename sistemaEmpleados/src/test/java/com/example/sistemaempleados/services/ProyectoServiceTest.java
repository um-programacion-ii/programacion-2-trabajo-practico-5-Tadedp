package com.example.sistemaempleados.services;

import com.example.sistemaempleados.exceptions.ProyectoNoEncontradoException;
import com.example.sistemaempleados.models.Proyecto;
import com.example.sistemaempleados.repositories.ProyectoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProyectoServiceTest {
    @Mock
    private ProyectoRepository proyectoRepository;

    @InjectMocks
    private ProyectoServiceImpl proyectoService;

    private Proyecto proyecto;

    @BeforeEach
    void setUp() {
        proyecto = new Proyecto(
                1L,
                "Capacitación y Desarrollo",
                "Organización de cursos, talleres y formaciones",
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31),
                new HashSet<>()
        );
    }

    @Test
    void guardarProyecto(){
        when(proyectoRepository.save(proyecto)).thenReturn(proyecto);

        Proyecto savedProyecto = proyectoService.guardar(proyecto);
        assertNotNull(savedProyecto);
        assertEquals("Capacitación y Desarrollo", savedProyecto.getNombre());
    }

    @Test
    void actualizarProyecto(){
        when(proyectoRepository.existsById(proyecto.getId())).thenReturn(true);
        when(proyectoRepository.save(proyecto)).thenReturn(proyecto);

        Proyecto updatedProyecto = proyectoService.actualizar(proyecto.getId(), proyecto);

        assertNotNull(updatedProyecto);
        assertEquals("Capacitación y Desarrollo", updatedProyecto.getNombre());
    }

    @Test
    void actualizarProyectoNoExistente(){
        when(proyectoRepository.existsById(proyecto.getId())).thenReturn(false);
        assertThrows(ProyectoNoEncontradoException.class,()->proyectoService.actualizar(proyecto.getId(), proyecto));
    }

    @Test
    void eliminarProyecto(){
        when(proyectoRepository.existsById(proyecto.getId())).thenReturn(true);
        proyectoService.eliminar(proyecto.getId());
        verify(proyectoRepository).deleteById(proyecto.getId());
    }

    @Test
    void eliminarProyectoNoExistente(){
        when(proyectoRepository.existsById(proyecto.getId())).thenReturn(false);
        assertThrows(ProyectoNoEncontradoException.class,()->proyectoService.eliminar(proyecto.getId()));
        verify(proyectoRepository, never()).deleteById(proyecto.getId());
    }

    @Test
    void buscarProyectoPorId(){
        when(proyectoRepository.findById(proyecto.getId())).thenReturn(Optional.of(proyecto));
        Proyecto foundProyecto = proyectoService.buscarPorId(proyecto.getId());

        assertEquals(proyecto, foundProyecto);
    }

    @Test
    void buscarProyectoNoExistentePorId(){
        when(proyectoRepository.findById(proyecto.getId())).thenReturn(Optional.empty());
        assertThrows(ProyectoNoEncontradoException.class,()->proyectoService.buscarPorId(proyecto.getId()));
    }

    @Test
    void obtenerTodosProyecto(){
        when(proyectoRepository.findAll()).thenReturn(List.of(proyecto));
        List<Proyecto> foundProyectos = proyectoService.obtenerTodos();

        assertEquals(1, foundProyectos.size());
        assertEquals(proyecto, foundProyectos.getFirst());
    }

    @Test
    void obtenerProyectosActivos(){
        when(proyectoRepository.findByFechaFinAfter(LocalDate.now())).thenReturn(List.of(proyecto));
        List<Proyecto> foundProyectos = proyectoService.obtenerProyectosActivos();

        assertEquals(1, foundProyectos.size());
        assertEquals(proyecto, foundProyectos.getFirst());
    }
}
