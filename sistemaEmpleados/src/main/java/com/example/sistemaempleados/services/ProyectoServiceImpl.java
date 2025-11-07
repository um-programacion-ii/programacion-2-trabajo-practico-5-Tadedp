package com.example.sistemaempleados.services;

import com.example.sistemaempleados.exceptions.ProyectoNoEncontradoException;
import com.example.sistemaempleados.models.Proyecto;
import com.example.sistemaempleados.repositories.ProyectoRepository;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class ProyectoServiceImpl implements ProyectoService {
    private final ProyectoRepository proyectoRepository;

    public ProyectoServiceImpl(ProyectoRepository proyectoRepository) {
        this.proyectoRepository = proyectoRepository;
    }

    @Override
    public Proyecto guardar(Proyecto proyecto) {
        Proyecto savedProyecto = proyectoRepository.save(proyecto);

        Hibernate.initialize(savedProyecto.getEmpleados());

        return savedProyecto;
    }

    @Override
    public Proyecto buscarPorId(Long id) {
        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new ProyectoNoEncontradoException("Proyecto no encontrado con ID: " + id));

        Hibernate.initialize(proyecto.getEmpleados());

        return proyecto;
    }

    @Override
    public List<Proyecto> obtenerProyectosActivos() {
        List<Proyecto> proyectos = proyectoRepository.findByFechaFinAfter(LocalDate.now());

        proyectos.forEach(proyecto -> {
            Hibernate.initialize(proyecto.getEmpleados());
        });

        return proyectos;
    }

    @Override
    public List<Proyecto> obtenerTodos() {
        List<Proyecto> proyectos = proyectoRepository.findAll();

        proyectos.forEach(proyecto -> {
            Hibernate.initialize(proyecto.getEmpleados());
        });

        return proyectos;
    }

    @Override
    public Proyecto actualizar(Long id, Proyecto proyecto) {
        if (!proyectoRepository.existsById(id)) {
            throw new ProyectoNoEncontradoException("Proyecto no encontrado con ID: " + id);
        }
        proyecto.setId(id);
        Proyecto updatedProyecto = proyectoRepository.save(proyecto);

        Hibernate.initialize(updatedProyecto.getEmpleados());

        return updatedProyecto;
    }

    @Override
    public void eliminar(Long id) {
        if (!proyectoRepository.existsById(id)) {
            throw new ProyectoNoEncontradoException("Proyecto no encontrado con ID: " + id);
        }
        proyectoRepository.deleteById(id);
    }
}
