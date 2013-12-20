/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import DB.DBManager;
import DB.Groups;
import DB.Users;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author djinask
 */
public class ProfiloUtente extends HttpServlet {

    private DBManager manager;

    Users user = null;

    private String dirName;

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

            response.setContentType("text/html;charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                /* TODO output your page here. You may use following sample code. */
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>"
                        + "<link href=\"css/bootstrap.css\" rel=\"stylesheet\">");
            out.println("<link href=\"css/bootstrap-theme.css\" rel=\"stylesheet\">");
                out.println("<title>Profilo Utente</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<ul class=\"nav nav-tabs\">\n"
                    + "  <li><a href=\"LoggedHome\">Home</a></li>\n"
                    + "  <li class=\"active\"><a href=\"ProfiloUtente\">Profile</a></li>\n"
                    + "  <li style=\"float:right;position:relative;margin-right:1em;\"><a href=\"Logout\">Logout</a></li>\n"
                    + "</ul>");

                out.println("<div name = \"form\" style=\"min-height:100px;\">");
                out.println("<form class=\"navbar-form navbar-left\" "
                        + "action=\"ProfiloUtente\" "
                        + "enctype=\"multipart/form-data\" "
                        + "method=\"post\">");

                out.println("<div class=\"input-group input-group-lg\">");
                out.println("<span class=\"input-group-addon\">Avatar</span>");

                out.println("<input type=\"FILE\" name=\"avatar\">");
                out.println("<button  class=\"btn btn-default btn-lg btn-primary\" type=\"submit\">Crea</button>");
                out.println("</div>");

                out.println("</form>");

                out.println("</div>");

                out.println("</body>");
                out.println("</html>");
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
        doGet(request, response);
        MultipartRequest multi;
        String destination = getServletContext().getRealPath("/") + "/users";
        File theFile = new File(destination + "/" + user.getId());
        theFile.mkdirs();

        multi = new MultipartRequest(request, theFile.toString(), 10 * 1024 * 1024,
                "ISO-8859-1", new DefaultFileRenamePolicy());

        String file = null;
        file = multi.getFile("avatar").getName();
        try {
            manager.updateAvatar(user.getId(), "users/" + theFile.getName() + "/" + file);
            user = manager.getUser(user.getId());
            HttpSession session;
            session = request.getSession(false);

            session.setAttribute("user", user);
        } catch (SQLException ex) {
            Logger.getLogger(ProfiloUtente.class.getName()).log(Level.SEVERE, null, ex);
        }
        PrintWriter out = response.getWriter();
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
