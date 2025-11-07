package com.example.sistemaempleados.services;

import com.example.sistemaempleados.exceptions.DepartamentoNoEncontradoException;
import com.example.sistemaempleados.exceptions.NombreDuplicadoException;
import com.example.sistemaempleados.models.Departamento;
import com.example.sistemaempleados.repositories.DepartamentoRepository;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class DepartamentoServiceImpl implements DepartamentoService {
    private final DepartamentoRepository departamentoRepository;

    public DepartamentoServiceImpl(DepartamentoRepository departamentoRepository) {
        this.departamentoRepository = departamentoRepository;
    }

    @Override
    public Departamento guardar(Departamento departamento) {
        if (departamentoRepository.findByNombre(departamento.getNombre()).isPresent()) {
            throw new NombreDuplicadoException("El nombre ya está registrado: " + departamento.getNombre());
        }
        Departamento savedDepartamento = departamentoRepository.save(departamento);

        Hibernate.initialize(savedDepartamento.getEmpleados());

        return savedDepartamento;
    }

    @Override
    public Departamento buscarPorId(Long id) {
        Departamento departamento = departamentoRepository.findById(id)
                .orElseThrow(() -> new DepartamentoNoEncontradoException("Departamento no encontrado con ID: " + id));

        Hibernate.initialize(departamento.getEmpleados());

        return departamento;
    }

    @Override
    public List<Departamento> obtenerTodos() {
        List<Departamento> departamentos = departamentoRepository.findAll();

        departamentos.forEach(departamento -> {
            Hibernate.initialize(departamento.getEmpleados());
        });

        return departamentos;
    }

    @Override
    public Departamento actualizar(Long id, Departamento departamento) {
        if (!departamentoRepository.existsById(id)) {
            throw new DepartamentoNoEncontradoException("Departamento no encontrado con ID: " + id);
        }
        departamento.setId(id);
        Departamento updatedDepartamento = departamentoRepository.save(departamento);

        Hibernate.initialize(updatedDepartamento.getEmpleados());

        return updatedDepartamento;
    }

    @Override
    public void eliminar(Long id) {
        if (!departamentoRepository.existsById(id)) {
            throw new DepartamentoNoEncontradoException("Departamento no encontrado con ID: " + id);
        }
        departamentoRepository.deleteById(id);
    }
}