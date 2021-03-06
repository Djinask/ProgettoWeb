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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
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
        this.manager = (DBManager) super.getServletContext().getAttribute("dbmanager");

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
            out.println("<ul class=\"nav nav-tabs\">\n"
                        + "  <li><a href=\"LoggedHome\">Home</a></li>\n"
                        + "  <li><a href=\"ProfiloUtente\">Profile</a></li>\n"
                        + "  <li style=\"float:right;position:relative;margin-right:1em;\"><a href=\"Logout\">Logout</a></li>\n"
                        + "</ul>");
            out.println("<div class=\"jumbotron\">");

            out.println("<h1><span class=\"glyphicon glyphicon-user\"></span>Inviti di " + user.getName() + "!</h1>");
           

            out.println("</div>");
            out.println("<ul class=\"list-group\">");
         
            
          
           
           List<Invites> inviti = (ArrayList)manager.getInviti(user);
   

            if (inviti != null) {

                for (int i = 0; i < inviti.size(); i++) {

                    out.println("<li class=\"list-group-item\">"
                            + "<div style=\"float:left\">"
                            + " Date:" + inviti.get(i).getData_invito()
                            + " Gruppo: " + inviti.get(i).getNomeGruppo()
                            + " Proprietario: " + inviti.get(i).getFrom()
                            + "</div>"
                            + "<div style=\"float:right\">"
                            + "<a href=\"Inviti?id_invito="
                            + inviti.get(i).getId_invito()
                            + "&action=add\""
                            + " style=\"color:#228b22\"><span class=\"glyphicon glyphicon-ok\"></span></a>"
                            + "&nbsp&nbsp"
                            + "<a href=\"Inviti?id_invito="
                            + inviti.get(i).getId_invito()
                            + "&action=remove\""
                            + " style=\"color:#ff0000\"><span class=\"glyphicon glyphicon-remove\" ></span></a>"
                            + "</div>"
                            + "<br>"
                            + "</li>"
                            + "</a>");

                }
            } else {
                out.println("<h2>Non vi sono inviti!</h2>");

            }

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
         HttpSession session = request.getSession(false);
        this.manager = (DBManager) super.getServletContext().getAttribute("dbmanager");
        
        
        
        
        
       
        Boolean rimosso, aggiunto;

        if (session == null || session.getAttribute("user") == null) {

            response.sendRedirect(request.getContextPath() + "/Login");

        } else {
            user = (Users) session.getAttribute("user");
            
           int id_invito;
           String action;
        
        if(request.getParameter("id_invito")!=null&&request.getParameter("action")!=null){
            id_invito=Integer.parseInt(request.getParameter("id_invito"));
            action= request.getParameter("action");
                try {
                    if(action.equals("remove")){
                    rimosso=this.manager.RemoveInvites(id_invito);
                    }else if(action.equals("add")){
                        aggiunto=this.manager.AddPartecipation(user,id_invito);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Inviti.class.getName()).log(Level.SEVERE, null, ex);
                }
            
            
        }
            
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
            out.println("<ul class=\"nav nav-tabs\">\n"
                        + "  <li><a href=\"LoggedHome\">Home</a></li>\n"
                        + "  <li><a href=\"ProfiloUtente\">Profile</a></li>\n"
                        + "  <li style=\"float:right;position:relative;margin-right:1em;\"><a href=\"Logout\">Logout</a></li>\n"
                        + "</ul>");
            out.println("<div class=\"jumbotron\">");

            out.println("<h1><span class=\"glyphicon glyphicon-user\"></span>Inviti di " + user.getName() + "!</h1>");
            

            out.println("</div>");
            out.println("<ul class=\"list-group\">");
         
            
          
           
           List<Invites> inviti = new ArrayList();
            try {
                inviti = (ArrayList)manager.getInviti(user);
            } catch (SQLException ex) {
                Logger.getLogger(Inviti.class.getName()).log(Level.SEVERE, null, ex);
            }
   

            if (!inviti.isEmpty()) {

                for (int i = 0; i < inviti.size(); i++) {

                    out.println("<li class=\"list-group-item\">"
                            + "<div style=\"float:left\">"
                            + " Date:" + inviti.get(i).getData_invito()
                            + " Gruppo: " + inviti.get(i).getNomeGruppo()
                            + " Proprietario: " + inviti.get(i).getFrom()
                            + "</div>"
                            + "<div style=\"float:right\">"
                            + "<a href=\"Inviti?id_invito="
                            + inviti.get(i).getId_invito()
                            + "&action=add\""
                            + " style=\"color:#228b22\"><span class=\"glyphicon glyphicon-ok\"></span></a>"
                            + "&nbsp&nbsp"
                            + "<a href=\"Inviti?id_invito="
                            + inviti.get(i).getId_invito()
                            + "&action=remove\""
                            + " style=\"color:#ff0000\"><span class=\"glyphicon glyphicon-remove\" ></span></a>"
                            + "</div>"
                            + "<br>"
                            + "</li>"
                            + "</a>");

                }
            } else {
                out.println("<h2>Non vi sono inviti!</h2>");

            }

            out.println("</ul>");

            out.println("</body>");
            out.println("</html>");

            //EMETTERE il CODICE HTML della pagine qui
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
