/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.xtec.ioc.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Adrian
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/Login", "/Register", "/RegisterHairdressing"})
public class AccessServlet extends HttpServlet {

    final String LOGIN = "/Login";
    final String REGISTER = "/Register";
    final String REGISTER_HAIRDRESSING = "/RegisterHairdressing";

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
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        String path = request.getServletPath();
        RequestDispatcher rd = null;
        switch (path) {
            case LOGIN:
                String formLogin = "<form action='UserServlet/?action=login' method='post'>"                       
                        + "<input type='email' class='form-control' name='email' placeholder='Correu electrònic'/>"
                        + "<input type='password' class='form-control' name='password' placeholder='Contrasenya'/>"                        
                        + "<button type='submit' class='btn btn-primary'>Inicia</button>"
                        + "</form>";
                request.setAttribute("title", "Inicia sessió");
                request.setAttribute("form", formLogin);
                rd = request.getRequestDispatcher("access.jsp");
                rd.forward(request, response);
                break;
            case REGISTER:
                String formRegister = "<form action='UserServlet/?action=newClient' method='post'>"
                        + "<input type='text' class='form-control' name='nom' placeholder='Nom'/>"
                        + "<input type='text' class='form-control' name='cognom' placeholder='Cognom'/>"
                        + "<input type='email' class='form-control' name='email' placeholder='Correu electrònic'/>"
                        + "<input type='password' class='form-control' name='password' placeholder='Contrasenya'/>"
                        + "<input type='password' class='form-control' placeholder='Confirma contrasenya'/>"
                        + "<input type='text' class='form-control' name='phone_number' placeholder='Movil'/>"
                        + "<button type='submit' class='btn btn-primary p-t-5'>Registra</button>"
                        + "</form>";
                request.setAttribute("title", "Registrar-me");
                request.setAttribute("form", formRegister);
                rd = request.getRequestDispatcher("access.jsp");
                rd.forward(request, response);
                
                break;
            case REGISTER_HAIRDRESSING:
                String formRegisterHairdressing = "<form action='UserServlet/?action=newClient' method='post'>"
                        + "<input type='text' class='form-control' name='nom' placeholder='Nom'/>"
                        + "<input type='text' class='form-control' name='cognom' placeholder='Cognom'/>"
                        + "<input type='email' class='form-control' name='email' placeholder='Correu electrònic'/>"
                        + "<input type='password' class='form-control' name='password' placeholder='Contrasenya'/>"
                        + "<input type='password' class='form-control' placeholder='Confirma contrasenya'/>"
                        + "<input type='text' class='form-control' name='phone_number' placeholder='Movil'/>"
                        + "<button type='submit' class='btn btn-primary p-t-5'>Registra</button>"
                        + "</form>";
                request.setAttribute("title", "Registrar-me");
                request.setAttribute("form", formRegisterHairdressing);
                rd = request.getRequestDispatcher("access.jsp");
                rd.forward(request, response);
                
                break;
            default:
                break;
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
