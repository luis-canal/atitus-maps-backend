package br.edu.atitus.luis_canal.apisample.controllers;

import br.edu.atitus.luis_canal.apisample.dtos.PointDTO;
import br.edu.atitus.luis_canal.apisample.entities.PointEntity;
import br.edu.atitus.luis_canal.apisample.services.PointService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ws/point")
public class PointController {

    private final PointService service;


    public PointController(PointService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<PointEntity>> findAll() {
        var lista = service.findAll();
        return ResponseEntity.ok(lista);
    }

    @PostMapping
    public ResponseEntity<PointEntity> save(@RequestBody PointDTO dto) throws Exception {
        PointEntity point = new PointEntity();
        BeanUtils.copyProperties(dto, point);
        service.save(point);
        return ResponseEntity.status(201).body(point);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exceptionHandler(Exception ex) {
        String message = ex.getMessage().replace("\r\n", "");
        return ResponseEntity.badRequest().body(message);
    }
}
