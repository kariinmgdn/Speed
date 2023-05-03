package com.example.backend.controller;

import com.example.backend.domain.AverageSpeed;
import com.example.backend.domain.SpeedData;
import com.example.backend.service.SpeedDataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
        return service.getData(from, to, speed, page);
    }

    @GetMapping("/average")
    public List<AverageSpeed> getAverageSpeed(@RequestParam String date) {
        return service.getAverageSpeed(date);
    }
}
