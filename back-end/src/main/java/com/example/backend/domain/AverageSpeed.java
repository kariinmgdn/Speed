package com.example.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class AverageSpeed {

    private Integer hour;
    private Long speed;
}
