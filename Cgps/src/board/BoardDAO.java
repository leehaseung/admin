package board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;


import util.JDBCutil;

public class BoardDAO {
	 private Connection conn;
	    private PreparedStatement pstmt;
	    private ResultSet rs;
	    
	    private static BoardDAO instance;
	    
	    BoardDAO(){}
	    public static BoardDAO getInstance(){
	        if(instance==null)
	            instance=new BoardDAO();
	        return instance;
	    }
	    
	    public String getFileName(int boardNum)
	    {
	        String fileName = null;
	        
	        try {
	        	conn = JDBCutil.connect();
	            
	            StringBuffer sql = new StringBuffer();
	            sql.append("SELECT BOARD_FILE from MEMBER_BOARD where BOARD_NUM=?");
	            
	            pstmt = conn.prepareStatement(sql.toString());
	            pstmt.setInt(1, boardNum);
	            
	            rs = pstmt.executeQuery();
	            if(rs.next()) fileName = rs.getString("BOARD_FILE");
	            
	        } catch (Exception e) {
	            throw new RuntimeException(e.getMessage());
	        }
	        
	        close();
	        return fileName;
	    } // end getFileName

	    
	 // 게시글 삭제
	    public boolean deleteBoard(int boardNum) 
	    {
	        boolean result = false;
	 
	        try {
	        	conn = JDBCutil.connect();
	            conn.setAutoCommit(false); // 자동 커밋을 false로 한다.
	 
	            StringBuffer sql = new StringBuffer();
	            sql.append("DELETE FROM MEMBER_BOARD");
	            sql.append(" WHERE BOARD_NUM IN");
	            sql.append(" (SELECT BOARD_NUM");
	            sql.append(" FROM MEMBER_BOARD");
	            sql.append(" START WITH BOARD_NUM = ?");
	            sql.append(" CONNECT BY PRIOR BOARD_NUM = BOARD_PARENT) ");
	            
	            pstmt = conn.prepareStatement(sql.toString());
	            pstmt.setInt(1, boardNum);
	            
	            int flag = pstmt.executeUpdate();
	            if(flag > 0){
	                result = true;
	                conn.commit(); // 완료시 커밋
	            }    
	            
	        } catch (Exception e) {
	            try {
	                conn.rollback(); // 오류시 롤백
	            } catch (SQLException sqle) {
	                sqle.printStackTrace();
	            }
	            throw new RuntimeException(e.getMessage());
	        }
	 
	        close();
	        return result;
	    } // end deleteBoard


		
	    //게시판 조
	    public ArrayList<Board> getDBList() {
			
			conn = JDBCutil.connect();
			
			ArrayList<Board> datas = new ArrayList<Board>();
			
			String sql = "select * from member_board";
			try {
				pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery();
				while(rs.next()) {
					Board board = new Board();
					
					board.setBoard_num(rs.getInt("board_num"));
					board.setBoard_id(rs.getString("board_id"));
					board.setBoard_subject(rs.getString("Board_subject"));
					board.setBoard_content(rs.getString("board_content"));
					board.setBoard_file(rs.getString("board_file"));
					board.setBoard_re_ref(rs.getInt("board_re_ref"));
					board.setBoard_re_lev(rs.getInt("board_re_lev"));
					board.setBoard_re_seq(rs.getInt("board_re_seq"));
					board.setBoard_count(rs.getInt("board_count"));
					board.setBoard_date(rs.getDate("board_date"));
					datas.add(board);
						
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
	    
	    // 시퀀스를 가져온다.
	    public int getSeq()
	    {
	        int result = 1;
	        
	        try {
	        	conn = JDBCutil.connect();
	            
	            // 시퀀스 값을 가져온다. (DUAL : 시퀀스 값을 가져오기위한 임시 테이블)
	            StringBuffer sql = new StringBuffer();
	            sql.append("SELECT BOARD_NUM.NEXTVAL FROM DUAL");
	            
	            pstmt = conn.prepareStatement(sql.toString());
	            // 쿼리 실행
	            rs = pstmt.executeQuery();
	            
	            if(rs.next())    result = rs.getInt(1);
	 
	        } catch (Exception e) {
	            throw new RuntimeException(e.getMessage());
	        }
	        
	        close();
	        return result;    
	    } // end getSeq
	    
	    // 글 삽입
	    public boolean boardInsert(Board board)
	    {
	        boolean result = false;
	        
	        try {
	        	conn = JDBCutil.connect();
	            
	        	 // 자동 커밋을 false로 한다.
	           // conn.setAutoCommit(false);
	            
	            StringBuffer sql = new StringBuffer();
	            sql.append("INSERT INTO MEMBER_BOARD");
	            sql.append("(BOARD_NUM, BOARD_ID, BOARD_SUBJECT, BOARD_CONTENT, BOARD_FILE");
	            sql.append(", BOARD_RE_REF, BOARD_COUNT, BOARD_DATE, BOARD_PARENT)");
	            sql.append(" VALUES(?,?,?,?,?,?,?,sysdate,?)");
	            
	            int num = board.getBoard_num();            // 글번호(시퀀스 값)
	            int ref = board.getBoard_re_ref();         // 그룹번호
	            int parent = board.getBoard_parent();     // 부모글번호
	            
	            // 부모글일 경우 그룹번호와 글번호 동일
	            if(parent == 0) ref = num;
	            
	            pstmt = conn.prepareStatement(sql.toString());
	            pstmt.setInt(1, num);
	            pstmt.setString(2, board.getBoard_id());
	            pstmt.setString(3, board.getBoard_subject());
	            pstmt.setString(4, board.getBoard_content());
	            pstmt.setString(5, board.getBoard_file());
	            pstmt.setInt(6, ref);
	            pstmt.setInt(7, board.getBoard_count());
	            pstmt.setInt(8, parent);


	            
	            int flag = pstmt.executeUpdate();
	            if(flag > 0){
	               result = true;
	               conn.commit(); 
	            }

	        } catch (Exception e) {
	            try {
	                conn.rollback();
	            } catch (SQLException sqle) {
	                sqle.printStackTrace();
	            } 
	            e.printStackTrace();
	            throw new RuntimeException(e.getMessage());
	        }

	        close();
	        return result;    
	    } // end boardInsert();
	    
	    // 글목록 가져오기
	    public ArrayList<Board> getBoardList(HashMap<String, Object> listOpt)
	    {
	        ArrayList<Board> list = new ArrayList<Board>();
	        
	        String opt = (String)listOpt.get("opt"); // 검색옵션(제목, 내용, 글쓴이 등..)
	        String condition = (String)listOpt.get("condition"); // 검색내용
	        int start = (Integer)listOpt.get("start"); // 현재 페이지번호
	        
	        try {
	        		conn = JDBCutil.connect();
	            StringBuffer sql = new StringBuffer();
	            
	            // 글목록 전체를 보여줄 때
	            if(opt == null)
	            {
	            	/*
	                 * SELECT * FROM
	                 *            (SELECT  ROWNUM AS rnum,
	                 *                   data.*
	                 *             FROM
	                 *                   (SELECT LEVEL,
	                 *                              BOARD_NUM,
	                 *                             BOARD_ID,
	                 *                            BOARD_SUBJECT,
	                 *                             BOARD_CONTENT,
	                 *                             BOARD_FILE,
	                 *                             BOARD_COUNT,
	                 *                             BOARD_RE_REF,
	                 *                             BOARD_PARENT,
	                 *                             BOARD_DATE
	                 *                    FROM MEMBER_BOARD
	                 *                   START WITH BOARD_PARENT = 0
	                 *                   CONNECT BY PRIOR BOARD_NUM = BOARD_PARENT
	                 *                   ORDER SIBLINGS BY BOARD_RE_REF desc) 
	                 *             data)
	                  * WHERE rnum>=? and rnum<=? ;
	                 */
	                
	                sql.append("SELECT * FROM");
	                sql.append(" (SELECT  ROWNUM AS rnum, data.* FROM ");
	                sql.append("    (SELECT LEVEL, BOARD_NUM, BOARD_ID,    BOARD_SUBJECT,");
	                sql.append("            BOARD_CONTENT, BOARD_FILE,BOARD_COUNT,");
	                sql.append("            BOARD_RE_REF, BOARD_PARENT, BOARD_DATE");
	                sql.append("    FROM MEMBER_BOARD");
	                sql.append("    START WITH BOARD_PARENT = 0");
	                sql.append("    CONNECT BY PRIOR BOARD_NUM = BOARD_PARENT");
	                sql.append("    ORDER SIBLINGS BY BOARD_RE_REF desc)");              
	                sql.append(" data) ");
	                sql.append("WHERE rnum>=? and rnum<=?");

	                pstmt = conn.prepareStatement(sql.toString());
	                pstmt.setInt(1, start);
	                pstmt.setInt(2, start+9);
	                
	                // StringBuffer를 비운다.
	                sql.delete(0, sql.toString().length());
	            }
	            else if(opt.equals("0")) // 제목으로 검색
	            {
	            	sql.append("SELECT * FROM");
	                sql.append(" (SELECT  ROWNUM AS rnum, data.* FROM ");
	                sql.append("    (SELECT LEVEL, BOARD_NUM, BOARD_ID,    BOARD_SUBJECT,");
	                sql.append("            BOARD_CONTENT, BOARD_FILE,BOARD_COUNT,");
	                sql.append("            BOARD_RE_REF, BOARD_PARENT, BOARD_DATE");
	                sql.append("    FROM MEMBER_BOARD");
	                sql.append("     WHERE BOARD_SUBJECT like ?");
	                sql.append("    START WITH BOARD_PARENT = 0");
	                sql.append("    CONNECT BY PRIOR BOARD_NUM = BOARD_PARENT");
	                sql.append("    ORDER SIBLINGS BY BOARD_RE_REF desc)");              
	                sql.append(" data) ");
	                sql.append("WHERE rnum>=? and rnum<=?");

	                
	                pstmt = conn.prepareStatement(sql.toString());
	                pstmt.setString(1, "%"+condition+"%");
	                pstmt.setInt(2, start);
	                pstmt.setInt(3, start+9);
	                
	                sql.delete(0, sql.toString().length());
	            }
	            else if(opt.equals("1")) // 내용으로 검색
	            {
	            	sql.append("SELECT * FROM");
	                sql.append(" (SELECT  ROWNUM AS rnum, data.* FROM ");
	                sql.append("    (SELECT LEVEL, BOARD_NUM, BOARD_ID,    BOARD_SUBJECT,");
	                sql.append("            BOARD_CONTENT, BOARD_FILE,BOARD_COUNT,");
	                sql.append("            BOARD_RE_REF, BOARD_PARENT, BOARD_DATE");
	                sql.append("    FROM MEMBER_BOARD");
	                sql.append("     WHERE BOARD_CONTENT like ?");
	                sql.append("    START WITH BOARD_PARENT = 0");
	                sql.append("    CONNECT BY PRIOR BOARD_NUM = BOARD_PARENT");
	                sql.append("    ORDER SIBLINGS BY BOARD_RE_REF desc)");              
	                sql.append(" data) ");
	                sql.append("WHERE rnum>=? and rnum<=?");

	                pstmt = conn.prepareStatement(sql.toString());
	                pstmt.setString(1, "%"+condition+"%");
	                pstmt.setInt(2, start);
	                pstmt.setInt(3, start+9);
	                
	                sql.delete(0, sql.toString().length());
	            }
	            else if(opt.equals("2")) // 제목+내용으로 검색
	            {
	                sql.append("SELECT * FROM");
	                sql.append(" (SELECT  ROWNUM AS rnum, data.* FROM ");
	                sql.append("    (SELECT LEVEL, BOARD_NUM, BOARD_ID,    BOARD_SUBJECT,");
	                sql.append("            BOARD_CONTENT, BOARD_FILE,BOARD_COUNT,");
	                sql.append("            BOARD_RE_REF, BOARD_PARENT, BOARD_DATE");
	                sql.append("    FROM MEMBER_BOARD");
	                sql.append("     WHERE BOARD_SUBJECT like ?");
	                sql.append("     OR BOARD_CONTENT like ?");
	                sql.append("    START WITH BOARD_PARENT = 0");
	                sql.append("    CONNECT BY PRIOR BOARD_NUM = BOARD_PARENT");
	                sql.append("    ORDER SIBLINGS BY BOARD_RE_REF desc)");              
	                sql.append(" data) ");
	                sql.append("WHERE rnum>=? and rnum<=?");
	                
	                pstmt = conn.prepareStatement(sql.toString());
	                pstmt.setString(1, "%"+condition+"%");
	                pstmt.setString(2, "%"+condition+"%");
	                pstmt.setInt(3, start);
	                pstmt.setInt(4, start+9);
	                
	                sql.delete(0, sql.toString().length());
	            }
	            else if(opt.equals("3")) // 글쓴이로 검색
	            {
	            	sql.append("SELECT * FROM");
	                sql.append(" (SELECT  ROWNUM AS rnum, data.* FROM ");
	                sql.append("    (SELECT LEVEL, BOARD_NUM, BOARD_ID,    BOARD_SUBJECT,");
	                sql.append("            BOARD_CONTENT, BOARD_FILE,BOARD_COUNT,");
	                sql.append("            BOARD_RE_REF, BOARD_PARENT, BOARD_DATE");
	                sql.append("    FROM MEMBER_BOARD");
	                sql.append("     WHERE BOARD_ID like ?");
	                sql.append("    START WITH BOARD_PARENT = 0");
	                sql.append("    CONNECT BY PRIOR BOARD_NUM = BOARD_PARENT");
	                sql.append("    ORDER SIBLINGS BY BOARD_RE_REF desc)");              
	                sql.append(" data) ");
	                sql.append("WHERE rnum>=? and rnum<=?");
	                
	                pstmt = conn.prepareStatement(sql.toString());
	                pstmt.setString(1, "%"+condition+"%");
	                pstmt.setInt(2, start);
	                pstmt.setInt(3, start+9);
	                
	                sql.delete(0, sql.toString().length());
	            }
	            
	            rs = pstmt.executeQuery();
	            while(rs.next())
	            {
	             	 Board board = new Board();
	                 board.setBoard_re_lev(rs.getInt("LEVEL"));
	                 board.setBoard_num(rs.getInt("BOARD_NUM"));
	                 board.setBoard_id(rs.getString("BOARD_ID"));
	                 board.setBoard_subject(rs.getString("BOARD_SUBJECT"));
	                 board.setBoard_content(rs.getString("BOARD_CONTENT"));
	                 board.setBoard_file(rs.getString("BOARD_FILE"));
	                 board.setBoard_count(rs.getInt("BOARD_COUNT"));
	                 board.setBoard_re_ref(rs.getInt("BOARD_RE_REF"));
	                 board.setBoard_parent(rs.getInt("BOARD_PARENT"));
	                 board.setBoard_date(rs.getDate("BOARD_DATE"));
	                 list.add(board);

	            }
	            
	        } catch (Exception e) {
	          	e.printStackTrace();
	            throw new RuntimeException(e.getMessage());
	        }
	        
	        close();
	        return list;
	    } // end getBoardList

	    // 글의 개수를 가져오는 메서드
	    public int getBoardListCount(HashMap<String, Object> listOpt)
	    {
	        int result = 0;
	        String opt = (String)listOpt.get("opt"); // 검색옵션(제목, 내용, 글쓴이 등..)
	        String condition = (String)listOpt.get("condition"); // 검색내용
	        
	        try {
	        		conn = JDBCutil.connect();
	            StringBuffer sql = new StringBuffer();
	            
	            if(opt == null)    // 전체글의 개수
	            {
	                sql.append("select count(*) from MEMBER_BOARD");
	                pstmt = conn.prepareStatement(sql.toString());
	                
	                // StringBuffer를 비운다.
	                sql.delete(0, sql.toString().length());
	            }
	            else if(opt.equals("0")) // 제목으로 검색한 글의 개수
	            {
	                sql.append("select count(*) from MEMBER_BOARD where BOARD_SUBJECT like ?");
	                pstmt = conn.prepareStatement(sql.toString());
	                pstmt.setString(1, '%'+condition+'%');
	                
	                sql.delete(0, sql.toString().length());
	            }
	            else if(opt.equals("1")) // 내용으로 검색한 글의 개수
	            {
	                sql.append("select count(*) from MEMBER_BOARD where BOARD_CONTENT like ?");
	                pstmt = conn.prepareStatement(sql.toString());
	                pstmt.setString(1, '%'+condition+'%');
	                
	                sql.delete(0, sql.toString().length());
	            }
	            else if(opt.equals("2")) // 제목+내용으로 검색한 글의 개수
	            {
	                sql.append("select count(*) from MEMBER_BOARD ");
	                sql.append("where BOARD_SUBJECT like ? or BOARD_CONTENT like ?");
	                pstmt = conn.prepareStatement(sql.toString());
	                pstmt.setString(1, '%'+condition+'%');
	                pstmt.setString(2, '%'+condition+'%');
	                
	                sql.delete(0, sql.toString().length());
	            }
	            else if(opt.equals("3")) // 글쓴이로 검색한 글의 개수
	            {
	                sql.append("select count(*) from MEMBER_BOARD where BOARD_ID like ?");
	                pstmt = conn.prepareStatement(sql.toString());
	                pstmt.setString(1, '%'+condition+'%');
	                
	                sql.delete(0, sql.toString().length());
	            }
	            
	            rs = pstmt.executeQuery();
	            if(rs.next())    result = rs.getInt(1);
	            
	        } catch (Exception e) {
	            throw new RuntimeException(e.getMessage());
	        }
	        
	        close();
	        return result;
	    } // end getBoardListCount
	 
	    
	    // 상세보기
	    public Board getDetail(int boardNum)
	    {    
	        Board board = null;
	        
	        try {
	        	conn = JDBCutil.connect();
	            
	            StringBuffer sql = new StringBuffer();
	            sql.append("select * from MEMBER_BOARD where BOARD_NUM = ?");
	            
	            pstmt = conn.prepareStatement(sql.toString());
	            pstmt.setInt(1, boardNum);
	            
	            rs = pstmt.executeQuery();
	            if(rs.next())
	            {
	                board = new Board();
	                board.setBoard_num(boardNum);
	                board.setBoard_id(rs.getString("BOARD_ID"));
	                board.setBoard_subject(rs.getString("BOARD_SUBJECT"));
	                board.setBoard_content(rs.getString("BOARD_CONTENT"));
	                board.setBoard_file(rs.getString("BOARD_FILE"));
	                board.setBoard_count(rs.getInt("BOARD_COUNT"));
	                board.setBoard_re_ref(rs.getInt("BOARD_RE_REF"));
	                board.setBoard_date(rs.getDate("BOARD_DATE"));
	                board.setBoard_parent(rs.getInt("BOARD_PARENT"));

	            }
	            
	        } catch (Exception e) {
	            throw new RuntimeException(e.getMessage());
	        }
	        
	        close();
	        return board;
	    } // end getDetail()
	    
	    // 조회수 증가
	    public boolean updateCount(int boardNum)
	    {
	        boolean result = false;
	        
	        try {
	        	conn = JDBCutil.connect();
	        	// 자동 커밋을 false로 한다.
	            conn.setAutoCommit(false);

	            
	            StringBuffer sql = new StringBuffer();
	            sql.append("update MEMBER_BOARD set BOARD_COUNT = BOARD_COUNT+1 ");
	            sql.append("where BOARD_NUM = ?");
	            
	            pstmt = conn.prepareStatement(sql.toString());
	            pstmt.setInt(1, boardNum);
	            
	            int flag = pstmt.executeUpdate();
	            if(flag > 0){
	                result = true;
	                conn.commit(); // 완료시 커밋
	            }    
	        } catch (Exception e) {
	            try {
	                conn.rollback(); // 오류시 롤백
	            } catch (SQLException sqle) {
	                sqle.printStackTrace();
	            }
	            throw new RuntimeException(e.getMessage());
	        }
	        
	        close();
	        return result;
	    } // end updateCount
	    
	    
	    public boolean updateReSeq(Board board)
	    {
	        boolean result = false;
	        
	        int ref = board.getBoard_re_ref();     // 원본글 번호(그룹번호)
	        int seq = board.getBoard_re_seq();     // 답변글의 순서
	        
	        try {    
	            StringBuffer sql = new StringBuffer();
	            
	            conn = JDBCutil.connect();
	            conn.setAutoCommit(false); // 자동 커밋을 false로 한다.
	            
	            // ref(그룹번호)와 seq(답글순서)을 확인하여 원본 글에 다른 답변 글이 있으면, 
	            // 답변 글 중 답변 글보다 상위에 있는 글의 seq보다 높은 글의 seq값을 1씩 증가시킨다.
	            sql.append("update MEMBER_BOARD set BOARD_RE_SEQ = BOARD_RE_SEQ+1 ");
	            sql.append("where BOARD_RE_REF = ? and BOARD_RE_SEQ > ?");
	            
	            pstmt = conn.prepareStatement(sql.toString());
	            pstmt.setInt(1, ref);
	            pstmt.setInt(2, seq);
	            
	            int flag = pstmt.executeUpdate();
	            if(flag > 0){
	                result = true;
	                conn.commit(); // 완료시 커밋
	            }
	            
	        } catch (Exception e) {
	            try {
	                conn.rollback(); // 오류시 롤백
	            } catch (SQLException sqle) {
	                sqle.printStackTrace();
	            }
	            throw new RuntimeException(e.getMessage());
	        }
	        
	        close();
	        return result;
	    }



	    
	    // DB 자원해제
	    private void close()
	    {
	        try {
	            if ( pstmt != null ){ pstmt.close(); pstmt=null; }
	            if ( conn != null ){ conn.close(); conn=null;    }
	        } catch (Exception e) {
	            throw new RuntimeException(e.getMessage());
	        }
	    } // end close()

}
