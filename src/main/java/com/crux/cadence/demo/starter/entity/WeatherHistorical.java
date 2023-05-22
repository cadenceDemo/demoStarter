package com.crux.cadence.demo.starter.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Builder
@Getter
@Jacksonized
public class WeatherHistorical {
    private Double temperature;
    private String date;
}
