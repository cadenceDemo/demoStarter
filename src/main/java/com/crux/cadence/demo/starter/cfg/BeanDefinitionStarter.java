package com.crux.cadence.demo.starter.cfg;

import com.crux.cadence.demo.starter.entity.City;
import com.crux.cadence.demo.starter.entity.CityMap;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Configuration
@Import(com.crux.cadence.demo.cfg.BeanDefinition.class)
public class BeanDefinitionStarter {
    @Bean
    ObjectMapper initObjectMapper(){
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Bean
    CityMap initCitiesMap(ObjectMapper objectMapper) throws IOException {
        CityMap map = new CityMap();

        File file = ResourceUtils.getFile("classpath:cities.json");
        List<City> cities = objectMapper.readValue(file, new TypeReference<List<City>>() {});

        cities.stream().forEach(city -> {
            map.put(city.getCity(), city);
        });

        return map;
    }
}