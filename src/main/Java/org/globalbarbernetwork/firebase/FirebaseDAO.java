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
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.globalbarbernetwork.entities.Hairdressing;

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
    
    public void createUser(String email, String password) throws FirebaseAuthException {
        CreateRequest user = new CreateRequest()
            .setEmail(email)
            .setEmailVerified(false)
            .setPassword(password)
            //                .setPhoneNumber("+11234567890")
            .setDisplayName(email)                
            //                .setPhotoUrl("http://www.example.com/12345678/photo.png")
            .setDisabled(false);

        UserRecord userRecord = FirebaseAuth.getInstance().createUser(user);
        System.out.println("Successfully created new user: " + userRecord.getUid());

        //sendEmailVerificationAccount(email);
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
