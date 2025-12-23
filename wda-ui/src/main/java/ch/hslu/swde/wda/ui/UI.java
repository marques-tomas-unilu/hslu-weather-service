package ch.hslu.swde.wda.ui;

import ch.hslu.swde.wda.ui.impl.WeatherDataUIImpl;

import java.util.Scanner;

public class UI {

    private static final WeatherDataUIImpl weatherDataUI = new WeatherDataUIImpl();
    private static boolean uiSession;

    public static void main(String[] args) throws InterruptedException {
        while (true) {
            uiSession=true;
            Scanner scanner = new Scanner(System.in);
            String username;
            String password = null;
    //         Login-Funktion
            do {
                if (password != null) {
                    System.out.println("Falscher Benutzername oder Passwort. Melden Sie sich wieder an.");
                }
                System.out.println("Bitte melden Sie sich an.");

                System.out.print("Benutzername: ");
                username = scanner.nextLine().trim();

                System.out.print("Passwort: ");
                password = scanner.nextLine().trim();

            }while (!weatherDataUI.fetchUserDataByUsername(username,password));


            System.out.println("Anmeldung erfolgreich!");

            String greeting = "Willkommen in der Wetterdaten Applikation!";
            System.out.println("\n");

            // Animation des Begrüßungstextes
            for (int i = 0; i < greeting.length(); i++) {
                System.out.print(greeting.charAt(i));
                Thread.sleep(90);
            }

            System.out.println("\n");

            while (uiSession) {
                System.out.println("\nWetterdaten Abfragen:");
                System.out.println("1. A01 - Verfügbare Ortschaften");
                System.out.println("2. A02 - Konkrete Wetterdaten für eine Ortschaft");
                System.out.println("3. A03 - Durchschnittswetterdaten für eine Ortschaft");
                System.out.println("4. A04 - Maximal- und Minimalwerte für eine Ortschaft");
                System.out.println("5. A05 - Orte mit den höchsten/tiefsten Werten für Temperatur, Luftdruck oder Feuchtigkeit zu einem bestimmten Zeitpunkt");
                System.out.println("6. A06 - Verwaltung von Benutzerdaten (CRUD) & Login");
                System.out.println("7. A07 - Export von Wetterdaten");
                System.out.println("8. A08 - Aktualisierung von Wetterdaten");
                System.out.println("9. Ausloggen");
                System.out.println("10. Software beenden");

                System.out.print("\nBitte wählen Sie eine Option (1-10): ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        // A01: Verfügbare Ortschaften
                        showAvailableLocations();
                        break;
                    case 2:
                        // A02: Konkrete Wetterdaten
                        showConcreteWeatherData(scanner);
                        break;
                    case 3:
                        // A03: Durchschnittswerte
                        showAverageWeatherData(scanner);
                        break;
                    case 4:
                        // A04: Maximal- und Minimalwerte
                        showMaxMinWeatherData(scanner);
                        break;
                    case 5:
                        // A05: Höchste und tiefste Werte für Temperatur, Luftdruck, Feuchtigkeit
                        showExtremeWeatherValues(scanner);
                        break;
                    case 6:
                        // A06: Verwaltung von Benutzerdaten (CRUD) & Login
                        showCRUDforEmployee(scanner);
                        break;
                    case 7:
                        // A07: Export von Wetterdaten
                        exportWeatherData(scanner);
                        break;
                    case 8:
                        // A08: Aktualisierung von Wetterdaten
                        synchronizeData(scanner);
                        break;
                    case 9:
                        // Ausloggen
                        uiSession=false;
                        System.out.println("Du wurdest ausgeloggt!");
                        break;
                    case 10:
                        // Software beenden
                        System.out.println("Programm wird beendet.");
                        scanner.close();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Ungültige Auswahl, bitte wählen Sie eine Zahl zwischen 1 und 9.");
                }
            }
        }

    }

    /* ********************************************** */

    // A01: Verfügbare Ortschaften
    private static void showAvailableLocations() {
        System.out.println("\nVerfügbare Ortschaften:");
        weatherDataUI.showAvailableCities();
    }

    // A02: Konkrete Wetterdaten für eine Ortschaft und Zeitraum
    private static void showConcreteWeatherData(Scanner scanner) {
        scanner.nextLine();
        System.out.print("Ortschaft: ");
        String city = scanner.nextLine();

        System.out.print("Startzeitpunkt (im Format YYYY-MM-DD'T'HH:MM:SS): ");
        String startTime = scanner.nextLine();

        System.out.print("Endzeitpunkt (im Format YYYY-MM-DD'T'HH:MM:SS): ");
        String endTime = scanner.nextLine();

        weatherDataUI.showWeatherDataForCityForSpecificTimePeriod(city, startTime, endTime);
    }

