package ch.hslu.swde.wda.business.impl;

import ch.hslu.swde.wda.business.api.WeatherDataBusiness;
import ch.hslu.swde.wda.domain.City;
import ch.hslu.swde.wda.domain.WeatherData;
import ch.hslu.swde.wda.persister.impl.WeatherDataDaoImpl;
import ch.hslu.swde.wda.reader.WeatherDataReaderImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class WeatherDataBusinessImpl implements WeatherDataBusiness {
    private static final Logger logger = LogManager.getLogger(WeatherDataBusinessImpl.class);

    private final WeatherDataDaoImpl weatherDataDao = new WeatherDataDaoImpl();
    private  final WeatherDataReaderImpl weatherDataReader = new WeatherDataReaderImpl();

    public WeatherDataBusinessImpl() {}


    @Override
    public void updateWeatherData()  {
        logger.info("Updating weather data");
        try {
            List<City> cities =  weatherDataReader.getAvailableLocations();
            LocalDateTime lastUpdate = weatherDataDao.getLastDateTime();
            for (City city : cities) {
                if (city.getName().equals("St. Moritz")){
                    city.setName("St.%20Moritz");
                }
                if (city.getName().equals("St. Gallen")){
                    city.setName("St.%20Gallen");
                }
                List<WeatherData> weatherData = weatherDataReader.getWeatherDataForYear(city, 2024);
                List<WeatherData> filteredData= weatherData.stream().filter(weatherData1 -> weatherData1.getLastUpdateTime().isAfter(lastUpdate)).collect(Collectors.toList());
                weatherDataDao.saveWeatherData(filteredData);
                System.out.println("Finish writing weatherdata for City: " + city.getName() + " in database");
            }
            System.out.println("Finish updating database");
        }catch (Exception e){
            logger.error("Error fetching available locations." + e);
            throw new RuntimeException("Error fetching available locations.", e);
        }
    }

    @Override
    public List<City> fetchAvailableCities() {
        logger.info("Fetching available cities");
        try {
             return weatherDataReader.getAvailableLocations();
        } catch (Exception e) {
            logger.error("Error fetching available cities." + e);
            throw new RuntimeException("Error fetching available locations.", e);
        }
    }

    @Override
    public List<WeatherData> getWeatherDataForPeriod(int zipCode, LocalDateTime startDate, LocalDateTime endDate) {
        logger.info("Fetching weather data for period");
        try {
            return weatherDataDao.getWeatherDataForPeriod(
                    zipCode,
                    startDate,
                    endDate
            );
        } catch (Exception e) {
            logger.error("Error fetching weather data for period." + e);
            throw new RuntimeException("Error fetching weather data for the period.", e);
        }
    }

    @Override
    public List<String> calculateAverageWeatherData(int zipCode, LocalDateTime startDate, LocalDateTime endDate) {
        logger.info("Calculating average weather data for period");
        try {
            System.out.println(startDate);
            return weatherDataDao.getAvgByCityAndTimestamp(zipCode,startDate,endDate);
        } catch (Exception e) {
            logger.error("Error calculating average weather data for period." + e);
            throw new RuntimeException("Error calculating average weather data.", e);
        }
    }

    @Override
    public List<String> calculateMinMaxWeatherData(int zipCode, LocalDateTime startDate, LocalDateTime endDate) {
        logger.info("Calculating min max weather data for period");
        try {
            return weatherDataDao.getMinMaxByCityAndTimestamp(zipCode,startDate,endDate);
        } catch (Exception e) {
            logger.error("Error calculating min max weather data for period." + e);
            throw new RuntimeException("Error calculating min/max weather data.", e);
        }
    }

    @Override
    public List<Integer> fetchExtremeWeatherData(LocalDateTime startDate, LocalDateTime endDate) {
        logger.info("Fetching extreme weather data for period");
        try {
            return weatherDataDao.getMinMaxCityByTimestamp(startDate,endDate);
        } catch (Exception e) {
            logger.error("Error fetching extreme weather data for period." + e);
            throw new RuntimeException("Error fetching extreme weather data.", e);
        }
    }
}

