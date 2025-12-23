package ch.hslu.swde.wda.persister.impl;


import ch.hslu.swde.wda.domain.City;
import ch.hslu.swde.wda.domain.WeatherData;
import ch.hslu.swde.wda.persister.api.CityPersister;
import ch.hslu.swde.wda.persister.util.JpaCrud;
import ch.hslu.swde.wda.persister.util.JpaUtil;
import jakarta.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class CityDaoImpl implements CityPersister {
    final private JpaCrud<WeatherData> crud = new JpaCrud<>(WeatherData.class);
    private static final Logger logger = LogManager.getLogger(CityDaoImpl.class);

// bei den Implementierungen mit em.getTransaction().begin(); habe ich die erste methode durch chat gpt generieren lassen und dann die anderen abgekupfert.
    @Override
    public void saveCity(City city) throws Exception {
        logger.info("Saving city: " + city);
        EntityManager em = JpaUtil.createEntityManager();
        try {
            if (em.find(City.class,city.getZipCode()) == null) {
                em.getTransaction().begin();
                em.persist(city);
                em.getTransaction().commit();
                logger.info("Saved city: " + city);
            }else {
                logger.warn("City already exists: " + city);
                System.out.println(city.getName() + " is allready saved");
            }
        } catch (Exception e) {
            logger.error("Fehler beim Speichern der Stadt: " +e);
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new Exception("Fehler beim Speichern der Stadt: " + city.getName());
        } finally {
            em.close();
        }
    }

    @Override
    public void updateCity(City city) throws Exception {
        logger.info("Updating city: " + city);
        EntityManager em = JpaUtil.createEntityManager();
        try {
            em.getTransaction().begin();
            City existingCity = em.find(City.class, city.getZipCode());
            if (existingCity == null) {
                logger.warn("City does not exist: " + city);
                throw new IllegalArgumentException("City with ID " + city.getZipCode() + " does not exist.");
            }
            logger.info("Updating city: " + city);
            em.merge(city);
            em.getTransaction().commit();
        } catch (Exception e) {
            logger.error("Fehler beim updaten der Stadt: " + e);
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new Exception("Fehler beim updaten der City " + city.getZipCode());
        }finally {
            em.close();
        }
    }

    @Override
    public void deleteCity(City city) throws Exception {
        logger.info("Deleting city: " + city);
        EntityManager em = JpaUtil.createEntityManager();
        try {
            em.getTransaction().begin();
            City existingCity = em.find(City.class, city.getZipCode());
            if (existingCity == null) {
                logger.warn("City does not exist: " + city);
                throw new IllegalArgumentException("City with ID " + city.getZipCode() + " does not exist.");
            }

            em.remove(existingCity);

            em.getTransaction().commit();
            logger.info("City deleted successfully: {}", city);

        } catch (Exception e) {
            logger.error("Error deleting city: " + e);
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new Exception("Error deleting city with ID " + city.getStationID());
        }finally {
            em.close();
        }
    }

    @Override
    public City findCityByZipCode(int zipCode) throws Exception {
        logger.info("Finding city by zip code: " + zipCode);
        EntityManager em = JpaUtil.createEntityManager();
        try {

            City city = em.find(City.class, zipCode);

            if (city == null) {
                logger.warn("City does not exist: " + zipCode);
                throw new IllegalArgumentException("City with ID " + zipCode + " does not exist.");
            }
            logger.info("Finding city by zip code: " + zipCode);
            return city;
        } catch (IllegalArgumentException e) {
            logger.error("Error finding city by zip code: " + zipCode);
            throw e;
        } catch (Exception e) {
            throw new Exception("Error finding city with ID " + zipCode);
        }finally {
            em.close();
        }
    }

    @Override
    public City findCityByName(String name) throws Exception {
        logger.info("Finding city by name: " + name);
        EntityManager em = JpaUtil.createEntityManager();
        try {
            City city = em.createQuery(
                            "SELECT c FROM City c WHERE c.name = :name", City.class)
                    .setParameter("name", name)
                    .getSingleResult();
            logger.info("Finding city by name: " + name);
            return city;
        } catch (jakarta.persistence.NoResultException e) {
            logger.warn("City does not exist: " + name);
            throw new IllegalArgumentException("City with name '" + name + "' does not exist.");
        } catch (Exception e) {
            logger.error("Error finding city by name: " + name);
            throw new Exception("Error finding city with name '" + name + "'");
        }finally {
            em.close();
        }
    }

    @Override
    public List<City> getAllCities() throws Exception {
        logger.info("Getting all cities");
        EntityManager em = JpaUtil.createEntityManager();
        try {
            return em.createQuery("SELECT c FROM City c", City.class).getResultList();
        } catch (Exception e) {
            logger.error("Error finding all cities");
            throw new Exception("Error retrieving all cities.");
        }finally {
            em.close();
        }
    }
}