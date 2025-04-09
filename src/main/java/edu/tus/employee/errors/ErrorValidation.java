package edu.tus.employee.errors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.tus.employee.exception.EmployeeValidationException;
import edu.tus.employee.model.Employee;
import edu.tus.employee.repository.EmployeeRepository;

@Component
public class ErrorValidation {
	
	@Autowired
    EmployeeRepository empRepository;
	
	public void validateEmployee(Employee employee) throws EmployeeValidationException {
		if ("Joe".equalsIgnoreCase(employee.getFirstName()) && "Bloggs".equalsIgnoreCase(employee.getLastName())) {
            throw new EmployeeValidationException(ErrorMessages.JOE_BLOGGS.getMsg());
        }
        if (employee.getAge() < 18 || employee.getAge() > 65) {
            throw new EmployeeValidationException(ErrorMessages.INVALID_AGE.getMsg());
        }
	}
	
	

}
