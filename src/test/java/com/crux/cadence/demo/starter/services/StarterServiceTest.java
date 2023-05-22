package com.crux.cadence.demo.starter.services;

import com.crux.cadence.demo.dsl.public_.tables.records.CurrentweatherRecord;
import com.crux.cadence.demo.starter.entity.City;
import com.crux.cadence.demo.starter.entity.CityWeatherHistorical;
import com.crux.cadence.demo.starter.mapper.CityWeatherHistoricalMapper;
import com.uber.cadence.WorkflowExecution;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.management.InstanceNotFoundException;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StarterServiceTest {
    @Mock
    private CadenceService cadenceService;
    @Mock
    private CityService cityService;
    @Mock
    private JooqService jooqService;
    @Mock
    private CityWeatherHistoricalMapper mapper;

    @Mock
    List<CurrentweatherRecord> listOfWeather;
    @Mock
    CityWeatherHistorical cityWeatherHistorical;
    @Mock
    City city;
    @Mock
    WorkflowExecution execution;

    private StarterService service;

    @BeforeEach
    public void setUp() {
        service = new StarterService(cadenceService, cityService, jooqService, mapper);
    }

    @Test
    public void fetchHistoricalData() {
        when(jooqService.getWeather(anyString())).thenReturn(listOfWeather);
        when(mapper.toCityWeatherHistorical(anyList(), anyString())).thenReturn(cityWeatherHistorical);
        String cityName = "Test city";
        var result = service.getCityWeatherHistoricalData(cityName);

        verify(jooqService).getWeather(eq(cityName));
        verify(mapper).toCityWeatherHistorical(same(listOfWeather), eq(cityName));

        assertThat(result, sameInstance(cityWeatherHistorical));
    }

    @Test
    public void fetchHistoricalDataNoDataForCity() {
        when(jooqService.getWeather(anyString())).thenReturn(Collections.emptyList());
        String cityName = "Test city";
        var result = service.getCityWeatherHistoricalData(cityName);

        verify(jooqService).getWeather(eq(cityName));
        verify(mapper, never()).toCityWeatherHistorical(same(listOfWeather), eq(cityName));

        assertThat(result,  IsNull.nullValue());
    }

    @Test
    public void startAccumulateCityWeatherHistoricalData() throws InstanceNotFoundException {
        when(cityService.getCity(anyString())).thenReturn(city);
        when(cadenceService.executeWorkflow(any(City.class))).thenReturn(execution);

        String cityName = "Test city";
        service.startAccumulateCityWeatherHistoricalData(cityName);

        verify(cityService).getCity(eq(cityName));
        verify(cadenceService).executeWorkflow(eq(city));
    }

    @Test
    public void startAccumulateCityWeatherHistoricalDataNoCity() throws InstanceNotFoundException {
        when(cityService.getCity(anyString())).thenThrow(new InstanceNotFoundException());

        String cityName = "Test city";
        InstanceNotFoundException exception = Assertions.assertThrows(InstanceNotFoundException.class, () -> {
            service.startAccumulateCityWeatherHistoricalData(cityName);
        });

        verify(cityService).getCity(eq(cityName));
        assertThat(exception,  IsNull.notNullValue());
    }
}