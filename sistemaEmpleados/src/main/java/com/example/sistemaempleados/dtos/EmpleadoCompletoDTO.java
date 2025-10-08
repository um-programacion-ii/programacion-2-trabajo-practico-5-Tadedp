package com.example.sistemaempleados.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpleadoCompletoDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private LocalDate fechaContratacion;
    private Double salario;
    private DepartamentoDTO departamento;
    private List<ProyectoDTO> proyectos;
}
