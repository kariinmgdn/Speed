package com.example.backend.controller;

import com.example.backend.domain.AverageSpeed;
import com.example.backend.domain.SpeedData;
import com.example.backend.service.FileUploadService;
import com.example.backend.service.SpeedDataService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/speed")
public class SpeedDataController {
    private final SpeedDataService speedDataService;
    private final FileUploadService fileUploadService;

    public SpeedDataController(SpeedDataService speedDataService, FileUploadService fileUploadService) {
        this.speedDataService = speedDataService;
        this.fileUploadService = fileUploadService;
    }

    @GetMapping()
    public List<SpeedData> getSpeedData(@RequestParam String from,
                                        @RequestParam String to,
                                        @RequestParam String speed,
                                        @RequestParam String page) {
        if (Objects.equals(page, "")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return speedDataService.getData(from, to, speed, page);
    }

    @GetMapping("/average")
    public List<AverageSpeed> getAverageSpeed(@RequestParam String date) {
        if (Objects.equals(date, "")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return speedDataService.getAverageSpeed(date);
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        fileUploadService.deleteFiles();
        String fileName = fileUploadService.uploadFile(file);
        ServletUriComponentsBuilder.fromCurrentContextPath().path(fileName).toUriString();
        speedDataService.saveData(fileUploadService.getFilePath());
        return fileName;
    }
}
