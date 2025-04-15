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
import java.time.LocalDate;
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

    @Test
    void testGetEmployeesByJoinDateAfter_Found() throws Exception {
        Employee employee = new Employee();
        employee.setFirstName("Jane");
        employee.setLastName("Doe");
        employee.setEmailAddress("jane.doe@email.com");
        employee.setAge(28);
        employee.setDepartment("HR");
        employee.setDateOfJoining(LocalDate.of(2021, 1, 1));
        employee.setSalary(60000);
        employeeRepository.save(employee);

        mockMvc.perform(get("/api/employees/joined-after/2020-01-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Jane"));
    }

    @Test
    void testGetEmployeesByJoinDateAfter_NotFound() throws Exception {
        mockMvc.perform(get("/api/employees/joined-after/2025-01-01"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetEmployeesBySalaryRange_Found() throws Exception {
        Employee employee = new Employee();
        employee.setFirstName("Mike");
        employee.setLastName("Smith");
        employee.setEmailAddress("mike.smith@email.com");
        employee.setAge(35);
        employee.setDepartment("Finance");
        employee.setSalary(55000);
        employeeRepository.save(employee);

        mockMvc.perform(get("/api/employees/salary-range/50000/60000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Mike"));
    }

    @Test
    void testGetEmployeesBySalaryRange_NotFound() throws Exception {
        mockMvc.perform(get("/api/employees/salary-range/100000/200000"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetEmployeesByDepartmentAndMinSalary_Found() throws Exception {
        Employee employee = new Employee();
        employee.setFirstName("Emily");
        employee.setLastName("Johnson");
        employee.setEmailAddress("emily.johnson@email.com");
        employee.setAge(40);
        employee.setDepartment("Engineering");
        employee.setSalary(70000);
        employeeRepository.save(employee);

        mockMvc.perform(get("/api/employees/dept-salary/Engineering/60000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Emily"));
    }

    @Test
    void testGetEmployeesByDepartmentAndMinSalary_NotFound() throws Exception {
        mockMvc.perform(get("/api/employees/dept-salary/Engineering/100000"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetNumberEmployeesforDepartment_Found() throws Exception {
        Employee employee1 = new Employee();
        employee1.setFirstName("Tom");
        employee1.setLastName("Brown");
        employee1.setEmailAddress("tom.brown@email.com");
        employee1.setAge(29);
        employee1.setDepartment("Sales");
        employee1.setSalary(48000);
        employeeRepository.save(employee1);

        Employee employee2 = new Employee();
        employee2.setFirstName("Lucy");
        employee2.setLastName("Green");
        employee2.setEmailAddress("lucy.green@email.com");
        employee2.setAge(32);
        employee2.setDepartment("Sales");
        employee2.setSalary(52000);
        employeeRepository.save(employee2);

        mockMvc.perform(get("/api/employees/dept-number/Sales"))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));
    }

    @Test
    void testGetNumberEmployeesforDepartment_NotFound() throws Exception {
        mockMvc.perform(get("/api/employees/dept-number/NonExistentDept"))
                .andExpect(status().isOk())
                .andExpect(content().string("0"));
    }
}