package edu.tus.employee.exception;

public class EmployeeValidationException extends EmployeeException {

	private static final long serialVersionUID = 334051992916748022L;

	public EmployeeValidationException(final String errorMessage) {
		super(errorMessage);
	}

}

