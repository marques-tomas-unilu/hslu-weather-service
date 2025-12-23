package ch.hslu.swde.wda.persister.api;

import ch.hslu.swde.wda.domain.City;

import java.util.List;

public interface CityPersister {

    void saveCity(City city) throws Exception;
    void updateCity(City city) throws Exception;
    void deleteCity(City city) throws Exception;
    City findCityByZipCode(int zipCode) throws Exception;
    City findCityByName(String name) throws Exception;
    List<City> getAllCities() throws Exception;
}

