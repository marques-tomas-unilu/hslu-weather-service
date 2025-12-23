package ch.hslu.swde.wda.rws.srv;

import ch.hslu.swde.wda.rws.resources.WeatherResource;
import com.sun.net.httpserver.HttpServer;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

public class ServerApp {

    private static final String BASE_URI = "http://localhost:8080/";

    public static void main(final String[] args) {

        URI uri = URI.create(BASE_URI);

        ResourceConfig config = new ResourceConfig(WeatherResource.class);
        //Folgender Codeausschnitt aus Unterrichtsmaterialien
        HttpServer server = startHttpServer();
        System.out.println("Server is running at " + BASE_URI);
        System.out.println("Press ENTER to exit.");

        try {
            System.in.read();
        } catch (IOException e) {

        }

        server.stop(1);
        System.out.println("\nServer is stopped\n");

    }

    private static HttpServer startHttpServer() {
        ResourceConfig config = new ResourceConfig(WeatherResource.class);
        return JdkHttpServerFactory.createHttpServer(URI.create(BASE_URI), config);
    }
}
