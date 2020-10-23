/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.xtec.ioc.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 *
 * @author DAW IOC
 */
public class ConnectionFirebase {    
    public Firestore initFirebase() {
        try{
            FileInputStream serviceAccount = new FileInputStream("D:/tmp/global-barber-network-java-firebase-adminsdk-jmudk-8c8c34cb95.json");
            //FileInputStream serviceAccount = new FileInputStream("./firebase/global-barber-network-java-firebase-adminsdk-jmudk-8c8c34cb95.json");
            //FileInputStream serviceAccount = new FileInputStream("./src/main/resources/global-barber-network-java-firebase-adminsdk-jmudk-8c8c34cb95.json");
            
            FirebaseOptions options = new FirebaseOptions.Builder()
              .setCredentials(GoogleCredentials.fromStream(serviceAccount))
              .setDatabaseUrl("https://global-barber-network-java.firebaseio.com")
              .build();

            FirebaseApp.initializeApp(options);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return FirestoreClient.getFirestore();
    }
}
