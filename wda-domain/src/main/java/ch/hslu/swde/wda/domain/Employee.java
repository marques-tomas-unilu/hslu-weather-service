package ch.hslu.swde.wda.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Entity
public class Employee implements Serializable, Comparable<Employee> {


    @Id
    @Getter @Setter private String username;
    @Getter @Setter private String email;
    @Getter @Setter private String password;

    public Employee() {}

    public Employee( String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    /* ********************************************** */
    /* toString OUTPUT */

    @Override
    public String toString() {
        return "\nUser {" +
                ",\n    username: " + username +
                ",\n    email: " + email +
                "\n}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Employee employee = (Employee) o;
        return username.equals(employee.username) && email.equals(employee.email);
    }

    @Override
    public int compareTo(Employee o) {
        return this.username.compareTo(o.username);
    }
}
