package com.nibado.example.springtestcontainers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Counter {
    private String name;
    private int value;
}
