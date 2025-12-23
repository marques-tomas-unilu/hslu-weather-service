package ch.hslu.swde.wda.persister.api;

import java.util.List;

import ch.hslu.swde.wda.domain.Employee;

public interface EmployeePersister {

    Employee add(Employee employee);
    Employee delete(int id);
    Employee update(Employee employee);
    Employee find(int id);
    List<Employee> all();
    void saveEmployee(Employee employee) throws Exception;
    void updateEmployee(Employee employee) throws Exception;
    void deleteEmployee(Employee employee) throws Exception;
    Employee findEmployeeByUsername(String username) throws Exception;
    List<Employee> getAllEmployees() throws Exception;
}
