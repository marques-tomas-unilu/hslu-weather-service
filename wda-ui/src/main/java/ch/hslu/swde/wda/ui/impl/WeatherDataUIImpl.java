package ch.hslu.swde.wda.ui.impl;

import ch.hslu.swde.wda.domain.City;
import ch.hslu.swde.wda.domain.Employee;
import ch.hslu.swde.wda.ui.api.WeatherDataUI;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WeatherDataUIImpl implements WeatherDataUI {

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    private final String BASE_URI = "http://localhost:8080/";
    private final String MEDIA_TYPE = "application/json";
    private Employee employee;

    /* ********************************************** */

    @Override
    public void showAvailableCities() {
        try {
            URI uri = URI.create(BASE_URI + "weather/locations");
            HttpRequest httpRequest = HttpRequest.newBuilder(uri).GET().header("Accept", MEDIA_TYPE).build();
            HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (httpResponse.statusCode() == 200) {
                JsonNode cities = mapper.readTree(httpResponse.body());
                System.out.println("------------------------------------------------------------");
                for (JsonNode city : cities) {
                    City cityObj = new City(city.get("name").asText(), city.get("zipCode").asInt());
                    System.out.println(cityObj);
                }
                System.out.println("------------------------------------------------------------");
            } else {
                throw new IllegalArgumentException("ERROR: " + httpResponse.statusCode());
            }
        } catch (final Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void showWeatherDataForCityForSpecificTimePeriod(String city, String startDate, String endDate) {
        try {
            URI uri = URI.create(BASE_URI + "weather/data?zipCode=" + convertCityNameToZipCode(city) + "&startDate=" + startDate + "&endDate=" + endDate);
            HttpRequest httpRequest = HttpRequest.newBuilder(uri).GET().header("Accept", MEDIA_TYPE).build();
            HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (httpResponse.statusCode() == 200) {
                JsonNode weatherDataArray = mapper.readTree(httpResponse.body());

                if (weatherDataArray.isArray()) {
                    for (JsonNode weatherData : weatherDataArray) {
                        JsonNode cityNode = weatherData.get("city");
                        String cityName = cityNode.get("name").asText();
                        int zipCode = cityNode.get("zipCode").asInt();

                        int temperature = weatherData.get("currentTemperature").asInt();
                        String weatherSummary = weatherData.get("weatherSummary").asText();
                        String weatherDescription = weatherData.get("weatherDescription").asText();
                        int pressure = weatherData.get("pressure").asInt();
                        int humidity = weatherData.get("humidity").asInt();
                        double windSpeed = weatherData.get("windSpeed").asDouble();
                        int windDirection = weatherData.get("windDirection").asInt();

                        JsonNode lastUpdateTime = weatherData.get("lastUpdateTime");

                        System.out.println("------------------------------------------------------------");
                        System.out.println("Stadt: " + cityName + " (ZipCode: " + zipCode + ")");
                        System.out.println("Temperatur: " + temperature + "°C");
                        System.out.println("Wetter: " + weatherSummary + " (" + weatherDescription + ")");
                        System.out.println("Luftdruck: " + pressure + " hPa");
                        System.out.println("Luftfeuchtigkeit: " + humidity + "%");
                        System.out.println("Wind: " + windSpeed + " m/s, Richtung: " + windDirection + "°");
                        System.out.println("Letzte Aktualisierung: " + lastUpdateTime);
                        System.out.println("------------------------------------------------------------");
                    }
                } else {
                    System.out.println("Es wurden keine Wetterdaten für den angegebenen Zeitraum gefunden.");
                }
            } else {
                throw new IllegalArgumentException("ERROR: " + httpResponse.statusCode());
            }
        } catch (final Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void showAverageWeatherDataForCityForSpecificTimePeriod(String city, String startDate, String endDate) {
        try {
            URI uri = URI.create(BASE_URI + "weather/average?zipCode=" + convertCityNameToZipCode(city) + "&startDate=" + startDate + "&endDate=" + endDate);
            HttpRequest httpRequest = HttpRequest.newBuilder(uri).GET().header("Accept", MEDIA_TYPE).build();
            HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (httpResponse.statusCode() == 200) {
                JsonNode weatherData = mapper.readTree(httpResponse.body());

                if (weatherData.isArray() && weatherData.size() == 1) {
                    String[] values = weatherData.get(0).asText().replace("[", "").replace("]", "").split(",");
                    System.out.println("\n");
                    System.out.println("------------------------------------------------------------");
                    System.out.println("Durchschnittswetterdaten für " + city + " von " + startDate + " bis " + endDate + ":");
                    System.out.println("Temperatur: " + values[0] + " °C");
                    System.out.println("Luftdruck: " + values[1] + " hPa");
                    System.out.println("Luftfeuchtigkeit: " + values[2] + " %");
                    System.out.println("------------------------------------------------------------");
                } else {
                    System.out.println("Es wurden keine Wetterdaten für den angegebenen Zeitraum gefunden.");
                }
            } else {
                throw new IllegalArgumentException("ERROR: " + httpResponse.statusCode());
            }
        } catch (final Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void showMinMaxWeatherDataForCityForSpecificTimePeriod(String city, String startDate, String endDate) {
        try {
            URI uri = URI.create(BASE_URI + "weather/minmax?zipCode=" + convertCityNameToZipCode(city) + "&startDate=" + startDate + "&endDate=" + endDate);
            HttpRequest httpRequest = HttpRequest.newBuilder(uri).GET().header("Accept", MEDIA_TYPE).build();
            HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (httpResponse.statusCode() == 200) {
                JsonNode weatherData = mapper.readTree(httpResponse.body());

                if (weatherData.isArray() && weatherData.size() == 1) {
                    String[] values = weatherData.get(0).asText().replace("[", "").replace("]", "").split(",");
                    System.out.println("------------------------------------------------------------");
                    System.out.println("Wetter Daten für " + city + " vom " + startDate + " zum " + endDate + ":");
                    System.out.println("Min Temperatur: " + values[0] + " °C");
                    System.out.println("Max Temperatur: " + values[1] + " °C");
                    System.out.println("Min Luftdruck: " + values[2] + " hPa");
                    System.out.println("Max Luftdruck: " + values[3] + " hPa");
                    System.out.println("Min Luftfeuchtigkeit: " + values[4] + " %");
                    System.out.println("Max Luftfeuchtigkeit: " + values[5] + " %");
                    System.out.println("------------------------------------------------------------");
                } else {
                    System.out.println("Es wurden keine Wetterdaten für den angegebenen Zeitraum gefunden.");
                }
            } else {
                throw new IllegalArgumentException("ERROR: " + httpResponse.statusCode());
            }
        } catch (final Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void showCitiesWithExtremeWeatherValuesForSpecificTimePeriod(String startDate, String endDate) {
        try {
            URI uri = URI.create(BASE_URI + "weather/extreme?startDate=" + startDate + "&endDate=" + endDate);
            HttpRequest httpRequest = HttpRequest.newBuilder(uri).GET().header("Accept", MEDIA_TYPE).build();
            HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (httpResponse.statusCode() == 200) {
                JsonNode cities = mapper.readTree(httpResponse.body());
                if (cities.isArray()) {
                    System.out.println("------------------------------------------------------------");
                    for (JsonNode city : cities) {
                        String cityName = convertZipCodeToCityName(city.asInt());
                        System.out.println(cityName);
                    }
                    System.out.println("------------------------------------------------------------");
                } else {
                    System.out.println("Es wurden keine Städte gefunden.");
                }
            } else {
                throw new IllegalArgumentException("ERROR: " + httpResponse.statusCode());
            }
        } catch (final Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void exportWeatherDataForCityForSpecificTimePeriod(String city, String startDate, String endDate) {
        try {
            URI uri = URI.create(BASE_URI + "weather/data?zipCode=" + convertCityNameToZipCode(city) + "&startDate=" + startDate + "&endDate=" + endDate);
            HttpRequest httpRequest = HttpRequest.newBuilder(uri).GET().header("Accept", MEDIA_TYPE).build();
            HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (httpResponse.statusCode() == 200) {
                JsonNode weatherDataArray = mapper.readTree(httpResponse.body());

                if (weatherDataArray.isArray()) {
                    File file = new File("output.json");
                    if (file.exists()) {
                        new FileWriter(file).close();
                    }

                    ArrayNode arrayNode = mapper.createArrayNode();
                    for (JsonNode weatherData : weatherDataArray) {
                        arrayNode.add(weatherData);
                    }

                    mapper.writerWithDefaultPrettyPrinter().writeValue(file, arrayNode);

                    System.out.println("\n- Alle abgefragten Daten wurden in folgendem Pfad abgespeichert: " + file.getAbsolutePath());
                }
            } else {
                throw new IllegalArgumentException("ERROR: " + httpResponse.statusCode());
            }
        } catch (final Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void synchronizeData() {
        try {
            URI uri = URI.create(BASE_URI + "weather/sync");
            HttpRequest httpRequest = HttpRequest.newBuilder(uri).GET().header("Accept", MEDIA_TYPE).build();
            HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (httpResponse.statusCode() == 200) {
                System.out.println("\n- Alle Daten wurden erfolgreich synchronisiert.");
            } else {
                throw new IllegalArgumentException("ERROR: " + httpResponse.statusCode());
            }
        } catch (final Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public boolean fetchUserDataByUsername(String username,String password) {
        try {
            URI uri = URI.create(BASE_URI + "weather/user/fetch?username=" + username);
            HttpRequest httpRequest = HttpRequest.newBuilder(uri).GET().header("Accept", MEDIA_TYPE).build();
            HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (httpResponse.statusCode() == 200) {
                ObjectMapper objectMapper = new ObjectMapper();
                employee = objectMapper.readValue(httpResponse.body(), Employee.class);
                return employee.getUsername().equals(username) & employee.getPassword().equals(password);
            }
            return false;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveUser(String username, String email, String password) {
        try {
            URI uri = URI.create(BASE_URI + "weather/user/save?username=" + username + "&password=" + password + "&email="  + email );
            HttpRequest httpRequest = HttpRequest.newBuilder(uri).GET().header("Accept", MEDIA_TYPE).build();
            HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (httpResponse.statusCode() == 200 || httpResponse.statusCode() == 201) {
                System.out.println("\n- User gespeichert in der Datenbank");
            } else {
                throw new IllegalArgumentException("ERROR: " + httpResponse.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void changePassword(String newPassword) {
        try {
            URI uri = URI.create(BASE_URI + "weather/user/update?username=" + employee.getUsername() + "&password=" + newPassword + "&email="  + employee.getEmail() );
            HttpRequest httpRequest = HttpRequest.newBuilder(uri).GET().header("Accept", MEDIA_TYPE).build();
            HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (httpResponse.statusCode() == 200 || httpResponse.statusCode() == 201) {
                System.out.println("\n- Passwort gewechselt");
            } else {
                throw new IllegalArgumentException("ERROR: " + httpResponse.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void changeEmail(String newEmail) {
        try {
            URI uri = URI.create(BASE_URI + "weather/user/update?username=" + employee.getUsername() + "&password=" + employee.getPassword() + "&email="  + newEmail );
            HttpRequest httpRequest = HttpRequest.newBuilder(uri).GET().header("Accept", MEDIA_TYPE).build();
            HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (httpResponse.statusCode() == 200 || httpResponse.statusCode() == 201) {
                System.out.println("\n- Email gewechselt");
            } else {
                throw new IllegalArgumentException("ERROR: " + httpResponse.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteUser() {
        try {
            URI uri = URI.create(BASE_URI + "weather/user/delete?username=" + employee.getUsername());
            HttpRequest httpRequest = HttpRequest.newBuilder(uri).GET().header("Accept", MEDIA_TYPE).build();
            HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (httpResponse.statusCode() == 200 || httpResponse.statusCode() == 201) {
                System.out.println("\n- User gelöst");
            } else {
                throw new IllegalArgumentException("ERROR: " + httpResponse.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /* ********************************************** */

    public int convertCityNameToZipCode(String cityName) {
        try {
            URI uri = URI.create(BASE_URI + "weather/locations");
            HttpRequest httpRequest = HttpRequest.newBuilder(uri).GET().build();
            HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (httpResponse.statusCode() == 200) {
                JsonNode cities = mapper.readTree(httpResponse.body());

                for (JsonNode city : cities) {
                    if (cityName.equalsIgnoreCase(city.get("name").asText())) {
                        return city.get("zipCode").asInt();
                    }
                }
                throw new IllegalArgumentException(cityName + " wurde nicht gefunden.");
            } else {
                throw new IllegalArgumentException("ERROR: " + httpResponse.statusCode());
            }
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public String convertZipCodeToCityName(int zipCode) {
        try {
            URI uri = URI.create(BASE_URI + "weather/locations");
            HttpRequest httpRequest = HttpRequest.newBuilder(uri).GET().build();
            HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (httpResponse.statusCode() == 200) {
                JsonNode cities = mapper.readTree(httpResponse.body());

                for (JsonNode city : cities) {
                    if (zipCode == city.get("zipCode").asInt()) {
                        return city.get("name").asText();
                    }
                }
                throw new IllegalArgumentException("PLZ: " + zipCode + " wurde nicht gefunden.");
            } else {
                throw new IllegalArgumentException("ERROR: " + httpResponse.statusCode());
            }
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
