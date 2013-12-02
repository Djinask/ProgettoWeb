/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servlet;

import DB.DBManager;
import DB.Groups;
import DB.Invites;
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
public class Inviti extends HttpServlet {
    private DBManager manager;
    
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
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
      HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {

            response.sendRedirect(request.getContextPath() + "/Login");

        } else {
            user = (Users) session.getAttribute("user");
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head><link href=\"css/bootstrap.css\" rel=\"stylesheet\">");
            out.println("<link href=\"css/bootstrap-theme.css\" rel=\"stylesheet\">");
            out.println("<title>Inviti</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class=\"jumbotron\">");
            
             out.println("<h1><span class=\"glyphicon glyphicon-lock\"></span>Inviti di " + user.getName() + "!</h1>");
              out.println("<a class=\"btn btn-primary \" role=\"button\" href=\"Logout\">LOGOUT</a>");
                out.println("</div>");
      out.println("<ul class=\"list-group\">");
         out.println(user);
               List<Invites> inviti = (ArrayList)manager.getInviti(user);
                 //out.println(gruppi);

                if (inviti != null) {
                     
                  
                   
                     for(int i=0; i<inviti.size();i++){
                        
                        out.println("<li class=\"list-group-item\">"
   
                                + " Date:" + inviti.get(i).getData_invito()
                                + " Gruppo: " + inviti.get(i).getNomeGruppo()
                                + " Proprietario: " + inviti.get(i).getFrom()
                      
                                + "</li>"
                                + "</a>");
                        
                     }
                    }
                 else {
                    out.println("<h2>Non vi sono gruppi!</h2>");
                   
                }

               
                //ciclo    out.println("<li class=\"list-group-item\">Cras justo odio</li>");
                out.println("</ul>");

            
            out.println("</body>");
            out.println("</html>");

            //EMETTERE il CODICE HTML della pagine qui
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
            Logger.getLogger(Inviti.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Inviti.class.getName()).log(Level.SEVERE, null, ex);
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
