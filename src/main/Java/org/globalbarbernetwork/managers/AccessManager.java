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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

/**
 *
 * @author Grup 3
 */
public class AccessManager extends Manager implements ManagerInterface {

    final static String LOGIN = "login";
    final static String REGISTER = "register";
    final static String REGISTER_HAIRDRESSING = "registerHairdressing";
    private final FirebaseDAO firebaseDAO = new FirebaseDAO();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, String action) {

        RequestDispatcher rd = null;

        switch (action) {
            case LOGIN:

                if ("POST".equals(request.getMethod())) {
                    String email = request.getParameter("email");
                    String password = request.getParameter("password");

                    Map<String, String> errorsInAuth = authUser(request, email, password);
                    if (!errorsInAuth.isEmpty()) {
                        request.setAttribute("errors", errorsInAuth);
                        rd = request.getRequestDispatcher("/login.jsp");
                    } else {
                        rd = request.getRequestDispatcher("/index.jsp");
                    }
                } else {
                    rd = request.getRequestDispatcher("/index.jsp");
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
            // Accedemos a Firebase para introducir el nuevo usuario en autentificacion
            Client client = new Client(name, surname, null, email, phoneNum, displayName, "client");
            UserRecord newUser = firebaseDAO.createUser(client, password);
            Boolean isCreated = newUser != null;

            if (isCreated) {
                client.setUID(newUser.getUid());
                insertUser(client);
                createEmailVerification(email, displayName);
            }
            return request.getRequestDispatcher("/" + LOGIN_JSP);
        } else {
            Client client = new Client(name, surname, null, email, phoneNum, null, null);
            request.setAttribute("client", client);
            request.setAttribute("msgErrorEmail", "Aquest correu ja existeix, introdueix un altre correu");
            return request.getRequestDispatcher("/" + REGISTER_JSP);
        }
    }

    private RequestDispatcher registerHairdressing(HttpServletRequest request) {
        //Comprobamos primero que el email exista:
        // True - Lo mandamos de vuelta a la pantalla de registro
        // False - Creamos el usuario y vamos a login

        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(AccessManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        String email = request.getParameter("email");
        String companyName = request.getParameter("name");
        String displayName = companyName;
        String phoneNumber = request.getParameter("phoneNumber");
        String password = request.getParameter("password");

        String address = request.getParameter("address");
        String city = request.getParameter("city");
        String country = request.getParameter("country");
        String province = request.getParameter("province");
        Integer zipCode = Integer.parseInt(request.getParameter("zipCode"));

        String[] geolocation = request.getParameter("coordHairdressing").split(",");
        GeoPoint coordinates = new GeoPoint(Double.parseDouble(geolocation[1]), Double.parseDouble(geolocation[0]));

        Boolean userExists = firebaseDAO.getUserByEmail(email) != null;

        if (!userExists) {
            // Accedemos a Firebase para introducir el nuevo usuario en autentificacion
            Hairdressing newHairdressing = new Hairdressing(companyName, "", city, address, country, province, zipCode, coordinates, "", "", "", email, phoneNumber, displayName, "hairdressing");

            UserRecord newUser = firebaseDAO.createUser(newHairdressing, password);
            Boolean isCreated = newUser != null;

            if (isCreated) {
                newHairdressing.setUID(newUser.getUid());
                insertUser(newHairdressing);
                createEmailVerification(email, displayName);
            }
            return request.getRequestDispatcher("/" + LOGIN_JSP);
        } else {
            Hairdressing hairdressing = new Hairdressing(companyName, "", city, address, country, province, zipCode, coordinates, "", "", "", email, phoneNumber, displayName, "hairdressing");
            request.setAttribute("hairdrsg", hairdressing);
            request.setAttribute("msgErrorEmail", "Aquest correu ja existeix, introdueix un altre correu");
            return request.getRequestDispatcher("/" + REGISTER_HAIRDRESSING_JSP);
        }
    }

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

    private void insertUser(User user) {
        firebaseDAO.insertUser(user);

        UserBO userBO = new UserBO();
        userBO.insertUserByType(user);
    }

    private Map authUser(HttpServletRequest request, String email, String password) {

        Map<String, String> errors = new HashMap<String, String>();

        try {
            FirebaseDAO firebaseDAO = new FirebaseDAO();
            UserRecord userRecord = firebaseDAO.getUserByEmail(email);

            if (userRecord != null) {
                if (!userRecord.isEmailVerified()) {
                    errors.put("403", "Has de confirmar el correu");
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

            List options = this.buildMenuOptionsByUser(user);

            request.getSession().setAttribute("user", user);
            request.getSession().setAttribute("options", options);

        } catch (FirebaseAuthException ex) {
            Logger.getLogger(AccessManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(AccessManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return errors;
    }

    private List buildMenuOptionsByUser(User user) {

        List<Map> options = new ArrayList<Map>();

        if (user instanceof Hairdressing) {

            addMenuOption("Editar perfil", "ServletX", "", options);
            addMenuOption("Gestio calendari", "ServletX", "", options);

        } else if (user instanceof Client) {
            addMenuOption("Editar perfil", "ServletX", "", options);
        }

        return options;
    }

    private void addMenuOption(String label, String url, String params, List<Map> options) {
        Map<String, String> option = new HashMap<>();
        option.put("label", label);
        option.put("url", url);
        option.put("params", params);
        options.add(option);
    }
}
