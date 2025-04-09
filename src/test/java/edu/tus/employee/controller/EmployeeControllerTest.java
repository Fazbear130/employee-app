package edu.tus.employee.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tus.employee.model.Employee;
import edu.tus.employee.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
    }

    @Test
    void testCreateEmployee_Valid() throws Exception {
        Employee employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmailAddress("john.doe@email.com");
        employee.setAge(30);
        employee.setDepartment("Engineering");
        employee.setSalary(50000);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void testCreateEmployee_InvalidAge() throws Exception {
        Employee employee = new Employee();
        employee.setFirstName("Alice");
        employee.setLastName("Smith");
        employee.setEmailAddress("alice.smith@email.com");
        employee.setAge(17);
        employee.setDepartment("Marketing");
        employee.setSalary(40000);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Employees must be over 18 and under 66"));
    }

    @Test
    void testGetEmployeeByEmail_Found() throws Exception {
        Employee employee = new Employee();
        employee.setFirstName("Bob");
        employee.setLastName("Brown");
        employee.setEmailAddress("bob.brown@email.com");
        employee.setAge(25);
        employee.setDepartment("Sales");
        employee.setSalary(45000);
        employeeRepository.save(employee);

        mockMvc.perform(get("/api/employees/by-email/bob.brown@email.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.emailAddress").value("bob.brown@email.com"));
    }

    @Test
    void testGetEmployeeByEmail_NotFound() throws Exception {
        mockMvc.perform(get("/api/employees/by-email/nonexistent@email.com"))
                .andExpect(status().isNotFound());
    }
}