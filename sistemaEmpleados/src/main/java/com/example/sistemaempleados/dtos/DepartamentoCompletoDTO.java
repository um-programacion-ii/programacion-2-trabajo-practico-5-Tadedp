package com.example.sistemaempleados.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartamentoCompletoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private List<EmpleadoDTO> empleados;
}
