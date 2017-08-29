<%@ tag body-content="scriptless" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<form name="loginform" method="post" action="../member/MemberServ">

<c:if test="${not empty mbAdmin  }">
<a href="../Product/product_update.tile">상품등록</a>
<a href="../member/admin.tile">관리자</a>
</c:if>

<c:choose>
<c:when test="${mbId != null}">
	
	<input type="hidden" name="action" value="logout">
	${mbId }님
	<c:if test="${empty mbAdmin }">
	<a href="../Order/client_order.tile">주문내역</a>
	</c:if>
	<input type="submit" value="로그아웃">	
</c:when>
<c:otherwise>
	
		<!-- <li><a href="#">Login</a></li> -->
			<input type="hidden" name="action" value="login">
		&nbsp;<input type="text" name="mbId" size="10"> <input type="password" name="mbPass" size="10"><input type="submit" value="로그인">
		
</c:otherwise>
</c:choose>
	
	
	
	
	
</form>
