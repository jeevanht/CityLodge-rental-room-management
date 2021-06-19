package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.entity.StandardRoom;
import model.entity.SuiteRoom;
import model.entity.Room;
import model.Exceptions.PerformMaintenanceException;
import model.Utilities.DateTime;
import model.Utilities.DBConnect;

public class RoomOperations {
	private static Connection connection = DBConnect.getConnection();


	public static Room getHotelRoomByRoomId(String roomId) {
		Statement statement = null;
		Room hotelRoom = null;
		try {
			statement = connection.createStatement();
			String sql = "SELECT * FROM hotel_room where room_id='" + roomId + "'";
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				if (resultSet.getString(6).equals("standard_room")) {
					hotelRoom = new StandardRoom();
					hotelRoom.setRoomId(resultSet.getString(1));
					hotelRoom.setRoomFloor(resultSet.getString(2));
					hotelRoom.setRoomNumber(resultSet.getString(3));
					hotelRoom.setNumberOfBeds(resultSet.getString(4));
					hotelRoom.setBeds(resultSet.getInt(5));
					hotelRoom.setRoomType("standard_room");
					hotelRoom.setStatus(resultSet.getString(7));
					hotelRoom.setImageName(resultSet.getString(8));
					hotelRoom.setDescription(resultSet.getString(9));

					((StandardRoom) hotelRoom)
							.setPerDayFee(StandardRoomOperations.getRoomFee(hotelRoom.getRoomId()));
				} else if (resultSet.getString(6).equals("premium_suite")) {
					hotelRoom = new SuiteRoom();
					hotelRoom.setRoomId(resultSet.getString(1));
					hotelRoom.setRoomFloor(resultSet.getString(2));
					hotelRoom.setRoomNumber(resultSet.getString(3));
					hotelRoom.setNumberOfBeds(resultSet.getString(4));
					hotelRoom.setBeds(resultSet.getInt(5));
					hotelRoom.setRoomType("premium_suite");
					hotelRoom.setStatus(resultSet.getString(7));
					hotelRoom.setImageName(resultSet.getString(8));
					hotelRoom.setDescription(resultSet.getString(9));

					((SuiteRoom) hotelRoom)
							.setPerDayFee(SuiteRoomOperations.findPerDayFeeByRoomId(hotelRoom.getRoomId()));
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
		return hotelRoom;
	}

	public static void updateStatus(Room hotelRoom) throws PerformMaintenanceException {
		Statement statement = null;
		try {
			statement = connection.createStatement();
			String sql = "UPDATE hotel_room SET " + "room_status='" + hotelRoom.getStatus() + "' "
					+ "WHERE room_id='" + hotelRoom.getRoomId() + "'";
			if (statement.executeUpdate(sql) != 1) {
				throw new SQLException("updating rent record into table rental_record failed;");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new PerformMaintenanceException("sql error:" + e.getMessage());
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

	public static List<Room> exportData() {
		ArrayList<Room> rentalRoom = new ArrayList<>();
		Statement statement = null;

		try {
			statement = connection.createStatement();
			String sql = "SELECT * FROM hotel_room";

			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				Room hotelRoom = null;
				if (resultSet.getString(6).equals("standard_room")) {
					hotelRoom = new StandardRoom();
					hotelRoom.setRoomId(resultSet.getString(1));
					hotelRoom.setRoomFloor(resultSet.getString(2));
					hotelRoom.setRoomNumber(resultSet.getString(3));
					hotelRoom.setNumberOfBeds(resultSet.getString(4));
					hotelRoom.setBeds(resultSet.getInt(5));
					hotelRoom.setRoomType("standard_room");
					hotelRoom.setStatus(resultSet.getString(7));
					hotelRoom.setImageName(resultSet.getString(8));
					hotelRoom.setDescription(resultSet.getString(9));
					hotelRoom.setCreateTime(resultSet.getString(10));
				} else if (resultSet.getString(6).equals("premium_suite")) {
					hotelRoom = new SuiteRoom();
					hotelRoom.setRoomId(resultSet.getString(1));
					hotelRoom.setRoomFloor(resultSet.getString(2));
					hotelRoom.setRoomNumber(resultSet.getString(3));
					hotelRoom.setNumberOfBeds(resultSet.getString(4));
					hotelRoom.setBeds(resultSet.getInt(5));
					hotelRoom.setRoomType("premium_suite");
					hotelRoom.setStatus(resultSet.getString(7));
					hotelRoom.setImageName(resultSet.getString(8));
					hotelRoom.setDescription(resultSet.getString(9));
					hotelRoom.setCreateTime(resultSet.getString(10));
				}
				rentalRoom.add(hotelRoom);
			}

			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rentalRoom;
	}

	public static void saveAll(List<Room> rentalRoom) throws Exception {
		Statement statement = null;
		try {
			statement = connection.createStatement();
			String sql = "INSERT INTO hotel_room VALUES ";
			StringBuffer temp = new StringBuffer();
			for (Room hotelRoom : rentalRoom) {
				temp.append("(");
				temp.append(String.format("'%s',", hotelRoom.getRoomId()));
				temp.append(String.format("'%s',", hotelRoom.getRoomFloor()));
				temp.append(String.format("'%s',", hotelRoom.getRoomNumber()));
				temp.append(String.format("'%s',", hotelRoom.getNumberOfBeds()));
				temp.append(String.format("'%d',", hotelRoom.getBeds()));
				temp.append(String.format("'%s',", hotelRoom.getRoomType()));
				temp.append(String.format("'%s',", hotelRoom.getStatus()));
				temp.append(String.format("'%s',", hotelRoom.getImageName()));
				temp.append(String.format("'%s',", hotelRoom.getDescription()));
				temp.append(String.format("'%s'", hotelRoom.getCreateTime()));
				temp.append("),");
			}
			temp.delete(temp.length() - 1, temp.length());
			sql = sql + temp.toString();
			statement.execute(sql);
		} catch (Exception e) {
			throw e;
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

	public static List<Room> getAllHotelRooms() {
		ArrayList<Room> rentalProperties = new ArrayList<>();
		Statement statement = null;

		try {
			statement = connection.createStatement();
			String sql = "SELECT * FROM hotel_room order by create_time desc";

			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				Room hotelRoom = null;
				if (resultSet.getString(6).equals("standard_room")) {
					hotelRoom = new StandardRoom();
					hotelRoom.setRoomId(resultSet.getString(1));
					hotelRoom.setRoomFloor(resultSet.getString(2));
					hotelRoom.setRoomNumber(resultSet.getString(3));
					hotelRoom.setNumberOfBeds(resultSet.getString(4));
					hotelRoom.setBeds(resultSet.getInt(5));
					hotelRoom.setRoomType("standard_room");
					hotelRoom.setStatus(resultSet.getString(7));
					hotelRoom.setImageName(resultSet.getString(8));
					hotelRoom.setDescription(resultSet.getString(9));

					((StandardRoom) hotelRoom)
							.setPerDayFee(StandardRoomOperations.getRoomFee(hotelRoom.getRoomId()));
				} else if (resultSet.getString(6).equals("premium_suite")) {
					hotelRoom = new SuiteRoom();
					hotelRoom.setRoomId(resultSet.getString(1));
					hotelRoom.setRoomFloor(resultSet.getString(2));
					hotelRoom.setRoomNumber(resultSet.getString(3));
					hotelRoom.setNumberOfBeds(resultSet.getString(4));
					hotelRoom.setBeds(resultSet.getInt(5));
					hotelRoom.setRoomType("premium_suite");
					hotelRoom.setStatus(resultSet.getString(7));
					hotelRoom.setImageName(resultSet.getString(8));
					hotelRoom.setDescription(resultSet.getString(9));

					((SuiteRoom) hotelRoom)
							.setPerDayFee(SuiteRoomOperations.findPerDayFeeByRoomId(hotelRoom.getRoomId()));
					((SuiteRoom) hotelRoom).setLastMaintenance(
							SuiteRoomOperations.findLastMaintenanceByRoomId(hotelRoom.getRoomId()));
				}
				rentalProperties.add(hotelRoom);
			}

			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rentalProperties;
	}

	public static List<Room> filterRooms(String condition) {
		ArrayList<Room> rentalRooms = new ArrayList<>();
		Statement statement = null;

		try {
			statement = connection.createStatement();
			String sql = "SELECT * FROM hotel_room WHERE " + condition + " ORDER BY create_time DESC";

			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				Room hotelRoom = null;
				if (resultSet.getString(6).equals("standard_room")) {
					hotelRoom = new StandardRoom();
					hotelRoom.setRoomId(resultSet.getString(1));
					hotelRoom.setRoomFloor(resultSet.getString(2));
					hotelRoom.setRoomNumber(resultSet.getString(3));
					hotelRoom.setNumberOfBeds(resultSet.getString(4));
					hotelRoom.setBeds(resultSet.getInt(5));
					hotelRoom.setRoomType("standard_room");
					hotelRoom.setStatus(resultSet.getString(7));
					hotelRoom.setImageName(resultSet.getString(8));
					hotelRoom.setDescription(resultSet.getString(9));

					((StandardRoom) hotelRoom)
							.setPerDayFee(StandardRoomOperations.getRoomFee(hotelRoom.getRoomId()));
				} else if (resultSet.getString(6).equals("premium_suite")) {
					hotelRoom = new SuiteRoom();
					hotelRoom.setRoomId(resultSet.getString(1));
					hotelRoom.setRoomFloor(resultSet.getString(2));
					hotelRoom.setRoomNumber(resultSet.getString(3));
					hotelRoom.setNumberOfBeds(resultSet.getString(4));
					hotelRoom.setBeds(resultSet.getInt(5));
					hotelRoom.setRoomType("premium_suite");
					hotelRoom.setStatus(resultSet.getString(7));
					hotelRoom.setImageName(resultSet.getString(8));
					hotelRoom.setDescription(resultSet.getString(9));

					((SuiteRoom) hotelRoom)
							.setPerDayFee(SuiteRoomOperations.findPerDayFeeByRoomId(hotelRoom.getRoomId()));
					((SuiteRoom) hotelRoom).setLastMaintenance(
							SuiteRoomOperations.findLastMaintenanceByRoomId(hotelRoom.getRoomId()));
				}
				rentalRooms.add(hotelRoom);
			}

			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rentalRooms;
	}
}
