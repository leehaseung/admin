package member;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.apache.commons.beanutils.BeanUtils;



/**
 * Servlet implementation class MemberServ
 */
@WebServlet("/member/MemberServ")
public class MemberServ extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MemberServ() {
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
		Member member= new Member();
		//<jsp:setProperty
		try {
			if(request.getParameterMap() != null)
			BeanUtils.copyProperties(member, request.getParameterMap());
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		MemberDAO mdao= new MemberDAO();
		
		// 컨트롤러 요청 action 코드값
		String action = request.getParameter("action");
		
		//내장 객체 얻어오기
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		
		//System.out.println("=======",action);
		// 신규 회원등록
		if(action.equals("new")) {
			if(mdao.insertDB(member)) {
				out.println("<script>alert('정상적으로 등록 되었습니다. 로그인 하세요!!');opener.window.location.reload();window.close();</script>");
				response.sendRedirect("../main/main.tile");
			}
			else
				out.println("<script>alert('같은 아이디가 존재 합니다!!');history.go(-1);</script>");
			
			
		}else if(action.equals("idcheck")){
			 String id = request.getParameter("mbId");
		       
			  MemberDAO dao = new MemberDAO();
		        
		        boolean result = dao.duplicateIdCheck(id);
		        
		        response.setContentType("text/html;charset=euc-kr");
		      //  PrintWriter out = response.getWriter();
		 
		        if(result)    out.println("0"); // 아이디 중복
		        else        out.println("1");
		        
		        out.close();
		      

			
		}else if(action.equals("list2")){
			
			Member mb = null;
			try {
				mb = mdao.getDB(request.getParameter("mbId"));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			request.setAttribute("ab", mb);
			//결과페이지로 포워드
			request.getRequestDispatcher("listTest2.jsp")
			.forward(request, response);
			
			
		}else if(action.equals("list")){
			ArrayList<Member> list =mdao.getDBList();
			request.setAttribute("datas", list);
			//결과페이지로 포워드
			request.getRequestDispatcher("listTest.jsp")
			.forward(request, response);
		
			
		}else if(action.equals("login")) { //로그인
			if(mdao.login(member.getMbId(),member.getMbPass())) {
				System.out.println("=======");
				// 로그인 성공시 세션에 "userid" 저장
				session.setAttribute("mbId",member.getMbId());
				//관리자 체크
				try {
				member = mdao.getAdmin(member.getMbId());
				//System.out.println("================================="+member.getMbAdmin());
				if(member.getMbAdmin() !=null && member.getMbAdmin().equals("y")){
					session.setAttribute("mbAdmin",member.getMbAdmin());
				}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				
				response.sendRedirect("../main/main.tile");
			}
			else {
				out.println("<script>alert('아이디나 비밀번호가 틀렸습니다.!!');history.go(-1);</script>");
			}
		} 
		// 로그아웃
		else if(action.equals("logout")) {
			// 세션에 저장된 값 초기화
			session.removeAttribute("mbId");
			//session.removeAttribute("suserid");
			session.removeAttribute("mbAdmin");
			response.sendRedirect("../main/main.tile");		
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
