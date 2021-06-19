package model.entity;

import model.Utilities.DateTime;

public class RentalRecord {
	private String customerId;
	private Room hotelRoom;
	private DateTime rentDate;
	private String recordId;
	private DateTime actualReturnDate;
	private DateTime estimatedReturnDate;
	private double rentalFee;
	private double lateFee;
	private final double penaltySuite = 1099;
	private final double penaltyStandardRoom = 1.35;


	public RentalRecord() {

	}

	public RentalRecord(Room hotelRoom, String customerId,DateTime rentDate, DateTime estimatedReturnDate) {
		this.customerId = customerId;
		this.hotelRoom = hotelRoom;
		this.rentDate=rentDate;
		this.estimatedReturnDate = estimatedReturnDate;
		this.recordId=hotelRoom.getRoomId()+"_"+customerId+"_"+rentDate.getEightDigitDate();
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public Room getHotelRoom() {
		return hotelRoom;
	}

	public void setHotelRoom(Room rentalProperty) {
		this.hotelRoom = rentalProperty;
	}

	public DateTime getRentDate() {
		return rentDate;
	}

	public void setRentDate(DateTime rentDate) {
		this.rentDate = rentDate;
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public DateTime getActualReturnDate() {
		return actualReturnDate;
	}

	public void setActualReturnDateAndFees(DateTime actualReturnDate) {
		this.actualReturnDate = actualReturnDate;
		int diffDays=DateTime.diffDays(this.actualReturnDate, this.estimatedReturnDate);
		if(this.hotelRoom.roomType.equals("standard_room")) {
			if(diffDays>0) {
				this.rentalFee=DateTime.diffDays(this.estimatedReturnDate, this.rentDate)*((StandardRoom)this.hotelRoom).getPerDayFee();
				this.lateFee=DateTime.diffDays(this.actualReturnDate, this.estimatedReturnDate)*((StandardRoom)this.hotelRoom).getPerDayFee()*penaltyStandardRoom;
			}else {
				this.lateFee=0;
				this.rentalFee=DateTime.diffDays(this.actualReturnDate, this.rentDate)*((StandardRoom)this.hotelRoom).getPerDayFee();
			}
		}else if(this.hotelRoom.roomType.equals("premium_suite")) {
			if(diffDays>0) {
				this.rentalFee=DateTime.diffDays(this.estimatedReturnDate, this.rentDate)*((SuiteRoom)this.hotelRoom).getPerDayFee();
				this.lateFee=DateTime.diffDays(this.actualReturnDate, this.estimatedReturnDate)*penaltySuite;
			}else {
				this.lateFee=0;
				this.rentalFee=DateTime.diffDays(this.actualReturnDate, this.rentDate)*((SuiteRoom)this.hotelRoom).getPerDayFee();
			}
		}
	}
	public void setActualReturnDate(DateTime actualReturnDate) {
		this.actualReturnDate = actualReturnDate;
	}
	
	public DateTime getEstimatedReturnDate() {
		return estimatedReturnDate;
	}

	public void setEstimatedReturnDate(DateTime estimatedReturnDate) {
		this.estimatedReturnDate = estimatedReturnDate;
	}

	public double getRentalFee() {
		return rentalFee;
	}

	public void setRentalFee(double rentalFee) {
		this.rentalFee = rentalFee;
	}

	public double getLateFee() {
		return lateFee;
	}

	public void setLateFee(double lateFee) {
		this.lateFee = lateFee;
	}

}
