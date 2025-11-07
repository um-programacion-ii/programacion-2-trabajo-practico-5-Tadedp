package com.example.sistemaempleados.mappers;

import com.example.sistemaempleados.dtos.EmpleadoCompletoDTO;
import com.example.sistemaempleados.dtos.EmpleadoDTO;
import com.example.sistemaempleados.models.Empleado;

public class EmpleadoDTOMapper {
    public static EmpleadoDTO toDTO(Empleado empleado) {
        if (empleado == null) {
            return null;
        }

        return new EmpleadoDTO(
                empleado.getId(),
                empleado.getNombre(),
                empleado.getApellido(),
                empleado.getEmail(),
                empleado.getFechaContratacion(),
                empleado.getSalario()
        );
    }

    public static EmpleadoCompletoDTO toDTOCompleto(Empleado empleado) {
        if (empleado == null) {
            return null;
        }

        return new EmpleadoCompletoDTO(
                empleado.getId(),
                empleado.getNombre(),
                empleado.getApellido(),
                empleado.getEmail(),
                empleado.getFechaContratacion(),
                empleado.getSalario(),
                DepartamentoDTOMapper.toDTO(empleado.getDepartamento()),
                empleado.getProyectos()
                        .stream()
                        .map(ProyectoDTOMapper::toDTO)
                        .toList()
        );
    }
}
