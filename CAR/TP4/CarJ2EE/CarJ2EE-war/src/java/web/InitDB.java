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
import static java.lang.System.out;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author erwan
 */
public class InitDB extends HttpServlet {

    @EJB
    private UsersFacadeLocal usersFacade;

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
        List<Book> books = bookFacade.findAll();
        List<Users> users = usersFacade.findAll();
        if (books.isEmpty()) {
            bookFacade.create(new Book("Why I hate Entity Beans", "Mike Hogan", 2002, (float) 23));
            bookFacade.create(new Book("Why cocaine can be cool", "Kurt Cobain", 1994, (float) 15.99));
            bookFacade.create(new Book("Brochettes ou boulettes de porc ?", "Jean-Pierre Coffe", 2001, (float) 9.8));
            bookFacade.create(new Book("Static Analysis for Extracting Permission Checks of a Large Scale Framework: The Challenges And Solutions for Analyzing Android", "Martin Monperrus", 2014, (float) 0));
            System.out.println("Default books added");
        }
        if (users.isEmpty()) {
            usersFacade.create(new Users("admin", "admin"));
            usersFacade.create(new Users("anonymous", "anonymous"));
            System.out.println("Default users added");
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
