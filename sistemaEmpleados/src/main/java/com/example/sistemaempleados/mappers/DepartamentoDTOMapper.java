package com.example.sistemaempleados.mappers;

import com.example.sistemaempleados.dtos.DepartamentoCompletoDTO;
import com.example.sistemaempleados.dtos.DepartamentoDTO;
import com.example.sistemaempleados.models.Departamento;

public class DepartamentoDTOMapper {
    public static DepartamentoDTO toDTO(Departamento departamento) {
        if (departamento == null) {
            return null;
        }

        return new DepartamentoDTO(
                departamento.getId(),
                departamento.getNombre(),
                departamento.getDescripcion()
        );
    }

    public static DepartamentoCompletoDTO toDTOCompleto(Departamento departamento) {
        if (departamento == null) {
            return null;
        }

        return new DepartamentoCompletoDTO(
                departamento.getId(),
                departamento.getNombre(),
                departamento.getDescripcion(),
                departamento.getEmpleados()
                        .stream()
                        .map(EmpleadoDTOMapper::toDTO)
                        .toList()
        );
    }
}
