package member;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


import util.JDBCutil;


public class MemberDAO {

	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs;
/*	
	 private static MemberDAO instance;
	 
	   MemberDAO(){}
	    public static MemberDAO getInstance(){
	        if(instance==null)
	            instance=new MemberDAO();
	        return instance;
	    }
	*/
	    /**
	     * 아이디 중복체크를 한다.
	     * @param id 아이디
	     * @return x : 아이디 중복여부 확인값
	     */
	    public boolean duplicateIdCheck(String id)
	    {
	        
	        boolean x= false;
	        
	        try {
	            // 쿼리
	            StringBuffer query = new StringBuffer();
	            query.append("SELECT mb_id FROM MEMBER WHERE mb_id=?");
	                        
	            conn = JDBCutil.connect();
	            pstmt = conn.prepareStatement(query.toString());
	            pstmt.setString(1, id);
	            rs = pstmt.executeQuery();
	            
	            if(rs.next()) x= true; //해당 아이디 존재
	            
	            return x;
	            
	        } catch (Exception sqle) {
	            throw new RuntimeException(sqle.getMessage());
	        } finally {
	            try{
	                if ( pstmt != null ){ pstmt.close(); pstmt=null; }
	                if ( conn != null ){ conn.close(); conn=null;    }
	            }catch(Exception e){
	                throw new RuntimeException(e.getMessage());
	            }
	        }
	    } // end duplicateIdCheck()

	
	public boolean login(String mbId, String mbPass) {
		conn = JDBCutil.connect();
		
		String sql = "select mb_id, mb_password,mb_admin from member where mb_id = ?";
		boolean result = false;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, mbId);
			rs = pstmt.executeQuery();
			rs.next();
			if(rs.getString("mb_password").equals(mbPass))
				result=true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		finally {
			try {
				pstmt.close();
				JDBCutil.disconnect(pstmt, conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public ArrayList<Member> getDBList() {
		
		conn = JDBCutil.connect();
		
		ArrayList<Member> datas = new ArrayList<Member>();
		
		String sql = "select * from member";
		try {
			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				Member member = new Member();
				
				member.setMbId(rs.getString("mb_id"));
				member.setMbPass(rs.getString("mb_password"));
				member.setMbName(rs.getString("mb_name"));
				member.setMbBirth(rs.getString("mb_birth"));
				member.setMbAddress(rs.getString("mb_address"));
				member.setMbEmail(rs.getString("mb_email"));
				member.setMbPhone(rs.getString("mb_phone"));
				member.setMbRegdate(rs.getString("mb_regdate"));
				member.setMbAdmin(rs.getString("mb_admin"));
				member.setMbSex(rs.getString("mb_sex"));
				datas.add(member);
					
			}
			rs.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			JDBCutil.disconnect(pstmt, conn);
		}
		return datas;
	}

	
	public boolean insertDB(Member member) {
		
		conn = JDBCutil.connect();
		
		// sql 문자열 , gb_id 는 자동 등록 되므로 입력하지 않는다.
				
		String sql ="insert into member(mb_id,mb_password,mb_name,mb_birth,mb_address,"
				+ " mb_email,mb_phone,mb_regdate,mb_admin,mb_sex)"
				+ " values(?,?,?,?,?,?,?,sysdate,?,?)";
		
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,member.getMbId());
			pstmt.setString(2,member.getMbPass());
			pstmt.setString(3,member.getMbName());
			pstmt.setString(4,member.getMbBirth());
			pstmt.setString(5,member.getMbAddress());
			pstmt.setString(6,member.getMbEmail());
			pstmt.setString(7,member.getMbPhone());
			pstmt.setString(8,member.getMbAdmin());
			pstmt.setString(9,member.getMbSex());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		finally {
			JDBCutil.disconnect(pstmt, conn);
		}
		return true;
	}
	
	// 특정 주소록 게시글 가져오는 메서드
		public Member getDB(String mbId) throws SQLException {
		
			conn = JDBCutil.connect();
			
			String sql = "select * from member where mb_id=?";
			Member member = new Member();
			
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1,mbId);
				ResultSet rs = pstmt.executeQuery();
				
				// 데이터가 하나만 있으므로 rs.next()를 한번만 실행 한다.
				if(rs.next()){
					member.setMbId(rs.getString("mb_id"));
					member.setMbPass(rs.getString("mb_password"));
					member.setMbName(rs.getString("mb_name"));
					member.setMbBirth(rs.getString("mb_birth"));
					member.setMbAddress(rs.getString("mb_address"));
					member.setMbEmail(rs.getString("mb_email"));
					member.setMbPhone(rs.getString("mb_phone"));
					member.setMbRegdate(rs.getString("mb_regdate"));
					member.setMbAdmin(rs.getString("mb_admin"));
					member.setMbSex(rs.getString("mb_sex"));
					}
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			finally {
				
				JDBCutil.disconnect(pstmt, conn);
			}
			return member;
		}

	// 특정 주소록 게시글 가져오는 메서드
	public Member getAdmin(String mbId) throws SQLException {
	
		conn = JDBCutil.connect();
		String sql = "select * from member where mb_id=?";
		Member member = new Member();
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,mbId);
			ResultSet rs = pstmt.executeQuery();
			
			// 데이터가 하나만 있으므로 rs.next()를 한번만 실행 한다.
			if(rs.next()){
			/*member.setId(rs.getString("id"));
			member.setName(rs.getString("name"));
			member.setPasswd(rs.getString("passwd"));
			member.setEmail(rs.getString("email"));
			member.setRegDate(rs.getString("reg_date"));*/
			member.setMbAdmin(rs.getString("mb_admin"));
				}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			pstmt.close();
			JDBCutil.disconnect(pstmt, conn);
		}
		return member;
	}

}
