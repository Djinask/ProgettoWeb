/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author djinask
 */
public class Viewer extends HttpServlet {
private static final Logger log = Logger.getLogger(Viewer.class.getName());
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
         String url = (String) getServletContext().getRealPath("/")+request.getParameter("url");

        log.info("The URL is " + url);
        String format = "application/pdf";
//           String format = "image/gif";

        streamBinaryData(url, format, response);

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

    
    
    private void streamBinaryData(
            String urlstr,
            String format,
            HttpServletResponse resp) {

        ServletOutputStream outstr = null;
        String ErrorStr = null;

        try {
            outstr = resp.getOutputStream();

            //find the right MIME type and set it as contenttype
            resp.setContentType(format);
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            try {
                URL url = new URL(urlstr);
                URLConnection urlc = url.openConnection();
                int length = urlc.getContentLength();

                resp.setContentLength(length);
//                resp.setHeader("Content-Length", String.valueOf(+length));
//                resp.setHeader("Content-Disposition", "inline");

                // Use Buffered Stream for reading/writing.
                InputStream in = urlc.getInputStream();
                bis = new BufferedInputStream(in);
                bos = new BufferedOutputStream(outstr);
                byte[] buff = new byte[length];
                int bytesRead;
                // Simple read/write loop.
                while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                    log.info("Got a chunk of " + bytesRead);
                    bos.write(buff, 0, bytesRead);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ErrorStr = "Error Streaming the Data";
                outstr.print(ErrorStr);
            } finally {
                log.info("finally!!!");
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.close();
                }
                if (outstr != null) {
                    outstr.flush();
                    outstr.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
