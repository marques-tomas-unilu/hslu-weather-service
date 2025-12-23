package ch.hslu.swde.wda.persister;

import ch.hslu.swde.wda.persister.impl.CityDaoImpl;
import ch.hslu.swde.wda.domain.City;
import ch.hslu.swde.wda.persister.util.JpaUtil;
import org.junit.jupiter.api.*;
import jakarta.persistence.EntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CityDaoImplTest {

    private final CityDaoImpl dao = new CityDaoImpl();

    /* Alle SQL Queries wurden mitHilfe von KI implementiert und ausgebessert */

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
    void testEquals_whenSendEntityToDB_thenDBAndReceiveDataAreEqual() throws Exception {
        City city = new City(12,"test",2,2.2,1.2);

        dao.saveCity(city);
        City city2 = dao.findCityByZipCode(2);

        assertEquals(2,city2.getZipCode());
    }
    @Test
    void testEquals_whenUpdateDBEntity_thenDBandUpdatedEntityAreEqual() throws Exception {
        City city = new City(12,"test",2,2.2,1.2);

        dao.saveCity(city);
        city.setName("test1");
        dao.updateCity(city);
        City city2 = dao.findCityByZipCode(2);
        assertEquals(city.getName(),city2.getName());
    }

    @Test
    void testEquals_whenDeleteDBEntity_thenThrowsIllegalArgumentException() throws Exception {
        City city = new City(12,"test",2,2.2,1.2);

        dao.saveCity(city);
        dao.deleteCity(city);
        try {
            City city2 = dao.findCityByZipCode(2);
        }catch (IllegalArgumentException e){
            assertEquals("City with ID " + city.getZipCode() + " does not exist.",e.getMessage());
        }
    }

    @Test
    void testGetAllCities() throws Exception {
        City city1 = new City(12,"test",2,2.2,1.2);

        City city2 = new City(13,"test1",3,2.2,1.2);

        City city3 = new City(14,"test2",4,2.2,1.2);

        dao.saveCity(city1);
        dao.saveCity(city2);
        dao.saveCity(city3);

        List<City> cities = dao.getAllCities();

        assertNotNull(cities);
        assertEquals(3, cities.size());
        assertTrue(cities.stream().anyMatch(city -> city.getName().equals("test")));
        assertTrue(cities.stream().anyMatch(city -> city.getName().equals("test1")));
    }
}
