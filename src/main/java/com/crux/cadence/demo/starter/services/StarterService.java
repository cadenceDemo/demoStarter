package com.crux.cadence.demo.starter.services;

import com.crux.cadence.demo.dsl.public_.tables.records.CurrentweatherRecord;
import com.crux.cadence.demo.starter.entity.City;
import com.crux.cadence.demo.starter.entity.CityWeatherHistorical;
import com.crux.cadence.demo.starter.excheptions.BusinessException;
import com.crux.cadence.demo.starter.mapper.CityWeatherHistoricalMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.management.InstanceNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StarterService {
    @NonNull
    CadenceService cadenceService;
    @NonNull
    CityService cityService;
    @NonNull
    JooqService jooqService;
    @NonNull
    CityWeatherHistoricalMapper mapper;

    public CityWeatherHistorical getCityWeatherHistoricalData(String city) {
        List<CurrentweatherRecord> weatherRecordList = jooqService.getWeather(city);
        if(weatherRecordList == null || weatherRecordList.isEmpty()){
            return null;
        } else {
            return mapper.toCityWeatherHistorical(weatherRecordList, city);
        }
    }

    public void startAccumulateCityWeatherHistoricalData(String cityName) throws InstanceNotFoundException {
        City city = cityService.getCity(cityName);
        cadenceService.executeWorkflow(city);
    }

    public void registerDomain(String domain) throws BusinessException {
        cadenceService.registerDomain(domain);
    }
}
