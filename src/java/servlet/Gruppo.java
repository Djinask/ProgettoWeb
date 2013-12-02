/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import DB.DBManager;
import DB.Groups;
import DB.Posts;
import DB.Users;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Timestamp;
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
import org.apache.derby.client.am.DateTime;

/**
 *
 * @author djinask
 */
public class Gruppo extends HttpServlet {

    private DBManager manager;
    Groups group = null;
    Users user = null;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy h:mm:ss k");

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
            int group_id = Integer.parseInt(request.getParameter("id"));

            try {
                group = manager.getGroup(group_id, user);
            } catch (SQLException ex) {
                Logger.getLogger(Gruppo.class.getName()).log(Level.SEVERE, null, ex);
            }

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
                out.println("<h1>Gruppo " + group.getGroupName() + "</h1>");
                out.println("<a class=\"btn btn-primary \" role=\"button\" href=\"Logout\">LOGOUT</a>");
                                out.println("<a class=\"btn btn-primary \" role=\"button\" href=\"Invita?id_group="+group.getId()+"\">INVITA</a>");

                out.println("</div>");
               
out.println("<div name = \"form\">");
                out.println("<form class=\"navbar-form navbar-left\"action=\"Gruppo\" method=\"post\">");
                out.println("<center>");
                out.println("<div class=\"input-group input-group-lg\">");
                out.println("<span class=\"input-group-addon\">Post</span>");

                out.println("<input type=\"text\" name=\"post\" class=\"form-control\" placeholder=\"Scrivi qui il tuo post\">");
                out.println("</div>");
                out.println("<br>");

                out.println("<br>");
                out.println("<button  class=\"btn btn-default btn-lg\" type=\"submit\">Crea</button>");
                out.println("<br>");
                out.println("</center>");
                out.println("</form>");
                out.println("</div>");
                  out.println("<br>");
                   out.println("<div name=\"post\">");
                out.println("<ul class=\"list-group\">");
            
                List<Posts> posts = (ArrayList) manager.getPosts(group);
                if (posts != null) {
                    for (int i = 0; i < posts.size(); i++) {
                        out.println("<li class=\"list-group-item\">"
                                + " Autore: " + posts.get(i).getCreatore()
                                + "<br> Data: " + posts.get(i).getData()
                                + "<br> Testo:" + posts.get(i).getTesto()
                                + "</li>"
                                + "</a>");
                    }
                } else {
                    out.println("<h2>Non vi sono Post!</h2>");

                }

                //ciclo    out.println("<li class=\"list-group-item\">Cras justo odio</li>");
                out.println("</ul>");
                  out.println("</div>");
                out.println("</body>");
                out.println("</html>");
            } catch (SQLException ex) {
                Logger.getLogger(Gruppo.class.getName()).log(Level.SEVERE, null, ex);
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
        if (session == null || session.getAttribute("user") == null) {

            response.sendRedirect(request.getContextPath() + "/Login");

        } else {

            String Post = request.getParameter("post");
            try {
                Boolean fatto = this.manager.AddPost(Post, user, group);
            } catch (SQLException ex) {
                Logger.getLogger(Gruppo.class.getName()).log(Level.SEVERE, null, ex);
            }

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
                out.println("<h1>Gruppo " + group.getGroupName() + "</h1>");
                out.println("<a class=\"btn btn-primary \" role=\"button\" href=\"Logout\">LOGOUT</a>");
                                                out.println("<a class=\"btn btn-primary \" role=\"button\" href=\"Invita?id_group="+group.getId()+"\">INVITA</a>");

                out.println("</div>");
       out.println("<div name=\"form\">");

                out.println("<form class=\"navbar-form navbar-left\"action=\"Gruppo\" method=\"post\">");
                out.println("<center>");
                out.println("<div class=\"input-group input-group-lg\">");
                out.println("<span class=\"input-group-addon\">Post</span>");

                out.println("<input type=\"text\" name=\"post\" class=\"form-control\" placeholder=\"Scrivi qui il tuo post\">");
                out.println("</div>");
                out.println("<br>");

                out.println("<br>");
                out.println("<button  class=\"btn btn-default btn-lg\" type=\"submit\">Crea</button>");
                out.println("<br>");
                out.println("</center>");
                out.println("</form>");
                    out.println("</div>");
                      out.println("<br>");
                    out.println("<div name=\"post\">");
                out.println("<ul class=\"list-group\">");
            
                List<Posts> posts = (ArrayList) manager.getPosts(group);
                if (posts != null) {
                    for (int i = 0; i < posts.size(); i++) {
                        out.println("<li class=\"list-group-item\">"
                                + " Autore: " + posts.get(i).getCreatore()
                                + "<br> Data: " + posts.get(i).getData()
                                + "<br> Testo:" + posts.get(i).getTesto()
                                + "</li>"
                                + "</a>");
                    }
                } else {
                    out.println("<h2>Non vi sono Post!</h2>");

                }

                //ciclo    out.println("<li class=\"list-group-item\">Cras justo odio</li>");
                out.println("</ul>");
                  out.println("</div>");
                out.println("</body>");
                out.println("</html>");
            } catch (SQLException ex) {
                Logger.getLogger(Gruppo.class.getName()).log(Level.SEVERE, null, ex);
            }
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
