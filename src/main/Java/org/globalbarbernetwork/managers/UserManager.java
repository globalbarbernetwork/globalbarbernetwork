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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.globalbarbernetwork.bo.UserBO;
import org.globalbarbernetwork.entities.Client;
import org.globalbarbernetwork.entities.User;
import org.globalbarbernetwork.firebase.FirebaseDAO;
import org.globalbarbernetwork.interfaces.ManagerInterface;
import static org.globalbarbernetwork.constants.Constants.*;
import org.globalbarbernetwork.entities.Hairdressing;

/**
 *
 * @author Grup 3
 */
public class UserManager extends Manager implements ManagerInterface {

    private final FirebaseDAO firebaseDAO = new FirebaseDAO();
    private final String CLIENT = "client";
    private final String HAIRDRESSING = "hairdressing";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, String action) {

        RequestDispatcher rd = null;
        UserBO userBo = new UserBO();
        User user = this.getCurrentUser(request);

        switch (action) {
            case CLIENT:
                Client client = null;
                client = (Client) this.getCurrentUser(request);

                if (request.getMethod().equals(POST)) {
                    rd = this.updateClient(client, request, response);
                }else{
                    rd = request.getRequestDispatcher("/" + EDIT_CLIENT_JSP);
                }
                                
                request.setAttribute("client", client);
                break;
            case HAIRDRESSING:
                Hairdressing hairdressing = null;
                hairdressing = (Hairdressing) this.getCurrentUser(request);

                if (request.getMethod().equals(POST)) {
                    rd = this.updateHairdressing(hairdressing, request, response);
                } else {
                    rd = request.getRequestDispatcher("/" + EDIT_HAIRDRESSING_JSP);
                }

                request.setAttribute("hairdressing", hairdressing);
                break;                
            case "changePassword":

                userBo = new UserBO();
                user = (User) this.getCurrentUser(request);
                userBo.changePassword(user, request);
                this.closeSession(request, response);

                break;

        }

        try {
            if (rd != null) {
                this.buildMenuOptions(request, response);
                rd.forward(request, response);
            }
        } catch (ServletException ex) {
            Logger.getLogger(AccessManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AccessManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private RequestDispatcher updateHairdressing(Hairdressing hairdressing, HttpServletRequest request, HttpServletResponse response) {

        UserBO userBo = new UserBO();
        RequestDispatcher rd;

        userBo.updateHairdressing(hairdressing, request);

        rd = request.getRequestDispatcher("/" + EDIT_HAIRDRESSING_JSP);
        return rd;

    }

    private RequestDispatcher updateClient(Client client, HttpServletRequest request, HttpServletResponse response) {

        UserBO userBo = new UserBO();
        RequestDispatcher rd;

        userBo.updateClient(client, request);

        rd = request.getRequestDispatcher("/" + EDIT_CLIENT_JSP);
        return rd;

    }

}
