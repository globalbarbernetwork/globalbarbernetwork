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
package org.globalbarbernetwork.managers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.globalbarbernetwork.entities.Client;
import org.globalbarbernetwork.entities.Hairdressing;
import org.globalbarbernetwork.entities.User;

/**
 *
 * @author Grup 3
 */
public class Manager {

    /**
     * This method return the current user logged in the app
     *
     * @param request the request
     * @return the current user
     */
    public User getCurrentUser(HttpServletRequest request) {
        return (User) request.getSession().getAttribute("user");
    }

    /**
     * This method close the session for the current user
     *
     * @param request the request
     * @param response the response
     */
    public void closeSession(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().removeAttribute("user");

        try {
            response.sendRedirect(request.getContextPath() + "/ManagementServlet/index.jsp");
        } catch (IOException ex) {
            Logger.getLogger(AccessManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method build the menu options and adds him to the request
     *
     * @param request the request
     * @param response the response
     */
    public void buildMenuOptions(HttpServletRequest request, HttpServletResponse response) {

        User user = this.getCurrentUser(request);
        List options = this.buildMenuOptionsByUser(user);

        request.setAttribute("options", options);
    }

    /**
     * This method will build the menu options by user
     *
     * @param user the user
     * @return List
     */
    private List buildMenuOptionsByUser(User user) {
        List<Map> options = new ArrayList<Map>();

        if (user instanceof Hairdressing) {
            options.add(addMenuOption("Editar perfil", "/ManagementServlet/menuOption/editProfile/hairdressing/editHairdressing.jsp", ""));
            options.add(addMenuOption("Gestió reserves", "/ManagementServlet/schedule/loadManageReserves/manageReserves.jsp", ""));
            options.add(addMenuOption("Gestió", "/ManagementServlet/menuOption/manageHairdressing/loadListsToManage/manageService.jsp", ""));
            options.add(addMenuOption("Tancar sessió", "/ManagementServlet/access/logout", ""));
        } else if (user instanceof Client) {
            options.add(addMenuOption("Editar perfil", "/ManagementServlet/menuOption/editProfile/client/editClient.jsp", ""));
            options.add(addMenuOption("Historial de reserves", "/ManagementServlet/menuOption/schedule/loadClientHistorical/clientHistorical.jsp", ""));
            options.add(addMenuOption("Tancar sessió", "/ManagementServlet/access/logout", ""));
        }

        return options;
    }

    /**
     * Add menu options
     *
     * @param label the label
     * @param url the url
     * @param params the params
     * @return Map
     */
    private Map addMenuOption(String label, String url, String params) {
        Map<String, String> option = new HashMap<>();
        option.put("label", label);
        option.put("url", url);
        option.put("params", params);
        return option;
    }

}
