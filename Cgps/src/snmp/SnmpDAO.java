package snmp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;



public class SnmpDAO {
	
	 public  String byteCalc(String bytes) {
         String retFormat = "0";
        Double size = Double.parseDouble(bytes); 
         String[] s = { "bytes", "KB", "MB", "GB", "TB", "PB" };
        
         if (bytes != "0") {
               int idx = (int) Math.floor(Math.log(size) / Math.log(1024));
               DecimalFormat df = new DecimalFormat("#,###.##");
               double ret = ((size / Math.pow(1024, Math.floor(idx))));
               retFormat = df.format(ret) + " " + s[idx];
          } else {
               retFormat += " " + s[0];
          }
          return retFormat;
}
	
	
	 public  String byteCalculation(String bytes) {
         String retFormat = "0";
        Double size = Double.parseDouble(bytes) * 1024; // 1024 빼면 byte 1024 추가 하면 kb
         String[] s = { "bytes", "KB", "MB", "GB", "TB", "PB" };
        
         if (bytes != "0") {
               int idx = (int) Math.floor(Math.log(size) / Math.log(1024));
               DecimalFormat df = new DecimalFormat("#,###.##");
               double ret = ((size / Math.pow(1024, Math.floor(idx))));
               retFormat = df.format(ret) + " " + s[idx];
          } else {
               retFormat += " " + s[0];
          }
          return retFormat;
         
}
	 
	 public ArrayList<SnmpData> getDBList5() throws ClassNotFoundException, SQLException {
			//부서정보 조회
			//0.드라이버로딩
			Class.forName("oracle.jdbc.OracleDriver");// 해당 클래스를 메모리에 로딩
			 
			//1.connection
			String url ="jdbc:oracle:thin:@leehs.iptime.org:1521:orcl";
			Connection conn = DriverManager.getConnection(url,"lee","oracle");
			
			//2.Statement
			Statement stmt = conn.createStatement();
			
			String sql="select to_char(s_date,'hh24:mi:ss') sdate," + 
					" round((ifhcout - lead(ifhcout) over (order by s_date desc))/1024) ifhcout" + 
					" from svrdata" + 
					" where svr_no =1 and s_date > sysdate-8/(24)";
			
			ResultSet rs = stmt.executeQuery(sql);  //select (조회) -> 결과집합
			
			ArrayList<SnmpData> list = new ArrayList<SnmpData>();
			while(rs.next()){
				SnmpData snmp = new SnmpData();
				//snmp.setSvrNo(rs.getString("svr_name"));
				snmp.setIfHcout(rs.getDouble("ifHcout"));
				snmp.setSDate(rs.getString("sdate"));
				list.add(snmp);
			}
			
			//4.disconnect
			conn.close();
				
			return list;
		}
	 
	 
	 
	 
	 
