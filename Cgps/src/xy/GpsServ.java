package xy;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class GpsServ
 */
@WebServlet("/GpsServ")
public class GpsServ extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/xml; charset=utf-8");
		//response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String cmd = request.getParameter("cmd");
		if (cmd == null) {
			StringBuffer sb = new StringBuffer();
			sb.append("<result>");
			sb.append("<code>error</code>");
			sb.append("<data><![CDATA[ ");
			sb.append("cmd null");
			sb.append("]]></data>");
			sb.append("</result>");
			out.print(sb.toString());
		} else if (cmd.equals("find")) { // 전체조회
			try {
				String starttime = request.getParameter("starttime");
				String endtime =  request.getParameter("endtime");
				System.out.println(starttime);
				List<HashMap<String, Object>> list = XyDAO.getInstance().find(starttime,endtime);
				out.print(selectAll(list));
			} catch (Exception e) {
				StringBuffer sb = new StringBuffer();

				sb.append("<result>");
				sb.append("<code>error</code>");
				sb.append("<data><![CDATA[ ");
				sb.append(e.getMessage());
				sb.append("]]></data>");
				sb.append("</result>");
				out.print(sb.toString());
			}
		
		} else if (cmd.equals("selectAll")) { // 전체조회
			try {
				List<HashMap<String, Object>> list = XyDAO.getInstance().selectAll();
				out.print(selectAll(list));
			} catch (Exception e) {
				StringBuffer sb = new StringBuffer();

				sb.append("<result>");
				sb.append("<code>error</code>");
				sb.append("<data><![CDATA[ ");
				sb.append(e.getMessage());
				sb.append("]]></data>");
				sb.append("</result>");
				out.print(sb.toString());
			}
		
		}  else {
			StringBuffer sb = new StringBuffer();
			sb.append("<result>");
			sb.append("<code>error</code>");
			sb.append("<data><![CDATA[ ");
			sb.append("cmd not found");
			sb.append("]]></data>");
			sb.append("</result>");
			out.print(sb.toString());
		}

	}

	private String toXML(String result, Object data) throws IOException{
		StringBuffer sb = new StringBuffer();
		sb.append("<result>");
		sb.append("<code>");
		sb.append(result);
		sb.append("</code>");
		sb.append(" <data><![CDATA[ ");
		if ( data instanceof String) {
			sb.append(data);
		} else {
			/*StringWriter sw = new StringWriter();
			JSONValue.writeJSONString(data, sw);
			sb.append(sw.toString());*/
			sb.append(JSONObject.fromObject(data));
		}
		sb.append("]]></data>");
		sb.append("</result>");
		return sb.toString();
		}	
	
	
	private String selectAll(List<HashMap<String, Object>> list) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("<result>");
		sb.append("<code>success</code>");
		sb.append(" <data><![CDATA[ ");
		System.out.println(list);
		sb.append(JSONArray.fromObject(list));
		/*sb.append(" [ ");
		for (int i = 0; i < list.size(); i++) {
			HashMap<String, Object> map = list.get(i);
			sb.append("{ ");
			sb.append(" id:" + map.get("id"));
			sb.append(", name:'" + map.get("name") + "'");
			sb.append(", content:'" + map.get("content") + "'");
			sb.append("}");
			if (i != list.size()) {
				sb.append(", ");
			}
		}
		sb.append("]\n");*/
		sb.append("]]></data>");
		sb.append("</result>");
		return sb.toString();
	}
}
