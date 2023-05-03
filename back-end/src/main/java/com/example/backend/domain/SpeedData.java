package com.example.backend.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Table(name = "data_table")
public class SpeedData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;
    private int speed;
    @Column(name = "registration_plate")
    private String registrationPlate;

    public SpeedData(LocalDateTime time, int speed, String registrationPlate) {
        this.time = time;
        this.speed = speed;
        this.registrationPlate = registrationPlate;
    }
}
