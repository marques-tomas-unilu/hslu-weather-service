package ch.hslu.swde.wda.business.impl;

import ch.hslu.swde.wda.business.api.EmployeeBusiness;
import ch.hslu.swde.wda.domain.Employee;
import ch.hslu.swde.wda.persister.api.EmployeePersister;
import ch.hslu.swde.wda.persister.impl.EmployeeDaoImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;

public class EmployeeBusinessImpl implements EmployeeBusiness {
    private static final Logger logger = LogManager.getLogger(EmployeeBusinessImpl.class);

    private final EmployeePersister employeePersister = new EmployeeDaoImpl();

    public EmployeePersister getEmployeePersister() {
        return employeePersister;
    }

    public EmployeeBusinessImpl() {}

    @Override
    public Employee fetchUserDataByUsername(String username) {
        logger.info("Fetching user data for username " + username);
        try {
            // Rückgabe des Mitarbeiters mit dem entsprechenden Benutzernamen
            return employeePersister.findEmployeeByUsername(username);
        } catch (Exception e) {
            logger.error("Error fetching user by username: " + e);
            throw new RuntimeException("Error fetching user by username: " + username, e);
        }
    }

    @Override
    public void saveUser(String username, String password, String email) {
        logger.info("Saving user data for username " + username);
        try {
            // Speichern des Mitarbeiters in der Datenbank
            employeePersister.saveEmployee(new Employee(username, email, password));
        } catch (Exception e) {
            logger.error("Error saving user by username: " + e);
            throw new RuntimeException("Error saving user: " + username, e);
        }
    }

    @Override
    public void updateEmployee(String username, String password, String email) {
        logger.info("Updating user data for username " + username);
        try {
            // Aktualisieren des Mitarbeiters
            employeePersister.updateEmployee(new Employee(username,email, password));
        } catch (Exception e) {
            logger.error("Error updating user by username: " + e);
            throw new RuntimeException("Error updating user: " + username, e);
        }
    }

    @Override
    public void deleteEmployee(String username) {
        logger.info("Deleting user data for username " + username);
        try {
            // Löschen des Mitarbeiters aus der Datenbank
            Employee employeeToDelete = employeePersister.findEmployeeByUsername(username);
            employeePersister.deleteEmployee(employeeToDelete);
        } catch (Exception e) {
            logger.error("Error deleting user by username: " + e);
            throw new RuntimeException("Error deleting user: " + username, e);
        }
    }

    @Override
    public Employee findEmployeeByUsername(String username) {
        logger.info("Fetching user data for username " + username);
        try {
            // Rückgabe des Mitarbeiters mit dem entsprechenden Benutzernamen
            return employeePersister.findEmployeeByUsername(username);
        } catch (Exception e) {
            logger.error("Error fetching user by username: " + e);
            throw new RuntimeException("Error finding employee: " + username, e);
        }
    }

    @Override
    public List<Employee> getAllEmployees() {
        logger.info("Fetching all user employees");
        try {
            // Abrufen aller Mitarbeiter aus der Datenbank
            List<Employee> employees = employeePersister.getAllEmployees();
            return employees != null ? employees : List.of(); // Rückgabe einer leeren Liste, falls keine Mitarbeiter vorhanden sind
        } catch (Exception e) {
            logger.error("Error fetching all user employees: " + e);
            throw new RuntimeException("Error getting all employees.", e);
        }
    }
}
