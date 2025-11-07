package com.example.sistemaempleados.services;

import com.example.sistemaempleados.exceptions.EmailDuplicadoException;
import com.example.sistemaempleados.exceptions.EmpleadoNoEncontradoException;
import com.example.sistemaempleados.models.Empleado;
import com.example.sistemaempleados.repositories.EmpleadoRepository;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class EmpleadoServiceImpl implements EmpleadoService {
    private final EmpleadoRepository empleadoRepository;

    public EmpleadoServiceImpl(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    @Override
    public Empleado guardar(Empleado empleado) {
        if (empleadoRepository.findByEmail(empleado.getEmail()).isPresent()) {
            throw new EmailDuplicadoException("El email ya está registrado: " + empleado.getEmail());
        }
        Empleado savedEmpleado = empleadoRepository.save(empleado);

        if (savedEmpleado.getDepartamento() != null) {
            Hibernate.initialize(savedEmpleado.getDepartamento());
        }
        Hibernate.initialize(savedEmpleado.getProyectos());

        return savedEmpleado;
    }

    @Override
    public Empleado buscarPorId(Long id) {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new EmpleadoNoEncontradoException("Empleado no encontrado con ID: " + id));

        if (empleado.getDepartamento() != null) {
            Hibernate.initialize(empleado.getDepartamento());
        }
        Hibernate.initialize(empleado.getProyectos());

        return empleado;
    }

    @Override
    public List<Empleado> buscarPorDepartamento(String nombreDepartamento) {
        List<Empleado> empleados = empleadoRepository.findByDepartamento(nombreDepartamento);

        empleados.forEach(empleado -> {
            if (empleado.getDepartamento() != null) {
                Hibernate.initialize(empleado.getDepartamento());
            }
            Hibernate.initialize(empleado.getProyectos());
        });

        return empleados;
    }

    @Override
    public List<Empleado> buscarPorContratados(LocalDate fecha) {
        List<Empleado> empleados = empleadoRepository.findByFechaContratacionAfter(fecha);

        empleados.forEach(empleado -> {
            if (empleado.getDepartamento() != null) {
                Hibernate.initialize(empleado.getDepartamento());
            }
            Hibernate.initialize(empleado.getProyectos());
        });

        return empleados;
    }

    @Override
    public List<Empleado> buscarPorRangoSalario(Double salarioMin, Double salarioMax) {
        List<Empleado> empleados = empleadoRepository.findBySalarioBetween(salarioMin, salarioMax);

        empleados.forEach(empleado -> {
            if (empleado.getDepartamento() != null) {
                Hibernate.initialize(empleado.getDepartamento());
            }
            Hibernate.initialize(empleado.getProyectos());
        });

        return empleados;
    }

    @Override
    public Double obtenerSalarioPromedioPorDepartamento(Long departamentoId) {
        return empleadoRepository.findAverageSalarioByDepartamento(departamentoId)
                .orElse(0.0);
    }

    @Override
    public List<Empleado> obtenerTodos() {
        List<Empleado> empleados = empleadoRepository.findAll();

        empleados.forEach(empleado -> {
            if (empleado.getDepartamento() != null) {
                Hibernate.initialize(empleado.getDepartamento());
            }
            Hibernate.initialize(empleado.getProyectos());
        });

        return empleados;
    }

    @Override
    public Empleado actualizar(Long id, Empleado empleado) {
        if (!empleadoRepository.existsById(id)) {
            throw new EmpleadoNoEncontradoException("Empleado no encontrado con ID: " + id);
        }
        empleado.setId(id);
        Empleado updatedEmpleado = empleadoRepository.save(empleado);

        if (updatedEmpleado.getDepartamento() != null) {
            Hibernate.initialize(updatedEmpleado.getDepartamento());
        }
        Hibernate.initialize(updatedEmpleado.getProyectos());

        return updatedEmpleado;
    }

    @Override
    public void eliminar(Long id) {
        if (!empleadoRepository.existsById(id)) {
            throw new EmpleadoNoEncontradoException("Empleado no encontrado con ID: " + id);
        }
        empleadoRepository.deleteById(id);
    }
}
