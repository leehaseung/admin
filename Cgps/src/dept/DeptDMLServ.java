package dept;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

@WebServlet("/DeptDMLServ.do")
public class DeptDMLServ extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//���ڵ�
		response.setCharacterEncoding("utf-8");
		request.setCharacterEncoding("utf-8");
		
		//�Ķ���� -> beans
		//1. �Ķ���� �޾Ƽ� �� ���		
		DeptBeans bean = new DeptBeans();
		bean.setDepartmentId(request.getParameter("departmentId"));
		bean.setDepartmentName(request.getParameter("departmentName"));
		bean.setLocationId(request.getParameter("locationId"));
		bean.setManagerId(request.getParameter("managerId"));
		
		//����ó�� insert()   -> pk
		int r = 0;
		String oper = request.getParameter("oper");
		if(oper.equals("edit")) {
			r = DeptDAO.getInstance().update(bean);
		} else if(oper.equals("add")) {
			r = DeptDAO.getInstance().insert(bean);
		} else if(oper.equals("del")) {
			//r = DeptDAO.getInstance().delete(bean);
		}
		//��ϵ� pk ���� 
		PrintWriter out = response.getWriter();
		if( r == 1 )
			out.append(JSONArray.fromObject( bean ).toString());
		else 
			out.append("[]");
	}
}
