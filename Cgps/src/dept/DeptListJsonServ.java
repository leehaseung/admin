package dept;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONValue;

@WebServlet("/DeptListJsonServ.do")
public class DeptListJsonServ extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//�μ� ��� ��ȸ��� JSON ���� �ѱ�
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		
		String page = "1";
		if(request.getParameter("page")!=null) {
			page = request.getParameter("page");
		}
		int pageNo = Integer.parseInt(page);
		int first = (pageNo - 1) * 10 + 1;
		int last = first + 10 - 1;
		System.out.println(last);
		List<Map<String, Object>> list = 
				DeptDAO.getInstance().selectPage(first, last);

		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("page", page);     //���� ������ ��ȣ
		map.put("total", "5");     //��������   
		map.put("records", "50");  //��ü ���ڵ� �Ǽ�
		map.put("rows", list );    //���
		out.append(JSONValue.toJSONString(map));
		

	}
}
