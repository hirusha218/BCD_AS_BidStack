<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>Dashboard</title>
</head>
<body>
    <h2>Welcome, <c:out value="${requestScope.username}"/>!</h2>
    <p>This is your dashboard.</p>

    <h3>Your Details:</h3>
    <p><strong>Username:</strong> <c:out value="${requestScope.username}"/></p>
    <p><strong>Email:</strong> <c:out value="${requestScope.email}"/></p>
    <p><strong>Registration Date:</strong> <fmt:formatDate value="${requestScope.registrationDate}" pattern="yyyy-MM-dd HH:mm:ss"/></p>

    <br/>
    <form method="POST" action="${pageContext.request.contextPath}/logout">
        <input type="submit" value="Logout">
    </form>
     <br/>
     <!-- Example link to auction functionality -->
     <p><a href="${pageContext.request.contextPath}/auction">View Auctions</a></p>
</body>
</html>
