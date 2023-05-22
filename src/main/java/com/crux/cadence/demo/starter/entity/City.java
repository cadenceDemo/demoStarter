package com.crux.cadence.demo.starter.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Builder
@Getter
@Jacksonized
public class City {
    private String city;
    private Double latitude;
    private Double longitude;
    private String state;
}
