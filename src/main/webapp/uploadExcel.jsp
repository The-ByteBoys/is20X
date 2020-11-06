<%@ page import ="tools.UserAuth" import="models.UserModel" import="tools.htmltools.HtmlConstants" import="enums.UserLevel" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%
    UserModel currentUser = UserAuth.requireLogin(request, response, UserLevel.COACH);
    if(currentUser == null){ return; }
%>
<!DOCTYPE html>
<html lang="no">
<head>
    <title>Register - Roing Webapp</title>
    <%=HtmlConstants.getHtmlHeaders()%>
</head>
<body>

<div id="nav-placeholder"></div>
<%=UserAuth.navBarLogin(currentUser)%>
<script src="js/menu.js"></script>

<div class="container-fluid" style="text-align: left;">
    <div style='text-align: center; margin-top: 15vh;'>
        <form method="POST" enctype="multipart/form-data" action="massinsert">
            <h3><b>Upload an .xlsx file to start parsing</b></h3>
            <p><label>.xlsx to upload: <input type="file" name="upfile" accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" /></label></p>
            <p><label><input type="submit" value="Press"> to upload the file!</label></p>
        </form>
    </div>
</div>

</body>
</html>
