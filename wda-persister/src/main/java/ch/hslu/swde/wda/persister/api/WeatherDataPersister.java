package ch.hslu.swde.wda.persister.api;

import ch.hslu.swde.wda.domain.WeatherData;
import java.time.LocalDateTime;
import java.util.List;

public interface WeatherDataPersister {

    void saveWeatherData(List<WeatherData> weatherData) throws Exception;
    LocalDateTime getLastDateTime() throws Exception;
    void deleteWeatherData(WeatherData weatherData) throws Exception;
    WeatherData delete(int zipCode);
    WeatherData update(WeatherData weatherData);
    WeatherData find(int id);
    List<WeatherData> all();
    List<WeatherData> getWeatherData(String name) throws Exception;

    List<WeatherData> getWeatherDataForPeriod(int zipCode, LocalDateTime startDate, LocalDateTime endDate) throws Exception;
    List<String> getAvgByCityAndTimestamp(int zipCode, LocalDateTime startDate, LocalDateTime endDate) throws Exception;
    List<String> getMinMaxByCityAndTimestamp(int zipCode, LocalDateTime startDate, LocalDateTime endDate) throws Exception;
    List<Integer> getMinMaxCityByTimestamp(LocalDateTime startDate, LocalDateTime endDate) throws Exception;

   /* List<WeatherData> getWeatherDataForPeriod(City city, Date startDate, Date endDate) throws Exception;

    void removeWeatherData(WeatherData weatherData) throws Exception;
    */

}
