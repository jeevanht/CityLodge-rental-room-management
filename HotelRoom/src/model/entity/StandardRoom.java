package model.entity;

import model.RoomOperations;
import model.RecordOperations;
import model.Exceptions.PerformMaintenanceException;
import model.Exceptions.RentException;
import model.Exceptions.ReturnException;
import model.Utilities.DateTime;

public class StandardRoom extends Room {

	private double perDayFee;
	private double latePerDayFee;
	
	public StandardRoom() {
	}

	public StandardRoom(String roomId, String floor, String roomNumber, String numberOfBeds, String roomType,
			int beds) {
		this.roomId = "standard_room".charAt(0) + "_" + floor + roomNumber + numberOfBeds;
		this.roomFloor = floor;
		this.roomNumber = roomNumber;
		this.numberOfBeds = numberOfBeds;
		this.roomType = "standard_room";
		this.beds = beds;
		status = "currently available for rent";
	}

	public double getPerDayFee() {
		return perDayFee;
	}

	public void setPerDayFee(double perDayFee) {
		this.perDayFee = perDayFee;
		this.latePerDayFee=1.15*perDayFee;
	}

	public void rent(String customerId, DateTime rentDate, DateTime estimatedReturnDate) throws RentException, PerformMaintenanceException {
		if (rentDate.getTime() >= new DateTime().getTime() && status.equals("currently available for rent")) {
			int diffDays=DateTime.diffDays(estimatedReturnDate, rentDate);
			if (rentDate.DayOfWeek().equals("Friday") || rentDate.DayOfWeek().equals("Saturday")) {
				if (diffDays >= 3 && diffDays <= 10) {
					status ="being rented";
					RentalRecord latestRecord=new RentalRecord(this,customerId,rentDate,estimatedReturnDate);
					
					RecordOperations.save(latestRecord);
					RoomOperations.updateStatus(this);
				} else {
					throw new RentException(1);
				}
			} else {
				if (diffDays >= 2 && diffDays <= 10) {
					status = "being rented";
					RentalRecord latestRecord=new RentalRecord(this, customerId, rentDate,estimatedReturnDate);
					
					RecordOperations.save(latestRecord);
					RoomOperations.updateStatus(this);
				} else {
					throw new RentException(2);
				}
			}
		} else {
			throw new RentException(3);
		}
	}

	public void returnRoom(DateTime returnDate) throws ReturnException, PerformMaintenanceException {
		RentalRecord latestRecord=RecordOperations.findLatestRecordByProperty(this);
		if (returnDate.getTime() > latestRecord.getRentDate().getTime() && status.equals("being rented")) {
			latestRecord.setActualReturnDateAndFees(returnDate);
			status = "currently available for rent";
			
			RecordOperations.update(latestRecord);
			RoomOperations.updateStatus(this);
		} else {
			throw new ReturnException();
		}
	}
}
