package common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DAO {
	
	public Connection connect() throws Exception {
		//1. 드라이버로딩
		Class.forName("oracle.jdbc.driver.OracleDriver");
		
		//2. DB 연결
		String url= "jdbc:oracle:thin:@leehs.iptime.org:1521:orcl";
		Connection conn = DriverManager.getConnection(url, "lee", "oracle");
		
		return conn;
	}
	
	public void disconnect(Connection conn) {
		if ( conn != null)
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
}
