package com.example.backend.controller;

import com.example.backend.domain.AverageSpeed;
import com.example.backend.domain.SpeedData;
import com.example.backend.service.SpeedDataService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/speed")
public class SpeedDataController {
    private final SpeedDataService service;

    public SpeedDataController(SpeedDataService service) {
        this.service = service;
    }

    @GetMapping()
    public List<SpeedData> getSpeedData(@RequestParam String from,
                                        @RequestParam String to,
                                        @RequestParam String speed,
                                        @RequestParam String page) throws Exception {
        if (Objects.equals(page, "")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return service.getData(from, to, speed, page);
    }

    @GetMapping("/average")
    public List<AverageSpeed> getAverageSpeed(@RequestParam String date) {
        if (Objects.equals(date, "")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return service.getAverageSpeed(date);
    }
}
