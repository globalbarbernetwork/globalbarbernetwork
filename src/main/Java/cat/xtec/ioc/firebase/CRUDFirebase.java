/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.xtec.ioc.firebase;

import cat.xtec.ioc.entity.Hairdressing;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;
import com.google.firebase.cloud.FirestoreClient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DAW IOC
 */
public class CRUDFirebase {

    private static Firestore db;

    public CRUDFirebase() {
        ConnectionFirebase connectFb = new ConnectionFirebase();
        db = connectFb.initFirebase();
    }

    public void createUser() throws FirebaseAuthException {
        CreateRequest request = new CreateRequest()
                .setEmail("user@example.com")
                .setEmailVerified(false)
                .setPassword("secretPassword")
                .setPhoneNumber("+11234567890")
                .setDisplayName("John Doe")
                .setPhotoUrl("http://www.example.com/12345678/photo.png")
                .setDisabled(false);

        UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
        System.out.println("Successfully created new user: " + userRecord.getUid());

    }

    public List<Hairdressing> getAllBarbers() {
        List<Hairdressing> listBarbers = new ArrayList<>();
        ApiFuture<QuerySnapshot> future = db.collection("peluqueria").get();
        List<QueryDocumentSnapshot> documents;
        try {
            documents = future.get().getDocuments();

            if (documents.size() > 0) {
                for (QueryDocumentSnapshot document : documents) {
                    listBarbers.add(document.toObject(Hairdressing.class));
                }
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (ExecutionException ex) {
            ex.printStackTrace();
        }

        return listBarbers;
    }

    public String generateLink() throws FirebaseAuthException {
        String link = FirebaseAuth.getInstance().generateEmailVerificationLink("user@example.com");
        // Construct email verification template, embed the link and send
        // using custom SMTP server.
        //sendCustomEmail(email, displayName, link);
        return link;
    }

    public void atrEmailVerifiedUser() throws FirebaseAuthException {
        UserRecord user = FirebaseAuth.getInstance().getUserByEmail("user@example.com");

        System.out.println("EmailVerified:" + user.isEmailVerified());
    }

    public void addFirebase() {
        try {
            Map<String, Object> docBarber = new HashMap<>();
            docBarber.put("Nombre", "Antonio El Barbas");
            
            Firestore db2 = FirestoreClient.getFirestore();
            ApiFuture<WriteResult> future = db2.collection("peluqueria").document(UUID.randomUUID().toString()).set(docBarber);
            
            System.out.println("Update time ADDED: " + future.get().getUpdateTime());
        } catch (InterruptedException ex) {
            Logger.getLogger(CRUDFirebase.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(CRUDFirebase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
