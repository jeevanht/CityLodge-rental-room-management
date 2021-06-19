package model.entity;

import model.SuiteRoomOperations;
import model.RoomOperations;
import model.RecordOperations;
import model.Exceptions.CompleteMaintenanceException;
import model.Exceptions.PerformMaintenanceException;
import model.Exceptions.RentException;
import model.Exceptions.ReturnException;
import model.Utilities.DateTime;

public class SuiteRoom extends Room {
	private DateTime lastMaintenance;
	private double perDayFee;

	public SuiteRoom() {
		
	}

	public SuiteRoom(String roomId, String floor, String roomNumber, String numberOfBeds, String roomType,
			DateTime lastMaintenance) {
		this.roomId = roomId;
		this.roomFloor = floor;
		this.roomNumber = roomNumber;
		this.numberOfBeds = numberOfBeds;
		this.roomType = roomType;
		this.lastMaintenance = lastMaintenance;
		this.roomId = "S_" + floor + roomNumber + numberOfBeds;
		status = "currently available for rent";
		perDayFee = 554;
	}	
	
	public DateTime getLastMaintenance() {
		return lastMaintenance;
	}

	public void setLastMaintenance(DateTime lastMaintenance) {
		this.lastMaintenance = lastMaintenance;
	}

	public double getPerDayFee() {
		return perDayFee;
	} 

	public void setPerDayFee(double perDayFee) {
		this.perDayFee = perDayFee;
	} 

	public void rent(String customerId, DateTime rentDate, DateTime estimatedReturnDate) throws RentException, PerformMaintenanceException {
		if (status.equals("being rented") || status.equals("under maintenance")) {
			throw new RentException(3);
		}
		if (rentDate.getTime()>=new DateTime().getTime()&&status.equals("currently available for rent")
				&& DateTime.diffDays(estimatedReturnDate, rentDate) < DateTime.diffDays((new DateTime(lastMaintenance, 10)), lastMaintenance)) {
			status = "being rented";
			RentalRecord record=new RentalRecord(this, customerId, rentDate,estimatedReturnDate);
			
			RecordOperations.save(record);
			RoomOperations.updateStatus(this);
		}else {
			throw new PerformMaintenanceException(0);
		}

	}

	public void returnRoom(DateTime returnDate) throws ReturnException, PerformMaintenanceException {
		RentalRecord record=RecordOperations.findLatestRecordByProperty(this);
		if (returnDate.getTime() < record.getRentDate().getTime()) {
			throw new ReturnException();
		} else {
			status = "currently available for rent";
			record.setActualReturnDateAndFees(returnDate);
			
			RecordOperations.update(record);
			RoomOperations.updateStatus(this);
		}
	}

	public void performMaintenance() throws PerformMaintenanceException {
		if (status.equals("being rented")) {
			throw new PerformMaintenanceException();
		} else {
			status = "under maintenance";
			RoomOperations.updateStatus(this);
		}
	}

	public void completeMaintenance(DateTime completeDate) throws CompleteMaintenanceException, PerformMaintenanceException {
		if (status.equals("under maintenance")) {
			status = "currently available for rent";
			lastMaintenance = completeDate;
			SuiteRoomOperations.updateLastMaintenance(this);
			RoomOperations.updateStatus(this);
		} else {
			throw new CompleteMaintenanceException("this property isn't under maintenance.");
		}
	}
}
