<%-- 
    Document   : userFormular
    Created on : 6 mai 2014, 17:59:52
    Author     : erwan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Registration</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <%
        if (session.getAttribute("user") != null) {
            response.sendRedirect("index.jsp");
        } 
    %>
    <body>
        <h1>Registration</h1> 

        <form method=POST action="UserCreator">
            <table>
                <tr>
                    <td>Login :</td>
                    <td>
                        <input type=text name="login">
                    </td>
                </tr>

                <tr>
                    <td>Password: </td>
                    <td>
                        <INPUT type=text name="password" >
                    </td>
                </tr>

                <tr>
                    <td>Name: </td>
                    <td>
                        <INPUT type=text name="name" >
                    </td>
                </tr>

                <tr>
                    <td>Surname: </td>
                    <td>
                        <INPUT type=text name="surname" >
                    </td>
                </tr>

                <tr>
                    <td>Email: </td>
                    <td>
                        <INPUT type=text name="email" >
                    </td>
                </tr>

                <tr>
                    <td>Phone: </td>
                    <td>
                        <INPUT type=text name="phone" >
                    </td>
                </tr>

                <tr>
                    <td>Adress: </td>
                    <td>
                        <INPUT type=text name="address" >
                    </td>
                </tr>

                <tr>
                    <td>Country</td>
                    <td>
                        <INPUT type=text name="country" >
                    </td>
                </tr>
                <tr>
                    <TD COLSPAN=2>
                        <INPUT type="submit" value="Register">
                    </td>
                </tr>
            </table>
        </form>
    </body>
</html>
