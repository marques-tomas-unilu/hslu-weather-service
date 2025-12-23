package ch.hslu.swde.wda.business.impl;

import ch.hslu.swde.wda.business.api.CityBusiness;
import ch.hslu.swde.wda.domain.City;
import ch.hslu.swde.wda.persister.impl.CityDaoImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;

public class CityBusinessImpl implements CityBusiness {
    private static final Logger logger = LogManager.getLogger(CityBusinessImpl.class);

    private final CityDaoImpl cityPersister = new CityDaoImpl();

    public CityBusinessImpl() {}


    @Override
    public void saveCity(City city) {
        logger.info("Saving city: " + city);
        try {
            cityPersister.saveCity(city);
            logger.info("Saved city: " + city);
        } catch (Exception e) {
            logger.error("Error saving City."+ city,e);
            throw new RuntimeException("Error saving City." + city, e);

        }
    }


    @Override
    public City getCity(int zipCode) {
        logger.info("Retrieving city by zip code: " + zipCode);
        try {
            City city = cityPersister.findCityByZipCode(zipCode);
            if (city == null) {
                logger.warn("City with zipCode " + zipCode + " not found.");
                throw new RuntimeException("City with zipCode " + zipCode + " not found.");
            }
            return city;
        } catch (Exception e) {
            logger.error("Error retrieving City with zipCode " + zipCode, e);
            throw new RuntimeException("Error retrieving City with zipCode " + zipCode, e);
        }
    }

    @Override
    public void deleteCity(City city) {
        logger.info("Deleting city: " + city);
        try {
            cityPersister.deleteCity(city);
            logger.info("Deleted city: " + city);
        } catch (Exception e) {
            logger.error("Error deleting city."+ city,e);
            throw new RuntimeException("Error deleting City: " + city, e);
        }
    }

    @Override
    public void updateCity(City city) {
        logger.info("Updating city: " + city);
        try {
            cityPersister.updateCity(city);
            logger.info("Updated city: " + city);
        } catch (Exception e) {
            logger.error("Error updating city."+ city,e);
            throw new RuntimeException("Error updating City."+ city, e);
        }
    }

    @Override
    public List<City> getAllCities() {
        logger.info("Retrieving all cities");
        try {
            return cityPersister.getAllCities();
        } catch (Exception e) {
            logger.error("Error retrieving all cities."+ e,e);
            throw new RuntimeException("Error getting all Cities.", e);
        }}

    @Override
    public City findCityByName(String name) {
        logger.info("Retrieving city by name: " + name);
        try {
            return cityPersister.findCityByName(name);
        } catch (Exception e) {
            logger.error("Error retrieving city by name: " + name,e);
            throw new RuntimeException("Error finding City." + name, e);
        }
}}
