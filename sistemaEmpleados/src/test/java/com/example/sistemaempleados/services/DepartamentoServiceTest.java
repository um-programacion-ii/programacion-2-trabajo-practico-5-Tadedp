package com.example.sistemaempleados.services;

import com.example.sistemaempleados.exceptions.DepartamentoNoEncontradoException;
import com.example.sistemaempleados.exceptions.NombreDuplicadoException;
import com.example.sistemaempleados.models.Departamento;
import com.example.sistemaempleados.repositories.DepartamentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DepartamentoServiceTest {
    @Mock
    private DepartamentoRepository departamentoRepository;

    @InjectMocks
    private DepartamentoServiceImpl departamentoService;

    private Departamento departamento;

    @BeforeEach
    void setUp() {
        departamento = new Departamento(
                1L,
                "Contabilidad",
                "Gestión de operaciones financieras",
                new ArrayList<>()
        );
    }

    @Test
    void guardarDepartamento(){
        when(departamentoRepository.findByNombre("Contabilidad")).thenReturn(Optional.empty());
        when(departamentoRepository.save(departamento)).thenReturn(departamento);

        Departamento savedDepartamento = departamentoService.guardar(departamento);
        assertNotNull(savedDepartamento);
        assertEquals("Contabilidad", savedDepartamento.getNombre());
    }

    @Test
    void guardarDepartamentoNombreDuplicado(){
        when(departamentoRepository.findByNombre("Contabilidad")).thenReturn(Optional.of(departamento));
        assertThrows(NombreDuplicadoException.class,()->departamentoService.guardar(departamento));
    }

    @Test
    void actualizarDepartamento(){
        when(departamentoRepository.existsById(departamento.getId())).thenReturn(true);
        when(departamentoRepository.save(departamento)).thenReturn(departamento);

        Departamento updatedDepartamento = departamentoService.actualizar(departamento.getId(), departamento);

        assertNotNull(updatedDepartamento);
        assertEquals("Contabilidad", updatedDepartamento.getNombre());
    }

    @Test
    void actualizarDepartamentoNoExistente(){
        when(departamentoRepository.existsById(departamento.getId())).thenReturn(false);
        assertThrows(DepartamentoNoEncontradoException.class,()->departamentoService.actualizar(departamento.getId(), departamento));
    }

    @Test
    void eliminarDepartamento(){
        when(departamentoRepository.existsById(departamento.getId())).thenReturn(true);
        departamentoService.eliminar(departamento.getId());
        verify(departamentoRepository).deleteById(departamento.getId());
    }

    @Test
    void eliminarDepartamentoNoExistente(){
        when(departamentoRepository.existsById(departamento.getId())).thenReturn(false);
        assertThrows(DepartamentoNoEncontradoException.class,()->departamentoService.eliminar(departamento.getId()));
        verify(departamentoRepository, never()).deleteById(departamento.getId());
    }

    @Test
    void buscarDepartamentoPorId(){
        when(departamentoRepository.findById(departamento.getId())).thenReturn(Optional.of(departamento));
        Departamento foundDepartamento = departamentoService.buscarPorId(departamento.getId());

        assertEquals(departamento, foundDepartamento);
    }

    @Test
    void buscarDepartamentoNoExistentePorId(){
        when(departamentoRepository.findById(departamento.getId())).thenReturn(Optional.empty());
        assertThrows(DepartamentoNoEncontradoException.class,()->departamentoService.buscarPorId(departamento.getId()));
    }

    @Test
    void obtenerTodosDepartamento(){
        when(departamentoRepository.findAll()).thenReturn(List.of(departamento));
        List<Departamento> foundDepartamentos = departamentoService.obtenerTodos();

        assertEquals(1, foundDepartamentos.size());
        assertEquals(departamento, foundDepartamentos.getFirst());
    }
}
