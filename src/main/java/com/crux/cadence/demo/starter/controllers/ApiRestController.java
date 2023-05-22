package com.crux.cadence.demo.starter.controllers;

import com.crux.cadence.demo.starter.entity.CityWeatherHistorical;
import com.crux.cadence.demo.starter.excheptions.BusinessException;
import com.crux.cadence.demo.starter.services.StarterService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ApiRestController {
    @NonNull
    StarterService starterService;

    @GetMapping("/startCollecting/city/{city}")
    public void startCollecting(@PathVariable String city) throws BusinessException {
        try {
            starterService.startAccumulateCityWeatherHistoricalData(city);
        } catch (Exception ex) {
            throw new BusinessException("failed to start collecting data");
        }
    }

    @GetMapping("/historicalWeather/city/{city}")
    public CityWeatherHistorical fetchCities(@PathVariable String city) throws BusinessException {
        try {
            return starterService.getCityWeatherHistoricalData(city);
        } catch (Exception ex) {
            throw new BusinessException("No data for city");
        }
    }

    @GetMapping("/registerDomain")
    public void registerDomain(@Value("${app.cadence.domain}") String domain) throws BusinessException {
        starterService.registerDomain(domain);
    }
}
