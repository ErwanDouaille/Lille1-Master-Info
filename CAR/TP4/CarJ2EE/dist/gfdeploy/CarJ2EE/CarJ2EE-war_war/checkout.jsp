<%-- 
    Document   : checkout
    Created on : 6 mai 2014, 20:30:56
    Author     : erwan
--%>

<%@page import="ejb.Users"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Mega Book store</title>
    </head>
    <body>
        <h1>Checkout</h1>        
        <jsp:include page="cartView.jsp" />
        <%
            if (session.getAttribute("user") == null || session.getAttribute("cart") == null) {
                response.sendRedirect("index.jsp");
            }
        %>
        <%
            if (session.getAttribute("user") == null) {
                response.sendRedirect("login.jsp");
            } else {
                Users user = (Users) session.getAttribute("user");
                out.println("<form method=POST action=\"Checkout\">");
                out.println("<table>");
                out.println("<tr><td>Name :</td><td><input type=text name=\"name\" value=\"" + user.getName() + "\"></td></tr>");
                out.println("<tr><td>Surname :</td><td><input type=text name=\"surname\" value=\"" + user.getSurname() + "\"></td></tr>");
                out.println("<tr><td>Address :</td><td><input type=text name=\"address\" value=\"" + user.getAddress() + "\"></td></tr>");
                out.println("<tr><td>Country :</td><td><input type=text name=\"country\"value=\"" + user.getCountry() + "\"></td></tr>");
                out.println("<tr><td>Credit card :</td><td><input type=number name=\"credit_card\"></td></tr>");
                out.println("<tr><td COLSPAN=2><input type=submit value=\"Command\"></td></tr>");
            }
        %>            
    </body>
</html>
