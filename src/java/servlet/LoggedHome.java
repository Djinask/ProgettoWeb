/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import DB.DBManager;
import DB.Users;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author djinask
 */
public class LoggedHome extends HttpServlet {

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
            Users user = (Users) session.getAttribute("user");
            
            int notifiche = 0;
            try {
                notifiche = this.manager.getInvitiNumber(user);
            } catch (SQLException ex) {
                Logger.getLogger(LoggedHome.class.getName()).log(Level.SEVERE, null, ex);
            }
            response.setIntHeader("Refresh", 5);
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head><link href=\"css/bootstrap.css\" rel=\"stylesheet\">");
            out.println("<link href=\"css/bootstrap-theme.css\" rel=\"stylesheet\">");
            out.println("<title>Home</title>");
            out.println("<script type=”text/javascript”><!–"
                    + "setTimeout(\'location.href=\"LoggedHome\"\',2000);"
                    + "–>"
                    + "</script> ");
            out.println("</head>");
            out.println("<body>");
            out.println("<ul class=\"nav nav-tabs\">\n"
                    + "  <li class=\"active\"><a href=\"LoggedHome\">Home</a></li>\n"
                    + "  <li><a href=\"ProfiloUtente\">Profile</a></li>\n"
                    + "  <li style=\"float:right;position:relative;margin-right:1em;\"><a href=\"Logout\">Logout</a></li>\n"
                    + "</ul>");
            out.println("<div class=\"jumbotron\">");
            out.println("<h4>"+ new Time(session.getLastAccessedTime())+"</h4>");
            out.println("<h4>"+ session.getCreationTime()+"</h4>");
            out.println("<h1>");
            if (user.getAvatar_path().equals("null")) {
                out.println("<span class=\"glyphicon glyphicon-user\"></span>");
            } else {
                out.println("<img src=\"" + user.getAvatar_path() + "\" class=\"img-thumbnail\"  height=\"60\" width=\"60\" />");
            }
            out.println("Benvenuto,  " + user.getName() + "!</h1>");
           
            out.println("</div>");
            out.println("<div class=\"btn-toolbar\" role=\"toolbar\">");
            out.println("<div class=\"btn-group btn-group-justified\">");

            out.println("<a class=\"btn btn-default\" href=\"ShowGroups\" role=\"button\">Gruppi</a>");
            out.println("<a class=\"btn btn-default\" href=\"CreaGruppo\" role=\"button\" >Crea Gruppo</a>");
            out.println("<a class=\"btn btn-default\" href=\"Inviti\" role=\"button\"  >Gestisci inviti &nbsp&nbsp");
            if (notifiche > 0) {
                out.println("<span class=\"badge\">"
                        + notifiche
                        + "</span>");
            }
            out.println("</a>");
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
