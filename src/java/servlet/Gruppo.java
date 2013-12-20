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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    static int group_id;
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
                out.println("<head>");
                out.println("<meta charset=\"UTF-8\">");
                out.println("<link href=\"css/bootstrap.min.css\" rel=\"stylesheet\">");
                out.println("<link href=\"css/bootstrap-theme.css\" rel=\"stylesheet\">");
                out.println("<script src=\"https://code.jquery.com/jquery-1.10.2.min.js\"></script>");

                out.println("<title>Gruppi utente</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<ul class=\"nav nav-tabs\">\n"
                        + "  <li><a href=\"LoggedHome\">Home</a></li>\n"
                        + "  <li><a href=\"ProfiloUtente\">Profile</a></li>\n"
                        + "  <li style=\"float:right;position:relative;margin-right:1em;\"><a href=\"Logout\">Logout</a></li>\n"
                        + "</ul>");
                out.println("<div class=\"jumbotron\">");
                out.println("<h1>Gruppo " + group.getGroupName());

                out.println("<br><small>" + group.getThread() + "</small></h1>");

                out.println("</div>");
                out.println("<div name = \"form\" style=\"min-height:100px;\">");
                out.println("<form class=\"navbar-form navbar-left\" "
                        + "accept-charset=\"UTF-8\" "
                        + "action=\"Gruppo\" "
                        + "enctype=\"multipart/form-data\" "
                        + "method=\"post\">");

                out.println("<div class=\"input-group input-group-lg\">");
                out.println("<span class=\"input-group-addon\">Post</span>");

                out.println("<input type=\"text\" name=\"post\" class=\"form-control\" placeholder=\"Scrivi qui il tuo post\">");
                out.println("<input type=\"FILE\" multiple name=\"file\">");

                out.println("</div>");

                out.println("</form>");

                out.println("</div>");
                out.println("<ul class=\"nav nav-tabs\" >"
                        + "  <li class=\"dropdown\" style=\"float:left;\"> "
                        + "<a class=\"dropdown-toggle\" data-toggle=\"dropdown\" href=\"#\">\n"
                        + "      Partecipanti <span class=\"caret\"></span>\n"
                        + "    </a>"
                        + "    <ul class=\"dropdown-menu\">"
                        + "<li><b>"
                        + group.getOwner()
                        + "</b></li>");
                List<Users> utenti = new ArrayList();
                try {
                    utenti = (ArrayList) manager.getUsersGroup(user, group.getId(), false);
                } catch (SQLException ex) {
                    Logger.getLogger(Gruppo.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (utenti != null) {
                    for (int i = 0; i < utenti.size(); i++) {
                        out.println("<li class=\"list-group-item\">"
                                + utenti.get(i).getName()
                                + "</li>");
                    }
                }

                out.println("    </ul>"
                        + "  </li>"
                        + "");
                if (user.getId() == group.getAdmin()) {

                    out.println(""
                            + "  <li class=\"dropdown\" style=\"float:right;\"> "
                            + "<a class=\"dropdown-toggle\" data-toggle=\"dropdown\" href=\"#\">\n"
                            + "      Gestisci Gruppo <span class=\"caret\"></span>\n"
                            + "    </a>"
                            + "    <ul class=\"dropdown-menu\">"
                            + "<li><a href=\"Invita?id_group=" + group.getId() + "\">Invita</a></li>"
                            + "<li><a href=\"#\"data-target=\"#myModal\" data-toggle=\"modal\">\n"
                            + "  Modifica Gruppo\n"
                            + "</a></li>"
                            + "<li><a href=\"DeleteUsers?id_group=" + group.getId() + "\">Elimina Utenti</a></li>"
                            + "<li><a href=\"PdfWriter1?id_group="+ group.getId() +"\">Report</a></li>"
                           
                            + "    </ul>"
                            + "  </li>"
                            + "");

                }
                out.println("</ul>");
                out.println("<div class=\"modal fade\" id=\"myModal\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\" aria-hidden=\"true\">\n"
                        + "  <div class=\"modal-dialog\">\n"
                        + "    <div class=\"modal-content\">\n"
                        + "      <div class=\"modal-header\">\n"
                        + "        <button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">&times;</button>\n"
                        + "        <h4 class=\"modal-title\" id=\"myModalLabel\">Edit</h4>\n"
                        + "      </div>\n"
                        + "      <div class=\"modal-body\">\n");
                out.println("<form class=\"navbar-form navbar-left\" action=\"EditGroup?group_id=" + group.getId() + "\" method=\"post\">");
                out.println("<input type=\"text\" name=\"group_name\" class=\"form-control\" placeholder=\"Nome Gruppo\">");
                out.println("<br>");
                out.println("<input type=\"text\" name=\"thread\" class=\"form-control\" placeholder=\"Thread\">");
                out.println("<br>");
                out.println("<input type=\"hidden\" name=\"group_id\" value=\"" + group.getId() + "\">");
                out.println("<br>");
                out.println("<button  class=\"btn btn-default btn-lg btn-primary\" type=\"submit\">Modifica</button>");
                out.println("<br>");
                out.println("</form>");
                out.println("</div>\n"
                        + "      <div class=\"modal-footer\">\n"
                        + "      </div>\n"
                        + "    </div><!-- /.modal-content -->\n"
                        + "  </div><!-- /.modal-dialog -->\n"
                        + "</div><!-- /.modal -->");

                out.println("<div name=\"posts\" class=\"jumbotron\">");
                out.println("<ul class=\"list-group\">");

                List<Posts> posts = (ArrayList) manager.getPosts(group);
                if (posts != null) {
                    for (int i = 0; i < posts.size(); i++) {
                        out.println("<li class=\"list-group-item\" style=\"margin-bottom:5px;\">");
                        out.println("<div name=\"post\" style=\"min-height:70px;overflow:auto;\">");
                        out.println("<div name=\"avatar\" style=\"width:10%;height:70px;float:left;\" >");

                        out.println("<img src=\"" + posts.get(i).getUser_avatar_path() + "\" class=\"img-circle\"  height=\"60\" width=\"60\" />");

                        out.println("</div>");
                        out.println("<div  name=\"content\" style=\"float:right;width:90%;\">");

                        out.println("<p>" + posts.get(i).getCreatore() + " " + posts.get(i).getData() + "</p> ");

                        out.println("<p>" + posts.get(i).getTesto() + "</p> ");

                        if (posts.get(i).isAllegato()) {
                            MyFiles file;
                            file = manager.getAnnex(posts.get(i).getId_post());
                            out.print("<a href=\"Viewer?url=");
                            out.print("files" + file.getPath());
                            out.print("\" >");
                            out.println(file.getName());
                            out.println("<span class=\"glyphicon glyphicon-paperclip\"></span>");
                            out.println("</a>");
                        }

                        out.println("</div>");
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

            List<MyFiles> myFiles = new ArrayList();

            try {

                // Use an advanced form of the constructor that specifies a character
                // encoding of the request (not of the file contents) and a file
                // rename policy.
                MultipartRequest multi;
                String destination = getServletContext().getRealPath("/") + "/files";
                File theFile = new File(destination + "/" + group.getId() + "/" + user.getId());
                theFile.mkdirs();

                multi = new MultipartRequest(request, theFile.toString(), 10 * 1024 * 1024,
                        "ISO-8859-1", new DefaultFileRenamePolicy());

                System.out.println();

                System.out.println("FILES:");

                Enumeration files = null;
                files = multi.getFileNames();

                while (files.hasMoreElements()) {
                    MyFiles mio_file = new MyFiles();

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
                        mio_file.setPath("/" + group.getId() + "/" + user.getId() + "/" + mio_file.getName());

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
                    String testo = multi.getParameter("post");
                    String new_name = null;
                    String new_thread = null;
                    new_name = request.getParameter("group_name");
                    new_thread = request.getParameter("thread");
                    if (new_name != null || new_thread != null) {
                        try {
                            this.manager.editGroup(group.getId(), new_name, new_thread, user);
                        } catch (SQLException ex) {
                            Logger.getLogger(Gruppo.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }

                    String formattedText = new String(testo.getBytes("ISO-8859-15"), "UTF-8");
                    System.out.print("testo=" + formattedText);
                    ;

                    //String formattedText = testo.replace("è", "&egrave;");
                    post.setTesto(getLink(testo));
                    post.setId_creatore(user.getId());
                    post.setCreatore(user.getName());
                    post.setId_gruppo(group_id);
                    post.setAllegato(allegato);

                    try {
                        this.manager.AddPost(post, myFiles);
                    } catch (SQLException ex) {
                        Logger.getLogger(Gruppo.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            } catch (IOException lEx) {

                this.getServletContext().log(lEx, "error reading or saving file");

            }

            response.setContentType("text/html; charset=UTF-8");

            try (PrintWriter out = response.getWriter()) {
                /* TODO output your page here. You may use following sample code. */
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<meta charset=\"UTF-8\">");
                out.println("<link href=\"css/bootstrap.css\" rel=\"stylesheet\">");
                out.println("<link href=\"css/bootstrap-theme.css\" rel=\"stylesheet\">");
                out.println("<script src=\"https://code.jquery.com/jquery-1.10.2.min.js\"></script>");

                out.println("<title>Gruppi utente</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<ul class=\"nav nav-tabs\">\n"
                        + "  <li><a href=\"LoggedHome\">Home</a></li>\n"
                        + "  <li><a href=\"ProfiloUtente\">Profile</a></li>\n"
                        + "  <li style=\"float:right;position:relative;margin-right:1em;\"><a href=\"Logout\">Logout</a></li>\n"
                        + "</ul>");
                out.println("<div class=\"jumbotron\">");
                out.println("<h1>Gruppo " + group.getGroupName());

                out.println("<br><small>" + group.getThread() + "</small></h1>");

                out.println("</div>");
                out.println("<ul class=\"nav nav-tabs\" >"
                        + "  <li class=\"dropdown\" style=\"float:left;\"> "
                        + "<a class=\"dropdown-toggle\" data-toggle=\"dropdown\" href=\"#\">\n"
                        + "      Partecipanti <span class=\"caret\"></span>\n"
                        + "    </a>"
                        + "    <ul class=\"dropdown-menu\">"
                        + "<li><b>"
                        + group.getOwner()
                        + "</b></li>");
                List<Users> utenti = new ArrayList();
                try {
                    utenti = (ArrayList) manager.getUsersGroup(user, group.getId(), false);
                } catch (SQLException ex) {
                    Logger.getLogger(Gruppo.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (utenti != null) {
                    for (int i = 0; i < utenti.size(); i++) {
                        out.println("<li class=\"list-group-item\">"
                                + utenti.get(i).getName()
                                + "</li>");
                    }
                }

                out.println("    </ul>"
                        + "  </li>"
                        + "");
                if (user.getId() == group.getAdmin()) {

                    out.println(""
                            + "  <li class=\"dropdown\" style=\"float:right;\"> "
                            + "<a class=\"dropdown-toggle\" data-toggle=\"dropdown\" href=\"#\">\n"
                            + "      Gestisci Gruppo <span class=\"caret\"></span>\n"
                            + "    </a>"
                            + "    <ul class=\"dropdown-menu\">"
                            + "<li><a href=\"Invita?id_group=" + group.getId() + "\">Invita</a></li>"
                            + "<li><a href=\"#\"data-target=\"#myModal\" data-toggle=\"modal\">\n"
                            + "  Modifica Gruppo\n"
                            + "</a></li>"
                            + "<li><a href=\"DeleteUsers?id_group=" + group.getId() + "\">Elimina Utenti</a></li>"
                            + "<li><a href=\"PdfWriter1?id_group=" + group.getId() + "\">Report</a></li>"
                            + "    </ul>"
                            + "  </li>"
                            + "");

                }
                out.println("</ul>");
                out.println("<div class=\"modal fade\" id=\"myModal\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\" aria-hidden=\"true\">\n"
                        + "  <div class=\"modal-dialog\">\n"
                        + "    <div class=\"modal-content\">\n"
                        + "      <div class=\"modal-header\">\n"
                        + "        <button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">&times;</button>\n"
                        + "        <h4 class=\"modal-title\" id=\"myModalLabel\">Edit</h4>\n"
                        + "      </div>\n"
                        + "      <div class=\"modal-body\">\n");
                out.println("<form class=\"navbar-form navbar-left\" action=\"EditGroup?group_id=" + group.getId() + "\" method=\"post\">");
                out.println("<input type=\"text\" name=\"group_name\" class=\"form-control\" placeholder=\"Nome Gruppo\">");
                out.println("<br>");
                out.println("<input type=\"text\" name=\"thread\" class=\"form-control\" placeholder=\"Thread\">");
                out.println("<br>");
                out.println("<input type=\"hidden\" name=\"group_id\" value=\"" + group.getId() + "\">");
                out.println("<br>");
                out.println("<button  class=\"btn btn-default btn-lg btn-primary\" type=\"submit\">Modifica</button>");
                out.println("<br>");
                out.println("</form>");
                out.println("</div>\n"
                        + "      <div class=\"modal-footer\">\n"
                        + "      </div>\n"
                        + "    </div><!-- /.modal-content -->\n"
                        + "  </div><!-- /.modal-dialog -->\n"
                        + "</div><!-- /.modal -->");
                out.println("<div name = \"form\" style=\"min-height:100px;\">");
                out.println("<form class=\"navbar-form navbar-left\" "
                        + "accept-charset=\"UTF-8\" "
                        + "action=\"Gruppo\" "
                        + "enctype=\"multipart/form-data\" "
                        + "method=\"post\">");

                out.println("<div class=\"input-group input-group-lg\">");
                out.println("<span class=\"input-group-addon\">Post</span>");

                out.println("<input type=\"text\" name=\"post\" class=\"form-control\" placeholder=\"Scrivi qui il tuo post\">");
                out.println("<input type=\"FILE\" multiple name=\"file\">");

                out.println("</div>");

                out.println("</form>");

                out.println("</div>");

                out.println("<div name=\"posts\" class=\"jumbotron\">");
                out.println("<ul class=\"list-group\">");

                List<Posts> posts = (ArrayList) manager.getPosts(group);
                if (posts != null) {
                    for (int i = 0; i < posts.size(); i++) {
                        out.println("<li class=\"list-group-item\" style=\"margin-bottom:5px;\">");
                        out.println("<div name=\"post\" style=\"min-height:70px;overflow:auto;\">");
                        out.println("<div name=\"avatar\" style=\"width:10%;height:70px;float:left;\" >");

                        out.println("<img src=\"" + posts.get(i).getUser_avatar_path() + "\" class=\"img-circle\"  height=\"60\" width=\"60\" />");

                        out.println("</div>");
                        out.println("<div  name=\"content\" style=\"float:right;width:90%;\">");

                        out.println("<p>" + posts.get(i).getCreatore() + " " + posts.get(i).getData() + "</p> ");

                        out.println("<p>" + posts.get(i).getTesto() + "</p> ");

                        if (posts.get(i).isAllegato()) {
                            MyFiles file;
                            file = manager.getAnnex(posts.get(i).getId_post());
                            out.print("<a href=\"");
                            out.print("files" + file.getPath());
                            out.print("\" >");
                            out.println(file.getName());
                            out.println("<span class=\"glyphicon glyphicon-paperclip\"></span>");
                            out.println("</a>");
                        }

                        out.println("</div>");
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

    private String getLink(String testo) {

        String formattedLink = new String();
        Pattern patternLink, pattern$$;
        Matcher matcher$$, matcherLink;

        final String URL_PATTERN = "/((([A-Za-z]{3,9}:(?:\\/\\/)?)(?:[-;:&=\\+\\$,\\w]+@)?[A-Za-z0-9.-]+|(?:www.|[-;:&=\\+\\$,\\w]+@)[A-Za-z0-9.-]+)((?:\\/[\\+~%\\/.\\w-_]*)?\\??(?:[-\\+=&;%@.\\w_]*)#?(?:[\\w]*))?)/";
        final String $$_PATTERN = "\\$\\$\\S(.*?)\\$\\$";

        pattern$$ = Pattern.compile($$_PATTERN);
        patternLink = Pattern.compile(URL_PATTERN);

        matcher$$ = pattern$$.matcher(testo);

        int count = 0;
        while (matcher$$.find()) {
            String link = null;
            System.out.print("parola=" + matcher$$.group().toString());
            System.out.print("start=" + matcher$$.start());
            System.out.print("end=" + matcher$$.end());
            link = (String) testo.subSequence(matcher$$.start() + 2, matcher$$.end() - 2);

            System.out.print(link);
            matcherLink = patternLink.matcher(link);
            if (matcherLink.find()) {
                testo = testo.replace(matcher$$.group().toString(), "<a href=\"" + link + "\">" + link + "</a>");

            } else {

                testo = testo.replace(matcher$$.group().toString(), "<a href=\"http://" + link + "\">" + link + "</a>");
            }
            System.out.print(testo);
            matcher$$ = pattern$$.matcher(testo);
            count++;
        }

        System.out.println(count);

        formattedLink = testo;

        return formattedLink;
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
