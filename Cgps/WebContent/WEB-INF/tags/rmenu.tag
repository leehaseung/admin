<%@ tag body-content="scriptless" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="rid" %>
<%@ attribute name="ruserid" %>
<%@ attribute name="curmsg" %>

<c:if test="${userid == ruserid}">
[<a href="sns_control.jsp?action=delreply&rid=${rid}&curmsg=${curmsg}&cnt=${cnt}&suserid=${suserid}">삭제</a>] 
</c:if>