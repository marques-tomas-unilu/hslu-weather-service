package ch.hslu.swde.wda.business.impl;

import ch.hslu.swde.wda.business.api.EmployeeBusiness;
import ch.hslu.swde.wda.domain.Employee;
import ch.hslu.swde.wda.persister.util.JpaUtil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeBusinessIT {

    EmployeeBusiness employeeBusiness = new EmployeeBusinessImpl();
    List<Employee> employeeList = employeeBusiness.getAllEmployees();

    @BeforeEach
    void setUp() {
        System.setProperty("APP_ENV", "test");
        EntityManager em = JpaUtil.createEntityManager();
        em.getTransaction().begin();

        em.createQuery("DELETE FROM Employee e").executeUpdate();

        em.getTransaction().commit();

        em.close();
    }

    @AfterEach
    void tearDown() {
        EntityManager em = JpaUtil.createEntityManager();
        em.getTransaction().begin();

        em.createQuery("DELETE FROM Employee e").executeUpdate();

        em.getTransaction().commit();

        em.close();
        System.setProperty("APP_ENV", "production");
    }

    @Test
    void saveUser_SavedEmployeeEqualsEmployeeInDatabase_IsTrue() {

        Employee employee1 = new Employee();
        employee1.setEmail("test1@test.ch");
        employee1.setUsername("test1");
        employee1.setPassword("password1");
        employeeBusiness.saveUser("test1","password1","test1@test.ch");

        assertEquals(employee1, employeeBusiness.findEmployeeByUsername("test1"));
    }

    @Test
    void updateEmployee_SavedEmployeeEqualsEmployeeInDatabase_IsTrue() {

        employeeBusiness.saveUser("test1","password1","test1@test.ch");


        employeeBusiness.updateEmployee("test1","updatedTest1","updatedTest1@test.ch");
        Employee updatedEmployee1 = employeeBusiness.findEmployeeByUsername("test1");

        assertEquals("updatedTest1@test.ch", updatedEmployee1.getEmail());
        assertEquals("test1", updatedEmployee1.getUsername());
        assertEquals("updatedTest1", updatedEmployee1.getPassword());

    }


    @Test
    void deleteEmployee_SavedEmployeeEqualsEmployeeInDatabase_IsTrue() {

        employeeBusiness.saveUser("test1","password1","test1@test.ch");

        employeeBusiness.deleteEmployee("test1");
        assertEquals(0, employeeBusiness.getAllEmployees().size());
    }


    @Test
    void getAllEmployees_SaveAllEmployeesInDatabase_ShouldReturnSameListOfEmployees() {

        Employee employee1 = new Employee();
        employee1.setEmail("test1@test.ch");
        employee1.setUsername("test1");
        employee1.setPassword("password1");
        employeeBusiness.saveUser("test1","password1","test1@test.ch");

        Employee employee2 = new Employee();
        employee2.setEmail("test2@test.ch");
        employee2.setUsername("test2");
        employee2.setPassword("password2");
        employeeBusiness.saveUser("test2","password2","test2@test.ch");

        Employee employee3 = new Employee();
        employee3.setEmail("test3@test.ch");
        employee3.setUsername("test3");
        employee3.setPassword("password3");
        employeeBusiness.saveUser("test3","password3","test3@test.ch");

        List<Employee> allEmployees = employeeBusiness.getAllEmployees();

        assertEquals(3, allEmployees.size());
        assertTrue(allEmployees.contains(employee1));
        assertTrue(allEmployees.contains(employee2));
        assertTrue(allEmployees.contains(employee3));
    }

    @Test
    void fetchUserDataByUsername_SavedEmployeeEqualsEmployeeInDatabase_IsTrue() {

        Employee employee1 = new Employee();
        employee1.setEmail("test1@test.ch");
        employee1.setUsername("test1");
        employee1.setPassword("password1");
        employeeBusiness.saveUser("test1","password1","test1@test.ch");

        Employee fetchedEmployee1 = employeeBusiness.fetchUserDataByUsername("test1");
        assertEquals(employee1, fetchedEmployee1);
    }

    @Test
    void findEmployeeByUsername_SavedEmployeeEqualsEmployeeInDatabase_IsTrue() {

        Employee employee1 = new Employee();
        employee1.setEmail("test1@test.ch");
        employee1.setUsername("test1");
        employee1.setPassword("password1");
        employeeBusiness.saveUser("test1","password1","test1@test.ch");

        Employee findEmployee1 = employeeBusiness.findEmployeeByUsername("test1");
        assertEquals(employee1, findEmployee1);
     }
}