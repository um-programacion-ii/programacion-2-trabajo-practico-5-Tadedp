package com.example.sistemaempleados.controllers;

import com.example.sistemaempleados.exceptions.DepartamentoNoEncontradoException;
import com.example.sistemaempleados.exceptions.NombreDuplicadoException;
import com.example.sistemaempleados.models.Departamento;
import com.example.sistemaempleados.services.DepartamentoServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DepartamentoController.class)
public class DepartamentoControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    DepartamentoServiceImpl departamentoService;

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
    void getDepartamentos() throws Exception {
        when(departamentoService.obtenerTodos()).thenReturn(List.of(departamento));

        mockMvc.perform(get("/api/departamentos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Contabilidad"));
    }

    @Test
    void getDepartamento() throws Exception{
        when(departamentoService.buscarPorId(eq(1L))).thenReturn(departamento);

        mockMvc.perform(get("/api/departamentos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Contabilidad"));
    }

    @Test
    void getDepartamentoNoExistente() throws Exception {
        when(departamentoService.buscarPorId(eq(1L))).thenThrow(new DepartamentoNoEncontradoException(""));

        mockMvc.perform(get("/api/departamentos/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void postDepartamento() throws Exception {
        when(departamentoService.guardar(any(Departamento.class))).thenReturn(departamento);

        mockMvc.perform(post("/api/departamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(departamento)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Contabilidad"));
    }

    @Test
    void postDepartamentoNombreDuplicado() throws Exception {
        when(departamentoService.guardar(any(Departamento.class))).thenThrow(new NombreDuplicadoException(""));

        mockMvc.perform(post("/api/departamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(departamento)))
                .andExpect(status().isConflict());
    }

    @Test
    void putDepartamento() throws Exception{
        when(departamentoService.actualizar(eq(1L), any(Departamento.class))).thenReturn(departamento);

        mockMvc.perform(put("/api/departamentos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(departamento)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Contabilidad"));
    }

    @Test
    void putDepartamentoNoExistente() throws Exception{
        when(departamentoService.actualizar(eq(1L), any(Departamento.class))).thenThrow(new DepartamentoNoEncontradoException(""));

        mockMvc.perform(put("/api/departamentos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(departamento)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteDepartamento() throws Exception {
        doNothing().when(departamentoService).eliminar(eq(1L));

        mockMvc.perform(delete("/api/departamentos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteDepartamentoNoExistente() throws Exception {
        doThrow(new DepartamentoNoEncontradoException("")).when(departamentoService).eliminar(eq(1L));

        mockMvc.perform(delete("/api/departamentos/1"))
                .andExpect(status().isNotFound());
    }
}
