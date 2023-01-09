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
package org.globalbarbernetwork.managers;

import com.google.cloud.firestore.GeoPoint;
import static org.globalbarbernetwork.constants.Constants.*;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.globalbarbernetwork.bo.UserBO;
import org.globalbarbernetwork.entities.Client;
import org.globalbarbernetwork.entities.Hairdressing;
import org.globalbarbernetwork.entities.User;
import org.globalbarbernetwork.firebase.FirebaseDAO;
import org.globalbarbernetwork.services.SmtpService;
import org.globalbarbernetwork.firebase.MyFirebaseAuth;
import org.globalbarbernetwork.interfaces.ManagerInterface;
import org.json.JSONObject;

/**
 *
 * @author Grup 3
 */
public class AccessManager extends Manager implements ManagerInterface {

    final static String LOGIN = "login";
    final static String REGISTER = "register";
    final static String REGISTER_HAIRDRESSING = "registerHairdressing";
    final static String LOGOUT = "logout";
    private final FirebaseDAO firebaseDAO = new FirebaseDAO();

    @Override

    /**
     * This method will be executed on load AccesManager
     *
     * @param request the request
     * @param response the response
     * @param action the action
     */
    public void execute(HttpServletRequest request, HttpServletResponse response, String action) {

        RequestDispatcher rd = null;

        switch (action) {
            case LOGIN:

                if ("POST".equals(request.getMethod())) {
                    String email = request.getParameter("email");
                    String password = request.getParameter("password");
                    try {
                        Map<String, String> errorsInAuth = authUser(request, email, password);
                        if (!errorsInAuth.isEmpty()) {
                            request.setAttribute("errors", new JSONObject(errorsInAuth));
                            request.setAttribute("newUserCreated", false);
                            rd = request.getRequestDispatcher("/" + LOGIN_JSP);
                        } else {
                            response.sendRedirect(request.getContextPath() + "/ManagementServlet/index.jsp");
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(AccessManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    request.setAttribute("newUserCreated", false);
                    rd = request.getRequestDispatcher("/" + LOGIN_JSP);
                }
                break;

            case REGISTER:
                if (POST.equals(request.getMethod())) {
                    rd = registerClient(request);
                } else {
                    rd = request.getRequestDispatcher("/" + REGISTER_JSP);
                }
                break;
            case REGISTER_HAIRDRESSING:
                if (POST.equals(request.getMethod())) {
                    rd = registerHairdressing(request);
                } else {
                    rd = request.getRequestDispatcher("/" + REGISTER_HAIRDRESSING_JSP);
                }
                break;
            case LOGOUT:
                this.closeSession(request, response);
                break;
        }

        try {
            if (rd != null) {
                rd.forward(request, response);
            }
        } catch (ServletException ex) {
            Logger.getLogger(AccessManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AccessManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Method to register a client in Firebase DB, that will be write on Users
     * and Client collections, and will create an object on FirebaseAuthentication
     *
     * @param request the request
     * @return RequestDispatcher
     */
    private RequestDispatcher registerClient(HttpServletRequest request) {

        //Comprobamos primero que el email exista:
        // True - Lo mandamos de vuelta a la pantalla de registro
        // False - Creamos el usuario y vamos a login 
        String email = request.getParameter("email");
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String phoneNum = request.getParameter("mobilePhone");
        String password = request.getParameter("password");
        String displayName = name + " " + surname;

        Boolean userExists = firebaseDAO.getUserByEmail(email) != null;

        if (!userExists) {
            Boolean userExistsWithSamePhone = firebaseDAO.getUserByPhone("+34" + phoneNum) != null;
            if (!userExistsWithSamePhone) {
                // Accedemos a Firebase para introducir el nuevo usuario en autentificacion
                Client client = new Client(name, surname, null, email, phoneNum, displayName, "client");
                UserRecord newUser = firebaseDAO.createUser(client, password);
                Boolean isCreated = newUser != null;

                if (isCreated) {
                    client.setUID(newUser.getUid());
                    insertUser(client);
                    createEmailVerification(email, displayName);
                }
                
                request.setAttribute("newUserCreated", isCreated);
                return request.getRequestDispatcher("/" + LOGIN_JSP);
            } else {
                Client client = new Client(name, surname, null, email, phoneNum, null, null);
                request.setAttribute("client", client);
                request.setAttribute("msgErrorPhone", "* Aquest número de mòbil ja existeix, introdueix un altre número");
                return request.getRequestDispatcher("/" + REGISTER_JSP);
            }
        } else {
            Client client = new Client(name, surname, null, email, phoneNum, null, null);
            request.setAttribute("client", client);
            request.setAttribute("msgErrorEmail", "* Aquest correu ja existeix, introdueix un altre correu");
            return request.getRequestDispatcher("/" + REGISTER_JSP);
        }
    }

    /**
     * Method to register a hairdressing in Firebase DB, that will be write on Users
     * and Hairdressing collections, and will create an object on FirebaseAuthentication
     *
     * @param request the request
     * @return RequestDispatcher
     */
    private RequestDispatcher registerHairdressing(HttpServletRequest request) {

        //Comprobamos primero que el email exista:
        // True - Lo mandamos de vuelta a la pantalla de registro
        // False - Creamos el usuario y vamos a login
        String email = request.getParameter("email");
        String companyName = request.getParameter("name");
        String displayName = companyName;
        String phoneNumber = request.getParameter("phoneNumber");
        String password = request.getParameter("password");

        String address = request.getParameter("address");
        String city = request.getParameter("cityHidden");
        String country = request.getParameter("countryHidden");
        String province = request.getParameter("provinceHidden");
        String zipCode = request.getParameter("zipCode");

        String[] geolocation = request.getParameter("coordHairdressing").split(",");
        GeoPoint coordinates = new GeoPoint(Double.parseDouble(geolocation[1]), Double.parseDouble(geolocation[0]));

        Boolean userExists = firebaseDAO.getUserByEmail(email) != null;

        if (!userExists) {
            Boolean userExistsWithSamePhone = firebaseDAO.getUserByPhone("+34" + phoneNumber) != null;
            if (!userExistsWithSamePhone) {
                // Accedemos a Firebase para introducir el nuevo usuario en autentificacion
                Hairdressing newHairdressing = new Hairdressing(companyName, "", city, address, country, province, zipCode, coordinates, "", "", "", email, phoneNumber, displayName, "hairdressing");

                UserRecord newUser = firebaseDAO.createUser(newHairdressing, password);
                Boolean isCreated = newUser != null;

                if (isCreated) {
                    newHairdressing.setUID(newUser.getUid());
                    insertUser(newHairdressing);
                    createEmailVerification(email, displayName);
                }
                
                request.setAttribute("newUserCreated", isCreated);
                return request.getRequestDispatcher("/" + LOGIN_JSP);
            } else {
                Hairdressing hairdressing = new Hairdressing(companyName, "", city, address, country, province, zipCode, coordinates, "", "", "", email, phoneNumber, displayName, "hairdressing");
                request.setAttribute("hairdrsg", hairdressing);
                request.setAttribute("msgErrorPhone", "* Aquest número de mòbil ja existeix, introdueix un altre número");
                return request.getRequestDispatcher("/" + REGISTER_HAIRDRESSING_JSP);
            }
        } else {
            Hairdressing hairdressing = new Hairdressing(companyName, "", city, address, country, province, zipCode, coordinates, "", "", "", email, phoneNumber, displayName, "hairdressing");
            request.setAttribute("hairdrsg", hairdressing);
            request.setAttribute("msgErrorEmail", "* Aquest correu ja existeix, introdueix un altre correu");
            return request.getRequestDispatcher("/" + REGISTER_HAIRDRESSING_JSP);
        }
    }

    /**
     * This method will generate link to verify the email on register and will send it to the current user registered
     *
     * @param email the email
     * @param displayName the display name
     */
    private void createEmailVerification(String email, String displayName) {

        try {
            String linkVerification = firebaseDAO.generateLink(email);
            SmtpService stmpService = new SmtpService();
            String bodyMailVerification = BODY_MAIL_VERIFICATION.replace("%LINK_VERIFICATION%", linkVerification).replace("%DISPLAY_NAME%", displayName);
            stmpService.sendEmail(email, SUBJECT_MAIL_VERIFICATION, bodyMailVerification);
        } catch (FirebaseAuthException ex) {
            Logger.getLogger(AccessManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method will crate an user in Firebase DB
     *
     * @param user the user
     */
    private void insertUser(User user) {

        firebaseDAO.insertUser(user);

        UserBO userBO = new UserBO();
        userBO.insertUserByType(user);
    }

    /**
     * This method will authenticate an user via API in Firebase console, and create the user session var, we have to pass the email and password
     *
     * @param request the request
     * @param email the email
     * @param password the password
     * @return Map
     */
    private Map authUser(HttpServletRequest request, String email, String password) {

        Map<String, String> errors = new HashMap<String, String>();

        try {
            FirebaseDAO firebaseDAO = new FirebaseDAO();
            UserRecord userRecord = firebaseDAO.getUserByEmail(email);

            if (userRecord != null) {
                if (!userRecord.isEmailVerified()) {
                    errors.put("403", "Has de confirmar el correu electrònic");
                    return errors;
                }
            }

            JsonObject userJson = MyFirebaseAuth.getInstance().auth(email, password);

            if (userJson == null) {
                errors.put("401", "Email o contrasenya incorrecte");
                return errors;
            }

            UserBO userBO = new UserBO();
            User user = userBO.getUserByType(userRecord.getUid());

            request.getSession().setAttribute("user", user);

        } catch (FirebaseAuthException ex) {
            Logger.getLogger(AccessManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(AccessManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return errors;
    }
}
