/*
 * Copyright (C) 2020 Adrian
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public User getCurrentUser(HttpServletRequest request) {
        return (User) request.getSession().getAttribute("user");
    }

    public void buildMenuOptions(HttpServletRequest request, HttpServletResponse response) {

        User user = this.getCurrentUser(request);
        List options = this.buildMenuOptionsByUser(user);

        request.setAttribute("options", options);
    }

    private List buildMenuOptionsByUser(User user) {

        List<Map> options = new ArrayList<Map>();

        if (user instanceof Hairdressing) {
            options.add(addMenuOption("Editar perfil", "/ManagementServlet/menuOption/editProfile/hairdressing", ""));
            options.add(addMenuOption("Gestio calendari", "ServletX", ""));
            options.add(addMenuOption("Gestio ", "/ManagementServlet/menuOption/manageHairdressing/loadEmployee", ""));
            options.add(addMenuOption("Cerrar sesión", "/ManagementServlet/access/logout", ""));
        } else if (user instanceof Client) {
            options.add(addMenuOption("Editar perfil", "/ManagementServlet/menuOption/editProfile/client", ""));
            options.add(addMenuOption("Historial de reserves", "ServletX", ""));
            options.add(addMenuOption("Cerrar sesión", "/ManagementServlet/access/logout", ""));
        }

        return options;
    }

    private Map addMenuOption(String label, String url, String params) {
        Map<String, String> option = new HashMap<>();
        option.put("label", label);
        option.put("url", url);
        option.put("params", params);
        return option;
    }

}
