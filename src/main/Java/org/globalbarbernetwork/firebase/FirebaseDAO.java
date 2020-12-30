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
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;
import com.google.firebase.auth.UserRecord.UpdateRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.globalbarbernetwork.entities.Client;
import org.globalbarbernetwork.entities.Employee;
import org.globalbarbernetwork.entities.Hairdressing;
import org.globalbarbernetwork.entities.Reserve;
import org.globalbarbernetwork.entities.Service;
import org.globalbarbernetwork.entities.User;
import static org.globalbarbernetwork.constants.Constants.*;

/**
 *
 * @author Grup 3
 */
public class FirebaseDAO {

    private static Firestore db;

    /**
     *
     * It is a constructor that initialize the connection with Firebase
     *
     */
    public FirebaseDAO() {

        if (db == null) {
            ConnectFirebase connectFb = new ConnectFirebase();
            db = connectFb.initFirebase();
        }
    }

    /**
     *
     * Create user type firebase
     *
     * @param user the user
     * @param password the password
     * @return UserRecord
     */
    public UserRecord createUser(User user, String password) {

        UserRecord userRecord = null;
        try {
            CreateRequest newUser = new CreateRequest()
                    .setEmail(user.getEmail())
                    .setEmailVerified(false)
                    .setPassword(password)
                    .setPhoneNumber("+34 " + user.getPhoneNumber())
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

    /**
     *
     * Generate link to send it by mail and verify yourself
     *
     * @param email the email
     * @return String
     * @throws FirebaseAuthException
     */
    public String generateLink(String email) throws FirebaseAuthException {

        String link = FirebaseAuth.getInstance().generateEmailVerificationLink(email);
        return link;
    }

    /**
     *
     * Gets the user firebase by email
     *
     * @param email the email
     * @return the user by email
     */
    public UserRecord getUserByEmail(String email) {

        UserRecord userRecord = null;
        try {
            userRecord = FirebaseAuth.getInstance().getUserByEmail(email);
        } catch (FirebaseAuthException ex) {
            Logger.getLogger(FirebaseDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return userRecord;
    }

    /**
     *
     * Gets the user firebase by phone
     *
     * @param phone the phone
     * @return the user by phone
     */
    public UserRecord getUserByPhone(String phone) {

        UserRecord userRecord = null;
        try {
            userRecord = FirebaseAuth.getInstance().getUserByPhoneNumber(phone);
        } catch (FirebaseAuthException ex) {
            Logger.getLogger(FirebaseDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return userRecord;
    }

    /**
     *
     * Insert user application
     *
     * @param user the user
     */
    public void insertUser(User user) {

        Map<String, Object> newUser = new HashMap<>();
        newUser.put("uid", user.getUID());
        newUser.put("displayName", user.getDisplayName());
        newUser.put("email", user.getEmail());
        newUser.put("phoneNumber", user.getPhoneNumber());
        newUser.put("type", user.getType());
        db.collection("users").document(user.getUID()).set(newUser);
    }

    /**
     *
     * Insert client which is a type of user
     *
     * @param newClient the new client
     */
    public void insertClient(Client newClient) {

        db.collection("clients").document(newClient.getUID()).set(newClient);
    }

    /**
     *
     * Insert hairdressing which is a type of user
     *
     * @param newHairDrsg the new hairdressing
     */
    public void insertHairdressing(Hairdressing newHairDrsg) {

        db.collection("hairdressings").document(newHairDrsg.getUID()).set(newHairDrsg);
    }

    /**
     *
     * Gets the user application
     *
     * @param uid the uid
     * @return the user
     */
    public User getUser(String uid) {

        List<User> usersList = new ArrayList<>();
        CollectionReference users = db.collection("users");
        Query query = users.whereEqualTo("uid", uid);

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

    /**
     *
     * Gets the hairdressing
     *
     * @param uid the uid
     * @return the hairdressing
     */
    public Hairdressing getHairdressing(String uid) {

        List<Hairdressing> hairdressingList = new ArrayList<>();
        CollectionReference hairdressings = db.collection("hairdressings");
        Query query = hairdressings.whereEqualTo("uid", uid);
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

    /**
     *
     * Gets the client
     *
     * @param uid the uid
     * @return the client
     */
    public Client getClient(String uid) {

        List<Client> clientList = new ArrayList<>();
        CollectionReference clients = db.collection("clients");
        Query query = clients.whereEqualTo("uid", uid);
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

    /**
     *
     * Gets the all hairdressings registered in the application
     *
     * @return the list of all hairdressings
     */
    public List<Hairdressing> getAllHairdressings() {

        List<Hairdressing> listHairdressings = new ArrayList<>();
        ApiFuture<QuerySnapshot> future = db.collection("hairdressings").get();

        try {
            for (QueryDocumentSnapshot document : future.get().getDocuments()) {
                listHairdressings.add(document.toObject(Hairdressing.class));
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (ExecutionException ex) {
            ex.printStackTrace();
        }

        return listHairdressings;
    }

    /**
     *
     * Gets the schedule hairdressing
     *
     * @param uid the uid of hairdressing
     * @return the schedule hairdressing on Map
     */
    public Map<String, Object> getScheduleHairdressing(String uid) {

        DocumentReference docRef = db.collection("schedule").document(uid);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                return document.getData();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(FirebaseDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(FirebaseDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     *
     * Gets the all employees of specific hairdressing
     *
     * @param idHairdressing the id hairdressing
     * @return the list of all employees
     */
    public List<Employee> getAllEmployees(String idHairdressing) {

        List<Employee> listEmployees = new ArrayList<>();
        ApiFuture<QuerySnapshot> hairdressings = db.collection("employees").document(idHairdressing).collection("employees").get();
        try {
            for (QueryDocumentSnapshot document : hairdressings.get().getDocuments()) {
                listEmployees.add(document.toObject(Employee.class));
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (ExecutionException ex) {
            ex.printStackTrace();
        }

        return listEmployees;
    }

    /**
     *
     * Gets the employee by identifier number of specific hairdressing
     *
     * @param idHairdressing the id hairdressing
     * @param idNumber the id number of employee
     * @return the employee
     */
    public Employee getEmployeeByIDNumber(String idHairdressing, String idNumber) {

        Employee employee = null;
        ApiFuture<QuerySnapshot> future = db.collection("employees").document(idHairdressing).collection("employees").whereEqualTo("idNumber", idNumber).get();
        try {
            for (QueryDocumentSnapshot document : future.get().getDocuments()) {
                employee = document.toObject(Employee.class);
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (ExecutionException ex) {
            ex.printStackTrace();
        }

        return employee;
    }

    /**
     *
     * Insert employee on specific hairdressing
     *
     * @param newEmployee the new employee
     */
    public void insertEmployee(Employee newEmployee) {

        db.collection("employees").document(newEmployee.getIdHairdressing()).collection("employees").document().set(newEmployee);
    }

    /**
     *
     * Delete employee on specific hairdressing
     *
     * @param idNumber the id number of employee
     * @param idHairdressing the id hairdressing
     */
    public void deleteEmployee(String idNumber, String idHairdressing) {

        ApiFuture<QuerySnapshot> future = db.collection("employees").document(idHairdressing).collection("employees").whereEqualTo("idNumber", idNumber).get();
        try {
            for (QueryDocumentSnapshot document : future.get().getDocuments()) {
                document.getReference().delete();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (ExecutionException ex) {
            ex.printStackTrace();
        }
    }

    /**
     *
     * Update employee on specific hairdressing
     *
     * @param newEmployee the new employee
     */
    public void updateEmployee(Employee newEmployee) {

        ApiFuture<QuerySnapshot> future = db.collection("employees").document(newEmployee.getIdHairdressing()).collection("employees").whereEqualTo("idNumber", newEmployee.getIdNumber()).get();
        try {
            for (QueryDocumentSnapshot document : future.get().getDocuments()) {
                document.getReference().update(
                        "contractEndDate", newEmployee.getContractEndDate(),
                        "contractIniDate", newEmployee.getContractIniDate(),
                        "idHairdressing", newEmployee.getIdHairdressing(),
                        "idNumber", newEmployee.getIdNumber(),
                        "name", newEmployee.getName(),
                        "phoneNumber", newEmployee.getPhoneNumber(),
                        "surname", newEmployee.getSurname()
                );
            }

        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (ExecutionException ex) {
            ex.printStackTrace();
        }
    }

    /**
     *
     * Update user firebase and client application
     *
     * @param client the client
     */
    public void updateClient(Client client) {

        try {
            UpdateRequest request = new UserRecord.UpdateRequest(client.getUID())
                    .setEmail(client.getEmail())
                    .setPhoneNumber("+34" + client.getPhoneNumber())
                    .setEmailVerified(true)
                    .setDisplayName(client.getDisplayName());

            UserRecord userRecord = FirebaseAuth.getInstance().updateUser(request);

            DocumentReference docRef = db.collection("clients").document(client.getUID());
            docRef.set(client);

        } catch (FirebaseAuthException ex) {
            Logger.getLogger(FirebaseDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * Change password of user
     *
     * @param UID the UID
     * @param newPassword the new password
     */
    public void changePassword(String UID, String newPassword) {

        try {
            UpdateRequest request = new UserRecord.UpdateRequest(UID).setPassword(newPassword);
            UserRecord userRecord = FirebaseAuth.getInstance().updateUser(request);
        } catch (FirebaseAuthException ex) {
            Logger.getLogger(FirebaseDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * Insert holidays employee of a specific hairdressing
     *
     * @param idHairdressing the id hairdressing
     * @param idEmployee the id employee
     * @param holidays the holidays
     */
    public void insertHolidaysEmployee(String idHairdressing, String idEmployee, Map<String, Object> holidays) {

        db.collection("holidaysEmployee").document(idHairdressing).collection("employees").document(idEmployee).set(holidays);
    }

    /**
     *
     * Gets the holidays employee of a specific hairdressing
     *
     * @param idHairdressing the id hairdressing
     * @param idEmployee the id employee
     * @return the holidays employee
     */
    public ArrayList<Timestamp> getHolidaysEmployee(String idHairdressing, String idEmployee) {

        ArrayList<Timestamp> listHolidays = new ArrayList();
        ApiFuture<DocumentSnapshot> future = db.collection("holidaysEmployee").document(idHairdressing).collection("employees").document(idEmployee).get();
        try {
            Map<String, Object> docData = future.get().getData();
            if (docData != null) {
                listHolidays = (ArrayList<Timestamp>) docData.get("holidays");
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (ExecutionException ex) {
            ex.printStackTrace();
        }

        return listHolidays;
    }

    /**
     *
     * Delete holidays employee of a specific hairdressing
     *
     * @param idHairdressing the id hairdressing
     * @param idEmployee the id employee
     */
    public void deleteHolidaysEmployee(String idHairdressing, String idEmployee) {

        db.collection("holidaysEmployee").document(idHairdressing).collection("employees").document(idEmployee).delete();
    }

    /**
     *
     * Gets the all services of a specific hairdressing
     *
     * @param idHairdressing the id hairdressing
     * @return the list of all services
     */
    public List<Service> getAllServices(String idHairdressing) {

        List<Service> listServices = new ArrayList<>();
        ApiFuture<QuerySnapshot> services = db.collection("services").document(idHairdressing).collection("services").get();
        try {
            for (QueryDocumentSnapshot document : services.get().getDocuments()) {
                listServices.add(document.toObject(Service.class));
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (ExecutionException ex) {
            ex.printStackTrace();
        }

        return listServices;
    }

    /**
     *
     * Gets the service by identifier of a specific hairdressing
     *
     * @param idHairdressing the id hairdressing
     * @param idService the id service
     * @return the service
     */
    public Service getServiceById(String idHairdressing, String idService) {

        Service service = null;
        ApiFuture<QuerySnapshot> services = db.collection("services").document(idHairdressing).collection("services").whereEqualTo("id", idService).get();
        try {
            if (services.get().size() == 1) {
                service = (services.get().getDocuments().get(0)).toObject(Service.class);
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (ExecutionException ex) {
            ex.printStackTrace();
        }

        return service;
    }

    /**
     *
     * Insert service of a specific hairdressing
     *
     * @param activeUser the active user (hairdressing)
     * @param service the service
     */
    public void insertService(User activeUser, Service service) {

        String autoId = db.collection("services").document(activeUser.getUID()).collection("services").document().getId();
        service.setId(autoId);
        db.collection("services").document(activeUser.getUID()).collection("services").document(autoId).set(service);
    }

    /**
     *
     * Update service of a specific hairdressing
     *
     * @param activeUser the active user (hairdressing)
     * @param service the service
     */
    public void updateService(User activeUser, Service service) {

        db.collection("services").document(activeUser.getUID()).collection("services").document(service.getId()).set(service);
    }

    /**
     *
     * Delete service by identifier of a specific hairdressing
     *
     * @param activeUser the active user (hairdressing)
     * @param idService the id service
     */
    public void deleteService(User activeUser, Integer idService) {

        ApiFuture<QuerySnapshot> future = db.collection("services").document(activeUser.getUID()).collection("services").whereEqualTo("id", idService).get();
        try {
            for (QueryDocumentSnapshot document : future.get().getDocuments()) {
                document.getReference().delete();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (ExecutionException ex) {
            ex.printStackTrace();
        }
    }

    /**
     *
     * Update user firebase and hairdressing application
     *
     * @param hairdressing the hairdressing
     */
    public void updateHairdressing(Hairdressing hairdressing) {

        try {
            UpdateRequest request = new UserRecord.UpdateRequest(hairdressing.getUID())
                    .setPhoneNumber("+34" + hairdressing.getPhoneNumber())
                    .setEmailVerified(true)
                    .setDisplayName(hairdressing.getDisplayName());

            UserRecord userRecord = FirebaseAuth.getInstance().updateUser(request);

            DocumentReference docRef = db.collection("hairdressings").document(hairdressing.getUID());
            docRef.set(hairdressing);

        } catch (FirebaseAuthException ex) {
            Logger.getLogger(FirebaseDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * Update schedule of a specific hairdressing
     *
     * @param schedule the schedule
     * @param user the user (hairdressing)
     */
    public void updateSchedule(Map<String, Object> schedule, User user) {

        db.collection("schedule").document(user.getUID()).set(schedule);
    }

    /**
     *
     * Gets the reserves employee by state pending
     *
     * @param idHairdressing the id hairdressing
     * @param yearReserve the year reserve
     * @param monthReserve the month reserve
     * @param dateReserve the date reserve
     * @param idEmployee the id employee
     * @return the reserves employee by state pending
     */
    public ArrayList<Reserve> getReservesEmployeeByStatePending(String idHairdressing, String yearReserve, String monthReserve, String dateReserve, String idEmployee) {

        ArrayList<Reserve> listReserve = new ArrayList();
        ApiFuture<QuerySnapshot> reserves = db.collection("reserves").document(idHairdressing).collection(yearReserve).document(monthReserve).collection(dateReserve)
                .whereEqualTo("idEmployee", idEmployee).whereEqualTo("state", STATE_PENDING).get();
        try {
            for (QueryDocumentSnapshot document : reserves.get().getDocuments()) {
                listReserve.add(document.toObject(Reserve.class));
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (ExecutionException ex) {
            ex.printStackTrace();
        }

        return listReserve;
    }

    /**
     *
     * Gets the all reserves employee of a specific hairdressing
     *
     * @param idHairdressing the id hairdressing
     * @param idEmployee the id employee
     * @return the list of all reserves employee
     */
    public ArrayList<Reserve> getAllReservesEmployee(String idHairdressing, String idEmployee) {

        ArrayList<Reserve> listReserve = new ArrayList();
        ApiFuture<QuerySnapshot> reserves = db.collection("reservesEmployee").document(idHairdressing).collection(idEmployee).get();
        try {

            for (QueryDocumentSnapshot document : reserves.get().getDocuments()) {
                DocumentSnapshot reserveRef = ((DocumentReference) document.get("reserveRef")).get().get();
                listReserve.add(reserveRef.toObject(Reserve.class));
            }

        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (ExecutionException ex) {
            ex.printStackTrace();
        }

        return listReserve;
    }

    /**
     *
     * Gets the reserves by state pending
     *
     * @param idHairdressing the id hairdressing
     * @param yearReserve the year reserve
     * @param monthReserve the month reserve
     * @param dateReserve the date reserve
     * @return the list of reserves by state pending
     */
    public ArrayList<Reserve> getReservesByStatePending(String idHairdressing, String yearReserve, String monthReserve, String dateReserve) {

        ArrayList<Reserve> listReserve = new ArrayList();
        ApiFuture<QuerySnapshot> reserves = db.collection("reserves").document(idHairdressing).collection(yearReserve).document(monthReserve).collection(dateReserve)
                .whereEqualTo("state", STATE_PENDING).get();

        try {
            for (QueryDocumentSnapshot document : reserves.get().getDocuments()) {
                listReserve.add(document.toObject(Reserve.class));
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (ExecutionException ex) {
            ex.printStackTrace();
        }

        return listReserve;
    }

    /**
     *
     * Gets the all reserves of a specific hairdressing
     *
     * @param idHairdressing the id hairdressing
     * @return the reserves
     */
    public ArrayList<Reserve> getReserves(String idHairdressing) {

        ArrayList<Reserve> listReserves = new ArrayList();

        try {
            Iterable<CollectionReference> yearsCollections = db.collection("reserves").document(idHairdressing).listCollections();
            for (CollectionReference yearCollection : yearsCollections) {
                //ApiFuture<QuerySnapshot> monthsDocuments = db.collection("reserves").document(idHairdressing).collection(yearCollection.getId()).get();
                for (int i = 1; i <= 12; i++) {
                    Iterable<CollectionReference> datesDocuments = db.collection("reserves").document(idHairdressing).collection(yearCollection.getId()).document(String.valueOf(i)).listCollections();
                    for (CollectionReference dateDocument : datesDocuments) {
                        ApiFuture<QuerySnapshot> reservesDocuments = db.collection("reserves").document(idHairdressing).collection(yearCollection.getId()).document(String.valueOf(i)).collection(dateDocument.getId()).get();
                        for (QueryDocumentSnapshot reserveDocuments : reservesDocuments.get().getDocuments()) {
                            listReserves.add(reserveDocuments.toObject(Reserve.class));
                        }
                    }
                }
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(FirebaseDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(FirebaseDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return listReserves;
    }

    /**
     *
     * Insert reserve
     *
     * @param reserve the reserve
     * @param yearReserve the year reserve
     * @param monthReserve the month reserve
     * @param dateReserve the date reserve
     * @return String path collection
     */
    public String insertReserve(Reserve reserve, String yearReserve, String monthReserve, String dateReserve) {

        String autoId = db.collection("reserves").document(reserve.getIdHairdressing()).collection(yearReserve).document(monthReserve).collection(dateReserve).document().getId();
        reserve.setId(autoId);

        db.collection("reserves").document(reserve.getIdHairdressing()).collection(yearReserve).document(monthReserve).collection(dateReserve).document(autoId).set(reserve);

        return db.collection("reserves").document(reserve.getIdHairdressing()).collection(yearReserve).document(monthReserve).collection(dateReserve).document(autoId).getPath();
    }

    /**
     *
     * Insert reserve client
     *
     * @param idClient the id client
     * @param reference the reference
     * @return String path collection
     */
    public String insertReserveClient(String idClient, String reference) {

        Map<String, Object> data = new HashMap();
        data.put("reserveRef", db.document(reference));

        // Se guarda el id creado por firebase para este nuevo registro
        String autoId = db.collection("reservesClient").document(idClient).collection("reserves").document().getId();
        db.collection("reservesClient").document(idClient).collection("reserves").document(autoId).set(data);

        return db.collection("reservesClient").document(idClient).collection("reserves").document(autoId).getPath();
    }

    /**
     *
     * Update reserve client
     *
     * @param idClient the id client
     * @param idDocument the id document
     * @param reference the reference collection reserve
     * @param referenceEmployee the reference collection reserve employee
     */
    public void updateReserveClient(String idClient, String idDocument, String reference, String referenceEmployee) {

        Map<String, Object> data = new HashMap();
        data.put("reserveEmployeeRef", db.document(referenceEmployee));

        db.document(idDocument).update(data);
    }

    /**
     *
     * Insert reserve employee
     *
     * @param idHairdressing the id hairdressing
     * @param idEmployee the id employee
     * @param reference the reference reserve
     * @param reserveClientRef the reserve client reference
     * @return String path collection
     */
    public String insertReserveEmployee(String idHairdressing, String idEmployee, String reference, String reserveClientRef) {

        Map<String, Object> data = new HashMap();
        data.put("reserveRef", db.document(reference));
        data.put("reserveClientRef", db.document(reserveClientRef));

        // Se guarda el id creado por firebase para este nuevo registro
        String autoId = db.collection("reservesEmployee").document(idHairdressing).collection(idEmployee).document().getId();

        db.collection("reservesEmployee").document(idHairdressing).collection(idEmployee).document(autoId).set(data);

        return db.collection("reservesEmployee").document(idHairdressing).collection(idEmployee).document(autoId).getPath();
    }

    /**
     *
     * Update state reserve
     *
     * @param reserve the reserve
     * @param yearReserve the year reserve
     * @param monthReserve the month reserve
     * @param dateReserve the date reserve
     */
    public void updateStateReserve(Reserve reserve, String yearReserve, String monthReserve, String dateReserve) {

        db.collection("reserves").document(reserve.getIdHairdressing()).collection(yearReserve).document(monthReserve).collection(dateReserve).document(reserve.getId()).set(reserve);
    }

    /**
     *
     * Delete reserves employee
     *
     * @param idHairdressing the id hairdressing
     * @param idEmployee the id employee
     */
    public void deleteReservesEmployee(String idHairdressing, String idEmployee) {

        ApiFuture<QuerySnapshot> reserves = db.collection("reservesEmployee").document(idHairdressing).collection(idEmployee).get();
        try {

            for (QueryDocumentSnapshot document : reserves.get().getDocuments()) {
                // Eliminar registro en reserves
                ((DocumentReference) document.get("reserveRef")).delete();
                // Eliminar registro en reservesClient
                ((DocumentReference) document.get("reserveClientRef")).delete();
                // Eliminar registro en reservesEmployee
                document.getReference().delete();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (ExecutionException ex) {
            ex.printStackTrace();
        }
    }

    /**
     *
     * Update holidays of a specific hairdressing
     *
     * @param hairdressing the hairdressing
     * @param holidays the holidays
     */
    public void updateHolidays(User hairdressing, Map<String, Object> holidays) {

        db.collection("holidays").document(hairdressing.getUID()).set(holidays);
    }

    /**
     *
     * Gets the holidays of a specific hairdressing
     *
     * @param idHairdressing the id hairdressing
     * @return the list of holidays
     */
    public ArrayList<Timestamp> getHolidays(String idHairdressing) {

        ArrayList<Timestamp> listHolidays = new ArrayList();
        ApiFuture<DocumentSnapshot> future = db.collection("holidays").document(idHairdressing).get();
        try {
            Map<String, Object> docData = future.get().getData();
            if (docData != null) {
                listHolidays = (ArrayList<Timestamp>) docData.get("holidays");
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (ExecutionException ex) {
            ex.printStackTrace();
        }

        return listHolidays;
    }

    /**
     *
     * Gets the client historical reserves
     *
     * @param activeUser the active user (client)
     * @return the list of reserves
     */
    public List<Reserve> getClientHistorical(User activeUser) {

        ApiFuture<QuerySnapshot> future = db.collection("reservesClient").document(activeUser.getUID()).collection("reserves").get();
        ArrayList<Reserve> reserves = new ArrayList<>();

        try {

            for (QueryDocumentSnapshot document : future.get().getDocuments()) {
                DocumentSnapshot reserveRef = ((DocumentReference) document.get("reserveRef")).get().get();
                reserves.add(reserveRef.toObject(Reserve.class));
            }

        } catch (InterruptedException ex) {
            Logger.getLogger(FirebaseDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(FirebaseDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return reserves;

    }
}
