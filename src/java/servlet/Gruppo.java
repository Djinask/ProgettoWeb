/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import DB.DBManager;
import DB.Groups;
import DB.MyFiles;
import DB.Posts;
import DB.Users;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
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
public class Gruppo extends HttpServlet {

    private DBManager manager;
    Groups group = null;
    Users user = null;
    int group_id;
    Boolean fatto;
    private String dirName;
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
    public void init(ServletConfig config) throws ServletException {

        super.init(config);

        // read the uploadDir from the servlet parameters
        dirName = config.getInitParameter("uploadDir");

        if (dirName == null) {

            throw new ServletException("Please supply uploadDir parameter");

        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        this.manager = (DBManager) super.getServletContext().getAttribute("dbmanager");
        if (session == null || session.getAttribute("user") == null) {

            response.sendRedirect(request.getContextPath() + "/Login");

        } else {
            user = (Users) session.getAttribute("user");

            if (request.getParameter("id") != null) {
                group_id = Integer.parseInt(request.getParameter("id"));
            }
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
                out.println("<script src=\"https://code.jquery.com/jquery-1.10.2.min.js\"></script>");

                out.println("<title>Gruppi utente</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<div class=\"jumbotron\">");
                out.println("<h1>Gruppo " + group.getGroupName() + "</h1>");
                out.println("<a class=\"btn btn-primary \" role=\"button\" href=\"Logout\">LOGOUT</a>");
                out.println("<a class=\"btn btn-primary \" role=\"button\" href=\"LoggedHome\">HOME</a>");
                if (user.getId() == group.getAdmin()) {

                    out.println("<a class=\"btn btn-primary \" role=\"button\" href=\"Invita?id_group=" + group.getId() + "\">INVITA</a>");
                    out.println("<a class=\"btn btn-primary \" role=\"button\" href=#>MODIFICA TITOLO</a>");
                    out.println("<a class=\"btn btn-primary \" role=\"button\" href=#>REPORT</a>");

                }
                out.println("</div>");

                out.println("<div name = \"form\" style=\"min-height:100px;\">");
                out.println("<form class=\"navbar-form navbar-left\" "
                        + "action=\"Gruppo\" "
                        + "enctype=\"multipart/form-data\" "
                        + "method=\"post\">");

                out.println("<div class=\"input-group input-group-lg\">");
                out.println("<span class=\"input-group-addon\">Post</span>");

                out.println("<input type=\"text\" name=\"post\" class=\"form-control\" placeholder=\"Scrivi qui il tuo post\">");
                out.println("<input type=\"FILE\" name=\"file\">");

                out.println("</div>");

                out.println("</form>");

                out.println("</div>");

                out.println("<div name=\"post\">");
                out.println("<ul class=\"list-group\">");

                List<Posts> posts = (ArrayList) manager.getPosts(group);
                if (posts != null) {
                    for (int i = 0; i < posts.size(); i++) {
                        out.println("<li class=\"list-group-item\">"
                                + " Autore: " + posts.get(i).getCreatore()
                                + "<br> Data: " + posts.get(i).getData()
                                + "<br> Testo:" + posts.get(i).getTesto()
                        );
                        if (posts.get(i).isAllegato()) {
                            out.println("<span class=\"glyphicon glyphicon-paperclip\"></span>");
                        }
                        out.println("</li>");

                    }
                } else {
                    out.println("<h2>Non vi sono Post!</h2>");

                }

                //ciclo    out.println("<li class=\"list-group-item\">Cras justo odio</li>");
                out.println("</ul>");
                out.println("</div>");

                out.println("<script src=\"js/bootstrap.js\"></script>");
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

            Posts post = new Posts();

            List<MyFiles> myFiles= new ArrayList();
            

            try {

                // Use an advanced form of the constructor that specifies a character
                // encoding of the request (not of the file contents) and a file
                // rename policy.
                MultipartRequest multi;
                String destination = dirName; // main location for uploads
                File theFile = new File(destination + "/" + group.getGroupName() + "/" + user.getmail());
                theFile.mkdirs();

                multi = new MultipartRequest(request, theFile.toString(), 10 * 1024 * 1024,
                        "ISO-8859-1", new DefaultFileRenamePolicy());

                System.out.println();

                System.out.println("FILES:");

                Enumeration files = null;
                files = multi.getFileNames();

                while (files.hasMoreElements()) {
                    MyFiles mio_file= new MyFiles();
                   
                    

                    String name = (String) files.nextElement();
                    

                    String filename = multi.getFilesystemName(name);
                     mio_file.setName(filename);

                    String originalFilename = multi.getOriginalFileName(name);
                    mio_file.setOrigin_name(originalFilename);

                    String type = multi.getContentType(name);
                    mio_file.setType(type);

                    File f = multi.getFile(name);

                    System.out.println("name: " + name);

                    System.out.println("filename: " + filename);

                    System.out.println("originalFilename: " + originalFilename);

                    System.out.println("type: " + type);

                    if (f != null) {

                        System.out.println("f.toString(): " + f.toString());
                        mio_file.setPath(f.toString());

                        System.out.println("f.getName(): " + f.getName());

                        System.out.println("f.exists(): " + f.exists());

                        System.out.println("f.length(): " + f.length());
                        mio_file.setLength(f.length());
                        
                        
                        

                    }
                    myFiles.add(mio_file);

                    System.out.println();

                    Boolean allegato = false;
                    if (filename != null) {
                        allegato = true;

                    }
                    post.setTesto(multi.getParameter("post"));
                    post.setId_creatore(user.getId());
                    post.setCreatore(user.getName());
                    post.setId_gruppo(group_id);
                    post.setAllegato(allegato);

                    try {
                        this.manager.AddPost(post,myFiles);
                    } catch (SQLException ex) {
                        Logger.getLogger(Gruppo.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            } catch (IOException lEx) {

                this.getServletContext().log(lEx, "error reading or saving file");

            }

            response.setContentType("text/html;charset=UTF-8");

            try (PrintWriter out = response.getWriter()) {
                /* TODO output your page here. You may use following sample code. */
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head><link href=\"css/bootstrap.css\" rel=\"stylesheet\">");
                out.println("<link href=\"css/bootstrap-theme.css\" rel=\"stylesheet\">");
                out.println("<script src=\"https://code.jquery.com/jquery-1.10.2.min.js\"></script>");

                out.println("<title>Gruppi utente</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<div class=\"jumbotron\">");
                out.println("<h1>Gruppo " + group.getGroupName() + "</h1>");
                out.println("<a class=\"btn btn-primary \" role=\"button\" href=\"Logout\">LOGOUT</a>");
                out.println("<a class=\"btn btn-primary \" role=\"button\" href=\"LoggedHome\">HOME</a>");
                if (user.getId() == group.getAdmin()) {

                    out.println("<a class=\"btn btn-primary \" role=\"button\" href=\"Invita?id_group=" + group.getId() + "\">INVITA</a>");
                    out.println("<a class=\"btn btn-primary \" role=\"button\" href=#>MODIFICA TITOLO</a>");
                    out.println("<a class=\"btn btn-primary \" role=\"button\" href=#>REPORT</a>");

                }

                out.println("</div>");

                out.println("<div name = \"form\" style=\"min-height:100px;\">");

                out.println("<form class=\"navbar-form navbar-left\" "
                        + "action=\"Gruppo\" "
                        + "enctype=\"multipart/form-data\" "
                        + "method=\"post\">");

                out.println("<div class=\"input-group input-group-lg\">");
                out.println("<span class=\"input-group-addon\">Post</span>");

                out.println("<input type=\"text\" name=\"post\" class=\"form-control\" placeholder=\"Scrivi qui il tuo post\">");
                out.println("<input type=\"FILE\" name=\"file\">");

                out.println("</div>");

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
                        );
                        if (posts.get(i).isAllegato()) {
                            out.println("<span class=\"glyphicon glyphicon-paperclip\"></span>");
                        }
                        out.println("</li>");

                    }
                } else {
                    out.println("<h2>Non vi sono Post!</h2>");

                }

                out.println("</ul>");
                out.println("</div>");
                out.println("<script src=\"js/bootstrap.js\"></script>");
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
