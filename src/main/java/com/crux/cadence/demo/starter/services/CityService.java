package com.crux.cadence.demo.starter.services;

import com.crux.cadence.demo.starter.entity.City;
import com.crux.cadence.demo.starter.entity.CityMap;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.management.InstanceNotFoundException;

@Service
@RequiredArgsConstructor
public class CityService {
    @NonNull
    CityMap cities;

    public City getCity(String name) throws InstanceNotFoundException {
        if(cities.containsKey(name)){
            return cities.get(name);
        } else {
            throw new InstanceNotFoundException("Not found "+name+" city in system");
        }
    }
}
