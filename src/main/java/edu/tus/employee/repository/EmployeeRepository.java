package edu.tus.employee.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import edu.tus.employee.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Employee findByEmailAddress(String emailId);

    // Find employees who joined after a certain date
    List<Employee> findByDateOfJoiningAfter(LocalDate date);

    // Find employees within a specific salary range
    List<Employee> findBySalaryBetween(double minSalary, double maxSalary);

    List<Employee> findByDepartmentAndSalaryGreaterThanEqual(String department, double minSalary);

    Long countByDepartment(String department);

}


