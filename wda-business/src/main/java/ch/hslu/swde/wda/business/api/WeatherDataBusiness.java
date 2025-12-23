package ch.hslu.swde.wda.business.api;

import ch.hslu.swde.wda.domain.City;
import ch.hslu.swde.wda.domain.WeatherData;

import java.time.LocalDateTime;
import java.util.List;

public interface WeatherDataBusiness {

    void updateWeatherData() throws Exception;
    List<City> fetchAvailableCities();
    List<WeatherData> getWeatherDataForPeriod(int zipCode, LocalDateTime startDate, LocalDateTime endDate);
    List<String> calculateAverageWeatherData(int zipCode, LocalDateTime startDate, LocalDateTime endDate);
    List<String> calculateMinMaxWeatherData(int zipCode, LocalDateTime startDate, LocalDateTime endDate);
    List<Integer> fetchExtremeWeatherData(LocalDateTime startDate, LocalDateTime endDate);

}
