package model.Exceptions;

public class PerformMaintenanceException extends Exception {
	/**
	 * 
	 */
	private int exceptionCode;
	public PerformMaintenanceException() {

	}

	public PerformMaintenanceException(String arg0) {
		super(arg0);
	}
	
	public PerformMaintenanceException(int exp) {
		this.exceptionCode=exp;
	}
	
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		if(exceptionCode == 0)
		return "Maintainanceis scheduled fo booked period!";
		else if (exceptionCode == 2)
			return "Please select more than 2 days";
		else
			return super.getMessage()+"\n"+exceptionCode;
	}
}
	

