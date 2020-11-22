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

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
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
    
    public UserRecord createUser(User user, String password) {
        UserRecord userRecord = null;
        try {
            CreateRequest newUser = new CreateRequest()
                    .setEmail(user.getEmail())
                    .setEmailVerified(false)
                    .setPassword(password)
                    .setPhoneNumber("+34 "+user.getPhoneNumber())
                    .setDisplayName(user.getDisplayName())
                    .setDisabled(false);
            
            userRecord = FirebaseAuth.getInstance().createUser(newUser);
        } catch (FirebaseAuthException ex) {
            //Crear una clase de excepciones para controlar los diferentes errores de firebase
            Logger.getLogger(FirebaseDAO.class.getName()).log(Level.SEVERE, null, ex);
            ex.getAuthErrorCode();
        }
        
        return userRecord;
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
        newUser.put("instagram", hairDrsg.getInstagram());
        newUser.put("phoneNumber", hairDrsg.getPhoneNumber());
        newUser.put("province", hairDrsg.getProvince());
        newUser.put("website", hairDrsg.getWebsite());
        newUser.put("zipCode", hairDrsg.getZipCode());
        db.collection("hairdressings").document().set(newUser);
    }

    public User getUser(String uid) {

        List<User> usersList = new ArrayList<>();
        CollectionReference users = db.collection("users");
        Query query = users.whereEqualTo("UID", uid);

        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        try {
            for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                usersList.add(document.toObject(User.class));
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(FirebaseDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(FirebaseDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return usersList.get(0);
    }

    public Hairdressing getHairdressing(String uid) {
        List<Hairdressing> hairdressingList = new ArrayList<>();
        CollectionReference hairdressings = db.collection("hairdressings");
        Query query = hairdressings.whereEqualTo("UID", uid);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        try {
            for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                hairdressingList.add(document.toObject(Hairdressing.class));
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(FirebaseDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(FirebaseDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hairdressingList.get(0);
    }

    public Client getClient(String uid) {
        List<Client> clientList = new ArrayList<>();
        CollectionReference clients = db.collection("clients");
        Query query = clients.whereEqualTo("UID", uid);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        try {
            for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                clientList.add(document.toObject(Client.class));
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(FirebaseDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(FirebaseDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return clientList.get(0);
    }
    
    public List<Hairdressing> getAllHairdressings(){
        List<Hairdressing> listHairdressings = new ArrayList<>();
        ApiFuture<QuerySnapshot> future = db.collection("hairdressings").get();
        List<QueryDocumentSnapshot> documents;
        try {
            documents = future.get().getDocuments();

            if (documents.size() > 0) {
                for (QueryDocumentSnapshot document : documents) {
                    listHairdressings.add(document.toObject(Hairdressing.class));
                }
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (ExecutionException ex) {
            ex.printStackTrace();
        }

        return listHairdressings;
    }
}
