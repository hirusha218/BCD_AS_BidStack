<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body class="body-login">
    <div class="login-container">
        <h1>Login Page</h1>
        <form action="loginServlet" method="post" class="login-form">
            Username: <input type="text" name="username" class="form-input"><br>
            Password: <input type="password" name="password" class="form-input"><br>
            <input type="submit" value="Login" class="login-button">
        </form>
        <p class="register-link">Don't have an account? <a href="register.jsp">Register here</a></p>
    </div>
</body>
</html>
