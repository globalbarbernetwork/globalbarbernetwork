/*
 * Copyright (C) 2020 Grup 3
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.globalbarbernetwork.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.globalbarbernetwork.managers.AccessManager;
import org.globalbarbernetwork.managers.IndexManager;
import org.globalbarbernetwork.managers.ManageHairdressingManager;
import org.globalbarbernetwork.managers.ScheduleManager;
import org.globalbarbernetwork.managers.UserManager;

/**
 *
 * @author Grup 3
 */
@WebServlet(name = "ManagementServlet", urlPatterns = {"/ManagementServlet/*"})
public class ManagementServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String contextPath = request.getPathInfo() != null ? request.getPathInfo() : request.getServletPath();

        if (contextPath != null) {
            String[] tmpAction = contextPath.split("/");
            String action = tmpAction.length > 2 ? tmpAction[2] : "";
            if ("/".equals(contextPath) || contextPath.startsWith("/ManagementServlet")) {
                IndexManager indexManager = new IndexManager();
                indexManager.execute(request, response, action);
            } else if (contextPath.startsWith("/access")) {
                AccessManager accessManager = new AccessManager();
                accessManager.execute(request, response, action);
            } else if (contextPath.startsWith("/schedule")) {
                ScheduleManager scheduleManager = new ScheduleManager();
                scheduleManager.execute(request, response, action);
            } else if (contextPath.startsWith("/menuOption")) {
                switch (action) {
                    case "manageHaird":
                        action = tmpAction.length > 3 ? tmpAction[3] : "";
                        ManageHairdressingManager menuOptionManager = new ManageHairdressingManager();
                        menuOptionManager.execute(request, response, action);
                        break;
                    case "editProfile":
                        action = tmpAction.length > 3 ? tmpAction[3] : "";
                        UserManager userManager = new UserManager();
                        userManager.execute(request, response, action);
                        break;
                }
            }
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
