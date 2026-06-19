package br.edu.atitus.luis_canal.apisample.controllers;

import br.edu.atitus.luis_canal.apisample.services.PointService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ws/point")
public class PointController {

    private final PointService service;


    public PointController(PointService service) {
        this.service = service;
    }
}
