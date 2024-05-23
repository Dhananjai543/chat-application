<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
    <title>Sign Up Form</title>
    <link rel="stylesheet" type="text/css" href="/resources/css/styles.css">
</head>
<body>
    <div class="container">
        <h2>Sign Up</h2>
        <form:form action="processSignUpForm"  modelAttribute="chatUser" method="POST">
            <div class="input-group">
                <label for="name">Name</label>
                <form:input path="user_name" />
            </div>
            <div class="input-group">
                <label for="email">Email</label>
                <form:input path="user_email" />
            </div>
            <div class="input-group">
                <label for="password">Password</label>
                <form:input path="user_password" />
            </div>
            <button type="submit">Sign Up</button>
        </form:form>
    </div>
</body>
</html>