<%-- 
    Document   : header.jsp
    Created on : 7 mai 2014, 14:31:35
    Author     : erwan
--%>
<%@page import="ejb.Users"%>
<html>
    <head>
        <link rel="stylesheet" href="style.css" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Book Megastore</title>
    </head>
    <body>
        <header>
            <div id="titre_principal">
                <h1>MegaBook store</h1>
            </div>

            <nav>
                <ul>
                    <li><a href="index.jsp">Accueil</a></li>
                    <li><a href="BookList">List existing books</a></li>
                    <li><a href="AuthorList">List authors</a></li>
                    <li><a href="logout.jsp">Logout</a></li>
                </ul>
            </nav>

            <%
                if (session.getAttribute("user") == null) {
                    response.sendRedirect("login.jsp");
                } else {
                    if (((Users) session.getAttribute("user")).getLogin().equals("admin")) {
                        out.println("<br><div id=\"titre_principal\"<h2>Admin:</h2></div><nav><ul><li><a href=\"bookFormular.jsp\">Add a Book</a></li></ul></nav>");
                    }
                }
            %>        <br>        
        </header>   