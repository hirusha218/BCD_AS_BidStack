<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Register</title>
</head>
<body>
    <h1>Registration Page</h1>
    <form action="registerServlet" method="post">
        Username: <input type="text" name="username"><br>
        Email: <input type="email" name="email"><br>
        Password: <input type="password" name="password"><br>
        Confirm Password: <input type="password" name="confirmPassword"><br>
        <input type="submit" value="Register">
    </form>
</body>
</html>
