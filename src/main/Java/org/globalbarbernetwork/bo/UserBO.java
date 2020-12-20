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
package org.globalbarbernetwork.bo;

import com.google.cloud.firestore.DocumentReference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord.UpdateRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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

    public UserBO() {
    }

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

    public Map<String, Boolean> updateClient(Client client, HttpServletRequest request) {
        Map<String, Boolean> response = new HashMap<String, Boolean>();
        String newPassword = request.getParameter("newPassword");        
        
        if(!Objects.isNull(newPassword)){
            firebaseDAO.changePassword(client.getUID(), newPassword);                
            response.put("password", true);
            return response;
        }

        client.setDisplayName(request.getParameter("displayName"));
        client.setName(request.getParameter("name"));
        client.setSurname(request.getParameter("surname"));
        client.setPhoneNumber(request.getParameter("phoneNumber"));
        
        request.getSession().setAttribute("user", client);

        firebaseDAO.updateClient(client);
        response.put("data", true);
        return response;
    }

}
