/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import DB.DBManager;
import DB.Groups;
import DB.Users;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;

import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Color;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;

/**
 *
 * @author djinask
 */
public class PdfWriter1 extends HttpServlet {

    
    private int id_group;
     private DBManager manager;

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
     * // <editor-fold defaultstate="collapsed" desc="HttpServlet methods.
     * Click on the + sign on the left to edit the code.">
     * /**
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
        this.id_group= Integer.parseInt(request.getParameter("id_group"));
        this.manager = (DBManager) super.getServletContext().getAttribute("dbmanager");
                HttpSession session = request.getSession(false);
                
                        Users user = (Users) session.getAttribute("user");
                        Groups gruppo = new Groups();
        try {
            gruppo =  (Groups) manager.getGroup(id_group, user);
        } catch (SQLException ex) {
            Logger.getLogger(PdfWriter1.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        //recupero gli user dal db
        java.util.List<Users> utenti = new ArrayList(); 
        try {
            utenti= this.manager.getUsersGroup(user, id_group, false);
        } catch (SQLException ex) {
            Logger.getLogger(PdfWriter1.class.getName()).log(Level.SEVERE, null, ex);
        }
        

        response.setContentType("application/pdf");
        try {
            // step 1
            Document document = new Document(PageSize.A4,50 , 50, 50, 50);
            // step 2
            PdfWriter.getInstance(document, response.getOutputStream());
            // step 3
            document.open();
            // step 4
            Paragraph p1 = new Paragraph(new Chunk("Report Gruppo: ",
                    FontFactory.getFont(FontFactory.HELVETICA, 20)));
            p1.add(new Phrase(gruppo.getGroupName(), FontFactory.getFont(FontFactory.HELVETICA, 18)));
            document.addTitle("Report" +gruppo.getId());
            
            document.add(p1);


           


            document.add(new Paragraph("Dati Gruppo"));
            List list;

            list = new List(false, 20);

            list.setListSymbol(new Chunk("\u2022",
                    FontFactory.getFont(FontFactory.HELVETICA, 20, Font.BOLD)));
          

            list.add(new ListItem("ID gruppo =" + gruppo.getId()));
            list.add(new ListItem("Nome gruppo =" + gruppo.getGroupName()));
            list.add(new ListItem("Thread gruppo =" + gruppo.getThread()));
           
            list.add(new ListItem("Data creazione =" + gruppo.getData()));
            list.add(new ListItem(new ListItem("Numero post =" )));
             list.add(new ListItem("Proprietario =" + gruppo.getOwner()));

            List sublist;

            sublist = new List(false, true, 10);

            sublist.setListSymbol(new Chunk("", FontFactory.getFont(FontFactory.HELVETICA, 8)));

            sublist.add(new ListItem("Id Proprietario =" + user.getId()));
            sublist.add(new ListItem("Mail Proprietario =" + user.getmail()));


            list.add(sublist);

            

            document.add(list);
            document.add(new Paragraph("\n"));
            

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            
            
            

            PdfPCell cell = new PdfPCell(new Paragraph("Utenti Gruppo " + gruppo.getGroupName()));
            cell.setHorizontalAlignment(300);
            cell.setPaddingLeft(150f);
            cell.setColspan(6);
            cell.setBackgroundColor(new BaseColor(0xC0, 0xC0, 0xC0));
           

            table.addCell(cell);
            

            table.addCell("Id");
            table.addCell("Avatar");
            table.addCell("Nome");
           
            table.addCell("Mail");
            table.addCell("Data ultimo post");
            table.addCell("# Post");
                        table.addCell(String.valueOf(user.getId()));
            Image jpg = Image.getInstance(getServletContext().getRealPath("/")+user.getAvatar_path()); 
            
            table.addCell(jpg);
           
            table.addCell(user.getName());
          
            table.addCell(user.getmail());
            table.addCell("Data ultimo post");
            table.addCell("# Post");
             if (utenti != null) {
                    for (int i = 0; i < utenti.size(); i++) {
                        
            table.addCell(String.valueOf(utenti.get(i).getId()));
             jpg = Image.getInstance(getServletContext().getRealPath("/")+utenti.get(i).getAvatar_path()); 
            
            table.addCell(jpg);
           
            table.addCell(utenti.get(i).getName());
          
            table.addCell(utenti.get(i).getmail());
            table.addCell("Data ultimo post");
            table.addCell("# Post");
                        
                    }
                }

          

            
            document.add(table);

            document.addAuthor("Group 23");

            document.addSubject("PdfProdotto da servlet");

            document.addKeywords("metadata");

            document.addCreator("Group 23");
            // step 5
            document.close();

        } catch (DocumentException de) {

            System.err.println(de.getMessage());

        } catch (IOException ioe) {

            System.err.println(ioe.getMessage());

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
