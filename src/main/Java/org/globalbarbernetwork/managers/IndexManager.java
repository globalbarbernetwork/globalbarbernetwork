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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.globalbarbernetwork.entities.Client;
import org.globalbarbernetwork.entities.Hairdressing;
import org.globalbarbernetwork.entities.User;
import org.globalbarbernetwork.firebase.FirebaseDAO;
import org.globalbarbernetwork.interfaces.ManagerInterface;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Grup 3
 */
public class IndexManager extends Manager implements ManagerInterface {

    private final FirebaseDAO firebaseDAO = new FirebaseDAO();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, String action) {
        try {
            RequestDispatcher rd = request.getRequestDispatcher("/index.jsp");
            String listHairdressingsJSON = getListHairdressingsToJSON();
            
            User user = this.getCurrentUser(request);
            List options = this.buildMenuOptionsByUser(user);
            
            request.setAttribute("listHairdressingsJSON", listHairdressingsJSON);            
            request.setAttribute("options", options);            
            rd.forward(request, response);
        } catch (ServletException ex) {
            Logger.getLogger(IndexManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(IndexManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String getListHairdressingsToJSON() {
        List<Hairdressing> hairdressings = firebaseDAO.getAllHairdressings();
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();

        LinkedHashMap<String, Object> jsonOrderedMap;
        LinkedHashMap<String, Double> jsonCoords;
        for (Hairdressing hairdressing : hairdressings) {
            jsonOrderedMap = new LinkedHashMap<>();
            jsonCoords = new LinkedHashMap<>();

            jsonOrderedMap.put("UID", hairdressing.getUID());
            jsonOrderedMap.put("address", hairdressing.getAddress());
            jsonOrderedMap.put("city", hairdressing.getCity());
            jsonOrderedMap.put("companyName", hairdressing.getCompanyName());

            jsonCoords.put("lat", hairdressing.getCoordinates().getLatitude());
            jsonCoords.put("lng", hairdressing.getCoordinates().getLongitude());

            jsonOrderedMap.put("coordinates", new JSONObject(jsonCoords));
            jsonOrderedMap.put("country", hairdressing.getCountry());
            jsonOrderedMap.put("displayName", hairdressing.getDisplayName());
            jsonOrderedMap.put("email", hairdressing.getEmail());
            jsonOrderedMap.put("instagram", hairdressing.getInstagram());
            jsonOrderedMap.put("phoneNumber", hairdressing.getPhoneNumber());
            jsonOrderedMap.put("province", hairdressing.getProvince());
            jsonOrderedMap.put("website", hairdressing.getWebsite());
            jsonOrderedMap.put("zipCode", hairdressing.getZipCode());
            jsonOrderedMap.put("description", hairdressing.getDescription());

            JSONObject member = new JSONObject(jsonOrderedMap);
            array.put(member);

            try {
                json.put("jsonArray", array);
            } catch (JSONException ex) {
                Logger.getLogger(IndexManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return json.toString();
    }

    private List buildMenuOptionsByUser(User user) {

        List<Map> options = new ArrayList<Map>();

        if (user instanceof Hairdressing) {
            options.add(addMenuOption("Editar perfil", "ServletX", ""));
            options.add(addMenuOption("Gestio calendari", "ServletX", ""));
            options.add(addMenuOption("Gestio ", "ServletX", ""));
            options.add(addMenuOption("Cerrar sesión", "/ManagementServlet/access/logout", ""));
        } else if (user instanceof Client) {
            options.add(addMenuOption("Editar perfil", "ServletX", ""));
            options.add(addMenuOption("Historial de reserves", "ServletX", ""));
            options.add(addMenuOption("Cerrar sesión", "/ManagementServlet/access/logout", ""));
        }

        return options;
    }

    private Map addMenuOption(String label, String url, String params) {
        Map<String, String> option = new HashMap<>();
        option.put("label", label);
        option.put("url", url);
        option.put("params", params);
        return option;
    }

}
