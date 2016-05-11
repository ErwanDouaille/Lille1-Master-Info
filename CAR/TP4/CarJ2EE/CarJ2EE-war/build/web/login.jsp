<%-- 
    Document   : login
    Created on : 6 mai 2014, 16:57:46
    Author     : erwan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>MegaBook store authentification</title>
	<link rel="stylesheet" type="text/css" href="login.css" />
    </head>
    <body>
        <%
            if (request.getParameter("failure") != null) {
                out.println("<h3>" + request.getParameter("failure") + "</h3>");
            }
        %>
        <form id="login-form" action="Authentification" method="post">
            <fieldset>
		
			<legend>Log in</legend>
			<label for="login">Login: </label>
                        <input type="text" id="login" name="login" value="" />
			<div class="clear"></div>
                        
			<label for="password">Password</label>
                        <input type="password" id="password" name="password" value="" />
			<div class="clear"></div>
                        
			<br />
			
			<input type="submit" style="margin: -20px 0 0 287px;" class="button" name="commit" value="Log in"/>	
		</fieldset>
        </form><a href=\CarJ2EE-war/userFormular.jsp>Create an account</a><br>
    </body>
</html>
