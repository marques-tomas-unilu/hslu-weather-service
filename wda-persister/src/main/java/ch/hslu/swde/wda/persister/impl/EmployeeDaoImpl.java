package ch.hslu.swde.wda.persister.impl;

import ch.hslu.swde.wda.domain.Employee;
import ch.hslu.swde.wda.persister.api.EmployeePersister;
import ch.hslu.swde.wda.persister.util.JpaCrud;
import ch.hslu.swde.wda.persister.util.JpaUtil;
import jakarta.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.List;

public class EmployeeDaoImpl implements EmployeePersister {

    final private JpaCrud<Employee> crud = new JpaCrud<>(Employee.class);
    private static final Logger logger = LogManager.getLogger(EmployeeDaoImpl.class);

    @Override
    public Employee add(Employee employee) {
        logger.info("Adding employee " + employee);
        try{
            Employee newEmployee = crud.add(employee);
            logger.info("New employee " + newEmployee);
            return crud.add(employee);
        }catch(Exception e){
            logger.error("Error adding employee: ", employee, e);
            throw e;
        }

    }

    @Override
    public Employee delete(int id) {
        logger.info("Deleting employee " + id);
        try{
            Employee employee = crud.delete(id);
            logger.info("Deleted employee " + employee);
            return crud.delete(id);
        }catch(Exception e){
            logger.error("Error deleting employee: ", e);
            throw e;
        }

    }

    @Override
    public Employee update(Employee employee) {
        logger.info("Updating employee " + employee);
        try{
            Employee updatedEmployee = crud.update(employee);
            logger.info("Updated employee " + updatedEmployee);
            return crud.update(updatedEmployee);
        }catch(Exception e){
            logger.error("Error updating employee: ", e);
            throw e;
        }

    }

    @Override
    public Employee find(int id) {
        logger.info("Finding employee " + id);
        try{
            Employee employee = crud.find(id);
            logger.info("Found employee " + employee);
            return employee;
        }catch(Exception e){
            logger.error("Error finding employee: ", e);
            throw e;
        }

    }

    @Override
    public List<Employee> all() {
        logger.info("Finding all employees");
        try{
            List<Employee> employees = crud.all();
            logger.info("Found " + employees.size() + " employees");
            return employees;
        }catch(Exception e){
            logger.error("Error finding all employees: ", e);
            throw e;
        }
    }

    @Override
    public void saveEmployee(Employee employee) throws Exception {
        logger.info("Saving employee " + employee);
        EntityManager em = JpaUtil.createEntityManager();
        if (em.find(Employee.class,employee.getUsername()) == null) {
            try {
                em.getTransaction().begin();
                em.persist(employee);
                em.getTransaction().commit();
                logger.info("Saved employee " + employee);
            } catch (Exception e) {
                logger.error("Error saving employee: ", e);
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                throw new Exception("Fehler beim Speichern der User: " + employee.getUsername());
            }finally {
                em.close();
            }
        }else {
            System.out.println(employee.getUsername() + " is allready saved");
            em.close();
        }

    }

    @Override
    public void updateEmployee(Employee employee) throws Exception {
        logger.info("Updating employee " + employee);
        EntityManager em = JpaUtil.createEntityManager();
        try {
            em.getTransaction().begin();
            Employee existingEmployee = em.find(Employee.class, employee.getUsername());

            if (existingEmployee == null) {
                logger.warn("Employee with Username: " + employee.getUsername() + " does not exist.");
                throw new IllegalArgumentException("Employee with Username: " + employee.getUsername() + " does not exist.");
            }
            em.merge(employee);
            em.getTransaction().commit();
            logger.info("Updated employee " + employee);
        } catch (Exception e) {
            logger.error("Error updating employee: ", e);
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new Exception("Update failed for Employee: " + employee.getUsername());
        }finally {
            em.close();
        }
    }

    @Override
    public void deleteEmployee(Employee employee) throws Exception {
        logger.info("Deleting employee " + employee);
        EntityManager em = JpaUtil.createEntityManager();
        try {
            em.getTransaction().begin();

            Employee existingEmployee = em.find(Employee.class, employee.getUsername());
            if (existingEmployee == null) {
                logger.warn("Employee with Username: " + employee.getUsername() + " does not exist.");
                throw new IllegalArgumentException("Employee with Username " + employee.getUsername() + " does not exist.");
            }

            em.remove(existingEmployee);

            em.getTransaction().commit();
        } catch (Exception e) {
            logger.error("Error deleting employee: ", e);
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new Exception("Error deleting Employee with Username " + employee.getUsername());
        }finally {
            em.close();
        }

    }

    @Override
    public Employee findEmployeeByUsername(String username) throws Exception {
        logger.info("Finding employee by username " + username);
        EntityManager em = JpaUtil.createEntityManager();
        try {

            Employee employee = em.find(Employee.class, username);

            if (employee == null) {
                logger.warn("Employee with Username: " + username + " does not exist.");
                throw new IllegalArgumentException("Employee with username " + username + " does not exist.");
            }
            logger.info("Found employee " + employee);
            return employee;
        } catch (IllegalArgumentException e) {
            logger.error("Error finding employee: ", e);
            throw e;
        } catch (Exception e) {
            throw new Exception("Error finding Employee with username " + username);
        }finally {
            em.close();
        }
    }

    @Override
    public List<Employee> getAllEmployees() throws Exception {
        logger.info("Finding all employees");
        EntityManager em = JpaUtil.createEntityManager();
        try {
            return em.createQuery("SELECT c FROM Employee c", Employee.class).getResultList();
        } catch (Exception e) {
            logger.error("Error finding all employees: ", e);
            throw new Exception("Error retrieving all employees.");
        }finally {
            em.close();
        }
    }
}
