package com.example.sistemaempleados.controllers;

import com.example.sistemaempleados.exceptions.ProyectoNoEncontradoException;
import com.example.sistemaempleados.models.Proyecto;
import com.example.sistemaempleados.services.ProyectoServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProyectoController.class)
public class ProyectoControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    ProyectoServiceImpl proyectoService;

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
    void getProyectos() throws Exception {
        when(proyectoService.obtenerTodos()).thenReturn(List.of(proyecto));

        mockMvc.perform(get("/api/proyectos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Capacitación y Desarrollo"));
    }

    @Test
    void getProyecto() throws Exception{
        when(proyectoService.buscarPorId(eq(1L))).thenReturn(proyecto);

        mockMvc.perform(get("/api/proyectos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Capacitación y Desarrollo"));
    }

    @Test
    void getProyectoNoExistente() throws Exception {
        when(proyectoService.buscarPorId(eq(1L))).thenThrow(new ProyectoNoEncontradoException(""));

        mockMvc.perform(get("/api/proyectos/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void postProyecto() throws Exception {
        when(proyectoService.guardar(any(Proyecto.class))).thenReturn(proyecto);

        mockMvc.perform(post("/api/proyectos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(proyecto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Capacitación y Desarrollo"));
    }

    @Test
    void putProyecto() throws Exception{
        when(proyectoService.actualizar(eq(1L), any(Proyecto.class))).thenReturn(proyecto);

        mockMvc.perform(put("/api/proyectos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(proyecto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Capacitación y Desarrollo"));
    }

    @Test
    void putProyectoNoExistente() throws Exception{
        when(proyectoService.actualizar(eq(1L), any(Proyecto.class))).thenThrow(new ProyectoNoEncontradoException(""));

        mockMvc.perform(put("/api/proyectos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(proyecto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteProyecto() throws Exception {
        doNothing().when(proyectoService).eliminar(eq(1L));

        mockMvc.perform(delete("/api/proyectos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteProyectoNoExistente() throws Exception {
        doThrow(new ProyectoNoEncontradoException("")).when(proyectoService).eliminar(eq(1L));

        mockMvc.perform(delete("/api/proyectos/1"))
                .andExpect(status().isNotFound());
    }
}
