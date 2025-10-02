package com.businessdevelop.POCEvento.controller;

import com.businessdevelop.POCEvento.model.Evento;
import com.businessdevelop.POCEvento.model.EventoDeportivo;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/eventos")
public class ServicioEvento {

    // Lista directamente en el controlador
    private List<Evento> eventos = new ArrayList<>();

    //Prueba para saber si el endpoint funciona en postman
    @GetMapping(value = "/healthCheck")
    public String healthCheck() {
        return "Status okay!!!";
    }

    //Servicio 1- Crear evento
    @PostMapping(value = "/crearEvento")
    public ResponseEntity<?> crearEvento(@Valid @RequestBody EventoDeportivo evento) {
        try {
            // Verificar duplicidad del idEvento
            boolean existe = eventos.stream()
                    .anyMatch(e -> e.getIdEvento().equalsIgnoreCase(evento.getIdEvento()));

            if (existe) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Ya existe un evento con el idEvento: " + evento.getIdEvento());
            }

            eventos.add(evento);
            return ResponseEntity.status(HttpStatus.CREATED).body(evento);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ocurrió un error al crear el evento");
        }
    }


    //Servicio 2- Buscar evento
    @GetMapping("/buscarEvento/{id}")
    public ResponseEntity<Evento> buscarEventoPorId(@PathVariable("id") String id) {
        return eventos.stream()
                .filter(e -> e.getIdEvento().equals(id))
                .findFirst()
                .map(ResponseEntity::ok) // si lo encuentra, devuelve 200 + evento
                .orElse(ResponseEntity.notFound().build()); // si no lo encuentra, devuelve 404
    }

    //Servicio 3- Listar todos los eventos
    @GetMapping("/listarEvento")
    public ResponseEntity<List<Evento>> listarEventos() {
        return ResponseEntity.ok(eventos);
    }


    // Servicio 4 - Listar los eventos por filtro (Ciudad y tipo de deporte)
    @GetMapping("/listarFiltroEvento")
    public ResponseEntity<?> filtrarEventos(
            @RequestParam(required = false) String ciudad,
            @RequestParam(required = false) String tipoDeporte) {

        // Validación: al menos un filtro debe estar presente
        if (ciudad == null && tipoDeporte == null) {
            return ResponseEntity.badRequest().body("Debes ingresar al menos un filtro: ciudad o tipoDeporte");
        }

        List<EventoDeportivo> filtrados = eventos.stream()
                .filter(e -> e instanceof EventoDeportivo)
                .map(e -> (EventoDeportivo) e)
                .filter(e -> {
                    // 1. Si viene ciudad y no tipoDeporte: filtrar por ciudad
                    if (ciudad != null && tipoDeporte == null) {
                        return e.getCiudad().equalsIgnoreCase(ciudad);
                    }
                    // 2. Si viene tipoDeporte y no ciudad: filtrar por tipoDeporte
                    else if (tipoDeporte != null && ciudad == null) {
                        return e.getTipoDeporte().equalsIgnoreCase(tipoDeporte);
                    }
                    // 3. Si vienen ambos: filtrar por ciudad y tipo de deporte
                    else {
                        return e.getCiudad().equalsIgnoreCase(ciudad)
                                && e.getTipoDeporte().equalsIgnoreCase(tipoDeporte);
                    }
                })
                .toList();

        return ResponseEntity.ok(filtrados);
    }


    //Servicio 5- Actualizar evento digitando el id del evento
    @PutMapping("/actualizarEvento/{idEvento}")
    public ResponseEntity<?> actualizarEvento(
            @PathVariable String idEvento,
            @Valid @RequestBody EventoDeportivo eventoActualizado) {

        for (int i = 0; i < eventos.size(); i++) {
            Evento e = eventos.get(i);

            if (e.getIdEvento().equalsIgnoreCase(idEvento)) {
                // Mantener el mismo idEvento (no se actualiza el ID)
                eventoActualizado.setIdEvento(e.getIdEvento());

                eventos.set(i, eventoActualizado);
                return ResponseEntity.ok(eventoActualizado);
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("No se encontró un evento el siguienteE idEvento: " + idEvento);
    }


    //Servicio 6- Eliminar evento digitando el id del evento
    @DeleteMapping("/eliminarEvento/{idEvento}")
    public ResponseEntity<?> eliminarEvento(@PathVariable String idEvento) {
        for (int i = 0; i < eventos.size(); i++) {
            Evento e = eventos.get(i);

            if (e.getIdEvento().equalsIgnoreCase(idEvento)) {
                eventos.remove(i);
                return ResponseEntity.ok("Evento con idEvento " + idEvento + " fue eliminado correctamente");
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("No se encontró un evento con idEvento: " + idEvento);
    }





}
