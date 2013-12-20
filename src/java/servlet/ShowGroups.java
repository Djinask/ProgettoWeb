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
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author djinask
 */
public class ShowGroups extends HttpServlet {

    private DBManager manager;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws java.sql.SQLException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        HttpSession session = request.getSession(false);
        this.manager = (DBManager) super.getServletContext().getAttribute("dbmanager");
        if (session == null || session.getAttribute("user") == null) {

            response.sendRedirect(request.getContextPath() + "/Login");

        } else {
            Users user = (Users) session.getAttribute("user");
            response.setContentType("text/html;charset=UTF-8");

            try (PrintWriter out = response.getWriter()) {
                /* TODO output your page here. You may use following sample code. */
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head><link href=\"css/bootstrap.css\" rel=\"stylesheet\">");
                out.println("<link href=\"css/bootstrap-theme.css\" rel=\"stylesheet\">");
                out.println("<title>Gruppi utente</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<ul class=\"nav nav-tabs\">\n"
                        + "  <li><a href=\"LoggedHome\">Home</a></li>\n"
                        + "  <li><a href=\"ProfiloUtente\">Profile</a></li>\n"
                        + "  <li style=\"float:right;position:relative;margin-right:1em;\"><a href=\"Logout\">Logout</a></li>\n"
                        + "</ul>");
                out.println("<div class=\"jumbotron\">");
                if (user.getAvatar_path().equals("null")) {
                    out.println("<span class=\"glyphicon glyphicon-user\"></span>");
                } else {
                    out.println("<img src=\"" + user.getAvatar_path() + "\" class=\"img-thumbnail\"  height=\"60\" width=\"60\" />");
                }
                out.println("<h1>Gruppi di " + user.getName() + "</h1>");

                out.println("</div>");
                out.println("<table class=\"table table-hover\">"
                        + "<tr class=\"active\">"
                        + "<th>#</th>"
                         + "<th>Nome Gruppo</th>"
                         + "<th>Proprietario</th>"
                         + "<th>Data Creazione</th>"
                        + "</tr>");

                List<Groups> gruppi = (ArrayList) manager.getGroups(user);
                //out.println(gruppi);
                if (gruppi != null) {
                    for (int i = 0; i < gruppi.size(); i++) {
                        String date;
                        date= new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(gruppi.get(i).getData());
                        out.println(""
                                + "<tr>"
                               
                                + "<td>" + (i+1)
                                + "</td>"
                                + "<td>"
                                + "<a href=\"Gruppo?id="
                                
                                + "" + gruppi.get(i).getId() +"\">" + gruppi.get(i).getGroupName()
                                + "</a>"
                                + "</td>"
                                + "<td>" + gruppi.get(i).getOwner()
                                + "</td>"
                                + "<td>" + date
                                + "</td>"
                                + "</tr>");

                    }
                } else {
                    out.println("<h2>Non vi sono gruppi!</h2>");

                }
                out.println("</table>");
                //ciclo    out.println("<li class=\"list-group-item\">Cras justo odio</li>");

                out.println("</body>");
                out.println("</html>");
            }
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
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(ShowGroups.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(ShowGroups.class.getName()).log(Level.SEVERE, null, ex);
        }
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
