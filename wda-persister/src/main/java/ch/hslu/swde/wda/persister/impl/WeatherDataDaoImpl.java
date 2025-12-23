package ch.hslu.swde.wda.persister.impl;

import ch.hslu.swde.wda.domain.City;
import ch.hslu.swde.wda.domain.WeatherData;
import ch.hslu.swde.wda.persister.api.WeatherDataPersister;
import ch.hslu.swde.wda.persister.util.JpaCrud;
import ch.hslu.swde.wda.persister.util.JpaUtil;
import jakarta.persistence.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.time.LocalDateTime;
import java.util.*;

public class WeatherDataDaoImpl implements WeatherDataPersister {

    /* Alle SQL Queries wurden mitHilfe von KI implementiert und ausgebessert */

    private static final Logger logger = LogManager.getLogger(WeatherDataDaoImpl.class);

    final private JpaCrud<WeatherData> crud = new JpaCrud<>(WeatherData.class);

    @Override
    public void saveWeatherData(List<WeatherData> weatherDataList) throws Exception {
        logger.info("Saving weather data");
        EntityManager em = JpaUtil.createEntityManager();
        try {
            em.getTransaction().begin();
            for (WeatherData weatherData : weatherDataList) {

                if (weatherData.getCity() != null && em.find(City.class, weatherData.getCity().getZipCode()) == null) {
                    em.persist(weatherData.getCity());
                }

                em.persist(weatherData);
                logger.info("Saved weather data");
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            logger.error("Error saving weather data list." + e);
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new Exception("fehler bei speichern von wetterdaten");
        } finally {
            em.close();
        }


    }

    @Override
    public LocalDateTime getLastDateTime() throws Exception {
        logger.info("Getting last weather datetime");
        EntityManager em = JpaUtil.createEntityManager();
        try {
            LocalDateTime startOf2024 = LocalDateTime.of(2024, 1, 1, 0, 0);
            LocalDateTime lastDateTime = (LocalDateTime) em.createQuery("SELECT MAX(w.lastUpdateTime) FROM WeatherData w").getSingleResult();
            logger.info("Last weather datetime is " + lastDateTime);
            return Objects.requireNonNullElse(lastDateTime, startOf2024);
        } catch (Exception e) {
            logger.error("Error getting last weather datetime");
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new Exception("fehler bei speichern von wetterdaten");
        } finally {
            em.close();
        }
    }

    @Override
    public WeatherData delete(int id) {
        logger.info("Deleting weather data with id " + id);
        return crud.delete(id);
    }

    @Override
    public WeatherData update(WeatherData weatherData) {
        logger.info("Updating weather data with id " + weatherData.getId());
        return crud.update(weatherData);
    }

    @Override
    public WeatherData find(int zipCode) {
        logger.info("Finding weather data with zipCode " + zipCode);
        return crud.find(zipCode);
    }

    @Override
    public List<WeatherData> all() {
        logger.info("Getting all weather data");
        return crud.all();
    }

    @Override
    public void deleteWeatherData(WeatherData weatherData) throws Exception {
        logger.info("Deleting weather data with id " + weatherData.getId());
        EntityManager em = JpaUtil.createEntityManager();
        try {
            em.getTransaction().begin();

            WeatherData existingWeatherData = em.find(WeatherData.class, weatherData.getId());
            if (existingWeatherData == null) {
                logger.warn("WeatherData with ID " + weatherData.getId() + " does not exist.");
                throw new IllegalArgumentException("WeatherData with ID " + weatherData.getId() + " does not exist.");
            }
            em.remove(existingWeatherData);

            em.getTransaction().commit();
            logger.info("Deleted weather data with id " + weatherData.getId());
        } catch (Exception e) {
            logger.error("Error deleting weather data with id " + weatherData.getId());
            if(em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new Exception("fehler beim löschen von wetterdaten");
        }finally {
            em.close();
        }
    }

    @Override
    public List<WeatherData> getWeatherData(String name) throws Exception {
        logger.info("Getting weather data with name " + name);
        EntityManager em = JpaUtil.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT wd FROM WeatherData wd JOIN wd.city c WHERE c.name = :name", WeatherData.class)
                    .setParameter("name", name)
                    .getResultList();

        } catch (Exception e) {
            logger.error("Error getting weather data with name " + name);
            if(em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new Exception("Error retrieving weather data for city: " + name, e);
        }finally {
            em.close();
        }
    }

    @Override
    public List<WeatherData> getWeatherDataForPeriod(int zipCode, LocalDateTime startDate, LocalDateTime endDate) throws Exception {
        logger.info("Retrieving weather data for zipCode: {} between {} and {}", zipCode, startDate, endDate);

        EntityManager em = JpaUtil.createEntityManager();
        try {

            return em.createQuery(
                            "SELECT wd FROM WeatherData wd WHERE wd.city.zipCode = :zipCode AND wd.lastUpdateTime BETWEEN :startDate AND :endDate",
                            WeatherData.class)
                    .setParameter("zipCode", zipCode)
                    .setParameter("startDate", startDate)
                    .setParameter("endDate", endDate)
                    .getResultList();

        } catch (Exception e) {
            logger.error("Error retrieving weather data for zipCode: {} between {} and {}", zipCode, startDate, endDate, e);

            if(em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new Exception("Error retrieving weather data for city: " + zipCode
                    + " between " + startDate + " and " + endDate, e);
        }finally {
            em.close();
        }
    }

    @Override
    public List<String> getAvgByCityAndTimestamp(int zipCode, LocalDateTime startDate, LocalDateTime endDate) throws Exception {
        logger.info("Calculating average weather data for zipCode: {} between {} and {}", zipCode, startDate, endDate);

        EntityManager em = JpaUtil.createEntityManager();
        try{
            em.getTransaction().begin();
            String query_MinMax = "SELECT AVG(w.currentTemperature), AVG(w.pressure), AVG(w.humidity) " +
                    "FROM WeatherData w " +
                    "WHERE w.city.zipCode = :zipCode " +
                    "AND w.lastUpdateTime BETWEEN :startDate AND :endDate";
            Query query = em.createQuery(query_MinMax);
            query.setParameter("zipCode", zipCode);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            Object[] result = (Object[]) query.getSingleResult();
            return Collections.singletonList(Arrays.toString(result)); // List<String>
        }catch (Exception e) {
            logger.error("Error calculating averages for zipCode: {} between {} and {}", zipCode, startDate, endDate, e);
            if(em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new Exception("Error retrieving weather data for city: " + zipCode);
        }finally {
            em.close();
        }
    }

    @Override
    public List<String> getMinMaxByCityAndTimestamp(int zipCode, LocalDateTime startDate, LocalDateTime endDate) throws Exception {
        logger.info("Retrieving Min/Max weather data for zipCode: {} between {} and {}", zipCode, startDate, endDate);
        EntityManager em = JpaUtil.createEntityManager();
        try{
            em.getTransaction().begin();
            String query_MinMax = "SELECT MIN(w.currentTemperature), MAX(w.currentTemperature), MIN(w.pressure), "+
                    "MAX(w.pressure), MIN(w.humidity), MAX(w.humidity) " +
                    "FROM WeatherData w " +
                    "WHERE w.city.zipCode = :zipCode " +
                    "AND w.lastUpdateTime BETWEEN :startDate AND :endDate";
            Query query = em.createQuery(query_MinMax);
            query.setParameter("zipCode", zipCode);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            Object[] result = (Object[]) query.getSingleResult();
            return Collections.singletonList(Arrays.toString(result)); // List<String>
        }catch (Exception e) {
            logger.error("Error retrieving Min/Max weather data for zipCode: {} between {} and {}", zipCode, startDate, endDate, e);
            if(em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new Exception("Error retrieving weather data for city: " + zipCode);

        }finally {
            em.close();
        }
    }

    @Override
    public List<Integer> getMinMaxCityByTimestamp(LocalDateTime startDate, LocalDateTime endDate) throws Exception {
        logger.info("Retrieving cities with Min/Max values between {} and {}", startDate, endDate);
        EntityManager em = JpaUtil.createEntityManager();
        try{
            List<Integer> cities = new ArrayList<>();
            em.getTransaction().begin();
            cities.add(getQueryOutputForMinMaxCity("MAX(w2.currentTemperature)","w.currentTemperature",startDate,endDate));
            cities.add(getQueryOutputForMinMaxCity("MIN(w2.currentTemperature)","w.currentTemperature",startDate,endDate));
            cities.add(getQueryOutputForMinMaxCity("MAX(w2.pressure)","w.pressure",startDate,endDate));
            cities.add(getQueryOutputForMinMaxCity("MIN(w2.pressure)","w.pressure",startDate,endDate));
            cities.add(getQueryOutputForMinMaxCity("MAX(w2.humidity)","w.humidity",startDate,endDate));
            cities.add(getQueryOutputForMinMaxCity("MIN(w2.humidity)","w.humidity",startDate,endDate));
            return cities; // List<Integer> [Max(Temp),Min(Temp),Max(pressure),Min(Pressure),Max(Humidity),Min(humidity)]
        }catch (Exception e) {
            logger.error("Error retrieving cities with Min/Max values between {} and {}", startDate, endDate, e);
            if(em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new Exception("Error retrieving weather data for city: "); // to do
        }finally {
            em.close();
        }
    }

    private Integer getQueryOutputForMinMaxCity(String queryComplement1, String queryComplement2, LocalDateTime startDate, LocalDateTime endDate) {
        logger.debug("Executing query for Min/Max city with complement1: {}, complement2: {}, between {} and {}",
                queryComplement1, queryComplement2, startDate, endDate);
        EntityManager em = JpaUtil.createEntityManager();
        try {
            String queryMinMax = "SELECT w.city.zipCode " +
                    "FROM WeatherData w " +
                    "WHERE w.lastUpdateTime BETWEEN :startDate AND :endDate " +
                    "  AND " + queryComplement2 + " = (" +
                    "      SELECT " + queryComplement1 +
                    "      FROM WeatherData w2 " +
                    "      WHERE w2.lastUpdateTime BETWEEN :startDate AND :endDate" +
                    ")";

            Query query = em.createQuery(queryMinMax);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            query.setMaxResults(1);

            Integer result = (Integer) query.getSingleResult();
            logger.info("Query result for Min/Max city: {}", result);
            return result;
        } catch (NoResultException e) {
            logger.warn("No result found for query with complement1: {}, complement2: {}", queryComplement1, queryComplement2);
            return null;
        } catch (Exception e) {
            logger.error("Error executing query for Min/Max city with complement1: {}, complement2: {}", queryComplement1, queryComplement2, e);
            throw new RuntimeException("Error executing query for Min/Max city", e);
        } finally {
            em.close();
        }
    }
}
