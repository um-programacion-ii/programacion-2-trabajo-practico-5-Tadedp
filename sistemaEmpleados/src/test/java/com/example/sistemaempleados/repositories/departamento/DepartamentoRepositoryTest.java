package com.example.sistemaempleados.repositories.departamento;

import com.example.sistemaempleados.models.Departamento;
import com.example.sistemaempleados.repositories.DepartamentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public abstract class DepartamentoRepositoryTest {
    @Autowired
    private DepartamentoRepository departamentoRepository;

    private Departamento departamento;

    @BeforeEach
    void setUp() {
        departamento = new Departamento(
                null,
                "Contabilidad",
                "Gestión de operaciones financieras",
                new ArrayList<>()
        );
    }

    @Test
    void saveDepartamento() {
        Departamento savedDepartamento = departamentoRepository.save(departamento);

        assertNotNull(savedDepartamento);
        assertEquals("Contabilidad", savedDepartamento.getNombre());
    }

    @Test
    void updateDepartamento() {
        Departamento savedDepartamento = departamentoRepository.save(departamento);
        savedDepartamento.setNombre("Contaduría");
        Departamento updatedDepartamento = departamentoRepository.save(savedDepartamento);

        assertNotNull(updatedDepartamento);
        assertEquals("Contaduría", updatedDepartamento.getNombre());
    }

    @Test
    void deleteByIdDepartamento() {
        Departamento savedDepartamento = departamentoRepository.save(departamento);
        departamentoRepository.deleteById(savedDepartamento.getId());

        assertFalse(departamentoRepository.findById(savedDepartamento.getId()).isPresent());
    }

    @Test
    void findByIdDepartamento() {
        Departamento savedDepartamento = departamentoRepository.save(departamento);
        Optional<Departamento> foundDepartamento = departamentoRepository.findById(savedDepartamento.getId());

        assertEquals("Contabilidad", foundDepartamento.get().getNombre());
    }

    @Test
    void findByNombreDepartamento() {
        Departamento savedDepartamento = departamentoRepository.save(departamento);
        Optional<Departamento> foundDepartamento = departamentoRepository.findByNombre(savedDepartamento.getNombre());

        assertEquals("Contabilidad", foundDepartamento.get().getNombre());
    }

    @Test
    void findAllDepartamento() {
        Departamento savedDepartamento = departamentoRepository.save(departamento);
        List<Departamento> foundDepartamentos = departamentoRepository.findAll();

        assertEquals(1, foundDepartamentos.size());
        assertEquals(departamento, foundDepartamentos.getFirst());
    }
}
