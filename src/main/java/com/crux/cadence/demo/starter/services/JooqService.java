package com.crux.cadence.demo.starter.services;

import com.crux.cadence.demo.dsl.public_.tables.Currentweather;
import com.crux.cadence.demo.dsl.public_.tables.records.CurrentweatherRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JooqService {
    @NonNull
    private DSLContext dsl;

    public List<CurrentweatherRecord> getWeather(String city){
        Currentweather weather = Currentweather.CURRENTWEATHER;

        return dsl.selectFrom(weather)
                .where(weather.CITY.eq(city))
                .orderBy(weather.CREATED_ON)
                .fetchInto(CurrentweatherRecord.class);
    }
}
