package com.example.sistemaempleados.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartamentoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
}
