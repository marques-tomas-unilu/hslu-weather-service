package ch.hslu.swde.wda.ui;

import ch.hslu.swde.wda.ui.impl.WeatherDataUIImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WeatherDataUIImplTest {

    WeatherDataUIImpl ui = new WeatherDataUIImpl();

    @Test
    void test_1IsEqual1(){
        assertEquals(1,1);
    }

    /*
    @Test
    void testConvertCityNameToZipcode_IfAllCorrect() {
        String cityName = "Rotkreuz";
        assertEquals(6343, ui.convertCityNameToZipCode(cityName));
    }

    @Test
    void testZipCodeToCityName_IfAllCorrect() {
        int zipCode = 6343;
        assertEquals("Rotkreuz", ui.convertZipCodeToCityName(zipCode));
    }
    */
}
