/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.xtec.ioc.servlet;

import cat.xtec.ioc.entity.Hairdressing;
import cat.xtec.ioc.firebase.CRUDFirebase;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author DAW IOC
 */
@WebServlet(name = "IndexServlet", urlPatterns = {"/index/*"})
public class IndexServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        switch (action) {
            case "loadPage":
                Hairdressing hd = getListHairdressing(request, response);
                request.setAttribute("listHairdressings", hd);
                break;
            default:
                break;
        }

//        String user = (String)request.getAttribute("user");
//        request.setAttribute("user", user);        
//        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
//        rd.forward(request, response);
        response.sendRedirect(request.getContextPath() + "/index.jsp");

    }

    private Hairdressing getListHairdressing(HttpServletRequest request, HttpServletResponse response) {
        CRUDFirebase firebaseDAO = CRUDFirebase.getInstance();

        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();

        List<Hairdressing> listHairdressings = firebaseDAO.getAllBarbers();

        if (listHairdressings != null && !listHairdressings.isEmpty()) {
            JSONObject member = new JSONObject(listHairdressings);
            array.put(member);
            try {
                json.put("jsonArray", member);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }

            try {
                PrintWriter out = response.getWriter();
                out.println(json.toString());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return listHairdressings.get(0);
    }

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
        processRequest(request, response);
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
        processRequest(request, response);
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
