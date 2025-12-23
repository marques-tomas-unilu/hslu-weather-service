package ch.hslu.swde.wda.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


class CityTest {
    @Test
    void testEquals_whenSameValuesInConstructor_thenToStringAreEqual() {

        City city1 = new City("Luzern", 1);
        City city2 = new City("Luzern", 1);


        assertEquals(city1.toString(),city2.toString());
    }

    @Test
    void testEquals_whenNotSameZipCode_thenToStringAreNotEqual() {

        City city1 = new City("Luzern", 2);
        City city2 = new City("Luzern", 1);


        assertNotEquals(city1.toString(),city2.toString());
    }

    @Test
    void testEquals_whenSameValuesStringAndToString_thenToStringAreEqual() {
        int stationId = 1;
        String name = "Luzern";
        int zipCode = 6000;
        double latitude = 47.05048;
        double longitude = 8.30635;

        City city = new City(stationId, name, zipCode, latitude, longitude);
        assertEquals(city.toString(), "\nCity {" + ",\n    name: '" + name + '\'' + ",\n    zipCode: " + zipCode + "\n}");
    }
}

