package dept;

import java.io.IOException;
import java.math.BigDecimal;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DeptDeleteAjaxServ
 */
@WebServlet("/DeptDeleteAjaxServ.do")
public class DeptDeleteAjaxServ extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1. �μ���ȣ �Ķ���� �ޱ�
		String departmentId = request.getParameter("departmentId");
		//2. �����Ͻ� ����ó��(����ó��)
		int r = DeptDAO.getInstance().delete(new BigDecimal(departmentId));
		//3. ������� ���� (String)
		String result = "";
		if (r == 1) {
			result = "true," + departmentId;
		}else {
			result = "false," + departmentId;
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