	 public ArrayList<SnmpData> getDBList4() throws ClassNotFoundException, SQLException {
			//부서정보 조회
			//0.드라이버로딩
			Class.forName("oracle.jdbc.OracleDriver");// 해당 클래스를 메모리에 로딩
			 
			//1.connection
			String url ="jdbc:oracle:thin:@leehs.iptime.org:1521:orcl";
			Connection conn = DriverManager.getConnection(url,"lee","oracle");
			
			//2.Statement
			Statement stmt = conn.createStatement();
			
			String sql4="select svr_name,to_char(s_date,'yy/mm/dd hh24:mi:ss') as sdate,cpu1load,cpu5load,cpu15load," + 
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
					"  from svrdata join server" + 
					" on svrdata.svr_no = server.svr_no" + 
					" where s_date = (select max(s_date) from svrdata)"; 
					
				
			//3.ResultSet
			ResultSet rs = stmt.executeQuery(sql4);  //select (조회) -> 결과집합
			
			ArrayList<SnmpData> list = new ArrayList<SnmpData>();
			while(rs.next()){
				SnmpData snmp = new SnmpData();
				snmp.setSvrNo(rs.getString("svr_name"));
				snmp.setCpu1Load(rs.getString("cpu1load"));
				snmp.setCpu5Load(rs.getString("cpu5load"));
				snmp.setCpu15Load(rs.getString("cpu15Load"));
				snmp.setUserCpu(rs.getString("userCpu"));
				snmp.setSysCpu(rs.getString("sysCpu"));
				snmp.setIdleCpu(rs.getString("idleCpu"));
				snmp.setAvailSwap(rs.getString("availSwap"));
				snmp.setTotalRam(rs.getString("totalRam"));
				snmp.setUsedRam(rs.getString("usedRam"));
				snmp.setFreeRam(rs.getString("freeRam"));
				snmp.setAvailDsik(rs.getString("availDsik"));
				snmp.setUsedDisk(rs.getString("usedDisk"));
				snmp.setSifHcin(rs.getString("ifHcin")); // 화면을 뿌려줄려고 만듦
				snmp.setSifHcout(rs.getString("ifHcout"));
				snmp.setSDate(rs.getString("sdate"));
				list.add(snmp);
			}
			
			//4.disconnect
			conn.close();
				
			return list;
		}
	 
	
	public ArrayList<SnmpData> getDBList3() throws ClassNotFoundException, SQLException {
		//부서정보 조회
		//0.드라이버로딩
		Class.forName("oracle.jdbc.OracleDriver");// 해당 클래스를 메모리에 로딩
		 
		//1.connection
		String url ="jdbc:oracle:thin:@leehs.iptime.org:1521:orcl";
		Connection conn = DriverManager.getConnection(url,"lee","oracle");
		
		//2.Statement
		Statement stmt = conn.createStatement();
		
		
		String sql2="SELECT * FROM" +
				 " (SELECT * FROM svrdata ORDER BY ROWNUM DESC)"
				 +" WHERE ROWNUM = 1";
		
		 String sql3 ="select svr_name," + 
				" to_char(s_date,'yy/mm/dd hh24:mi:ss') as sdate," + 
				" cpu1Load, cpu5Load, cpu15Load," + 
				" (" + 
				" select" + 
				" ifhcout - (" + 
				" select ifhcout from svrdata" + 
				" where s_date =" + 
				" (select min(s_date)" + 
				" from svrdata" + 
				" where s_date" + 
				" between to_date(to_char(sysdate,'yy/mm/dd')|| ' 00:00:00','yy/mm/dd hh24:mi:ss')" + 
				" and" + 
				" to_date(to_char(sysdate,'yy/mm/dd')||' 00:05:00','yy/mm/dd hh24:mi:ss'))) as ifhcout" + 
				" from" + 
				" svrdata" + 
				" where s_date = (select max(s_date) from svrdata)" + 
				" ) as ifhcout," + 
				" (" + 
				" select" + 
				" ifhcin - (" + 
				" select ifhcin from svrdata" + 
				" where s_date =" + 
				" (select min(s_date)" + 
				" from svrdata" + 
				" where s_date" + 
				" between to_date(to_char(sysdate,'yy/mm/dd')|| ' 00:00:00','yy/mm/dd hh24:mi:ss')" + 
				" and" + 
				" to_date(to_char(sysdate,'yy/mm/dd')||' 00:05:00','yy/mm/dd hh24:mi:ss'))) as ifhcin" + 
				" from" + 
				" svrdata" + 
				" where s_date = (select max(s_date) from svrdata)" + 
				" ) as ifhcin," + 
				" userCpu,sysCpu,idleCpu," + 
				" availSwap,totalRam,usedRam,freeRam,availDsik,usedDisk" + 
				" from svrdata join server" +
				"  on svrdata.svr_no = server.svr_no" +
				" where s_date = (select max(s_date) from svrdata)";
				
		
		
		
		
			
		//3.ResultSet
		ResultSet rs = stmt.executeQuery(sql3);  //select (조회) -> 결과집합
		
		ArrayList<SnmpData> list = new ArrayList<SnmpData>();
		while(rs.next()){
			SnmpData snmp = new SnmpData();
			snmp.setSvrNo(rs.getString("svr_name"));
			snmp.setCpu1Load(rs.getString("cpu1load"));
			snmp.setCpu5Load(rs.getString("cpu5load"));
			snmp.setCpu15Load(rs.getString("cpu15Load"));
			snmp.setUserCpu(rs.getString("userCpu"));
			snmp.setSysCpu(rs.getString("sysCpu"));
			snmp.setIdleCpu(rs.getString("idleCpu"));
			snmp.setAvailSwap(byteCalculation(rs.getString("availSwap")));
			snmp.setTotalRam(byteCalculation(rs.getString("totalRam")));
			snmp.setUsedRam(byteCalculation(rs.getString("usedRam")));
			snmp.setFreeRam(byteCalculation(rs.getString("freeRam")));
			snmp.setAvailDsik(byteCalculation(rs.getString("availDsik")));
			snmp.setUsedDisk(byteCalculation(rs.getString("usedDisk")));
			snmp.setIfHcin(rs.getDouble("ifHcin"));
			snmp.setIfHcout(rs.getDouble("ifHcout"));
			snmp.setSifHcin(byteCalc(Double.toString(rs.getDouble("ifHcin")))); // 화면을 뿌려줄려고 만듦
			snmp.setSifHcout(byteCalc(Double.toString(rs.getDouble("ifHcout"))));
			snmp.setSDate(rs.getString("sdate"));
			list.add(snmp);
		}
		
		//4.disconnect
		conn.close();
			
		return list;
	}
	
	
	
	
	
