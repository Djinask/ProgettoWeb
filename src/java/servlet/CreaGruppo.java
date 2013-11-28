/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import DB.DBManager;
import DB.Groups;
import DB.Users;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author djinask
 */
public class CreaGruppo extends HttpServlet {

    private DBManager manager;

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
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
       out.println("<head><link href=\"css/bootstrap.css\" rel=\"stylesheet\">");
        out.println("<link href=\"css/bootstrap-theme.css\" rel=\"stylesheet\">");
            out.println("<title>Servlet CreaGruppo</title>");
            out.println("</head>");
            out.println("<body>");
              out.println("<div class=\"jumbotron\">");
            out.println("<h1>Nuovo Gruppo</h1>");
                    out.println("<a class=\"btn btn-primary \" role=\"button\" href=\"Logout\">LOGOUT</a>");
                out.println("</div>");
     out.println("<form class=\"navbar-form navbar-left\" action=\"CreaGruppo\" method=\"post\">");
                   out.println("<input type=\"text\" name=\"group_name\" class=\"form-control\" placeholder=\"Nome Gruppo\">");
            out.println("<br>");

            out.println("<br>");
            out.println("<button  class=\"btn btn-default btn-lg btn-primary\" type=\"submit\">Crea</button>");
            out.println("<br>");
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
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
        //processRequest(request, response);
        HttpSession session = request.getSession(false);
        this.manager = (DBManager) super.getServletContext().getAttribute("dbmanager");
        String GroupName = request.getParameter("group_name");
        Users user = (Users) session.getAttribute("user");
        if(GroupName==null){
            processRequest(request,response);
        }
        Groups group = new Groups();
        group.setGroupName(GroupName);
        group.setAdmin(user);

        
        
            try {

                Boolean added = new Boolean(false);
                added = manager.AddGroup(group);
               

            } catch (SQLException ex) {
                throw new ServletException(ex);
            

       
            }
            response.sendRedirect(request.getContextPath() + "/LoggedHome");
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
