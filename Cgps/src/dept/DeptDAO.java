package dept;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.DAO;

public class DeptDAO extends DAO {
	Connection conn;
	Statement stmt;
	PreparedStatement pstmt;
	ResultSet rs;
	
	//�떛湲��넠 援ы쁽
	public static DeptDAO instance = new DeptDAO();
	public static DeptDAO getInstance() {
		return instance;
	}
	
	// Create, Read, Update, Delete
	public int insert(DeptBeans bean) {
		int r = 0;
		try{
			//1. connect
			conn = connect();
			String sql = "insert into departments (department_id, "
					+ " department_name, location_id, manager_id ) "
					+ " values( (select max(department_id)+10 from departments),"
					+ "   ?,?,?) ";
			//2 sql 援щЦ 以�鍮�
			pstmt = conn.prepareStatement(sql);
			//3. sql 援щЦ �떎�뻾
			pstmt.setString(1, bean.getDepartmentName());
			pstmt.setString(2, bean.getLocationId());
			pstmt.setString(3, bean.getManagerId());
			r = pstmt.executeUpdate();			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			//4.�뿰寃고빐�젣
			disconnect(conn);  
		}
		return r;
	}
	public int update(DeptBeans bean) {
		int r = 0;
		try{
			//1. connect
			conn = connect();
			String sql = "update departments "
					+ "      set department_name = ? "
		//			+ "          location_id = ? , "
		//			+ "          manager_id  = ? "
					+ "    where department_id = ? ";
			//2 sql 援щЦ 以�鍮�
			pstmt = conn.prepareStatement(sql);
			//3. sql 援щЦ �떎�뻾
			pstmt.setString(1, bean.getDepartmentName());
			//pstmt.setString(2, bean.getLocationId());
			//pstmt.setString(3, bean.getManagerId());
			pstmt.setString(2, bean.getDepartmentId());
			r = pstmt.executeUpdate();			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			//4.�뿰寃고빐�젣
			disconnect(conn);  
		}		
		return r;
	}
	public int delete(BigDecimal depaertmentId) {
		int r = 0;
		try{
			//1. connect
			conn = connect();
			String sql = "delete departments "
					+ "    where department_id = ? ";
			//2 sql 援щЦ 以�鍮�
			pstmt = conn.prepareStatement(sql);
			//3. sql 援щЦ �떎�뻾
			pstmt.setInt(1, depaertmentId.intValue());
			r = pstmt.executeUpdate();			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			//4.�뿰寃고빐�젣
			disconnect(conn);  
		}
		return r;
	}
	//�떒嫄댁“�쉶
	public DeptBeans selectOne(DeptBeans bean) {
		DeptBeans result = null;
		try{
			conn = connect();
			String sql = "select * from departments where department_id=" 
			               + bean.getDepartmentId()  ;
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if(rs.next()) {   //泥ル쾲吏� �젅肄붾뱶濡� �씠�룞
				result = new DeptBeans();
				result.setDepartmentName(rs.getString("Department_Name"));
				result.setDepartmentId(rs.getString("Department_id"));
				result.setManagerId(rs.getString("manager_id"));
				result.setLocationId(rs.getString("location_id"));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public List<Map<String,Object>> selectAll() {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		try {
			conn = connect();			
			String sql = " select svr_name,to_char(s_date,'yy/mm/dd hh24:mi:ss') as sdate,cpu1load,cpu5load,cpu15load," + 
					"  case" + 
					"        when ifhcin >= 1024*1024*1024 then round(ifhcin/1024/1024/1024,2) ||' GB'" + 
					"        when ifhcin >= 1024*1024  then round(ifhcin/1024/1024,2) ||' MB'" + 
					"        when ifhcin >= 1024 then round(ifhcin/1024,2) ||' KB'" + 
					"        when ifhcin < 1024 then round(ifhcin,2) ||' B'" + 
					" end as ifhcin," + 
					"  case" + 
					"        when ifhcout >= 1024*1024*1024 then round(ifhcout/1024/1024/1024,2) ||' GB'" + 
					"        when ifhcout >= 1024*1024  then round(ifhcout/1024/1024,2) ||' MB'" + 
					"        when ifhcout >= 1024 then round(ifhcout/1024,2) ||' KB'" + 
					"        when ifhcout < 1024 then round(ifhcout,2) ||' B'" + 
					" end as ifhcout,usercpu,syscpu,idlecpu," + 
					"    case" + 
					"        when availswap >= 1024*1024  then round(availswap/1024/1024,2) ||' GB'" + 
					"        when availswap >= 1024 then round(availswap/1024,2) ||' MB'" + 
					"        when availswap < 1024 then round(availswap,2) ||' KB'" + 
					" end as availswap," + 
					"      case" + 
					"        when totalram >= 1024*1024  then round(totalram/1024/1024,2) ||' GB'" + 
					"        when totalram >= 1024 then round(totalram/1024,2) ||' MB'" + 
					"        when totalram < 1024 then round(totalram,2) ||' KB'" + 
					" end as totalram," + 
					"  case" + 
					"        when usedram >= 1024*1024  then round(usedram/1024/1024,2) ||' GB'" + 
					"        when usedram >= 1024 then round(usedram/1024,2) ||' MB'" + 
					"        when usedram < 1024 then round(usedram,2) ||' KB'" + 
					" end as usedram," + 
					"   case" + 
					"        when freeram >= 1024*1024  then round(freeram/1024/1024,2) ||' GB'" + 
					"        when freeram >= 1024 then round(freeram/1024,2) ||' MB'" + 
					"        when freeram < 1024 then round(freeram,2) ||' KB'" + 
					" end as freeram," + 
					"   case" + 
					"        when availdsik >= 1024*1024  then round(availdsik/1024/1024,2) ||' GB'" + 
					"        when availdsik >= 1024 then round(availdsik/1024,2) ||' MB'" + 
					"        when availdsik < 1024 then round(availdsik,2) ||' KB'" + 
					" end as availdsik," + 
					"  case" + 
					"        when useddisk >= 1024*1024  then round(useddisk/1024/1024,2) ||' GB'" + 
					"        when useddisk >= 1024 then round(useddisk/1024,2) ||' MB'" + 
					"        when useddisk < 1024 then round(useddisk,2) ||' KB'" + 
					" end as useddisk" + 
					"  from svrdata join server " + 
					" on svrdata.svr_no = server.svr_no" + 
					" where s_date = (select max(s_date) from svrdata)";
			
			
			
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);			
			while(rs.next()) {
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("svrName", rs.getString("svr_name"));
				map.put("sdate", rs.getString("sdate"));
				map.put("cpu1load", rs.getString("cpu1load"));
				map.put("cpu5load", rs.getString("cpu5load"));
				map.put("cpu15load", rs.getString("cpu15load"));
				map.put("usercpu", rs.getString("usercpu"));
				map.put("syscpu", rs.getString("syscpu"));
				map.put("idlecpu", rs.getString("idlecpu"));
				map.put("availswap", rs.getString("availswap"));
				map.put("totalram", rs.getString("totalram"));
				map.put("usedram", rs.getString("usedram"));
				map.put("freeram", rs.getString("freeram"));
				map.put("availdsik", rs.getString("availdsik"));
				map.put("useddisk", rs.getString("useddisk"));
				map.put("ifhcin", rs.getString("ifhcin"));
				map.put("ifhcout", rs.getString("ifhcout"));
				
				list.add(map);
			}			
			//4. disconnect
		} catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<Map<String,Object>> selectPage(int first, int last) {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		try {
			conn = connect();			
			String sql = "select * from ( select rownum rnum, a.* from ( "+
					     "select * from departments order by department_id "+
					     ") a ) b where rnum between ? and ? " ;
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, first);
			pstmt.setInt(2, last);
			rs = pstmt.executeQuery();			
			while(rs.next()) {
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("departmentId", rs.getInt("department_id"));
				map.put("departmentName", rs.getString("department_name"));
				map.put("managerId", rs.getInt("manager_id"));
				map.put("locationId", rs.getBigDecimal("location_id"));
				list.add(map);
			}			
			//4. disconnect
		} catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}	
}
