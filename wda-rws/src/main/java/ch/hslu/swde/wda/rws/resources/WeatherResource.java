package ch.hslu.swde.wda.rws.resources;

import java.util.List;

import ch.hslu.swde.wda.business.impl.EmployeeBusinessImpl;
import ch.hslu.swde.wda.business.impl.WeatherDataBusinessImpl;
import ch.hslu.swde.wda.domain.City;
import ch.hslu.swde.wda.domain.Employee;
import ch.hslu.swde.wda.domain.WeatherData;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import jakarta.ws.rs.core.MediaType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;

@Path("/weather")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WeatherResource {
    private static final Logger logger = LogManager.getLogger(WeatherResource.class);


    private final WeatherDataBusinessImpl weatherDataBusiness = new WeatherDataBusinessImpl();
    private final EmployeeBusinessImpl employeeBusiness = new EmployeeBusinessImpl();


    @GET
    @Path("/locations")
    public Response fetchAvailableLocations() {
        logger.info("Fetching available locations");
        List<City> cities = weatherDataBusiness.fetchAvailableCities();
        if(cities.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        logger.info("Fetched available locations: {}", Response.ok(cities));
            return Response.ok(cities).build();
    }

    @GET
    @Path("/data")
    public Response getWeatherDataForPeriod(
            @QueryParam("zipCode") int zipCode,
            @QueryParam("startDate") String startDate,
            @QueryParam("endDate") String endDate) {
        logger.info("Fetching weather data for zipCode: {}, startDate: {}, endDate: {}", zipCode, startDate, endDate);
        List<WeatherData> weatherData = weatherDataBusiness.getWeatherDataForPeriod(zipCode, LocalDateTime.parse(startDate), LocalDateTime.parse(endDate));
        logger.info("Fetched weather data for zipCode: {}, startDate: {}", zipCode, startDate);
        return Response.ok(weatherData).build();
    }

    @GET
    @Path("/average")
    public Response calculateAverageWeatherData(
            @QueryParam("zipCode") int zipCode,
            @QueryParam("startDate") String startDate,
            @QueryParam("endDate") String endDate) {
        logger.info("Calculating average weather data for zipCode: {}, startDate: {}, endDate: {}", zipCode, startDate, endDate);
        List<String> averageData = weatherDataBusiness.calculateAverageWeatherData(zipCode, LocalDateTime.parse(startDate), LocalDateTime.parse(endDate));
        logger.info("Calculated average weather data: {}", averageData);
        return Response.ok(averageData).build();
    }

    @GET
    @Path("/minmax")
    public Response calculateMinMaxWeatherData(
            @QueryParam("zipCode") int zipCode,
            @QueryParam("startDate") String startDate,
            @QueryParam("endDate") String endDate) {
        logger.info("Calculate min max data for zipcode: {}, startDate: {}, endDate: {}", zipCode, startDate, endDate);
        List<String> minMaxData = weatherDataBusiness.calculateMinMaxWeatherData(zipCode, LocalDateTime.parse(startDate), LocalDateTime.parse(endDate));
        logger.info("Calculated min/max data: {}", minMaxData);
        return Response.ok(minMaxData).build();
    }

    @GET
    @Path("/extreme")
    public Response fetchExtremeWeatherData(
            @QueryParam("startDate") String startDate,
            @QueryParam("endDate") String endDate) {
    logger.info("Fetch extreme weather data for startDate: {}, endDate: {}", startDate, endDate);
        List<Integer> cities = weatherDataBusiness.fetchExtremeWeatherData(LocalDateTime.parse(startDate), LocalDateTime.parse(endDate));
        logger.info("Fetched extreme weather data: {}", cities);
        return Response.ok(cities).build();
    }

    @GET
    @Path("/user/fetch")
    public Response fetchUserDataByUsername(@QueryParam("username") String username) {
        logger.info("Fetching user data for username: {}", username);
        try {
            Employee employee = employeeBusiness.fetchUserDataByUsername(username);
            if (employee == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
            }
            return Response.ok(employee).build();
        } catch (Exception e) {
            logger.error("Error: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/user/save")
    public Response saveUser(
            @QueryParam("username") String username,
            @QueryParam("password") String password,
            @QueryParam("email") String email) {
        logger.info("Saving user data for username: {}", username);
        try {
            employeeBusiness.saveUser(username, password, email);
            return Response.status(Response.Status.CREATED).entity("User saved successfully").build();
        } catch (Exception e) {
            logger.error("Error: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error: " + e.getMessage()).build();
        }
    }
    @GET
    @Path("/user/delete")
    public Response deleteUser(
            @QueryParam("username") String username) {
        logger.info("Deleting user data for username: {}", username);
        try {
            employeeBusiness.deleteEmployee(username);
            return Response.status(Response.Status.CREATED).entity("User deleted successfully").build();
        } catch (Exception e) {
            logger.error("Error: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error: " + e.getMessage()).build();
        }
    }
    @GET
    @Path("/user/update")
    public Response updateUser(
            @QueryParam("username") String username,
            @QueryParam("password") String password,
            @QueryParam("email") String email) {
        logger.info("Updating user data for username: {}", username);
        try {
            employeeBusiness.updateEmployee(username, password, email);
            return Response.status(Response.Status.CREATED).entity("User saved successfully").build();
        } catch (Exception e) {
            logger.error("Error: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/sync")
    public Response synchronizeData() {
        logger.info("Synchronizing weather data");
        weatherDataBusiness.updateWeatherData();
        logger.info("Synchronized weather data");
        return Response.ok().build();
    }

}
