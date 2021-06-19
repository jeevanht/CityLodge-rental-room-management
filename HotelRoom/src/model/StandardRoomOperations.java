package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.entity.StandardRoom;
import model.Utilities.DBConnect;

public class StandardRoomOperations {
	private static Connection connection = DBConnect.getConnection();
	
	public static List<StandardRoom> fetchAllStandardRooms(){
		ArrayList<StandardRoom> standardRooms=new ArrayList<>();
		Statement statement = null;
		try {
			statement=connection.createStatement();
			String sql="SELECT * FROM standard_room";
			ResultSet resultSet=statement.executeQuery(sql);
			while (resultSet.next()) {
				StandardRoom standardRoom=new StandardRoom();
				standardRoom.setRoomId(resultSet.getString(1));
				standardRoom.setPerDayFee(resultSet.getDouble(2));
				standardRooms.add(standardRoom);
			}
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return standardRooms;
		}finally {
			if(statement!=null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return standardRooms;
	}
	
/*	public static void saveAll(List<StandardRoom> apt) throws Exception {
		Statement statement=null;
		try {
			statement=connection.createStatement();
			String sql="INSERT INTO standard_room VALUES ";
			StringBuffer temp=new StringBuffer();
			for (StandardRoom standard_room : apt) {
				temp.append("('").append(standard_room.getRoomId()).append("','").append(standard_room.getPerDayFee()).append("'),");
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
	}*/

	public static double getRoomFee(String roomId) {
		Statement statement = null;
		double Fees=0;
		try {
			statement=connection.createStatement();
			String sql="SELECT per_day_fee FROM standard_room WHERE room_id='"+roomId+"'";
			ResultSet resultSet=statement.executeQuery(sql);
			while (resultSet.next()) {
				Fees=resultSet.getDouble(1);
			}
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return Fees;
		}finally {
			if(statement!=null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return Fees;
	}
}
