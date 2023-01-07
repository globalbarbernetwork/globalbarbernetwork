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

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import java.io.FileInputStream;
import java.io.IOException;
import com.google.cloud.firestore.Firestore;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.globalbarbernetwork.constants.Constants;

/**
 *
 * @author Grup 3
 */
public class ConnectFirebase {
    private Properties props;
    

/** 
 * It is a constructor, do the connection with firebase with properties. 
 *
 */
    public ConnectFirebase() { 
        props = new Properties();
        try {
            InputStream is = new FileInputStream(Constants.PATH_PROPERTIES);
            props.load(is);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    

/** 
 * This method initialize firebase, setting all the necesary config options
 *
 * @return Firestore
 */
    public Firestore initFirebase() { 
        FileInputStream serviceAccount = null;
        try {
            serviceAccount = new FileInputStream(props.getProperty("pathServiceAccount"));
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://global-barber-network-java.firebaseio.com")
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ConnectFirebase.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ConnectFirebase.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (serviceAccount != null) {
                    serviceAccount.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(ConnectFirebase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return FirestoreClient.getFirestore();
    }
}
