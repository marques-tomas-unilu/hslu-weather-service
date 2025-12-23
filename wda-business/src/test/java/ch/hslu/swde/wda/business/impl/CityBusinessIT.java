package ch.hslu.swde.wda.business.impl;

import ch.hslu.swde.wda.business.api.CityBusiness;
import ch.hslu.swde.wda.business.api.WeatherDataBusiness;
import ch.hslu.swde.wda.persister.util.JpaUtil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.hslu.swde.wda.domain.City;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class CityBusinessIT {

    @BeforeEach
    void setUp() {
        System.setProperty("APP_ENV", "test");
        EntityManager em = JpaUtil.createEntityManager();
        em.getTransaction().begin();
        em.createQuery("DELETE FROM WeatherData e").executeUpdate();
        em.createQuery("DELETE FROM City e").executeUpdate();
        em.getTransaction().commit();

        em.close();
    }

    @AfterEach
    void tearDown() {
        EntityManager em = JpaUtil.createEntityManager();
        em.getTransaction().begin();
        em.createQuery("DELETE FROM WeatherData e").executeUpdate();
        em.createQuery("DELETE FROM City e").executeUpdate();
        em.getTransaction().commit();

        em.close();
        System.setProperty("APP_ENV", "production");
    }

    @Test
    void saveCity_SavedCityEqualsCityInDatabase_IsTrue() {
        //Create city business obj
        CityBusiness cityBusiness = new CityBusinessImpl();
        WeatherDataBusiness weatherDataBusiness = new WeatherDataBusinessImpl();

        //fetch all cities from rws
        List<City> cityList = weatherDataBusiness.fetchAvailableCities();

        //save and check saved city
        for (City c : cityList) {
            cityBusiness.saveCity(c);
            assertEquals(c, cityBusiness.findCityByName(c.getName()));
        }
    }

    @Test
    void getCity_SavedCityEqualsCityInDatabaseByZipCode_IsTrue() {
        //Create city business obj
        CityBusiness cityBusiness = new CityBusinessImpl();
        WeatherDataBusiness weatherDataBusiness = new WeatherDataBusinessImpl();

        //fetch all cities from rws
        List<City> cityList = weatherDataBusiness.fetchAvailableCities();

        //save and check saved city
        for (City c : cityList) {
            cityBusiness.saveCity(c);
            assertEquals(c, cityBusiness.getCity(c.getZipCode()));
        }
    }

    @Test
    void deleteCity_DeletedCityNotFoundInDatabase_ThrowsRuntimeException() {
        //create city business obj
        CityBusiness cityBusiness = new CityBusinessImpl();
        WeatherDataBusiness weatherDataBusiness = new WeatherDataBusinessImpl();

        //fetch all cities from rws
        List<City> cityList = weatherDataBusiness.fetchAvailableCities();

        //save and check saved city
        for (City c : cityList) {
            cityBusiness.saveCity(c);
            assertEquals(c, cityBusiness.getCity(c.getZipCode()));
        }

        //fetch all cities from database
        List<City> cityListToDelete = cityBusiness.getAllCities();

        //delete and check all cities
        for (City c : cityListToDelete) {
            cityBusiness.deleteCity(c);
            //Fehlermeldung in ChatGPT eingefügt: folgender Codeausschnitt wurde von KI generiert
            assertThrows(RuntimeException.class, () -> cityBusiness.findCityByName(c.getName()));
        }
    }

    @Test
    void updateCity_UpdateCityLucerneLatAndLong_IsSavedCorrectly() {
        //Create city business obj
        CityBusiness cityBusiness = new CityBusinessImpl();
        WeatherDataBusiness weatherDataBusiness = new WeatherDataBusinessImpl();

        //fetch all cities from rws
        List<City> cityList = weatherDataBusiness.fetchAvailableCities();

        //save and check saved city
        for (City c : cityList) {
            cityBusiness.saveCity(c);
            assertEquals(c, cityBusiness.getCity(c.getZipCode()));
        }

        //Find city lucerne and update
        City lucerne = cityBusiness.findCityByName("Lucerne");

        lucerne.setLatitude(47.05);
        lucerne.setLongitude(8.3);
        cityBusiness.updateCity(lucerne);

        //Find city lucerne again after update
        lucerne = cityBusiness.findCityByName("Lucerne");
        assertEquals(47.05, lucerne.getLatitude());
        assertEquals(8.3, lucerne.getLongitude());
    }

//    @Test
//    void getAllCities_SaveAllCitiesInDatabase_ShouldReturnSameListOfCities() {
//        //Create city business obj
//        CityBusiness cityBusiness = new CityBusinessImpl();
//        WeatherDataBusiness weatherDataBusiness = new WeatherDataBusinessImpl();
//
//        //fetch all cities from rws
//        List<City> cityList = weatherDataBusiness.fetchAvailableCities();
//
//        //save and check saved city
//        for (City c : cityList) {
//            cityBusiness.saveCity(c);
//        }
//
//        assertEquals(cityList, cityBusiness.getAllCities());
//    }

    @Test
    void findCityByName_SavedCityEqualsCityInDatabase_IsTrue() {
        //Create city business obj
        CityBusiness cityBusiness = new CityBusinessImpl();
        WeatherDataBusiness weatherDataBusiness = new WeatherDataBusinessImpl();

        //fetch all cities from rws
        List<City> cityList = weatherDataBusiness.fetchAvailableCities();

        //save and check saved city
        for (City c : cityList) {
            cityBusiness.saveCity(c);
            assertEquals(c, cityBusiness.findCityByName(c.getName()));
        }
    }
}
