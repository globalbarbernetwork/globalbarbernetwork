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

import cat.xtec.ioc.firebase.ConnectionFirebase;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;

/**
 *
 * @author Grup 3
 */
public class FirebaseDAO {
    private static Firestore db;

    public FirebaseDAO() {
         if (db == null) {
            ConnectionFirebase connectFb = new ConnectionFirebase();
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
}
