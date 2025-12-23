package ch.hslu.swde.wda.persister;

import ch.hslu.swde.wda.domain.Employee;
import ch.hslu.swde.wda.persister.impl.EmployeeDaoImpl;
import ch.hslu.swde.wda.persister.util.JpaUtil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EmployeeDaoImplTest {

    private final EmployeeDaoImpl dao = new EmployeeDaoImpl();

    /* Alle SQL Queries wurden mitHilfe von KI implementiert und ausgebessert */

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
    void testEquals_whenSendEntityToDB_thenDBAndReceiveDataAreEqual() throws Exception {
        Employee employee = new Employee("testUser","testUser@test.ch","dummy");

        dao.saveEmployee(employee);
        Employee employee1 = dao.findEmployeeByUsername("testUser");

        assertEquals("testUser",employee1.getUsername());
    }

    @Test
    void testEquals_whenUpdateDBEntity_thenDBandUpdatedEntityAreEqual() throws Exception {
        Employee employee = new Employee("testUser","testUser@test.ch","dummy");

        dao.saveEmployee(employee);
        employee.setEmail("testUser1@test.ch");
        dao.updateEmployee(employee);
        Employee employee1 = dao.findEmployeeByUsername("testUser");
        assertEquals(employee.toString(),employee1.toString());
    }

    @Test
    void testEquals_whenDeleteDBEntity_thenThrowsIllegalArgumentException() throws Exception {
        Employee employee = new Employee("testUser","testUser@test.ch","dummy");

        dao.saveEmployee(employee);
        dao.deleteEmployee(employee);
        try {
            Employee employee1 = dao.findEmployeeByUsername(employee.getUsername());
        }catch (IllegalArgumentException e){
            assertEquals("Employee with username " + employee.getUsername() + " does not exist.",e.getMessage());
        }
    }
    @Test
    void testGetAllEmployee() throws Exception {
        Employee employee1 = new Employee("testUser","testUser@test.ch","dummy");

        Employee employee2 = new Employee("testUser1","testUser1@test.ch","dummy");

        Employee employee3 = new Employee("testUser2","testUser2@test.ch","dummy");

        dao.saveEmployee(employee1);
        dao.saveEmployee(employee2);
        dao.saveEmployee(employee3);

        List<Employee> employees = dao.getAllEmployees();

        assertNotNull(employees);
        assertEquals(3, employees.size());
        assertTrue(employees.stream().anyMatch(employee -> employee.getUsername().equals("testUser")));
        assertTrue(employees.stream().anyMatch(employee -> employee.getUsername().equals("testUser1")));
    }
}
