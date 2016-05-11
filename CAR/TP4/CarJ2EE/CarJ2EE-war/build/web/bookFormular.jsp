<%-- 
    Document   : bookFormular
    Created on : 6 mai 2014, 17:55:25
    Author     : erwan
--%>

<%@page import="ejb.Users"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

    <head>
        <title>Add a book</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <%
        if (session.getAttribute("user") != null) {
            if (!((Users) session.getAttribute("user")).getLogin().equals("admin")) {
                response.sendRedirect("index.jsp");
            }
        } else {
            response.sendRedirect("index.jsp");
        }
    %>
    <body>
        <h1>Add a book</h1>

        <form method=POST action="BookCreator">
            <table>
                <tr>
                    <td>Title: </td>
                    <td>
                        <input type=text name="title">
                    </td>
                </tr>

                <tr>
                    <td>Author: </td>
                    <td>
                        <INPUT type=text name="author" >
                    </td>
                </tr>
                <tr>
                    <td>Date: </td>
                    <td>
                        <INPUT type="number" name="date" >
                    </td>
                </tr>
                <tr>
                    <td>Price: </td>
                    <td>
                        <INPUT type="number" name="price" >
                    </td>
                </tr>
                <tr>
                    <TD COLSPAN=2>
                        <INPUT type="submit" value="Validate">
                    </td>
                </tr>
            </table>
        </form>
    </body>
</html>
