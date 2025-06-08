<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Welcome to the Auction Site</title>
    <style>
        body { font-family: sans-serif; margin: 20px; }
        nav { margin-bottom: 20px; }
        nav a { margin-right: 15px; text-decoration: none; }
        nav a:hover { text-decoration: underline; }
        .user-info { margin-bottom: 10px; }
    </style>
</head>
<body>

    <h1>Auction Site</h1>

    <nav>
        <a href="${pageContext.request.contextPath}/index">Home</a>
        <c:if test="${requestScope.isLoggedIn}">
            <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
            <%-- Add other links for logged-in users if needed, e.g., View Auctions, My Bids --%>
            <a href="${pageContext.request.contextPath}/auction">View Auctions</a>
        </c:if>
    </nav>

    <div class="user-info">
        <c:choose>
            <c:when test="${requestScope.isLoggedIn}">
                Welcome, <strong><c:out value="${requestScope.username}"/></strong>!
                <form method="POST" action="${pageContext.request.contextPath}/logout" style="display: inline; margin-left: 20px;">
                    <input type="submit" value="Logout">
                </form>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/login">Login</a>
                <a href="${pageContext.request.contextPath}/signup">Register</a>
            </c:otherwise>
        </c:choose>
    </div>

    <hr>

    <h2>Main Content Area</h2>
    <p>
        <c:choose>
            <c:when test="${requestScope.isLoggedIn}">
                You are logged in. You can proceed to your <a href="${pageContext.request.contextPath}/dashboard">dashboard</a> or <a href="${pageContext.request.contextPath}/auction">view auctions</a>.
            </c:when>
            <c:otherwise>
                Please <a href="${pageContext.request.contextPath}/login">login</a> or <a href="${pageContext.request.contextPath}/signup">register</a> to participate in auctions.
            </c:otherwise>
        </c:choose>
    </p>

    <%-- Placeholder for other public content --%>
    <p>Latest auction items or site news could be displayed here for all users.</p>

</body>
</html>
