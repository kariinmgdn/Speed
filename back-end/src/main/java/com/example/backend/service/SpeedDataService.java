package com.example.backend.service;

import com.example.backend.domain.AverageSpeed;
import com.example.backend.domain.SpeedData;
import com.example.backend.repository.SpeedDataRepository;
import com.opencsv.CSVReader;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SpeedDataService {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final SpeedDataRepository dataRepository;
    private final FileUploadService fileUploadService;

    public SpeedDataService(SpeedDataRepository dataRepository, FileUploadService fileUploadService) {
        this.dataRepository = dataRepository;
        this.fileUploadService = fileUploadService;
    }

    public List<SpeedData> getData(String from, String to, String speed, String pageNr) {
        Pageable page = PageRequest.of(Integer.parseInt(pageNr), 20, Sort.by("time"));
        System.out.println(to);
        return dataRepository.filterData(
                !Objects.equals(from, "") ? LocalDateTime.parse(from, formatter) : null,
                !Objects.equals(to, "") ? LocalDateTime.parse(to, formatter) : null,
                !Objects.equals(speed, "") ? Integer.parseInt(speed) : null, page);
    }

    public List<AverageSpeed> getAverageSpeed(String date) {
        LocalDateTime from = LocalDate.parse(date, formatter).atStartOfDay();
        LocalDateTime to = LocalDate.parse(date, formatter).plusDays(1).atStartOfDay();
        List<SpeedData> dataList = dataRepository.getSpecificDayData(from, to);

        Map<Integer, List<SpeedData>> groupedByHours = dataList
                .stream()
                .collect(Collectors
                        .groupingBy(data -> data.getTime()
                                .getHour()));

        Map<Integer, Double> mapWithCalculatedAverageSpeed = groupedByHours.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue()
                        .stream()
                        .mapToInt(SpeedData::getSpeed)
                        .average()
                        .orElse(0)));

        List<AverageSpeed> averageSpeedList = mapWithCalculatedAverageSpeed
                .entrySet()
                .stream()
                .map(e -> new AverageSpeed(e.getKey(), Math.round(e.getValue())))
                .toList();
        return averageSpeedList.stream().sorted(Comparator.comparingInt(AverageSpeed::getHour)).toList();
    }

    public void saveData(String filePath) {
        dataRepository.deleteAll();
        if (dataRepository.checkIfEmpty() == 0 && !Objects.equals(filePath, null)) {
            Path path = Path.of(filePath);
            List<SpeedData> dataList = new ArrayList<>();
            try (Reader reader = Files.newBufferedReader(path)) {
                try (CSVReader csvReader = new CSVReader(reader, '\t')) {
                    String[] line;
                    while ((line = csvReader.readNext()) != null) {
                        dataList.add(
                                new SpeedData(LocalDateTime.parse(line[0], formatter),
                                        Integer.parseInt(line[1]),
                                        line[2]));
                    }
                    dataRepository.saveAll(dataList);
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            fileUploadService.deleteFiles();
        }
    }
}
