<%@ tag body-content="scriptless" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<%@ attribute name="mid" %>
<%@ attribute name="auserid" %>
<%@ attribute name="curmsg" %>

<c:if test="${userid == auserid}">
[<a href="sns_control.jsp?action=delmsg&mid=${mid}&curmsg=${curmsg}&cnt=${cnt}&suserid=${suserid}">삭제</a>] 
</c:if>
[<a href="sns_control.jsp?action=fav&mid=${mid}&curmsg=${curmsg}&cnt=${cnt}&suserid=${suserid}">좋아요</a>]