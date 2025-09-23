package com.example.sistemaempleados.controllers;

import com.example.sistemaempleados.exceptions.DepartamentoNoEncontradoException;
import com.example.sistemaempleados.exceptions.NombreDuplicadoException;
import com.example.sistemaempleados.models.Departamento;
import com.example.sistemaempleados.services.DepartamentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departamentos")
public class DepartamentoController {
    private final DepartamentoService departamentoService;

    public DepartamentoController(DepartamentoService departamentoService){
        this.departamentoService = departamentoService;
    }

    @GetMapping
    public ResponseEntity<List<Departamento>> obtenerTodos() {
        return ResponseEntity.ok(departamentoService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Departamento> obtenerPorId(@PathVariable Long id){
        try {
            return ResponseEntity.ok(departamentoService.buscarPorId(id));
        } catch (DepartamentoNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<Departamento> crear(@Valid @RequestBody Departamento departamento){
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(departamentoService.guardar(departamento));
        } catch (NombreDuplicadoException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Departamento> actualizar(@PathVariable Long id, @RequestBody Departamento departamento){
        try {
            return ResponseEntity.ok(departamentoService.actualizar(id, departamento));
        } catch (DepartamentoNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        try {
            departamentoService.eliminar(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } catch (DepartamentoNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
