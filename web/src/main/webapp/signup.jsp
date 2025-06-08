<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Sign Up</title>
</head>
<body>
    <h2>Sign Up</h2>
    <p style="color: red;">${requestScope.error}</p>
    <form method="POST" action="${pageContext.request.contextPath}/signup">
        Username: <input type="text" name="username" value="${param.username}" required><br/><br/>
        Email: <input type="email" name="email" value="${param.email}" required><br/><br/>
        Password: <input type="password" name="password" required><br/><br/>
        Confirm Password: <input type="password" name="confirmPassword" required><br/><br/>
        <input type="submit" value="Sign Up">
    </form>
    <br/>
    <p>Already have an account? <a href="${pageContext.request.contextPath}/login">Login</a></p>
</body>
</html>
