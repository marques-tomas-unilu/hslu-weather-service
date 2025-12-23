package ch.hslu.swde.wda.reader;

import ch.hslu.swde.wda.domain.City;
import ch.hslu.swde.wda.domain.WeatherData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WeatherDataReaderImpl implements WeatherDataReader {
    private static final Logger logger = LogManager.getLogger(WeatherDataReaderImpl.class);

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    private final String BASE_URI = "http://win-003.swde.ls.eee.intern:8080/weatherdata-provider";
    private final String MEDIA_TYPE = "application/json";

    @Override
    public List<City> getAvailableLocations() {
        logger.info("Getting available locations");
        try {
            URI uri = URI.create(BASE_URI + "/rest/weatherdata/cities");
            HttpRequest httpRequest = HttpRequest.newBuilder(uri).GET().header("Accept", MEDIA_TYPE).build();
            HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (httpResponse.statusCode() == 200) {
                List<City> cityList = new ArrayList<>();
                for (JsonNode jsonNode : mapper.readTree(httpResponse.body())) {
                    cityList.add(convertJSONObjectToCity(jsonNode));
                }
                logger.info("Found " + cityList.size() + " cities");
                return cityList;
            } else {
                logger.error("Failed to fetch available locations. HTTP Status: {}", httpResponse.statusCode());
                throw new IllegalArgumentException("ERROR: " + httpResponse.statusCode());
            }
        } catch (final Exception exception) {
            logger.error("Failed to fetch available locations", exception);
            throw new RuntimeException(exception);
        }
    }

    @Override
    public WeatherData getLatestWeatherData(final City city) {
        logger.info("Getting latest weather data");
        try {
            URI uri = URI.create(BASE_URI + "/rest/weatherdata/?city=" + city.getName());
            HttpRequest httpRequest = HttpRequest.newBuilder(uri).GET().header("Accept", MEDIA_TYPE).build();
            HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (httpResponse.statusCode() == 200) {
                logger.info("Latest weather data found");
                return convertJSONObjectToWeatherData(httpResponse.body());
            } else {
                logger.error("ERROR: " + httpResponse.statusCode());
                throw new IllegalArgumentException("ERROR: " + httpResponse.statusCode());
            }
        } catch (final Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public List<WeatherData> getWeatherDataForYear(final City city, final int year) {
        logger.info("Getting weather data for year {}", year);
        try {
            URI uri = URI.create(BASE_URI + "/rest/weatherdata/cityandyear?city=" + city.getName() + "&year=" + year);
            HttpRequest httpRequest = HttpRequest.newBuilder(uri).GET().header("Accept", MEDIA_TYPE).build();
            HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (httpResponse.statusCode() == 200) {
                logger.info("Weather data found");
                List<WeatherData> weatherDataList = new ArrayList<>();
                for (JsonNode jsonNode : mapper.readTree(httpResponse.body())) {
                    weatherDataList.add(convertJSONObjectToWeatherData(jsonNode.toString()));
                }
                if (!weatherDataList.isEmpty()) {
                    logger.info("Found " + weatherDataList.size() + " weather data");
                    return weatherDataList;
                } else {
                    logger.warn("Es wurden keine Daten für die Stadt '" + city.getName() + "' für das Jahr '" + year + "' gefunden.");
                    throw new NullPointerException("Es wurden keine Daten für die Stadt '" + city.getName() + "' für das Jahr '" + year + "' gefunden.");
                }
            } else {
                logger.error("ERROR: " + httpResponse.statusCode());
                throw new IllegalArgumentException("ERROR: " + httpResponse.statusCode());
            }
        } catch (final Exception exception) {
            logger.error("Failed to fetch weather data for year " + year, exception);
            throw new RuntimeException(exception);
        }
    }


    /* ************************************************************** */
    /* ************************************************************** */


    private City convertJSONObjectToCity(JsonNode response) {
        logger.debug("Converting JSON to City object.");
        String nameCity = response.path("name").asText();
        int zipCode = response.path("zip").asInt();

        return new City(nameCity, zipCode);
    }

    private WeatherData convertJSONObjectToWeatherData(String response) {
        logger.debug("Converting JSON to WeatherData object.");
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode cityJSONObject = objectMapper.readTree(response).get("city");
            JsonNode dataJSONObject = splitStringIntoJSONObject(objectMapper.readTree(response).get("data").toString());

            //System.out.println(dataJSONObject);

            String nameCity = cityJSONObject.path("name").asText();
            int zip = cityJSONObject.path("zip").asInt();

            int stationID = dataJSONObject.path("STATION_ID").asInt();
            double latitude = dataJSONObject.path("LATIUDE").asDouble();
            double longitude = dataJSONObject.path("LONGITUDE").asDouble();

            double pressure = dataJSONObject.path("PRESSURE").asDouble();
            double humidity = dataJSONObject.path("HUMIDITY").asDouble();

            double current_temperature = dataJSONObject.path("CURRENT_TEMPERATURE_CELSIUS").asDouble();
            String weather_summary = dataJSONObject.path("WEATHER_SUMMARY").asText();
            String weather_description = dataJSONObject.path("WEATHER_DESCRIPTION").asText();

            int wind_direction = dataJSONObject.path("WIND_DIRECTION").asInt();
            double wind_speed = dataJSONObject.path("WIND_SPEED").asDouble();

            LocalDateTime lastUpdateTime = LocalDateTime.parse(dataJSONObject.path("LAST_UPDATE_TIME").asText());

            return new WeatherData(stationID, nameCity, zip, latitude, longitude, pressure, humidity, current_temperature, weather_summary, weather_description, wind_direction, wind_speed, lastUpdateTime);
        } catch (Exception exception) {
            logger.error("Error converting JSON to WeatherData object.");
            throw new RuntimeException(exception);
        }
    }

    /* Diese Methode wurde mitHilfe von KI implementiert */
    private JsonNode splitStringIntoJSONObject(String inputString) {
        logger.debug("Converting JSON to JSON object.");
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonObject = objectMapper.createObjectNode();

        for (String entry : inputString.split("#")) {
            String[] keyValue = entry.split(":", 2);
            if (keyValue.length == 2) {
                String key = keyValue[0].trim().replace("\"", "");
                String value = keyValue[1].trim().replace("\"", "");

                if (key.contains("LAST_UPDATE_TIME")) {
                    value = value.replace(" ", "T");
                }
                jsonObject.put(key, value);
            }
        }
        return jsonObject;
    }

    /* ************************************************************** */
    /* ************************************************************** */
}