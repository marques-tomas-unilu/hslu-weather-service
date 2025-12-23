package ch.hslu.swde.wda.ui.api;

public interface WeatherDataUI {

    void showAvailableCities();

    void showWeatherDataForCityForSpecificTimePeriod(String city, String startDate, String endDate);

    void showAverageWeatherDataForCityForSpecificTimePeriod(String city, String startDate, String endDate);

    void showMinMaxWeatherDataForCityForSpecificTimePeriod(String city, String startDate, String endDate);

    void showCitiesWithExtremeWeatherValuesForSpecificTimePeriod(String startDate, String endDate);

    void exportWeatherDataForCityForSpecificTimePeriod(String city, String startDate, String endDate);

    void synchronizeData();
}
