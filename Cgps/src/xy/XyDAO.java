package xy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import util.JDBCutil;

public class XyDAO {
	
	
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs;
	Statement stmt = null;
	
	 
    private static XyDAO instance;
    
    XyDAO(){}
    public static XyDAO getInstance(){
        if(instance==null)
            instance=new XyDAO();
        return instance;
    }
    
    // 댓글목록
 	public List<HashMap<String, Object>> find(String starttime,String endtime) throws Exception {
 		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
 		
 		
 		
 		String sql ="select no,x,y,uuid,addr,to_char(GDATE,'yyyy-mm-ddhh24:mi:ss') " +
				 " gdate from gpst" +
				" where gdate between" +
				" to_date('"+ starttime +"','yyyy-mm-ddhh24:mi:ss') " +
			     " and to_date('" + endtime +"','yyyy-mm-ddhh24:mi:ss') order by 6";
 		
 		//System.out.println("==" + sql);
 		try {
 			conn = JDBCutil.connect();
 			
 			stmt = conn.createStatement();
 			rs = stmt.executeQuery(sql);
 			while (rs.next()) {
 				HashMap<String, Object> map = new HashMap<String, Object>();
 				map.put("no", rs.getInt("no"));
 				map.put("x", rs.getString("x"));
 				map.put(("y"), rs.getString("y"));
 				map.put(("uuid"), rs.getString("uuid"));
 				map.put(("addr"), rs.getString("addr"));
 				map.put(("gdate"), rs.getString("gdate"));
 				list.add(map);
 			}
 		} catch (Throwable e) {
 			System.out.println("gps selectAll error" + e.getMessage());
 			throw new Exception(e.getMessage());
 		} finally {
 			if (rs != null)
 				try {
 					rs.close();
 				} catch (SQLException ex) {
 				}
 			if (stmt != null)
 				try {
 					stmt.close();
 				} catch (SQLException ex) {
 				}
 			if (conn != null)
 				try {
 				
 					conn.close();
 				} catch (SQLException ex) {
 				}
 		}
 	
 		return list;
 	}// end of method

 // 댓글목록
 	public List<HashMap<String, Object>> selectAll() throws Exception {
 		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
 		
 		String sql2 ="select no,x,y,uuid,addr,to_char(GDATE,'yy/mm/dd hh24:mi:ss')  gdate from gpst" + 
 				" where gdate >= sysdate- 3/24/60" + 
 				" order by 6 desc";
 		
 		String sql ="select no,x,y,uuid,addr,to_char(GDATE,'yy/mm/dd hh24:mi:ss') " +
 				 " gdate from gpst" +
 				" where gdate between" +
 				" to_date('17/08/24 13:00:00','yy/mm/dd hh24:mi:ss') " +
 			     " and to_date('17/08/24 13:10:00','yy/mm/dd hh24:mi:ss') order by 6";
 		
 		try {
 			conn = JDBCutil.connect();
 			
 			stmt = conn.createStatement();
 			rs = stmt.executeQuery(sql);
 			while (rs.next()) {
 				HashMap<String, Object> map = new HashMap<String, Object>();
 				map.put("no", rs.getInt("no"));
 				map.put("x", rs.getString("x"));
 				map.put(("y"), rs.getString("y"));
 				map.put(("uuid"), rs.getString("uuid"));
 				map.put(("addr"), rs.getString("addr"));
 				map.put(("gdate"), rs.getString("gdate"));
 				list.add(map);
 			}
 		} catch (Throwable e) {
 			System.out.println("gps selectAll error" + e.getMessage());
 			throw new Exception(e.getMessage());
 		} finally {
 			if (rs != null)
 				try {
 					rs.close();
 				} catch (SQLException ex) {
 				}
 			if (stmt != null)
 				try {
 					stmt.close();
 				} catch (SQLException ex) {
 				}
 			if (conn != null)
 				try {
 				
 					conn.close();
 				} catch (SQLException ex) {
 				}
 		}
 		return list;
 	}// end of method

}
