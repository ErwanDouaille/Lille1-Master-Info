<%-- 
    Document   : logout
    Created on : 6 mai 2014, 17:12:40
    Author     : erwan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Disconnected</h1>
        <%
            session.setAttribute("user", null);
            session.setAttribute("cart", null);
        %>
        <jsp:forward page="login.jsp"/> 
    </body>
</html>
