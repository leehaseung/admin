<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<title>Insert title here</title>

<style>
div {width : 40%;
		float:left;
		border:1px solid blue;
		margin: 10px;
		height: 300px;
		overflow:auto;
	}
</style>
<script>


</script>
</head>
<body>
<sql:query var="rs" dataSource="jdbc/oraclegps">
select no,x,y,UUID,ADDR,to_char(GDATE,'YY/MM/DD hh24:mi:ss') as "date"
FROM GPST
where GDATE >=SYSDATE - 3/24/60
order by 6 DESC
</sql:query>




<c:forEach var="row" items="${rs.rows }">
	<tr>
	<td>${row.no} </td><td> ${row.x} </td><td> ${row.y } </td> 
	<td>${row.ADDR}</td><td> ${row.date} </td>
	
	</tr>
	<BR>
</c:forEach>




 
</body>
</html>