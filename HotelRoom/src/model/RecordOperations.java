package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.entity.Room;
import model.entity.RentalRecord;
import model.Exceptions.RentException;
import model.Exceptions.ReturnException;
import model.Utilities.DateTime;
import model.Utilities.DBConnect;

public class RecordOperations {
	private static Connection connection = DBConnect.getConnection();

	public static void save(RentalRecord record) throws RentException{
		Statement statement = null;
		try {
			statement = connection.createStatement();
			String sql = "INSERT INTO rental_record(record_id,room_id,customer_id,rent_date,estimated_return_date) "
					+ "VALUES('" + record.getRecordId() + "','" + record.getHotelRoom().getRoomId() + "','"
					+ record.getCustomerId() + "','" + record.getRentDate().toString() + "','"
					+ record.getEstimatedReturnDate().toString() + "')";
			if (statement.executeUpdate(sql) != 1) {
				throw new SQLException("inserting rent record into table rental_record failed;");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RentException(0);
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static RentalRecord findLatestRecordByProperty(Room hotelRoom){
		Statement statement = null;
		RentalRecord latestRecord=null;
		try {
			statement = connection.createStatement();
			String sql = "SELECT * FROM rental_record where room_id='"+hotelRoom.getRoomId()+"' order by rent_date desc limit 1";
			ResultSet resultSet=statement.executeQuery(sql);
			while (resultSet.next()) {
				latestRecord=new RentalRecord();
				latestRecord.setRecordId(resultSet.getString(1));
				latestRecord.setHotelRoom(RoomOperations.getHotelRoomByRoomId(resultSet.getString(2)));
				latestRecord.setCustomerId(resultSet.getString(3));
				latestRecord.setRentDate(new DateTime(resultSet.getString(4)));
				latestRecord.setEstimatedReturnDate(new DateTime(resultSet.getString(5)));
				if(resultSet.getString(6)!=null) {
					latestRecord.setActualReturnDateAndFees(new DateTime(resultSet.getString(6)));
				}
				if(resultSet.getDouble(7)!=0) {
					latestRecord.setRentalFee(resultSet.getDouble(7));
				}
				if(resultSet.getDouble(8)!=0) {
					latestRecord.setLateFee(resultSet.getDouble(8));
				}
			}
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return latestRecord;
	}

	public static void update(RentalRecord record) throws ReturnException{
		Statement statement = null;
		try {
			statement = connection.createStatement();
			String sql = "UPDATE rental_record "
					+ " SET room_id='" + record.getHotelRoom().getRoomId() + "',"
					+ " customer_id='" + record.getCustomerId() + "',"
					+ " rent_date='" + record.getRentDate().toString() + "',"
					+ " estimated_return_date='"+record.getEstimatedReturnDate().toString()+"',"
					+ " actual_return_date='"+record.getActualReturnDate().toString()+"',"
					+ " rental_fee='"+record.getRentalFee()+"',"
					+ " late_fee='"+record.getLateFee()+"' "
					+ " WHERE record_id='"+record.getRecordId()+"' ";
			if (statement.executeUpdate(sql) != 1) {
				throw new SQLException("updating rent record into table rental_record failed;");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ReturnException();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static List<RentalRecord> getAllRecordByProperty(Room room) {
		Statement statement = null;
		List<RentalRecord> rentalRecords=new ArrayList<>();
		RentalRecord record=null;
		try {
			statement = connection.createStatement();
			String sql = "SELECT * FROM rental_record where room_id='"+room.getRoomId()+"' order by rent_date desc";
			ResultSet resultSet=statement.executeQuery(sql);
			while (resultSet.next()) {
				record=new RentalRecord();
				record.setRecordId(resultSet.getString(1));
				record.setHotelRoom(RoomOperations.getHotelRoomByRoomId(resultSet.getString(2)));
				record.setCustomerId(resultSet.getString(3));
				record.setRentDate(new DateTime(resultSet.getString(4)));
				record.setEstimatedReturnDate(new DateTime(resultSet.getString(5)));
				if(resultSet.getString(6)!=null) {
					record.setActualReturnDateAndFees(new DateTime(resultSet.getString(6)));
				}
				if(resultSet.getDouble(7)!=0) {
					record.setRentalFee(resultSet.getDouble(7));
				}
				if(resultSet.getDouble(8)!=0) {
					record.setLateFee(resultSet.getDouble(8));
				}
				
				rentalRecords.add(record);
			}
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return rentalRecords;
	}
	
	public static List<RentalRecord> getAllRecord() {
		Statement statement = null;
		List<RentalRecord> rentalRecords=new ArrayList<>();
		RentalRecord record=null;
		try {
			statement = connection.createStatement();
			String sql = "SELECT * FROM rental_record";
			ResultSet resultSet=statement.executeQuery(sql);
			while (resultSet.next()) {
				record=new RentalRecord();
				record.setRecordId(resultSet.getString(1));
				Room rentalProperty=new Room();
				rentalProperty.setRoomId(resultSet.getString(2));
				record.setHotelRoom(rentalProperty);
				record.setCustomerId(resultSet.getString(3));
				record.setRentDate(new DateTime(resultSet.getString(4)));
				record.setEstimatedReturnDate(new DateTime(resultSet.getString(5)));
				if(resultSet.getString(6)!=null) {
					record.setActualReturnDate(new DateTime(resultSet.getString(6)));
				}
				if(resultSet.getDouble(7)!=0) {
					record.setRentalFee(resultSet.getDouble(7));
				}
				if(resultSet.getDouble(8)!=0) {
					record.setLateFee(resultSet.getDouble(8));
				}
				
				rentalRecords.add(record);
			}
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return rentalRecords;
	}

	public static void saveAll(List<RentalRecord> rentalRecords) throws Exception {
		Statement statement=null;
		try {
			statement=connection.createStatement();
			String sql="INSERT INTO rental_record VALUES ";
			StringBuffer temp=new StringBuffer();
			for (RentalRecord rentalRecord : rentalRecords) {
				temp.append("(");
				temp.append(String.format("'%s',", rentalRecord.getRecordId()));
				temp.append(String.format("'%s',", rentalRecord.getHotelRoom().getRoomId()));
				temp.append(String.format("'%s',", rentalRecord.getCustomerId()));
				temp.append(String.format("'%s',", rentalRecord.getRentDate().toString()));
				temp.append(String.format("'%s',", rentalRecord.getEstimatedReturnDate().toString()));
				if(rentalRecord.getActualReturnDate()==null) {
					temp.append("null,");
				}else {
					temp.append(String.format("'%s',", rentalRecord.getActualReturnDate().toString()));
				}
				temp.append(String.format("'%.2f',", rentalRecord.getRentalFee()));
				temp.append(String.format("'%.2f'", rentalRecord.getLateFee()));
				temp.append("),");
			}
			temp.delete(temp.length()-1, temp.length());
			sql=sql+temp.toString();
			statement.execute(sql);
		} catch (Exception e) {
			throw e;
		}finally {
			if(statement!=null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
