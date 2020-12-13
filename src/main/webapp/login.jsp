<%@ page import ="tools.UserAuth" import="models.UserModel" import="tools.htmltools.HtmlConstants" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%
    UserModel currentUser = UserAuth.verifyLogin(request);

    // IF ALREADY LOGGED IN, GO TO "Min side"
    if(currentUser != null){ response.sendRedirect("mypage.jsp"); }
%><!DOCTYPE html>
<html lang="no">
<head>
    <title>Login - Roing Webapp</title>
    <%=HtmlConstants.getHtmlHeaders()%>
</head>
<body>

<div id="nav-placeholder"></div>
<script src="js/menu.js"></script>

<div class="container" style="text-align: center;">
    <h1>User Login</h1>
    <%=UserAuth.getSessionNotes(session)%>

    <form action="login" method="post">

        <div class="form-group">
            <label>Username:
                <input type="email" class="form-control" name="email" size="30" />
            </label>
        </div>
        <div class="form-group">
            <label>Password:
                <input type="password" class="form-control" name="password" size="30" />
            </label>
        </div>
        <div class="form-group">
            <input type="submit" value="Login" class="btn btn-secondary" />
        </div>
    </form>

</div>
</body>
</html>