package edu.tus.employee.errors;

public enum ErrorMessages {
	JOE_BLOGGS("Joe Bloggs not allowed"),
	INVALID_AGE("Employees must be over 18 and under 66");
	
	private String errorMessage;
	
	ErrorMessages(String errMsg){
		this.errorMessage=errMsg;
	}
	
	public String getMsg(){
		return errorMessage;
	}
}