    // A03: Durchschnittswerte für eine Ortschaft und Zeitraum
    private static void showAverageWeatherData(Scanner scanner) {
        scanner.nextLine();
        System.out.print("Ortschaft: ");
        String city = scanner.nextLine();

        System.out.print("Startzeitpunkt (im Format YYYY-MM-DD'T'HH:MM:SS): ");
        String startTime = scanner.nextLine();

        System.out.print("Endzeitpunkt (im Format YYYY-MM-DD'T'HH:MM:SS): ");
        String endTime = scanner.nextLine();

        weatherDataUI.showAverageWeatherDataForCityForSpecificTimePeriod(city, startTime, endTime);
    }

    // A04: Maximal- und Minimalwerte für eine Ortschaft und Zeitraum
    private static void showMaxMinWeatherData(Scanner scanner) {
        scanner.nextLine();
        System.out.print("Ortschaft: ");
        String city = scanner.nextLine();

        System.out.print("Startzeitpunkt (im Format YYYY-MM-DD'T'HH:MM:SS): ");
        String startTime = scanner.nextLine();

        System.out.print("Endzeitpunkt (im Format YYYY-MM-DD'T'HH:MM:SS): ");
        String endTime = scanner.nextLine();

        weatherDataUI.showMinMaxWeatherDataForCityForSpecificTimePeriod(city, startTime, endTime);
    }

    // A05: Ortschaft wo Höchste und tiefste Werte für Temperatur, Luftdruck und Feuchtigkeit
    private static void showExtremeWeatherValues(Scanner scanner) {
        scanner.nextLine();
        System.out.print("Startzeitpunkt (im Format YYYY-MM-DD'T'HH:MM:SS): ");
        String startTime = scanner.nextLine();

        System.out.print("Endzeitpunkt (im Format YYYY-MM-DD'T'HH:MM:SS): ");
        String endTime = scanner.nextLine();

        weatherDataUI.showCitiesWithExtremeWeatherValuesForSpecificTimePeriod(startTime, endTime);
    }

    // A06: Verwaltung von Benutzerdaten (CRUD) & Login
    private static void showCRUDforEmployee(Scanner scanner) {
        scanner.nextLine();
        boolean crudSession = true;
        while(crudSession){
            System.out.println("\nUser Verwaltung:");
            System.out.println("1. User erstellen");
            System.out.println("2. Passwort wechseln");
            System.out.println("3. Email wechseln");
            System.out.println("4. User löschen");
            System.out.println("5. Zurück zum Hauptmenü");
            System.out.print("\nBitte wählen Sie eine Option (1-5): ");
            int choice1 = scanner.nextInt();

            switch (choice1) {
                case 1:
                    // 1. User erstellen
                    scanner.nextLine();
                    System.out.println("Neuer User Daten:");
                    System.out.print("Username: ");
                    String username = scanner.nextLine().trim();

                    System.out.print("Email: ");
                    String email = scanner.nextLine().trim();

                    System.out.print("Passwort: ");
                    String password = scanner.nextLine().trim();
                    weatherDataUI.saveUser(username,email,password);
                    break;
                case 2:
                    // 2. Passwort wechseln
                    scanner.nextLine();
                    System.out.print("Neues Passwort: ");
                    String newPassword = scanner.nextLine().trim();
                    weatherDataUI.changePassword(newPassword);
                    break;
                case 3:
                    // 3. Email wechseln
                    scanner.nextLine();
                    System.out.print("Neue Email: ");
                    String newEmail = scanner.nextLine().trim();
                    weatherDataUI.changeEmail(newEmail);
                    break;
                case 4:
                    // 4. User löschen
                    scanner.nextLine();
                    System.out.println("Möchtest du sicher deinen Benutzer löschen? (ja/nein)");
                    String deleteUser = scanner.nextLine().trim();
                    if (deleteUser.equals("ja")) {
                        weatherDataUI.deleteUser();
                        crudSession=false;
                        uiSession=false;
                    }

                    break;
                case 5:
                    crudSession = false;
                    break;
                default:
                    System.out.println("Ungültige Auswahl, bitte wählen Sie eine Zahl zwischen 1 und 5.");
            }
        }
    }

    // A07: Export von Wetterdaten
    private static void exportWeatherData(Scanner scanner) {
        scanner.nextLine();
        System.out.print("Ortschaft: ");
        String city = scanner.nextLine();

        System.out.print("Startzeitpunkt (im Format YYYY-MM-DD'T'HH:MM:SS): ");
        String startTime = scanner.nextLine();

        System.out.print("Endzeitpunkt (im Format YYYY-MM-DD'T'HH:MM:SS): ");
        String endTime = scanner.nextLine();

        weatherDataUI.exportWeatherDataForCityForSpecificTimePeriod(city, startTime, endTime);
    }

    // A08: Aktualisierung von Wetterdaten
    private static void synchronizeData(Scanner scanner) {
        weatherDataUI.synchronizeData();
    }


}