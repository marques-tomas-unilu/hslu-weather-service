package ch.hslu.swde.wda.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Entity

public class City implements Serializable, Comparable<City> {


    @Id
    @Getter private int zipCode;

    @Getter @Setter private int stationID;

    @Getter @Setter private String name;

    @Getter @Setter private double latitude;
    @Getter @Setter private double longitude;

    public City() {}

    public City(String name, int zipCode) {
        this.name = name;
        this.zipCode = zipCode;
    }

    public City(int stationId, String name, int zipCode, double latitude, double longitude) {
        this.stationID = stationId;
        this.name = name;
        this.zipCode = zipCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /* ********************************************** */
    /* toString OUTPUT */

    @Override
    public String toString() {
        return "\nCity {" +
                ",\n    name: '" + name + '\'' +
                ",\n    zipCode: " + zipCode +
                "\n}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        City city = (City) o;
        return zipCode == city.zipCode;
    }

    @Override
    public int compareTo(City o) {
        return this.zipCode - o.zipCode;
    }
}
