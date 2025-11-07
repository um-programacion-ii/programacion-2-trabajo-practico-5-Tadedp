package com.example.sistemaempleados.mappers;

import com.example.sistemaempleados.dtos.ProyectoCompletoDTO;
import com.example.sistemaempleados.dtos.ProyectoDTO;
import com.example.sistemaempleados.models.Proyecto;

import java.util.stream.Collectors;

public class ProyectoDTOMapper {
    public static ProyectoDTO toDTO(Proyecto proyecto) {
        if (proyecto == null) {
            return null;
        }

        return new ProyectoDTO(
                proyecto.getId(),
                proyecto.getNombre(),
                proyecto.getDescripcion(),
                proyecto.getFechaInicio(),
                proyecto.getFechaFin()
        );
    }

    public static ProyectoCompletoDTO toDTOCompleto(Proyecto proyecto) {
        if (proyecto == null) {
            return null;
        }

        return new ProyectoCompletoDTO(
                proyecto.getId(),
                proyecto.getNombre(),
                proyecto.getDescripcion(),
                proyecto.getFechaInicio(),
                proyecto.getFechaFin(),
                proyecto.getEmpleados()
                        .stream()
                        .map(EmpleadoDTOMapper::toDTO)
                        .collect(Collectors.toSet())
        );
    }
}
