package com.example.sistemaempleados.services;

import com.example.sistemaempleados.models.Proyecto;

import java.util.List;

public interface ProyectoService {
    Proyecto guardar(Proyecto proyecto);
    Proyecto buscarPorId(Long id);
    List<Proyecto> obtenerProyectosActivos();
    List<Proyecto> obtenerTodos();
    Proyecto actualizar(Long id, Proyecto proyecto);
    void eliminar(Long id);
}
