package ch.hslu.swde.wda.reader;

import ch.hslu.swde.wda.domain.City;
import ch.hslu.swde.wda.domain.WeatherData;

import java.util.List;

public interface WeatherDataReader {

    List<City> getAvailableLocations();
    WeatherData getLatestWeatherData(final City city);
    List<WeatherData> getWeatherDataForYear(final City city, final int year);
}
