package com.example.sistemaempleados.controllers;

import com.example.sistemaempleados.exceptions.EmailDuplicadoException;
import com.example.sistemaempleados.exceptions.EmpleadoNoEncontradoException;
import com.example.sistemaempleados.models.Empleado;
import com.example.sistemaempleados.services.EmpleadoServiceImpl;
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

@WebMvcTest(EmpleadoController.class)
public class EmpleadoControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    EmpleadoServiceImpl empleadoService;

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
                new HashSet<>()
        );
    }

    @Test
    void getEmpleados() throws Exception {
        when(empleadoService.obtenerTodos()).thenReturn(List.of(empleado));

        mockMvc.perform(get("/api/empleados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("t.drube@alumno.um.edu.ar"));
    }

    @Test
    void getEmpleado() throws Exception{
        when(empleadoService.buscarPorId(eq(1L))).thenReturn(empleado);

        mockMvc.perform(get("/api/empleados/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("t.drube@alumno.um.edu.ar"));
    }

    @Test
    void getEmpleadoNoExistente() throws Exception {
        when(empleadoService.buscarPorId(eq(1L))).thenThrow(new EmpleadoNoEncontradoException(""));

        mockMvc.perform(get("/api/empleados/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void postEmpleado() throws Exception {
        when(empleadoService.guardar(any(Empleado.class))).thenReturn(empleado);

        mockMvc.perform(post("/api/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(empleado)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("t.drube@alumno.um.edu.ar"));
    }

    @Test
    void postEmpleadoEmailDuplicado() throws Exception {
        when(empleadoService.guardar(any(Empleado.class))).thenThrow(new EmailDuplicadoException(""));

        mockMvc.perform(post("/api/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(empleado)))
                .andExpect(status().isConflict());
    }

    @Test
    void putEmpleado() throws Exception{
        when(empleadoService.actualizar(eq(1L), any(Empleado.class))).thenReturn(empleado);

        mockMvc.perform(put("/api/empleados/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(empleado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("t.drube@alumno.um.edu.ar"));
    }

    @Test
    void putEmpleadoNoExistente() throws Exception{
        when(empleadoService.actualizar(eq(1L), any(Empleado.class))).thenThrow(new EmpleadoNoEncontradoException(""));

        mockMvc.perform(put("/api/empleados/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(empleado)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteEmpleado() throws Exception {
        doNothing().when(empleadoService).eliminar(eq(1L));

        mockMvc.perform(delete("/api/empleados/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteEmpleadoNoExistente() throws Exception {
        doThrow(new EmpleadoNoEncontradoException("")).when(empleadoService).eliminar(eq(1L));

        mockMvc.perform(delete("/api/empleados/1"))
                .andExpect(status().isNotFound());
    }
}
