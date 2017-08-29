package board;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.commons.beanutils.BeanUtils;





/**
 * Servlet implementation class BoardServ
 */
@WebServlet("/board/BoardServ")
public class BoardServ extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BoardServ() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		//한글 받아올때
				request.setCharacterEncoding("utf-8");
				//응답결과 페이지 한글 인코딩
				response.setContentType("text/html;charset=utf-8");
				//<jsp:userBean
				Board board = new Board();
				//<jsp:setProperty
				try {
					if(request.getParameterMap() != null)
					
					BeanUtils.copyProperties(board, request.getParameterMap());
			
				} catch (IllegalAccessException | InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			//	BoardDAO bdao = new BoardDAO();
				BoardDAO bdao = BoardDAO.getInstance();
				
				// 컨트롤러 요청 action 코드값
				String action = request.getParameter("action");
				
				//내장 객체 얻어오기
				PrintWriter out = response.getWriter();
				//HttpSession session = request.getSession();
				
				if(action.equals("insert")) {
					try {
					board.setBoard_num(bdao.getSeq()); // 시퀀스값 가져와 세팅
					boolean result =  bdao.boardInsert(board);
					
					//결과페이지로 포워드
					if(result == true) {
						
						request.getRequestDispatcher("../board/BoardServ?action=list")
						.forward(request, response);
						
					}
				} catch (Exception e) {
		            e.printStackTrace();
		            System.out.println("글 작성 오류 : " + e.getMessage());
		        }
					


				
				}else if(action.equals("delete")){
					
					  // 글번호를 가져온다.
			        String num = request.getParameter("num");
			        int boardNum = Integer.parseInt(num);
			        
			        BoardDAO dao = BoardDAO.getInstance();
			        // 삭제할 글에 있는 파일 정보를 가져온다.
			        String fileName = dao.getFileName(boardNum);
			        // 글 삭제 - 답글이 있을경우 답글도 모두 삭제한다.
			        boolean result = dao.deleteBoard(boardNum);
			        
			        // 파일삭제 
			        if(fileName != null)
			        {
			            // 파일이 있는 폴더의 절대경로를 가져온다.
			            String folder = request.getServletContext().getRealPath("UploadFolder");
			            // 파일의 절대경로를 만든다.
			            String filePath = folder + "/" + fileName;
			 
			            File file = new File(filePath);
			            if(file.exists()) file.delete(); // 파일은 1개만 업로드 되므로 한번만 삭제하면 된다.
			        }
			        
			        if(result){
			        	  request.getRequestDispatcher("../board/BoardServ?action=list")
							.forward(request, response);
			        }
			       
					
					
				}else if(action.equals("reform")) {
					 BoardDAO dao = BoardDAO.getInstance();
				        int num = Integer.parseInt(request.getParameter("num"));
				        // 답글 작성 후 원래 페이지로 돌아가기 위해 페이지 번호가 필요하다.
				        String pageNum = request.getParameter("page");
				        
				        board = dao.getDetail(num);
				        request.setAttribute("board", board);
				        request.setAttribute("page", pageNum);
				        
				        //forward.setRedirect(false); // 단순한 조회이므로
				        request.getRequestDispatcher("../board/BoardReplyForm.tile")
						.forward(request, response);
					
					
				}
				else if(action.equals("reply")){
					  
					    request.setCharacterEncoding("utf-8");
				    
				        BoardDAO dao = BoardDAO.getInstance();
				        Board borderData = new Board();
				        
				        // 답글 작성 후 원래 페이지로 돌아가기 위해 페이지 번호가 필요하다.
				        String pageNum = request.getParameter("page");
				        
				        // 파리미터 값을 가져온다.
				        int num = Integer.parseInt(request.getParameter("board_num"));
				        String id = request.getParameter("board_id");
				        String subject = request.getParameter("board_subject");
				        String content = request.getParameter("board_content");
				        int ref = Integer.parseInt(request.getParameter("board_re_ref"));
				 
				        // 답글 저장
				        borderData.setBoard_num(dao.getSeq()); // 답글의 글번호는 시퀀스값 가져와 세팅
				        borderData.setBoard_id(id);
				        borderData.setBoard_subject(subject);
				        borderData.setBoard_content(content);
				        borderData.setBoard_re_ref(ref);
				        borderData.setBoard_parent(num);    // 부모글의 글번호를 저장
				        
				        boolean result = dao.boardInsert(borderData);
				        
				       
				            // 원래있던 페이지로 돌아가기 위해 페이지번호를 전달한다.
				            //forward.setNextPath("BoardListAction.bo?page="+pageNum); 
				            request.getRequestDispatcher("../board/BoardServ?action=list&page="+pageNum)
							.forward(request, response);
				            
				}else if(action.equals("detail")){
					
					try{
					// 파라미터로 넘어온 글번호를 가져온다.
			        String num = request.getParameter("num");
			       
			        int boardNum = Integer.parseInt(num);
			        
			        String pageNum = request.getParameter("pageNum");
			        
			        BoardDAO dao = BoardDAO.getInstance();
			         board = dao.getDetail(boardNum);
			        boolean result = dao.updateCount(boardNum);
			        
			        request.setAttribute("board", board);
			        request.setAttribute("pageNum", pageNum);
			        
			        if(result == true) {
						
						request.getRequestDispatcher("../board/board_Detail.tile")
						.forward(request, response);
						
					}
				} catch (Exception e) {
		            e.printStackTrace();
		            System.out.println("글 작성 오류 : " + e.getMessage());
		        }

					
				}else if(action.equals("list2")) {
				
					ArrayList<Board> list =bdao.getDBList();
					request.setAttribute("datas", list);
					//결과페이지로 포워드
					request.getRequestDispatcher("../board/board_list.tile")
					.forward(request, response);
				
					

				
				}else if(action.equals("list")) {
					
			        // 현재 페이지 번호 만들기
			        int spage = 1;
			        String page = request.getParameter("page");
			        
			        if(page != null)
			            spage = Integer.parseInt(page);
			        
			        // 검색조건과 검색내용을 가져온다.
			        String opt = request.getParameter("opt");
			        String condition = request.getParameter("condition");
			        
			        // 검색조건과 내용을 Map에 담는다.
			        HashMap<String, Object> listOpt = new HashMap<String, Object>();
			        listOpt.put("opt", opt);
			        listOpt.put("condition", condition);
			        listOpt.put("start", spage*10-9);
			        
			        BoardDAO dao = BoardDAO.getInstance();
			        int listCount = dao.getBoardListCount(listOpt);
			        ArrayList<Board> list =  dao.getBoardList(listOpt);
			        
			        // 한 화면에 10개의 게시글을 보여지게함
			        // 페이지 번호는 총 5개, 이후로는 [다음]으로 표시
			        
			        // 전체 페이지 수
			        int maxPage = (int)(listCount/10.0 + 0.9);
			        //시작 페이지 번호
			        int startPage = (int)(spage/5.0 + 0.8) * 5 - 4;
			        //마지막 페이지 번호
			        int endPage = startPage + 4;
			        if(endPage > maxPage)    endPage = maxPage;
			        
			        // 4개 페이지번호 저장
			        request.setAttribute("spage", spage);
			        request.setAttribute("maxPage", maxPage);
			        request.setAttribute("startPage", startPage);
			        request.setAttribute("endPage", endPage);
			        
			        // 글의 총 수와 글목록 저장
			        //request.setAttribute("listCount", listCount);
			        request.setAttribute("datas", list);
			        
			      
					request.getRequestDispatcher("../board/board_list.tile")
					.forward(request, response);
			    
					
					
					
				}else {
						out.println("<script>alert('아이디나 비밀번호가 틀렸습니다.!!');history.go(-1);</script>");
				}
		} 
			
		
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
