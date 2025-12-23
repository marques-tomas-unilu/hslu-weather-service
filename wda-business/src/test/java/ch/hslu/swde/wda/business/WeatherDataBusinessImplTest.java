package ch.hslu.swde.wda.business;

import ch.hslu.swde.wda.business.api.WeatherDataBusiness;
import ch.hslu.swde.wda.domain.City;
import ch.hslu.swde.wda.domain.WeatherData;
import ch.hslu.swde.wda.business.impl.WeatherDataBusinessImpl;
import ch.hslu.swde.wda.persister.impl.WeatherDataDaoImpl;
import ch.hslu.swde.wda.persister.util.JpaUtil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WeatherDataBusinessImplTest {
    WeatherDataBusinessImpl business = new WeatherDataBusinessImpl();
    WeatherDataDaoImpl dao = new WeatherDataDaoImpl();
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
/*
    @Test
    void updateWeatherData_WhenCalled_DataIsUpdatedSuccessfully() throws Exception {
        WeatherDataBusiness weatherDataBusiness = new WeatherDataBusinessImpl();

        weatherDataBusiness.updateWeatherData();

        List<WeatherData> weatherDataList = weatherDataBusiness.getWeatherDataForPeriod(
                1000,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now()
        );
        assertNotNull(weatherDataList);
        assertFalse(weatherDataList.isEmpty(), "Weather data should be updated successfully.");
    }

*/

    @Test
    void fetchAvailableCities_WhenCalled_ReturnsListOfCities() {
        try {


            WeatherData weatherData = new WeatherData(12, "Test", 1234, 0.1, 0.1, 0.1, 0.1, 0.1, "test", "test", 123, 0.1, LocalDateTime.of(2024, 1, 1, 0, 0));
            WeatherData weatherData2 = new WeatherData(12, "Test", 1234, 0.1, 0.1, 0.1, 0.1, 0.1, "test", "test", 123, 0.1, LocalDateTime.of(2024, 1, 1, 0, 0));
            List<WeatherData> weatherDataList = new LinkedList<>();
            weatherDataList.add(weatherData);
            weatherDataList.add(weatherData2);

            dao.saveWeatherData(weatherDataList);
            List<City> cityList = business.fetchAvailableCities();

            assertTrue(cityList.size() > 0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /*
    @Test
    void fetchAvailableCities_WhenNoCitiesExist_ReturnsEmptyList() {
        WeatherDataBusiness weatherDataBusiness = new WeatherDataBusinessImpl();

        List<City> cityList = weatherDataBusiness.fetchAvailableCities();

        assertNotNull(cityList);
        assertTrue(cityList.isEmpty(), "City list should be empty when no cities exist.");
    }


     */
/*
    @Test
    void getWeatherDataForPeriod_WhenDataExists_ReturnsCorrectWeatherData() {
            WeatherData weatherData = new WeatherData(12, "Test", 1234, 0.1, 0.1, 0.1, 0.1, 0.1, "test", "test", 123, 0.1, LocalDateTime.of(2024, 1, 1, 0, 0));

        WeatherDataBusiness weatherDataBusiness = new WeatherDataBusinessImpl();

        LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 1, 2, 0, 0);

        List<WeatherData> weatherDataList = weatherDataBusiness.getWeatherDataForPeriod(1000, startDate, endDate);

        assertNotNull(weatherDataList);
        assertFalse(weatherDataList.isEmpty(), "Weather data should exist for the given period.");
    }

     */
    @Test
    void getWeatherDataForPeriod_WhenNoDataExists_ReturnsEmptyList() {
        WeatherData weatherData = new WeatherData(12, "Test", 1234, 0.1, 0.1, 0.1, 0.1, 0.1, "test", "test", 123, 0.1, LocalDateTime.of(2024, 1, 1, 0, 0));

        WeatherDataBusiness weatherDataBusiness = new WeatherDataBusinessImpl();

        LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 1, 2, 0, 0);

        List<WeatherData> weatherDataList = weatherDataBusiness.getWeatherDataForPeriod(9999, startDate, endDate);

        assertNotNull(weatherDataList);
        assertTrue(weatherDataList.isEmpty(), "Weather data list should be empty for non-existing data.");
    }
    @Test
    void calculateAverageWeatherData_WhenDataExists_ReturnsCorrectAverages() {
        WeatherData weatherData = new WeatherData(12, "Test", 1234, 0.1, 0.1, 0.1, 0.1, 0.1, "test", "test", 123, 0.1, LocalDateTime.of(2024, 1, 1, 0, 0));

        WeatherDataBusiness weatherDataBusiness = new WeatherDataBusinessImpl();

        LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 1, 2, 0, 0);

        List<String> averages = weatherDataBusiness.calculateAverageWeatherData(1000, startDate, endDate);

        assertNotNull(averages);
        assertFalse(averages.isEmpty(), "Average weather data should be returned.");
    }
    /*
    @Test
    void calculateAverageWeatherData_WhenNoDataExists_ReturnsEmptyList() {
        WeatherDataBusiness weatherDataBusiness = new WeatherDataBusinessImpl();

        LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 1, 2, 0, 0);

        List<String> averages = weatherDataBusiness.calculateAverageWeatherData(9999, startDate, endDate);

        assertNotNull(averages);
        assertTrue(averages.isEmpty(), "Average weather data list should be empty when no data exists.");
    }
    */

    @Test
    void calculateMinMaxWeatherData_WhenDataExists_ReturnsMinMaxValues() {
        WeatherData weatherData = new WeatherData(12, "Test", 1234, 0.1, 0.1, 0.1, 0.1, 0.1, "test", "test", 123, 0.1, LocalDateTime.of(2024, 1, 1, 0, 0));

        WeatherDataBusiness weatherDataBusiness = new WeatherDataBusinessImpl();

        LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 1, 2, 0, 0);

        List<String> minMax = weatherDataBusiness.calculateMinMaxWeatherData(1000, startDate, endDate);

        assertNotNull(minMax);
        assertFalse(minMax.isEmpty(), "Min/Max weather data should be returned.");
    }
    /*
    @Test
    void calculateMinMaxWeatherData_WhenNoDataExists_ReturnsEmptyList() {
        WeatherDataBusiness weatherDataBusiness = new WeatherDataBusinessImpl();

        LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 1, 2, 0, 0);

        List<String> minMax = weatherDataBusiness.calculateMinMaxWeatherData(9999, startDate, endDate);

        assertNotNull(minMax);
        assertTrue(minMax.isEmpty(), "Min/Max weather data list should be empty when no data exists.");
    }
    */

    @Test
    void fetchExtremeWeatherData_WhenDataExists_ReturnsExtremeValues() {
        WeatherData weatherData = new WeatherData(12, "Test", 1234, 0.1, 0.1, 0.1, 0.1, 0.1, "test", "test", 123, 0.1, LocalDateTime.of(2024, 1, 1, 0, 0));

        WeatherDataBusiness weatherDataBusiness = new WeatherDataBusinessImpl();

        LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 1, 2, 0, 0);

        List<Integer> extremes = weatherDataBusiness.fetchExtremeWeatherData(startDate, endDate);

        assertNotNull(extremes);
        assertFalse(extremes.isEmpty(), "Extreme weather data should be returned.");
    }
    /*
    @Test
    void fetchExtremeWeatherData_WhenNoDataExists_ReturnsEmptyList() {
        WeatherDataBusiness weatherDataBusiness = new WeatherDataBusinessImpl();

        LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 1, 2, 0, 0);

        List<Integer> extremes = weatherDataBusiness.fetchExtremeWeatherData(startDate, endDate);

        assertNotNull(extremes);
        assertTrue(extremes.isEmpty(), "Extreme weather data list should be empty when no data exists.");
    }
*/
    /*
    @Test
    void test_1IsEqual1(){
        try{
//            System.out.println(business.fetchAvailableCities());
            long unDiaEnMilisegundos = 24 * 60 * 60 * 1000;
            System.out.println(new Date());
            System.out.println(LocalDateTime.of(2024, 12, 9, 22, 34, 48));
            System.out.println(LocalDateTime.parse("2024-12-09T22:34:48"));
            // System.out.println(business.getWeatherDataForPeriod(1000,LocalDateTime.of(2024, 12, 9, 22, 34, 48),LocalDateTime.now()));
            System.out.println(business.calculateAverageWeatherData(1000,LocalDateTime.of(2024, 12, 9, 22, 34, 48),LocalDateTime.now()));
            System.out.println(business.calculateMinMaxWeatherData(1000,LocalDateTime.of(2024, 12, 9, 22, 34, 48),LocalDateTime.now()));
            System.out.println(business.fetchExtremeWeatherData(LocalDateTime.of(2024, 12, 9, 22, 34, 48),LocalDateTime.now()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        assertEquals(1,1);
    }

*/
}
