package com.example.backend.service;

import com.example.backend.domain.AverageSpeed;
import com.example.backend.domain.SpeedData;
import com.example.backend.repository.SpeedDataRepository;
import com.opencsv.CSVReader;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public SpeedDataService(SpeedDataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public List<SpeedData> getData(String from, String to, String speed, String pageNr) throws Exception {
        if (dataRepository.checkIfEmpty() == 0) {
            saveData();
        }
        Pageable page = PageRequest.of(Integer.parseInt(pageNr), 20);
        return dataRepository.filterData(
                !Objects.equals(from, "") ? LocalDateTime.parse(from, formatter) : null,
                !Objects.equals(to, "") ? LocalDateTime.parse(to, formatter) : null,
                !Objects.equals(speed, "") ? Integer.parseInt(speed) : null, page);
    }

    public List<AverageSpeed> getAverageSpeed(String date) {
        LocalDateTime from = LocalDate.parse(date, formatter).atStartOfDay();
        LocalDateTime to = LocalDate.parse(date, formatter).plusDays(1).atStartOfDay();
        List<SpeedData> dataList = dataRepository.getSpecificDayData(from, to);

        Map<Integer, Long> dataMap = dataList
                .stream()
                .collect(Collectors
                        .groupingBy(data -> data.getTime()
                                .getHour()))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> Math
                        .round(entry.getValue()
                                .stream()
                                .mapToInt(SpeedData::getSpeed)
                                .average()
                                .orElse(0))));
        return dataMap.entrySet().stream().map(e -> new AverageSpeed(e.getKey(), e.getValue())).toList();
    }

    public void saveData() throws Exception {
        Path path = Path.of(ClassLoader.getSystemResource("speed.txt").toURI());
        List<SpeedData> dataList = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(path)) {
            try (CSVReader csvReader = new CSVReader(reader, '\t')) {
                String[] line;
                while ((line = csvReader.readNext()) != null) {
                    dataList.add(new SpeedData(LocalDateTime.parse(line[0], formatter), Integer.parseInt(line[1]), line[2]));
                }
                dataRepository.saveAll(dataList);
            }
        }
    }
}
