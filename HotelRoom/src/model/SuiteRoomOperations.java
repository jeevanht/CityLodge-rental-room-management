package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.entity.SuiteRoom;
import model.Exceptions.CompleteMaintenanceException;
import model.Utilities.DateTime;
import model.Utilities.DBConnect;

public class SuiteRoomOperations {
	private static Connection connection = DBConnect.getConnection();
	
	public static double findPerDayFeeByRoomId(String roomId) {
		Statement statement = null;
		double Fees=0;
		try {
			statement=connection.createStatement();
			String sql="SELECT per_day_fee FROM premium_suite WHERE room_id='"+roomId+"'";
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

	public static void saveToDb(List<SuiteRoom> suitesRooms) throws Exception {
		Statement statement=null;
		try {
			statement=connection.createStatement();
			String sql="INSERT INTO premium_suite VALUES ";
			StringBuffer temp=new StringBuffer();
			for (SuiteRoom suiteRoom2 : suitesRooms) {
				temp.append("('").append(suiteRoom2.getRoomId()).append("','").append(suiteRoom2.getLastMaintenance().getEightDigitDate()).append("','").append(suiteRoom2.getPerDayFee()).append("'),");
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

	public static void updateLastMaintenance(SuiteRoom suiteRooms1) throws CompleteMaintenanceException {
		Statement statement = null;
		try {
			statement = connection.createStatement();
			String sql = "UPDATE premium_suite SET "
					+ "last_maintenance_date='"+suiteRooms1.getLastMaintenance().getEightDigitDate()+"' "
					+ "WHERE room_id='"+suiteRooms1.getRoomId()+"'";
			if (statement.executeUpdate(sql) != 1) {
				throw new SQLException("updating rent record into table rental_record failed;");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CompleteMaintenanceException("sql error:"+e.getMessage());
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

	public static DateTime findLastMaintenanceByRoomId(String roomId) {
		Statement statement = null;
		DateTime dateTime=null;
		try {
			statement=connection.createStatement();
			String sql="SELECT last_maintenance_date FROM premium_suite WHERE room_id='"+roomId+"'";
			ResultSet resultSet=statement.executeQuery(sql);
			while (resultSet.next()) {
				dateTime=new DateTime(resultSet.getString(1));
			}
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return dateTime;
		}finally {
			if(statement!=null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return dateTime;
	}
	
	public static List<SuiteRoom> getAllSuiteRooms(){
		ArrayList<SuiteRoom> suiteRooms1=new ArrayList<>();
		Statement statement = null;
		try {
			statement=connection.createStatement();
			String sql="SELECT * FROM premium_suite";
			ResultSet resultSet=statement.executeQuery(sql);
			while (resultSet.next()) {
				SuiteRoom suiteRoom3=new SuiteRoom();
				suiteRoom3.setRoomId(resultSet.getString(1));
				suiteRoom3.setLastMaintenance(new DateTime(resultSet.getString(2)));
				suiteRoom3.setPerDayFee(resultSet.getDouble(3));
				suiteRooms1.add(suiteRoom3);
			}
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return suiteRooms1;
		}finally {
			if(statement!=null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return suiteRooms1;
	}


}
