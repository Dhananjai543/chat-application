<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>

<html>
<head>
    <title>Login Page</title>
    <link rel="stylesheet" type="text/css" href="/resources/css/styles.css">
</head>
<body>
    <div class="container">
        <h2>Login Page</h2>
        <form action="login" method="post">
            <div class="input-group">
                <label for="email">Email:</label>
                <input type="text" id="email" name="email">
            </div>
            <div class="input-group">
                <label for="password">Password:</label>
                <input type="password" id="password" name="password">
            </div>
            <button type="submit">Submit</button>
        </form>
    </div>
</body>
</html>