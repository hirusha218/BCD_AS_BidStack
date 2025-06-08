<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
    <h2>Login</h2>
    <%-- Display general messages (e.g., signup success, logout success) --%>
    <p style="color: green;">${requestScope.message}</p>
    <%-- Display error messages --%>
    <p style="color: red;">${requestScope.error}</p>

    <form method="POST" action="${pageContext.request.contextPath}/login">
        Username: <input type="text" name="username" required><br/><br/>
        Password: <input type="password" name="password" required><br/><br/>
        <input type="submit" value="Login">
    </form>
    <br/>
    <p>Don't have an account? <a href="${pageContext.request.contextPath}/signup">Sign Up</a></p>
</body>
</html>
