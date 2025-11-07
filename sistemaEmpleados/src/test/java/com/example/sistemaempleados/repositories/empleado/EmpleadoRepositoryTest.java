package com.example.sistemaempleados.repositories.empleado;

import com.example.sistemaempleados.models.Departamento;
import com.example.sistemaempleados.models.Empleado;
import com.example.sistemaempleados.repositories.DepartamentoRepository;
import com.example.sistemaempleados.repositories.EmpleadoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public abstract class EmpleadoRepositoryTest {
    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    private Empleado empleado;

    @BeforeEach
    void setUp() {
        empleado = new Empleado(
                null,
                "Tadeo",
                "Drube",
                "t.drube@alumno.um.edu.ar",
                LocalDate.of(2025, 1, 1),
                85000.0,
                null,
                new HashSet<>()
        );
    }

    @Test
    void saveEmpleado() {
        Empleado savedEmpleado = empleadoRepository.save(empleado);

        assertNotNull(savedEmpleado);
        assertEquals("t.drube@alumno.um.edu.ar", savedEmpleado.getEmail());
    }

    @Test
    void updateEmpleado() {
        Empleado savedEmpleado = empleadoRepository.save(empleado);
        savedEmpleado.setEmail("tadeodp22@gmail.com");
        Empleado updatedEmpleado = empleadoRepository.save(savedEmpleado);

        assertNotNull(updatedEmpleado);
        assertEquals("tadeodp22@gmail.com", updatedEmpleado.getEmail());
    }

    @Test
    void deleteByIdEmpleado() {
        Empleado savedEmpleado = empleadoRepository.save(empleado);
        empleadoRepository.deleteById(savedEmpleado.getId());

        assertFalse(empleadoRepository.findById(savedEmpleado.getId()).isPresent());
    }

    @Test
    void findByIdEmpleado() {
        Empleado savedEmpleado = empleadoRepository.save(empleado);
        Optional<Empleado> foundEmpleado = empleadoRepository.findById(savedEmpleado.getId());

        assertEquals("t.drube@alumno.um.edu.ar", foundEmpleado.get().getEmail());
    }

    @Test
    void findAllEmpleado() {
        Empleado savedEmpleado = empleadoRepository.save(empleado);
        List<Empleado> foundEmpleados = empleadoRepository.findAll();

        assertEquals(1, foundEmpleados.size());
        assertEquals(empleado, foundEmpleados.getFirst());
    }

    @Test
    void findByDepartamentoEmpleado() {
        Departamento departamento = new Departamento(
                null,
                "Contabilidad",
                "Gestión de operaciones financieras",
                new ArrayList<>()
        );

        Departamento savedDepartamento = departamentoRepository.save(departamento);

        empleado.setDepartamento(savedDepartamento);
        Empleado savedEmpleado = empleadoRepository.save(empleado);

        List<Empleado> foundEmpleados = empleadoRepository.findByDepartamento("Contabilidad");

        assertEquals(1, foundEmpleados.size());
        assertEquals(empleado, foundEmpleados.getFirst());
    }

    @Test
    void findByFechaContratacionAfterEmpleado() {
        Empleado savedEmpleado = empleadoRepository.save(empleado);
        List<Empleado> foundEmpleados = empleadoRepository.findByFechaContratacionAfter(LocalDate.of(2024, 12, 31));

        assertEquals(1, foundEmpleados.size());
        assertEquals(empleado, foundEmpleados.getFirst());
    }

    @Test
    void findBySalarioBetweenEmpleado() {
        Empleado savedEmpleado = empleadoRepository.save(empleado);
        List<Empleado> foundEmpleados = empleadoRepository.findBySalarioBetween(80000.0, 90000.0);

        assertEquals(1, foundEmpleados.size());
        assertEquals(empleado, foundEmpleados.getFirst());
    }

    @Test
    void findAverageSalarioByDepartamentoEmpleado() {
        Departamento departamento = new Departamento(
                null,
                "Contabilidad",
                "Gestión de operaciones financieras",
                new ArrayList<>()
        );

        Departamento savedDepartamento = departamentoRepository.save(departamento);

        empleado.setDepartamento(savedDepartamento);
        Empleado savedEmpleado = empleadoRepository.save(empleado);

        Optional<Double> salarioPromedio = empleadoRepository.findAverageSalarioByDepartamento(savedEmpleado.getDepartamento().getId());

        assertEquals(85000.0, salarioPromedio.get());
    }

    @Test
    void findByEmailEmpleado() {
        Empleado savedEmpleado = empleadoRepository.save(empleado);
        Optional<Empleado> foundEmpleado = empleadoRepository.findByEmail(savedEmpleado.getEmail());

        assertEquals("t.drube@alumno.um.edu.ar", foundEmpleado.get().getEmail());
    }
}
