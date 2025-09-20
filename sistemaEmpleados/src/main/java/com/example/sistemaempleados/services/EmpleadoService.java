package com.example.sistemaempleados.services;

import com.example.sistemaempleados.models.Departamento;
import com.example.sistemaempleados.models.Empleado;

import java.time.LocalDate;
import java.util.List;

public interface EmpleadoService {
    Empleado guardar(Empleado empleado);
    Empleado buscarPorId(Long id);
    List<Empleado> buscarPorDepartamento(Departamento departamento);
    List<Empleado> buscarPorNombreDepartamento(String nombreDepartamento);
    List<Empleado> buscarPorContratados(LocalDate fecha);
    List<Empleado> buscarPorRangoSalario(Double salarioMin, Double salarioMax);
    Double obtenerSalarioPromedioPorDepartamento(Long departamentoId);
    List<Empleado> obtenerTodos();
    Empleado actualizar(Long id, Empleado empleado);
    void eliminar(Long id);
}
