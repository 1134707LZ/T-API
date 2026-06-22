package br.edu.atitus.apisample.controllers;

import br.edu.atitus.apisample.dtos.BusStopDTO;
import br.edu.atitus.apisample.entities.BusStop;
import br.edu.atitus.apisample.services.BusStopService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/ws/point")
public class BusStopController {

    private final BusStopService busStopService;

    public BusStopController(BusStopService busStopService) {
        this.busStopService = busStopService;
    }

    // GET /ws/point — retorna somente os pontos do usuário autenticado
    @GetMapping
    public ResponseEntity<List<BusStop>> getBusStops() {
        return ResponseEntity.ok(busStopService.findAllByLoggedUser());
    }

    // POST /ws/point — cria novo ponto associado ao usuário logado
    @PostMapping
    public ResponseEntity<BusStop> postBusStop(@RequestBody BusStopDTO dto) throws Exception {
        BusStop newBusStop = new BusStop();
        BeanUtils.copyProperties(dto, newBusStop);
        BusStop saved = busStopService.save(newBusStop);
        return ResponseEntity.status(201).body(saved);
    }

    // PUT /ws/point/{id} — atualiza ponto existente, somente se for do usuário logado
    @PutMapping("/{id}")
    public ResponseEntity<BusStop> putBusStop(@PathVariable UUID id, @RequestBody BusStopDTO dto) throws Exception {
        BusStop updatedData = new BusStop();
        BeanUtils.copyProperties(dto, updatedData);
        BusStop updated = busStopService.update(id, updatedData);
        return ResponseEntity.ok(updated);
    }

    // DELETE /ws/point/{id} — remove ponto, somente se for do usuário logado
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBusStop(@PathVariable UUID id) throws Exception {
        busStopService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> excpetionHandler(Exception ex) {
        String message = ex.getMessage().replace("\r\n", "");
        return ResponseEntity.badRequest().body(message);
    }
}