	public ArrayList<SnmpData> getDBList2() throws ClassNotFoundException, SQLException {
		//부서정보 조회
		//0.드라이버로딩
		Class.forName("oracle.jdbc.OracleDriver");// 해당 클래스를 메모리에 로딩
		 
		//1.connection
		String url ="jdbc:oracle:thin:@leehs.iptime.org:1521:orcl";
		Connection conn = DriverManager.getConnection(url,"lee","oracle");
		
		//2.Statement
		Statement stmt = conn.createStatement();
		
		
		String sql=" select svr_name,to_char(s_date,'yy/mm/dd hh24:mi:ss') sdate," + 
				" case" + 
				"    when (ifhcin - lead(ifhcin) over (order by s_date desc) ) >= 1024*1024*1024  then round((ifhcin - lead(ifhcin) over (order by s_date desc))/1024/1024/1024,2 )||' GB'" + 
				"    when (ifhcin - lead(ifhcin) over (order by s_date desc) ) >= 1024*1024  then round(((ifhcin - lead(ifhcin) over (order by s_date desc))/1024)/1024,2 ) ||' MB'" + 
				"    when (ifhcin - lead(ifhcin) over (order by s_date desc) ) >= 1024   then round((ifhcin - lead(ifhcin) over (order by s_date desc))/1024,2) ||' KB'" + 
				"    when (ifhcin - lead(ifhcin) over (order by s_date desc) ) < 1024   then round(ifhcin - lead(ifhcin) over (order by s_date desc),2) ||' B'" + 
				"    end as avrifhcin," + 
				"    case" + 
				"    when (ifhcout - lead(ifhcout) over (order by s_date desc) ) >= 1024*1024*1024  then round((ifhcout - lead(ifhcout) over (order by s_date desc))/1024/1024/1024,2 )||' GB'" + 
				"    when (ifhcout - lead(ifhcout) over (order by s_date desc) ) >= 1024*1024  then round(((ifhcout - lead(ifhcout) over (order by s_date desc))/1024)/1024,2 ) ||' MB'" + 
				"    when (ifhcout - lead(ifhcout) over (order by s_date desc) ) >= 1024   then round((ifhcout - lead(ifhcout) over (order by s_date desc))/1024,2) ||' KB'" + 
				"    when (ifhcout - lead(ifhcout) over (order by s_date desc) ) < 1024   then round(ifhcout - lead(ifhcout) over (order by s_date desc),2) ||' B'" + 
				"    end as avrifhcout" + 
				" from svrdata join server" + 
				" on svrdata.svr_no = server.svr_no"
				+ " where svr_name = 'oracle'";
				
			
		//3.ResultSet
		ResultSet rs = stmt.executeQuery(sql);  //select (조회) -> 결과집합
		
		ArrayList<SnmpData> list = new ArrayList<SnmpData>();
		while(rs.next()){
			SnmpData snmp = new SnmpData();
			snmp.setSvrNo(rs.getString("svr_name"));
			snmp.setRsifHcin(rs.getString("avrifhcin"));
			snmp.setRsifHcout(rs.getString("avrifhcout"));
			snmp.setRsDate(rs.getString("sdate"));
			list.add(snmp);
		}
		
		//4.disconnect
		conn.close();
			
		return list;
	}
	
	
	public ArrayList<SnmpData> getDBList() throws ClassNotFoundException, SQLException {
		//부서정보 조회
		//0.드라이버로딩
		Class.forName("oracle.jdbc.OracleDriver");// 해당 클래스를 메모리에 로딩
		 
		//1.connection
		String url ="jdbc:oracle:thin:@leehs.iptime.org:1521:orcl";
		Connection conn = DriverManager.getConnection(url,"lee","oracle");
		
		//2.Statement
		Statement stmt = conn.createStatement();
		String sql = "select svr_no,cpu1load,cpu5load,cpu15Load,userCpu,sysCpu,"
				+ " idleCpu,availSwap,totalRam,usedRam,freeRam,availDsik,"
				+ " usedDisk,ifHcin,ifHcout,s_date"
				+ " from svrdata"
				+ " order by s_date desc";
		
		String sql2="SELECT * FROM" +
				 " (SELECT * FROM svrdata ORDER BY ROWNUM DESC)"
				 +" WHERE ROWNUM = 1";		
		
		String sql3="select"
				+ " *"
				+ " from svrdata"
				+ " order by s_date desc";
		
		String sqlbyte="select svr_name,to_char(s_date,'yy/mm/dd hh24:mi:ss') as sdate,cpu1load,cpu5load,cpu15load," + 
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
				"  case\r\n" + 
				"        when useddisk >= 1024*1024  then round(useddisk/1024/1024,2) ||' GB'" + 
				"        when useddisk >= 1024 then round(useddisk/1024,2) ||' MB'" + 
				"        when useddisk < 1024 then round(useddisk,2) ||' KB'" + 
				" end as useddisk" + 
				" from svrdata , server" + 
				" where svrdata.svr_no = server.svr_no" +
				"  order by s_date desc";
					
			
		//3.ResultSet
		ResultSet rs = stmt.executeQuery(sqlbyte);  //select (조회) -> 결과집합
		
		ArrayList<SnmpData> list = new ArrayList<SnmpData>();
		while(rs.next()){
			SnmpData snmp = new SnmpData();
			snmp.setSvrNo(rs.getString("svr_name"));
			snmp.setCpu1Load(rs.getString("cpu1load"));
			snmp.setCpu5Load(rs.getString("cpu5load"));
			snmp.setCpu15Load(rs.getString("cpu15Load"));
			snmp.setUserCpu(rs.getString("userCpu"));
			snmp.setSysCpu(rs.getString("sysCpu"));
			snmp.setIdleCpu(rs.getString("idleCpu"));
			snmp.setAvailSwap(rs.getString("availSwap"));
			snmp.setTotalRam(rs.getString("totalRam"));
			snmp.setUsedRam(rs.getString("usedRam"));
			snmp.setFreeRam(rs.getString("freeRam"));
			snmp.setAvailDsik(rs.getString("availDsik"));
			snmp.setUsedDisk(rs.getString("usedDisk"));
			//snmp.setIfHcin(rs.getDouble("ifHcin"));
		//	snmp.setIfHcout(rs.getDouble("ifHcout"));
			snmp.setSifHcin(rs.getString("ifHcin")); // 화면을 뿌려줄려고 만듦
			snmp.setSifHcout(rs.getString("ifHcout"));
			snmp.setSDate(rs.getString("sdate"));
			list.add(snmp);
		}
		
		//4.disconnect
		conn.close();
			
		return list;
	}
	
