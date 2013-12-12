/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.oreilly.servlet.MultipartRequest;

import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import java.util.Enumeration;

/**
 *
 * @author djinask
 */
public class Upload extends HttpServlet {

    private String dirName;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     *
     *
     *
     */
    public void init(ServletConfig config) throws ServletException {

        super.init(config);

        // read the uploadDir from the servlet parameters
        dirName = config.getInitParameter("uploadDir");

        if (dirName == null) {

            throw new ServletException("Please supply uploadDir parameter");

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
        // processRequest(request, response);

        PrintWriter out = response.getWriter();

        response.setContentType("text/plain");

        out.println("Demo Upload Servlet using MultipartRequest");

        out.println();

        try {

            // Use an advanced form of the constructor that specifies a character
            // encoding of the request (not of the file contents) and a file
            // rename policy.
            MultipartRequest multi
                    = new MultipartRequest(request, dirName, 10 * 1024 * 1024,
                            "ISO-8859-1", new DefaultFileRenamePolicy());

            out.println("PARAMS:");

            Enumeration params = multi.getParameterNames();

            while (params.hasMoreElements()) {

                String name = (String) params.nextElement();

                String value = multi.getParameter(name);

                out.println(name + "=" + value);

            }

            out.println();

            out.println("FILES:");

            Enumeration files = multi.getFileNames();

            while (files.hasMoreElements()) {

                String name = (String) files.nextElement();

                String filename = multi.getFilesystemName(name);

                String originalFilename = multi.getOriginalFileName(name);

                String type = multi.getContentType(name);

                File f = multi.getFile(name);

                out.println("name: " + name);

                out.println("filename: " + filename);

                out.println("originalFilename: " + originalFilename);

                out.println("type: " + type);

                if (f != null) {

                    out.println("f.toString(): " + f.toString());

                    out.println("f.getName(): " + f.getName());

                    out.println("f.exists(): " + f.exists());

                    out.println("f.length(): " + f.length());

                }

                out.println();

            }
        } catch (IOException lEx) {

            this.getServletContext().log(lEx, "error reading or saving file");

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
