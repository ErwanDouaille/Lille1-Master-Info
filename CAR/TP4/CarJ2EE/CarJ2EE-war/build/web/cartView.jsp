<%-- 
    Document   : cartView
    Created on : 6 mai 2014, 18:16:37
    Author     : erwan
--%>

<%@page import="ejb.Book"%>
<%@page import="java.util.ArrayList"%>
<%@page import="utils.Cart"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<aside>
    <%
        out.println("<h2>Cart</h2>");
        if (session.getAttribute("cart") != null) {
            Cart cart = (Cart) session.getAttribute("cart");
            ArrayList<Book> bookList = cart.getBookList();
            out.println("<table>");
            for (Book book : bookList) {
                out.println("<tr>");
                out.println("<td><b>" + book.getTitle() + "</b></td><td><b>" + book.getPrice() + " $</b></td><td><form method=POST action=\"AddInCart\"><input type=hidden name=\"title\" value=\"" + book.getTitle() + "\"><input type=\"submit\" value=\"+\"></form></td><td><form method=POST action=\"RemoveFromCart\"><input type=hidden name=\"title\" value=\"" + book.getTitle() + "\"><input type=\"submit\" value=\"-\"></form></td></b>");
                out.println("</tr>");
            }
            out.println("<tr>");
            out.println("<td><b>Total: </b></td><td><b>" + cart.totalPrice() + "</b></td></b>");
            out.println("</tr>");
            out.println("</table>");
            out.println("<a href=\"checkout.jsp\">Checkout</a><br>");
        }
    %>
</aside>