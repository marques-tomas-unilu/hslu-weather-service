package ch.hslu.swde.wda.domain;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class EmployeeTest {
    @Test
    void testEquals_whenSameValuesInConstructor_thenToStringAreEqual() {
        Employee user1 = new Employee("testUser", "test@example.com", "password123");
        Employee user2 = new Employee("testUser", "test@example.com", "password123");

        assertEquals(user1.toString(), user2.toString());
    }

    @Test
    void testEquals_whenEmployeeCreatedWithConstructor_thenProperStringFormat() {
        Employee user = new Employee("testUser", "test@example.com", "password123");
        String expected = "\nUser {" +
                ",\n    username: testUser" +
                ",\n    email: test@example.com" +
                "\n}";

        assertEquals(expected, user.toString());
    }

    @Test
    void testEquals_whenValuesSet_thenValuesRetrievedCorrectly() {
        Employee user = new Employee();
        user.setUsername("newUser");
        user.setEmail("new@example.com");
        user.setPassword("newPassword");

        assertEquals("newUser", user.getUsername());
        assertEquals("new@example.com", user.getEmail());
        assertEquals("newPassword", user.getPassword());
    }

    @Test
    void testEquals_whenEmptyConstructorUsed_thenProperStringFormat() {
        Employee user = new Employee();
        String expected = "\nUser {" +
                ",\n    username: null" +
                ",\n    email: null" +
                "\n}";

        assertEquals(expected, user.toString());
    }

    @Test
    void testEquals_whenDifferentValues_thenToStringNotEqual() {
        Employee user1 = new Employee( "testUser", "test@example.com", "password123");
        Employee user2 = new Employee( "otherUser", "other@example.com", "password456");

        assertNotEquals(user1.toString(), user2.toString());
    }

    @Test
    void testEquals_whenEmptyConstructorUsed_thenFieldsAreDefault() {
        Employee user = new Employee();

        assertNull(user.getUsername());
        assertNull(user.getEmail());
        assertNull(user.getPassword());
    }
}
