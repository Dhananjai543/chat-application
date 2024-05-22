<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>

<html>
<head>
    <title>Login Page</title>
</head>
<body>

    <div id="wrapper">
        <div id="header">
            <h2>Login Page</h2>
        </div>
    </div>

    <form action="login" method="post">
        <label for="username">Username:</label><br>
        <input type="text" id="username" name="username"><br>
        <input type="submit" value="Submit">
    </form>
</body>
</html>