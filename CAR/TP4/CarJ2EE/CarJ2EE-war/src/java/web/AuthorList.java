/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import ejb.Book;
import ejb.BookFacadeLocal;
import ejb.Users;
import ejb.UsersFacadeLocal;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author erwan
 */
public class AuthorList extends HttpServlet {

    @EJB
    private BookFacadeLocal bookFacade;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        try {
            if (session.getAttribute("user") == null) {
                this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
            }
            response.setContentType("text/html;charset=UTF-8");
            ServletContext sc = getServletContext();
            RequestDispatcher rd = sc.getRequestDispatcher("/header.jsp");
            rd.include(request, response);
            try (PrintWriter out = response.getWriter()) {
                out.println("<section>");
                rd = sc.getRequestDispatcher("/searchBar.jsp");
                rd.include(request, response);
                out.println("<article>");
                List<Book> books = bookFacade.findAll();
                List<String> authors = new ArrayList<String>();
                for (Book book : books) {
                    if (!authors.contains(book.getAuthor())) {
                        authors.add(book.getAuthor());
                    }
                }
                for (String author : authors) {

                    out.println("<table>");
                    out.println("<tr>");
                    out.println("<td><b>" + author + "</b></td></b>");
                    out.println("</tr>");
                    out.println("</table>");
                    out.println("<br><br>");
                }
                out.println("</article>");
                rd = sc.getRequestDispatcher("/cartView.jsp");
                rd.include(request, response);
                out.println("</section>");
                rd = sc.getRequestDispatcher("/foot.jsp");
                rd.include(request, response);
                out.close();
            }
        } catch (Exception e) {
            this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
