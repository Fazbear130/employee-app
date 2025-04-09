package edu.tus.employee.erros;

import edu.tus.employee.errors.ErrorValidation;
import edu.tus.employee.exception.EmployeeValidationException;
import edu.tus.employee.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ErrorValidationTest {

    private ErrorValidation errorValidation;

    @BeforeEach
    void setUp() {
        errorValidation = new ErrorValidation();
    }

    @Test
    void testValidateEmployee_ValidEmployee() {
        Employee employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setAge(30);
        assertDoesNotThrow(() -> errorValidation.validateEmployee(employee));
    }

    @Test
    void testValidateEmployee_JoeBloggs() {
        Employee employee = new Employee();
        employee.setFirstName("Joe");
        employee.setLastName("Bloggs");
        employee.setAge(30);
        EmployeeValidationException exception = assertThrows(EmployeeValidationException.class,
                () -> errorValidation.validateEmployee(employee));
        assertEquals("Joe Bloggs not allowed", exception.getMessage());
    }

    @Test
    void testValidateEmployee_InvalidAge_TooYoung() {
        Employee employee = new Employee();
        employee.setFirstName("Alice");
        employee.setLastName("Smith");
        employee.setAge(17);
        EmployeeValidationException exception = assertThrows(EmployeeValidationException.class,
                () -> errorValidation.validateEmployee(employee));
        assertEquals("Employees must be over 18 and under 66", exception.getMessage());
    }

    @Test
    void testValidateEmployee_InvalidAge_TooOld() {
        Employee employee = new Employee();
        employee.setFirstName("Bob");
        employee.setLastName("Johnson");
        employee.setAge(66);
        EmployeeValidationException exception = assertThrows(EmployeeValidationException.class,
                () -> errorValidation.validateEmployee(employee));
        assertEquals("Employees must be over 18 and under 66", exception.getMessage());
    }
}