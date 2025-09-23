package com.example.sistemaempleados.controllers;

import com.example.sistemaempleados.exceptions.EmailDuplicadoException;
import com.example.sistemaempleados.exceptions.EmpleadoNoEncontradoException;
import com.example.sistemaempleados.models.Empleado;
import com.example.sistemaempleados.services.EmpleadoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/api/empleados")
@Validated
public class EmpleadoController {
    private final EmpleadoService empleadoService;

    public EmpleadoController(EmpleadoService empleadoService){
        this.empleadoService = empleadoService;
    }

    @GetMapping
    public ResponseEntity<List<Empleado>> obtenerTodos() {
        return ResponseEntity.ok(empleadoService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empleado> obtenerPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(empleadoService.buscarPorId(id));
        } catch (EmpleadoNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<Empleado> crear(@Valid @RequestBody Empleado empleado) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(empleadoService.guardar(empleado));
        } catch (EmailDuplicadoException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Empleado> actualizar(@PathVariable Long id, @Valid @RequestBody Empleado empleado) {
        try {
            return ResponseEntity.ok(empleadoService.actualizar(id, empleado));
        } catch (EmpleadoNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            empleadoService.eliminar(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } catch (EmpleadoNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/departamento/{nombre}")
    public ResponseEntity<List<Empleado>> obtenerPorDepartamento(@PathVariable String nombre) {
        return ResponseEntity.ok(empleadoService.buscarPorDepartamento(nombre));
    }

    @GetMapping("/departamento/{id}")
    public ResponseEntity<Double> obtenerSalarioPromedioPorDepartamento(@PathVariable Long id) {
        return ResponseEntity.ok(empleadoService.obtenerSalarioPromedioPorDepartamento(id));
    }

    @GetMapping("/contratado/{fecha}")
    public ResponseEntity<List<Empleado>> obtenerPorFechaContratacion(@PathVariable String fecha) {
        try {
            return ResponseEntity.ok(empleadoService.buscarPorContratados(LocalDate.parse(fecha)));
        } catch (DateTimeParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/salario")
    public ResponseEntity<List<Empleado>> obtenerPorRangoSalario(
            @RequestParam Double min,
            @RequestParam Double max) {
        if (min == null || max == null || min < 0 || max < 0 || min > max) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.ok(empleadoService.buscarPorRangoSalario(min, max));
    }
}
