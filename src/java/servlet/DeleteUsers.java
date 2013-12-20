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
public class DeleteUsers extends HttpServlet {

   
    private DBManager manager;
    int id_gruppo;
    Users user;

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
        this.manager = (DBManager) super.getServletContext().getAttribute("dbmanager");
        if (session == null || session.getAttribute("user") == null) {

            response.sendRedirect(request.getContextPath() + "/Login");

        } else {
            user = (Users) session.getAttribute("user");
            id_gruppo = Integer.parseInt(request.getParameter("id_group"));
            System.out.println("id_gr =" + id_gruppo);

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
                out.println("<div class=\"jumbotron\">");
                Groups gruppo = (Groups) manager.getGroup(id_gruppo, user);

                out.println("<h1>Invita a " + gruppo.getGroupName() + "</h1>");
                out.println("<a class=\"btn btn-primary \" role=\"button\" href=\"Logout\">LOGOUT</a>");
                out.println("<a class=\"btn btn-primary \" role=\"button\" href=\"LoggedHome\">HOME</a>");

                out.println("</div>");
                out.println("<legend>Seleziona gli utenti che vuoi invitare al gruppo:</leged>");
                out.println("<form class=\"navbar-form navbar-left\"action=\"DeleteUsers\" method=\"post\">");

                out.println("<ul class=\"list-group\">");

                List<Users> utenti = (ArrayList) manager.getUsersGroup(user, id_gruppo, false);
                
                if (utenti != null) {
                    for (int i = 0; i < utenti.size(); i++) {
                        out.println("<li class=\"list-group-item\">"
                                + "<input type=\"checkbox\" name=\"rimossi\" value=\"" + utenti.get(i).getId() + "\"/>"
                                + " Nome: " + utenti.get(i).getName()
                                + "           Mail:" + utenti.get(i).getmail()
                                + " </li>"
                                + "</a>");
                    }
                } else {
                    out.println("<h2>Non vi sono utenti</h2>");

                }

                out.println("</ul>");
                out.println("<button  class=\"btn btn-default btn-lg\" type=\"submit\">Conferma Eliminati</button>");

                out.println("</form>");
                out.println("</body>");
                out.println("</html>");
            } catch (SQLException ex) {
                Logger.getLogger(Invita.class.getName()).log(Level.SEVERE, null, ex);
            }
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
        HttpSession session = request.getSession(false);
        this.manager = (DBManager) super.getServletContext().getAttribute("dbmanager");
        String[] rimossi = request.getParameterValues("rimossi");
        Boolean invitati = false;
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head><link href=\"css/bootstrap.css\" rel=\"stylesheet\">");
        out.println("<link href=\"css/bootstrap-theme.css\" rel=\"stylesheet\">");
        out.println("<title>Gruppi utente</title>");
        out.println("</head>");
        out.println("<body>");
        System.out.println("id_gr =" + id_gruppo);
        try {
            invitati = this.manager.DeleteUsers(id_gruppo, user, rimossi);

        } catch (SQLException ex) {
            Logger.getLogger(Invita.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (invitati) {
        response.sendRedirect(request.getContextPath() + "/LoggedHome");
        } else {
            out.println("Non Eliminati");

        }
                    

        out.println("</form>");
        out.println("</body>");
        out.println("</html>");
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