	public boolean insertDBcctv(SnmpData data){
		boolean result = false;
		try {
			//0.�뱶�씪�씠踰꾨줈�뵫
			Class.forName("oracle.jdbc.OracleDriver");// �빐�떦 �겢�옒�뒪瑜� 硫붾え由ъ뿉 濡쒕뵫
			 
			//1.connection
			String url ="jdbc:oracle:thin:@localhost:1521:orcl";
			Connection conn = DriverManager.getConnection(url,"lee","oracle");
			
			//2.Statement
		
			
			String sql = "insert into svrdata(datano,ifhcin,ifhcout,s_date,svr_no)" + 
					" VALUES(TB_SVRDATA_SEQ.NEXTVAL,?,?,sysdate,2)";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, data.getSifHcin());
			pstmt.setString(2, data.getSifHcout());
			pstmt.executeUpdate();
			result= true;
		
		} catch (Exception e) {
			result = false;
			e.printStackTrace();
		}finally {
			//4. close
		}
		
		return result;
	}

	
	

	
	public boolean insertDB(SnmpData data){
		boolean result = false;
		try {
			//0.�뱶�씪�씠踰꾨줈�뵫
			Class.forName("oracle.jdbc.OracleDriver");// �빐�떦 �겢�옒�뒪瑜� 硫붾え由ъ뿉 濡쒕뵫
			 
			//1.connection
			String url ="jdbc:oracle:thin:@localhost:1521:orcl";
			Connection conn = DriverManager.getConnection(url,"lee","oracle");
			
			//2.Statement
			
			String sql = "INSERT INTO svrdata (datano, cpu1Load, cpu5Load, cpu15Load, userCpu,sysCpu,idleCpu,"
					+ " availSwap,totalRam,usedRam,freeRam,availDsik,usedDisk,"
					+ " ifHcin,ifHcout,s_date,svr_no) "+
					" VALUES(TB_SVRDATA_SEQ.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate,1)";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, data.cpu1Load);
			pstmt.setString(2, data.cpu5Load);
			pstmt.setString(3, data.cpu15Load);
			pstmt.setString(4, data.userCpu);
			pstmt.setString(5, data.sysCpu);
			pstmt.setString(6, data.idleCpu);
			pstmt.setString(7, data.availSwap);
			pstmt.setString(8, data.totalRam);
			pstmt.setString(9, data.usedRam);
			pstmt.setString(10, data.freeRam);
			pstmt.setString(11, data.availDsik);
			pstmt.setString(12, data.usedDisk);
			pstmt.setDouble(13, data.ifHcin);
			pstmt.setDouble(14, data.ifHcout); 
					
			pstmt.executeUpdate();
			result= true;
			
			
		} catch (Exception e) {
			result = false;
			e.printStackTrace();
		}finally {
			//4. close
		}
		
		return result;
	}

}
