package com.crux.cadence.demo.starter.services;

import com.crux.cadence.demo.starter.entity.City;
import com.crux.cadence.demo.starter.entity.CityMap;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.management.InstanceNotFoundException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CityServiceTest {

    @Mock
    private CityMap cityMap;

    @Mock
    City city;

    private CityService service;

    @BeforeEach
    public void setUp() {
        service = new CityService(cityMap);
    }


    @Test
    void getCity() throws InstanceNotFoundException {
        String cityName = "Test city";
        when(cityMap.containsKey(anyString())).thenReturn(true);
        when(cityMap.get(anyString())).thenReturn(city);

        var result = service.getCity(cityName);

        assertThat(result, sameInstance(city));
    }

    @Test
    public void startAccumulateCityWeatherHistoricalDataNoCity() throws InstanceNotFoundException {
        String cityName = "Test city";
        when(cityMap.containsKey(anyString())).thenReturn(false);

        InstanceNotFoundException exception = Assertions.assertThrows(InstanceNotFoundException.class, () -> {
            service.getCity(cityName);
        });

        assertThat(exception,  IsNull.notNullValue());
    }
}