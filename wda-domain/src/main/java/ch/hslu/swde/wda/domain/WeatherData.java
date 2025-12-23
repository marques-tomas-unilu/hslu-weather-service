package ch.hslu.swde.wda.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDateTime;
@Entity

public class WeatherData  implements Serializable, Comparable<WeatherData> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter int id;

    @Getter @Setter
    @ManyToOne
    @JoinColumn(name = "zipCode")
    private City city;

    @Getter @Setter private double pressure;
    @Getter @Setter private double humidity;
    @Getter @Setter private double currentTemperature;

    @Getter @Setter private String weatherSummary;
    @Getter @Setter private String weatherDescription;

    @Getter @Setter private int windDirection;
    @Getter @Setter private double windSpeed;
    @Getter @Setter private LocalDateTime lastUpdateTime;

    public WeatherData() {}


    public WeatherData(int stationID, String name, int zipCode, double latitude, double longitude, double pressure, double humidity, double currentTemperature, String weatherSummary, String weatherDescription, int windDirection, double windSpeed, LocalDateTime lastUpdateTime) {
        this.city = new City(stationID, name, zipCode, latitude, longitude);
        this.pressure = pressure;
        this.humidity = humidity;
        this.currentTemperature = currentTemperature;

        this.weatherSummary = weatherSummary;
        this.weatherDescription = weatherDescription;
        this.windDirection = windDirection;
        this.windSpeed = windSpeed;

        this.lastUpdateTime = lastUpdateTime;
    }

    /* ********************************************** */
    /* toString OUTPUT */

    @Override
    public String toString() {
        return "\nWeatherData {" +
                "\n  city=" + city.getName() +
                ",\n  pressure=" + pressure + " hPa" +
                ",\n  humidity=" + humidity + "%" +
                ",\n  currentTemperature=" + currentTemperature + "°C" +
                ",\n  weatherSummary='" + weatherSummary + '\'' +
                ",\n  weatherDescription='" + weatherDescription + '\'' +
                ",\n  windDirection=" + windDirection + "°" +
                ",\n  windSpeed=" + windSpeed + " m/s" +
                ",\n  lastUpdateTime=" + (lastUpdateTime != null ? lastUpdateTime : "N/A") +
                "\n}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WeatherData weatherData = (WeatherData) o;
        return id == weatherData.id;
    }

    @Override
    public int compareTo(WeatherData o) {
        return this.lastUpdateTime.compareTo(o.lastUpdateTime);
    }
}

