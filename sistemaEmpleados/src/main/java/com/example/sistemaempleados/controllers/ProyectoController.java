package com.example.sistemaempleados.controllers;

import com.example.sistemaempleados.dtos.ProyectoCompletoDTO;
import com.example.sistemaempleados.exceptions.ProyectoNoEncontradoException;
import com.example.sistemaempleados.mappers.ProyectoDTOMapper;
import com.example.sistemaempleados.models.Proyecto;
import com.example.sistemaempleados.services.ProyectoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proyectos")
public class ProyectoController {
    private final ProyectoService proyectoService;

    public ProyectoController(ProyectoService proyectoService){
        this.proyectoService = proyectoService;
    }

    @GetMapping
    public ResponseEntity<List<ProyectoCompletoDTO>> obtenerTodos() {
        return ResponseEntity.ok(
                proyectoService.obtenerTodos()
                        .stream()
                        .map(ProyectoDTOMapper::toDTOCompleto)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProyectoCompletoDTO> obtenerPorId(@PathVariable Long id){
        try {
            return ResponseEntity.ok(
                    ProyectoDTOMapper.toDTOCompleto(proyectoService.buscarPorId(id))
            );
        } catch (ProyectoNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<ProyectoCompletoDTO> crear(@Valid @RequestBody Proyecto proyecto){
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ProyectoDTOMapper.toDTOCompleto(proyectoService.guardar(proyecto))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProyectoCompletoDTO> actualizar(@PathVariable Long id, @RequestBody Proyecto proyecto){
        try {
            return ResponseEntity.ok(
                    ProyectoDTOMapper.toDTOCompleto(proyectoService.actualizar(id, proyecto))
            );
        } catch (ProyectoNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        try {
            proyectoService.eliminar(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } catch (ProyectoNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/activos")
    public ResponseEntity<List<ProyectoCompletoDTO>> obtenerActivos(){
        return ResponseEntity.ok(
                proyectoService.obtenerProyectosActivos()
                        .stream()
                        .map(ProyectoDTOMapper::toDTOCompleto)
                        .toList()
        );
    }
}
