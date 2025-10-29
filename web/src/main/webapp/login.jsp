<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User Login</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles.css">
</head>
<body>
    <jsp:include page="/WEB-INF/header.jspf" />

    <h1>Login</h1>

    <% String errorMessage = (String) request.getAttribute("errorMessage");
       if (errorMessage != null) { %>
        <p style="color:red;"><%= errorMessage %></p>
    <% } %>
     <% String message = (String) request.getAttribute("message"); // General message, e.g., after registration
       if (message != null) { %>
        <p style="color:green;"><%= message %></p>
    <% } %>


    <form method="post" action="${pageContext.request.contextPath}/auction?action=login">
        <table>
            <tr>
                <td><label for="username">Username:</label></td>
                <td><input type="text" id="username" name="username" required></td>
            </tr>
            <tr>
                <td><label for="password">Password:</label></td>
                <td><input type="password" id="password" name="password" required></td>
            </tr>
            <tr>
                <td colspan="2"><input type="submit" value="Login"></td>
            </tr>
        </table>
    </form>
    <p>Don't have an account? <a href="${pageContext.request.contextPath}/auction?action=register">Register here</a></p>
</body>
</html>
