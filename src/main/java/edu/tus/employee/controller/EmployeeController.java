package edu.tus.employee.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import edu.tus.employee.errors.ErrorMessage;
import edu.tus.employee.errors.ErrorValidation;
import edu.tus.employee.exception.EmployeeException;
import edu.tus.employee.model.Employee;
import edu.tus.employee.repository.EmployeeRepository;

@RestController
@Service
@RequestMapping("/api/employees")
public class EmployeeController {

	@Autowired
	EmployeeRepository empRepository;


	@Autowired
	ErrorValidation errorValidation;

	@GetMapping
	public String testOK() {
		return "OK";
	}

	@GetMapping("/by-email/{email}")
	public ResponseEntity<?> searchEmployees(@PathVariable String email) {
		Employee employee = empRepository.findByEmailAddress(email);
		if (employee != null) {
			return ResponseEntity.ok(employee);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/joined-after/{date}")
	public ResponseEntity<List<Employee>> getEmployeesByJoinDateAfter(
			@PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		List<Employee> employees = empRepository.findByDateOfJoiningAfter(date);
		if (!employees.isEmpty()) {
			return ResponseEntity.ok(employees);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/salary-range/{minSalary}/{maxSalary}")
	public ResponseEntity<List<Employee>> getEmployeesBySalaryRange(
			@PathVariable("minSalary") double minSalary,
			@PathVariable("maxSalary") double maxSalary) {
		List<Employee> employees = empRepository.findBySalaryBetween(minSalary, maxSalary);
		if (!employees.isEmpty()) {
			return ResponseEntity.ok(employees);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/dept-salary/{department}/{minSalary}")
	public ResponseEntity<List<Employee>> getEmployeesByDepartmentAndMinSalary(
			@PathVariable("department") String department,
			@PathVariable("minSalary") double minSalary) {
		List<Employee> employees = empRepository.findByDepartmentAndSalaryGreaterThanEqual(department, minSalary);
		if (!employees.isEmpty()) {
			return ResponseEntity.ok(employees);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/dept-number/{department}")
	public ResponseEntity<Long> getNumberEmployeesforDepartment(
			@PathVariable("department") String department) {
		return ResponseEntity.ok(empRepository.countByDepartment(department));
	}

	@PostMapping
	public ResponseEntity createEmployee(@RequestBody Employee employee) {
		try {
			errorValidation.validateEmployee(employee);
			Employee savedEmployee = empRepository.save(employee);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployee);
		} catch (EmployeeException e) {
			ErrorMessage errorMessage=new ErrorMessage(e.getMessage());
			return ResponseEntity.badRequest().body(errorMessage);
		}
	}

}






