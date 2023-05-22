package com.crux.cadence.demo.starter.mapper;

import com.crux.cadence.demo.dsl.public_.tables.records.CurrentweatherRecord;
import com.crux.cadence.demo.starter.entity.City;
import com.crux.cadence.demo.starter.entity.CityWeatherHistorical;
import com.crux.cadence.demo.starter.entity.WeatherHistorical;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface CityWeatherHistoricalMapper {
    @Mapping(source = "temperature", target = "temperature")
    @Mapping(source = "createdOn", target = "date", dateFormat = "dd.MM.yyyy")
    WeatherHistorical toWeatherHistorical(CurrentweatherRecord dbObj);

    @Mapping(expression="java( city )", target = "cityName")
    CityWeatherHistorical toCityWeatherHistorical(List<CurrentweatherRecord> weathers, String city);
}
