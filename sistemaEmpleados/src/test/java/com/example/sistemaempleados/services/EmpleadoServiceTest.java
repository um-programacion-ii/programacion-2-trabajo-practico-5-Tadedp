package com.example.sistemaempleados.services;

import com.example.sistemaempleados.exceptions.EmailDuplicadoException;
import com.example.sistemaempleados.exceptions.EmpleadoNoEncontradoException;
import com.example.sistemaempleados.models.Departamento;
import com.example.sistemaempleados.models.Empleado;
import com.example.sistemaempleados.repositories.EmpleadoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmpleadoServiceTest {
    @Mock
    private EmpleadoRepository empleadoRepository;

    @InjectMocks
    private EmpleadoServiceImpl empleadoService;

    private Empleado empleado;

    @BeforeEach
    void setUp() {
        empleado = new Empleado(
                1L,
                "Tadeo",
                "Drube",
                "t.drube@alumno.um.edu.ar",
                LocalDate.of(2025, 1, 1),
                85000.0,
                null,
                null
        );
    }

    @Test
    void guardarEmpleado(){
        when(empleadoRepository.findByEmail("t.drube@alumno.um.edu.ar")).thenReturn(Optional.empty());
        when(empleadoRepository.save(empleado)).thenReturn(empleado);

        Empleado savedEmpleado = empleadoService.guardar(empleado);
        assertNotNull(savedEmpleado);
        assertEquals("t.drube@alumno.um.edu.ar", savedEmpleado.getEmail());
    }

    @Test
    void guardarEmpleadoEmailDuplicado(){
        when(empleadoRepository.findByEmail("t.drube@alumno.um.edu.ar")).thenReturn(Optional.of(empleado));
        assertThrows(EmailDuplicadoException.class,()->empleadoService.guardar(empleado));
    }

    @Test
    void actualizarEmpleado(){
        when(empleadoRepository.existsById(empleado.getId())).thenReturn(true);
        when(empleadoRepository.save(empleado)).thenReturn(empleado);

        Empleado updatedEmpleado = empleadoService.actualizar(empleado.getId(), empleado);

        assertNotNull(updatedEmpleado);
        assertEquals("t.drube@alumno.um.edu.ar", updatedEmpleado.getEmail());
    }

    @Test
    void actualizarEmpleadoNoExistente(){
        when(empleadoRepository.existsById(empleado.getId())).thenReturn(false);
        assertThrows(EmpleadoNoEncontradoException.class,()->empleadoService.actualizar(empleado.getId(), empleado));
    }

    @Test
    void eliminarEmpleado(){
        when(empleadoRepository.existsById(empleado.getId())).thenReturn(true);
        empleadoService.eliminar(empleado.getId());
        verify(empleadoRepository).deleteById(empleado.getId());
    }

    @Test
    void eliminarEmpleadoNoExistente(){
        when(empleadoRepository.existsById(empleado.getId())).thenReturn(false);
        assertThrows(EmpleadoNoEncontradoException.class,()->empleadoService.eliminar(empleado.getId()));
        verify(empleadoRepository, never()).deleteById(empleado.getId());
    }

    @Test
    void buscarEmpleadoPorId(){
        when(empleadoRepository.findById(empleado.getId())).thenReturn(Optional.of(empleado));
        Empleado foundEmpleado = empleadoService.buscarPorId(empleado.getId());

        assertEquals(empleado, foundEmpleado);
    }

    @Test
    void buscarEmpleadoNoExistentePorId(){
        when(empleadoRepository.findById(empleado.getId())).thenReturn(Optional.empty());
        assertThrows(EmpleadoNoEncontradoException.class,()->empleadoService.buscarPorId(empleado.getId()));
    }

    @Test
    void obtenerTodosEmpleado(){
        when(empleadoRepository.findAll()).thenReturn(List.of(empleado));
        List<Empleado> foundEmpleados = empleadoService.obtenerTodos();

        assertEquals(1, foundEmpleados.size());
        assertEquals(empleado, foundEmpleados.getFirst());
    }

    @Test
    void buscarEmpleadosPorDepartamento(){
        Departamento departamento = new Departamento(
                1L,
                "Contabilidad",
                "Gestión de operaciones financieras",
                new ArrayList<>()
        );

        empleado.setDepartamento(departamento);

        when(empleadoRepository.findByDepartamento("Contabilidad")).thenReturn(List.of(empleado));
        List<Empleado> foundEmpleados = empleadoService.buscarPorDepartamento("Contabilidad");

        assertEquals(1, foundEmpleados.size());
        assertEquals(empleado, foundEmpleados.getFirst());
    }

    @Test
    void buscarEmpleadosContratados(){
        when(empleadoRepository.findByFechaContratacionAfter(LocalDate.of(2025, 1, 1))).thenReturn(List.of(empleado));
        List<Empleado> foundEmpleados = empleadoService.buscarPorContratados(LocalDate.of(2025, 1, 1));

        assertEquals(1, foundEmpleados.size());
        assertEquals(empleado, foundEmpleados.getFirst());
    }

    @Test
    void buscarEmpleadosPorRangoSalario(){
        when(empleadoRepository.findBySalarioBetween(80000.0, 90000.0)).thenReturn(List.of(empleado));
        List<Empleado> foundEmpleados = empleadoService.buscarPorRangoSalario(80000.0, 90000.0);

        assertEquals(1, foundEmpleados.size());
        assertEquals(empleado, foundEmpleados.getFirst());
    }

    @Test
    void obtenerSalarioPromedio(){
        when(empleadoRepository.findAverageSalarioByDepartamento(1L)).thenReturn(Optional.of(85000.0));
        Double salarioPromedio = empleadoService.obtenerSalarioPromedioPorDepartamento(1L);

        assertEquals(85000.0, salarioPromedio);
    }
}
