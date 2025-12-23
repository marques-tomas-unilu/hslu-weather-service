package ch.hslu.swde.wda.persister;

import ch.hslu.swde.wda.domain.WeatherData;
import ch.hslu.swde.wda.persister.impl.WeatherDataDaoImpl;
import ch.hslu.swde.wda.persister.util.JpaUtil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WeatherDataDaoImplTest {

    private WeatherDataDaoImpl dao = new WeatherDataDaoImpl();

    /* Alle SQL Queries wurden mitHilfe von KI implementiert und ausgebessert */

    @BeforeEach
    void setUp() {
        System.setProperty("APP_ENV", "test");
        EntityManager em = JpaUtil.createEntityManager();
        em.getTransaction().begin();

        em.createQuery("DELETE FROM WeatherData e").executeUpdate();

        em.getTransaction().commit();

        em.close();
    }

    @AfterEach
    void tearDown() {
        EntityManager em = JpaUtil.createEntityManager();
        em.getTransaction().begin();

        em.createQuery("DELETE FROM WeatherData e").executeUpdate();

        em.getTransaction().commit();

        em.close();
        System.setProperty("APP_ENV", "production");
    }


    @Test
    void testEquals_whenSendEntityToDB_thenDBAndReceiveDataAreEqual() throws Exception {
        WeatherData weatherData = new WeatherData(
                12,"BCN",6003,2.2,2.1,
                2.2,2.1,10.2,"Good weather!",
                "raining",10,10.3,
                LocalDateTime.of(2024, 11, 28, 14, 0));
        List<WeatherData> weatherDataList = new ArrayList<>();
        weatherDataList.add(weatherData);
        dao.saveWeatherData(weatherDataList);
        WeatherData weatherData1 = dao.find(weatherData.getId());

        assertEquals(weatherData.toString(),weatherData1.toString());
    }

    @Test
    void testEquals_whenDeleteDBEntity_thenThrowsIllegalArgumentException() throws Exception {
        WeatherData weatherData = new WeatherData(
                12,"BCN",6003,2.2,2.1,
                2.2,2.1,10.2,"Good weather!",
                "raining",10,10.3,
                LocalDateTime.of(2024, 11, 28, 14, 0));
        List<WeatherData> weatherDataList = new ArrayList<>();
        weatherDataList.add(weatherData);
        dao.saveWeatherData(weatherDataList);
        dao.deleteWeatherData(weatherData);
        try {
            WeatherData weatherData1 = dao.find(weatherData.getId());
        }catch (IllegalArgumentException e){
            assertEquals("WeatherData with ID " + weatherData.getId() + " does not exist.",e.getMessage());
        }
    }

    @Test
    void testGetWeatherData() throws Exception {
        WeatherData weatherData = new WeatherData(
                12,"Luzern",6001,2.2,2.1,
                2.2,2.1,10.2,"Good weather!",
                "raining",10,10.3,
                LocalDateTime.of(2024, 11, 28, 14, 0));

        WeatherData weatherData1 = new WeatherData(
                12,"Luzern",6001,2.2,2.1,
                2.2,2.1,10.2,"Good weather!",
                "raining",10,10.3,
                LocalDateTime.of(2024, 11, 28, 14, 0));
        List<WeatherData> weatherDataList = new ArrayList<>();
        weatherDataList.add(weatherData);
        weatherDataList.add(weatherData1);
        dao.saveWeatherData(weatherDataList);

        List<WeatherData> weatherDataList2 = dao.getWeatherData("Luzern");
        assertEquals(2, weatherDataList2.size());
    }
    @Test
    void testGetWeatherDataForPeriod() throws Exception {
        WeatherData weatherData = new WeatherData(
                12,"BCN",6003,2.2,2.1,
                2.2,2.1,10.2,"Good weather!",
                "raining",10,10.3,
                LocalDateTime.of(2024, 11, 28, 14, 0));

        WeatherData weatherData1 = new WeatherData(
                12,"Luzern",6001,2.2,2.1,
                2.2,2.1,10.2,"Good weather!",
                "raining",10,10.3,
                LocalDateTime.of(2024, 11, 28, 14, 0));
        List<WeatherData> weatherDataList = new ArrayList<>();
        weatherDataList.add(weatherData);
        weatherDataList.add(weatherData1);
        dao.saveWeatherData(weatherDataList);

        List<WeatherData> weatherDataList1 = dao.getWeatherDataForPeriod(weatherData.getCity().getZipCode(),
                LocalDateTime.of(2024, 11, 27, 14, 0),
                LocalDateTime.of(2024, 11, 28, 14, 0));
        assertEquals(1, weatherDataList1.size());
    }

}
