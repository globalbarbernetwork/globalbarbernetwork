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
package org.globalbarbernetwork.bo;

import javax.servlet.http.HttpServletRequest;
import org.globalbarbernetwork.entities.Client;
import org.globalbarbernetwork.entities.Hairdressing;
import org.globalbarbernetwork.entities.User;
import org.globalbarbernetwork.firebase.FirebaseDAO;

/**
 *
 * @author Grup 3
 */
public class UserBO {

    private final FirebaseDAO firebaseDAO = new FirebaseDAO();

    /**
     *
     * It is a empty constructor.
     *
     */
    public UserBO() {

    }

    /**
     *
     * Gets the user by type (client or hairdressing)
     *
     * @param uid the uid
     * @return the user by type
     */
    public User getUserByType(String uid) {

        User tmpUser = firebaseDAO.getUser(uid);
        User user = null;

        if (tmpUser != null) {
            String type = tmpUser.getType();
            switch (type) {
                case "hairdressing":
                    user = firebaseDAO.getHairdressing(uid);
                    break;
                case "client":
                    user = firebaseDAO.getClient(uid);
                    break;
            }
        }
        return user;
    }

    /**
     *
     * Insert user by type (client or hairdressing)
     *
     * @param user the user
     */
    public void insertUserByType(User user) {

        if (user != null) {
            switch (user.getType()) {
                case "client":
                    firebaseDAO.insertClient((Client) user);
                    break;
                case "hairdressing":
                    firebaseDAO.insertHairdressing((Hairdressing) user);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     *
     * Update client
     *
     * @param client the client
     * @param request the request
     */
    public void updateClient(Client client, HttpServletRequest request) {

        client.setDisplayName(request.getParameter("displayName"));
        client.setName(request.getParameter("name"));
        client.setSurname(request.getParameter("surname"));
        client.setPhoneNumber(request.getParameter("phoneNumber"));

        request.getSession().setAttribute("user", client);

        firebaseDAO.updateClient(client);
    }

    /**
     *
     * Update hairdressing
     *
     * @param hairdressing the hairdressing
     * @param request the request
     */
    public void updateHairdressing(Hairdressing hairdressing, HttpServletRequest request) {

        hairdressing.setDisplayName(request.getParameter("displayName"));
        hairdressing.setAddress(request.getParameter("address"));
        hairdressing.setCompanyName(request.getParameter("companyName"));
        hairdressing.setInstagram(request.getParameter("instagram"));
        hairdressing.setWebsite(request.getParameter("website"));
        hairdressing.setDescription(request.getParameter("description"));
        hairdressing.setPhoneNumber(request.getParameter("phoneNumber"));
        hairdressing.setZipCode(request.getParameter("zipCode"));
        firebaseDAO.updateHairdressing(hairdressing);

        request.getSession().setAttribute("user", hairdressing);
    }

    /**
     *
     * Change password user
     *
     * @param user the user
     * @param request the request
     */
    public void changePassword(User user, HttpServletRequest request) {

        String newPassword = request.getParameter("newPassword");
        firebaseDAO.changePassword(user.getUID(), newPassword);
    }
}
