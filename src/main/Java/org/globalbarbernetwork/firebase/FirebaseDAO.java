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
package org.globalbarbernetwork.firebase;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.globalbarbernetwork.entities.Client;
import org.globalbarbernetwork.entities.Hairdressing;
import org.globalbarbernetwork.entities.User;

/**
 *
 * @author Grup 3
 */
public class FirebaseDAO {
    private static Firestore db;    

    public FirebaseDAO() {
         if (db == null) {
            ConnectFirebase connectFb = new ConnectFirebase();
            db = connectFb.initFirebase();
        }
    }
    
    public UserRecord createUser(Client client, String password) {
        UserRecord newUser = null;
        try {
            CreateRequest user = new CreateRequest()
                    .setEmail(client.getEmail())
                    .setEmailVerified(false)
                    .setPassword(password)
                    .setPhoneNumber("+34 "+client.getPhoneNumber())
                    .setDisplayName(client.getDisplayName())
                    .setDisabled(false);
            
            newUser = FirebaseAuth.getInstance().createUser(user);
        } catch (FirebaseAuthException ex) {
            //Crear una clase de excepciones para controlar los diferentes errores de firebase
            Logger.getLogger(FirebaseDAO.class.getName()).log(Level.SEVERE, null, ex);
            ex.getAuthErrorCode();
        }
        
        return newUser;
    }
    
    public String generateLink(String email) throws FirebaseAuthException {
        String link = FirebaseAuth.getInstance().generateEmailVerificationLink(email);
        return link;
    }
    
    public UserRecord getUserByEmail(String email) {
        UserRecord userRecord = null;
        try {
            userRecord = FirebaseAuth.getInstance().getUserByEmail(email);
        } catch (FirebaseAuthException ex) {
            Logger.getLogger(FirebaseDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return userRecord;
    }
    
    public void insertUser(User user){
        Map<String, Object> newUser = new HashMap<>();
        newUser.put("UID", user.getUID());
        newUser.put("displayName", user.getDisplayName());
        newUser.put("email", user.getEmail());
        newUser.put("phoneNumber", user.getPhoneNumber());
        newUser.put("type", user.getType());
        db.collection("users").document().set(newUser);
    }
    
    public void insertClient(Client client){
        Map<String, Object> newUser = new HashMap<>();
        newUser.put("UID", client.getUID());
        newUser.put("displayName", client.getDisplayName());
        newUser.put("email", client.getEmail());
        newUser.put("phoneNumber", client.getPhoneNumber());
        newUser.put("name", client.getName());
        newUser.put("surname", client.getSurname());
        db.collection("clients").document().set(newUser);
    }
    
    public void insertHairdressing(Hairdressing hairDrsg){
        Map<String, Object> newUser = new HashMap<>();
        newUser.put("UID", hairDrsg.getUID());
        newUser.put("address", hairDrsg.getAddress());
        newUser.put("city", hairDrsg.getCity());
        newUser.put("companyName", hairDrsg.getCompanyName());
        newUser.put("coordinates", hairDrsg.getCoordinates());
        newUser.put("country", hairDrsg.getCountry());
        newUser.put("displayName", hairDrsg.getDisplayName());
        newUser.put("email", hairDrsg.getEmail());
        newUser.put("instagram", "");
        newUser.put("phoneNumber", hairDrsg.getPhoneNumber());
        newUser.put("province", hairDrsg.getProvince());
        newUser.put("website", "");
        newUser.put("zipCode", hairDrsg.getZipCode());
        db.collection("hairdressings").document().set(newUser);
    }
}
