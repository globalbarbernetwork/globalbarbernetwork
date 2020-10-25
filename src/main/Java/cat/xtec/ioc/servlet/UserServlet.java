/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.xtec.ioc.servlet;

import cat.xtec.ioc.firebase.CRUDFirebase;
import cat.xtec.ioc.firebase.MyFirebaseAuth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author DAW IOC
 */
@WebServlet(name = "UserServlet", urlPatterns = {"/UserServlet/*"})
public class UserServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        RequestDispatcher rd = null;
        CRUDFirebase cf = CRUDFirebase.getInstance();

        switch (action) {
            case "login":
                JsonObject user = null;
                try {
                    user = MyFirebaseAuth.getInstance().auth(email, password);
                } catch (Exception ex) {
                    Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
                }

                if (user == null) {

                    String error = "Usuari o contrasenya incorrectes";
                    request.setAttribute("error", error);
                    rd = request.getRequestDispatcher("/Login");
                    rd.forward(request, response);

                } else {
                    request.setAttribute("user", true);
//                    response.sendRedirect(request.getContextPath()+"/index.jsp");
                    rd = request.getRequestDispatcher("/index");
                    rd.forward(request, response);
                }

                break;

            case "newClient":
                try {
                    cf.createUser(email, password);
                } catch (FirebaseAuthException ex) {
                    Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
                }

                response.sendRedirect(request.getContextPath() + "/index.jsp");
                break;
            case "newHairdressing":                
                try {
                    cf.createUser(email, password);
                } catch (FirebaseAuthException ex) {
                    Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
                }

                response.sendRedirect(request.getContextPath() + "/index.jsp");
                break;

            default:
                break;
        }
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
