package model.Exceptions;

public class RentException extends Exception{

	/**
	 * 
	 */
	
	private int exceptionCode;
	
	public RentException(int exceptionCode) {
		this.exceptionCode=exceptionCode;
	}
	
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		if(exceptionCode == 0)
			return "Please enter customer ID";
		else if(exceptionCode == 1)
			return "Please select more than 3 days";
		else if (exceptionCode == 2)
			return "Please select more than 2 days";
		else
			return super.getMessage()+"\n"+exceptionCode;
	}
	
	@Override
	public void printStackTrace() {
		System.err.println("Exceptions code is:"+exceptionCode);
		super.printStackTrace();
	}
}
