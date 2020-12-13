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

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.globalbarbernetwork.entities.Client;
import org.globalbarbernetwork.entities.User;
import org.globalbarbernetwork.firebase.FirebaseDAO;
import org.globalbarbernetwork.interfaces.ManagerInterface;

/**
 *
 * @author Adrian
 */
public class UserManager extends Manager implements ManagerInterface {

    private final FirebaseDAO firebaseDAO = new FirebaseDAO();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, String action) {

        RequestDispatcher rd = null;

        switch (action) {
            case "client":

                Client client = null;

                if (request.getMethod().equals("POST")) {

                    //To do
                    String email = request.getParameter("email");

                    User user = this.getCurrentUser(request);
                    client = firebaseDAO.getClient(user.getUID());

                    client.setDisplayName(request.getParameter("displayName"));
                    client.setName(request.getParameter("name"));
                    client.setSurname(request.getParameter("surname"));
                    client.setPhoneNumber(request.getParameter("phoneNumber"));

                    firebaseDAO.updateClient(client);

                } else {
                    client = (Client) this.getCurrentUser(request);
                }

                request.setAttribute("client", client);
                rd = request.getRequestDispatcher("/User/editClient.jsp");

                break;
            case "hairdressing":
                rd = request.getRequestDispatcher("/User/editHairdressing.jsp");
                break;
        }

        try {
            if (rd != null) {
                rd.forward(request, response);
            }
        } catch (ServletException ex) {
            Logger.getLogger(AccessManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AccessManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
