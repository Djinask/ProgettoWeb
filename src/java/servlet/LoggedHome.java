/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import DB.Users;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author djinask
 */
public class LoggedHome extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
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

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {

            response.sendRedirect(request.getContextPath() + "/Login");

        } else {
            Users user = (Users) session.getAttribute("user");
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head><link href=\"css/bootstrap.css\" rel=\"stylesheet\">");
            out.println("<link href=\"css/bootstrap-theme.css\" rel=\"stylesheet\">");
            out.println("<title>Home</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class=\"jumbotron\">");
            
             out.println("<h1><span class=\"glyphicon glyphicon-lock\"></span>Benvenuto,  " + user.getName() + "!</h1>");
              out.println("<a class=\"btn btn-primary \" role=\"button\" href=\"Logout\">LOGOUT</a>");
                out.println("</div>");
            out.println("<div class=\"btn-toolbar\" role=\"toolbar\">");
            out.println("<div class=\"btn-group btn-group-justified\">");

            out.println("<a class=\"btn btn-default\" href=\"ShowGroups\" role=\"button\">Gruppi</a>");
             out.println("<a class=\"btn btn-default\" href=\"CreaGruppo\" role=\"button\" >Crea Gruppo</a>");
              out.println("<a class=\"btn btn-default\" href=\"#\" role=\"button\"  >Invita</a>");
            out.println("</div>");
            out.println("</div>");
            out.println("<br>");

           
            out.println("<br>");
         
            
            out.println("</body>");
            out.println("</html>");

            //EMETTERE il CODICE HTML della pagine qui
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp); //To change body of generated methods, choose Tools | Templates.
        doGet(req, resp);
          //  RequestDispatcher rd = req.getRequestDispatcher("Login");
        // rd.forward(req, resp);

    }
        //req.setAttribute("message", "Logout effettuato con successo");
    //resp.sendRedirect("/Login");
    //RequestDispatcher rd = req.getRequestDispatcher("Login");
    //rd.forward(req, resp);

}

/**
 * Handles the HTTP <code>POST</code> method.
 *
 * @param request servlet request
 * @param response servlet response
 * @throws ServletException if a servlet-specific error occurs
 * @throws IOException if an I/O error occurs
 */
/**
 * Returns a short description of the servlet.
 *
 * @return a String containing servlet description
 */
