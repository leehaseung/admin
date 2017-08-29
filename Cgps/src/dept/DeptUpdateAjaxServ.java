package dept;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DeptUpdateAjaxServ
 */
@WebServlet("/DeptUpdateAjaxServ.do")
public class DeptUpdateAjaxServ extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1. �Ķ���� �޾Ƽ� �� ���
		DeptBeans bean = new DeptBeans();
		bean.setDepartmentId(request.getParameter("departmentId"));
		bean.setDepartmentName(request.getParameter("departmentName"));
		//2. ���� ���� ó��
		int r = DeptDAO.getInstance().update(bean);
		//3. ��� ����
		String result = "";
		if ( r == 1 ) {
			result = "true||" + bean.getDepartmentId() 
			            +"||" + bean.getDepartmentName(); 
		}else {
				
			result = "false";
		}
		response.getWriter().append(result);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
