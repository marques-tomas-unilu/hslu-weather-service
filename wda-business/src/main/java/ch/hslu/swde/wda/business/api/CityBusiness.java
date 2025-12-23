package ch.hslu.swde.wda.business.api;

import ch.hslu.swde.wda.domain.City;

import java.util.List;

public interface CityBusiness {
    void saveCity(City city);
    City getCity(int id);
    void deleteCity(City city);
    void updateCity(City city);
    List<City> getAllCities();
    City findCityByName(String name);

}
