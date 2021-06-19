package model.Utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ResourceBundle;

public class DBConnect {
	private static Connection connection=null;
	
	private DBConnect() {

	}
	public static Connection getConnection(){
		if(connection==null) {
			try {
				ResourceBundle resourceBundle=ResourceBundle.getBundle("model/db/jdbc");
				Class.forName("org.sqlite.JDBC");
				connection=DriverManager.getConnection(resourceBundle.getString("url"), resourceBundle.getString("user"), resourceBundle.getString("password"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return connection;
	}
}
