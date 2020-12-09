<%@ page import ="tools.UserAuth" import="models.UserModel" import="tools.htmltools.HtmlConstants" import="enums.User" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%
    UserModel currentUser = UserAuth.verifyLogin(request);
%><!DOCTYPE html>
<html lang="no">
<head>
    <title>Roing Webapp</title>
    <%=HtmlConstants.getHtmlHeaders()%>
</head>
<body style="text-align: center;">

<div id="nav-placeholder"></div>
<%=UserAuth.navBarLogin(currentUser)%>
<script src="js/menu.js"></script>
<style>
    .apiMethod {
        font-size: 14px;
        min-width: 80px;
        padding: 6px 15px;
        text-align: center;
        border-radius: 4px;
        font-family: sans-serif;
        font-weight: bold;
        color: #fff;
    }

    .list-group-item-primary > .apiMethod {
        background-color: #0e4377;
    }

    .list-group-item-success > .apiMethod {
        background-color: #1b5e20;
    }

    .apiPath {
        font-size: 16px;
        font-weight: bold;
        padding: 0 10px;
        font-family: monospace;
        display: flex;
    }

    .apiDesc {
        font-family: sans-serif;
        font-size: 13px;
    }

    .list-group-item {
        text-align: left;
        display: flex;
        line-height: 20px;
    }
</style>

<div class="container">
    <h1>API Dokumentasjon</h1>
    <p>Du finner apien på <a href="./api/">/api/</a>. Den vil forklare litt med en gang.</p>
    <br>
    <ul class="list-group">
        <%
            String[][] rows = {
                    {"GET", "/", "Main page for the API"},
                    {"POST", "/utøver/{utøver navn}", "Finn én utøver"},
                    {"GET", "/utøvere/", "Liste over alle utøverne"},
                    {"POST", "/klubb/{klubb navn}", "Informasjon om én klubb"},
                    {"GET", "/klubber/", "Liste over alle klubbene"}
            };
            for(String[] cols : rows){
                out.print("<li class=\"list-group-item list-group-item-"+(cols[0].equals("GET")?"primary":(cols[0].equals("POST")?"success":"danger"))+"\">");
                out.print("<span class=\"apiMethod\">"+cols[0]+"</span>");
                out.print("<span class=\"apiPath\">");

                if(cols[0].equals("GET")){
                    out.print("<a href=\"./api" + cols[1] + "\" target=\"_blank\">");
                    out.print("<pre>" + cols[1] + "</pre>");
                    out.print("</a>");
                }
                else {
                    out.print("<pre>" + cols[1] + "</pre>");
                }

                out.print("</span>");
                out.print("<span class=\"apiDesc\">"+cols[2]+"</span>");
                out.print("</li>\n");
            }
        %>
    </ul>
</div>

</body>
</html>
