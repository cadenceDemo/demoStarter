package com.crux.cadence.demo.starter.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Builder
@Getter
@Jacksonized
public class CityWeatherHistorical {
    private String cityName;
    private List<WeatherHistorical> weathers;
}
