<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User Registration</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles.css">
</head>
<body>
    <jsp:include page="/WEB-INF/header.jspf" />

    <h1>Register</h1>

    <% String errorMessage = (String) request.getAttribute("errorMessage");
       if (errorMessage != null) { %>
        <p style="color:red;"><%= errorMessage %></p>
    <% } %>
    <% String successMessage = (String) request.getAttribute("successMessage");
       if (successMessage != null) { %>
        <p style="color:green;"><%= successMessage %></p>
    <% } %>

    <form method="post" action="${pageContext.request.contextPath}/auction?action=register">
        <table>
            <tr>
                <td><label for="username">Username:</label></td>
                <td><input type="text" id="username" name="username" required></td>
            </tr>
            <tr>
                <td><label for="email">Email:</label></td>
                <td><input type="email" id="email" name="email" required></td>
            </tr>
            <tr>
                <td><label for="password">Password:</label></td>
                <td><input type="password" id="password" name="password" required></td>
            </tr>
            <tr>
                <td><label for="confirmPassword">Confirm Password:</label></td>
                <td><input type="password" id="confirmPassword" name="confirmPassword" required></td>
            </tr>
            <tr>
                <td colspan="2"><input type="submit" value="Register"></td>
            </tr>
        </table>
    </form>
    <p>Already have an account? <a href="${pageContext.request.contextPath}/auction?action=login">Login here</a></p>
</body>
</html>
