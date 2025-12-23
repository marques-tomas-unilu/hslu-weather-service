package ch.hslu.swde.wda.domain;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class WeatherDataTest {
    @Test
    void testEquals_whenSameValuesInConstructor_thenToStringAreEqual() {

        WeatherData weatherData = new WeatherData(
                1, "Test", 1, 2, 2,
                1.2, 3.2, 1.2,
                "Snowing", "Hot weather", 2, 2.2,
                LocalDateTime.of(2024, 11, 28, 14, 0)
        );
        String expected = "\nWeatherData {" +
                "\n  city=Test," +
                "\n  pressure=1.2 hPa," +
                "\n  humidity=3.2%," +
                "\n  currentTemperature=1.2°C," +
                "\n  weatherSummary='Snowing'," +
                "\n  weatherDescription='Hot weather'," +
                "\n  windDirection=2°," +
                "\n  windSpeed=2.2 m/s," +
                "\n  lastUpdateTime=2024-11-28T14:00" +
                "\n}";

        assertEquals(expected, weatherData.toString());
    }

    @Test
    public void testEquals_whenValuesSetTo2_2_thenGetPressureAreEqual() {
        // Arrange
        WeatherData weatherData = new WeatherData();

        // Act
        weatherData.setPressure(2.2);

        // Assert
        assertEquals(2.2, weatherData.getPressure());
    }

    @Test
    public void testEquals_whenValuesSetTo65_thenGetHumidityAreEqual() {
        // Arrange
        WeatherData weatherData = new WeatherData();

        // Act
        weatherData.setHumidity(65.0);

        // Assert
        assertEquals(65.0, weatherData.getHumidity());
    }

    @Test
    public void testEquals_whenValuesSetTo22_5_thenGetCurrentTemperatureAreEqual() {
        // Arrange
        WeatherData weatherData = new WeatherData();

        // Act
        weatherData.setCurrentTemperature(22.5);

        // Assert
        assertEquals(22.5, weatherData.getCurrentTemperature());
    }

    @Test
    public void testEquals_whenValuesSetToTest_thenGetWeatherSummaryAreEqual() {
        // Arrange
        WeatherData weatherData = new WeatherData();

        // Act
        weatherData.setWeatherSummary("Test");

        // Assert
        assertEquals("Test", weatherData.getWeatherSummary());
    }

    @Test
    public void testEquals_whenValuesSetToTest_thenGetWeatherDescriptionAreEqual() {
        // Arrange
        WeatherData weatherData = new WeatherData();

        // Act
        weatherData.setWeatherDescription("Test");

        // Assert
        assertEquals("Test", weatherData.getWeatherDescription());
    }

    @Test
    public void testEquals_whenValuesSetTo90_thenGetWindDirectionAreEqual() {
        // Arrange
        WeatherData weatherData = new WeatherData();

        // Act
        weatherData.setWindDirection(90);

        // Assert
        assertEquals(90, weatherData.getWindDirection());
    }

    @Test
    public void testEquals_whenValuesSetTo5_5_thenGetWindSpeedAreEqual() {
        // Arrange
        WeatherData weatherData = new WeatherData();

        // Act
        weatherData.setWindSpeed(5.5);

        // Assert
        assertEquals(5.5, weatherData.getWindSpeed());
    }

    @Test
    public void testEquals_whenValuesSetToLastUpdate_thenGetLastUpdateTimeAreEqual() {
        // Arrange
        WeatherData weatherData = new WeatherData();
        LocalDateTime lastUpdate = LocalDateTime.of(2024, 11, 28, 14, 0);

        // Act
        weatherData.setLastUpdateTime(lastUpdate);

        // Assert
        assertEquals(lastUpdate, weatherData.getLastUpdateTime());
    }

}
