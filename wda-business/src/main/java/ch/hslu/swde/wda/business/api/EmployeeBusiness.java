package ch.hslu.swde.wda.business.api;

import ch.hslu.swde.wda.domain.Employee;

import java.util.List;

public interface EmployeeBusiness {

    Employee fetchUserDataByUsername(String username);
    void saveUser(String username, String password, String email);
    //void saveEmployee(Employee employee);
    void updateEmployee(String username,  String password, String email);
    void deleteEmployee(String username);
    Employee findEmployeeByUsername(String username);
    List<Employee> getAllEmployees();
}
