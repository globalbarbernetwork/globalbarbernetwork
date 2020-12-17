/*
 * Copyright (C) 2020 IOC DAW
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
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.globalbarbernetwork.entities.Employee;
import org.globalbarbernetwork.entities.User;
import org.globalbarbernetwork.firebase.FirebaseDAO;
import org.globalbarbernetwork.interfaces.ManagerInterface;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author IOC DAW
 */
public class ManageHairdressingManager extends Manager implements ManagerInterface {

    final static String ADD_EMPLOYEE = "addEmployee";
    final static String EDIT_EMPLOYEE = "editEmployee";
    final static String DELETE_EMPLOYEE = "deleteEmployee";
    final static String GET_EMPLOYEES_AJAX = "getEmployeesAjax";
    final static String SAVE_HOLIDAYS_EMPLOYEE_AJAX = "saveHolidaysEmployeeAjax";
    private final FirebaseDAO firebaseDAO = new FirebaseDAO();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, String action) {
        try {
            RequestDispatcher rd = null;
            User activeUser = (User) request.getSession().getAttribute("user");

            switch (action) {
                case ADD_EMPLOYEE:
                    addEmployee(request, activeUser);
                    sendListEmployees(request, activeUser);
                    rd = request.getRequestDispatcher("/manageHairdressing.jsp");
                    break;
                case EDIT_EMPLOYEE:
                    editEmployee(request, activeUser);
                    sendListEmployees(request, activeUser);
                    rd = request.getRequestDispatcher("/manageHairdressing.jsp");
                    break;
                case DELETE_EMPLOYEE:
                    deleteEmployee(request, activeUser);
                    sendListEmployees(request, activeUser);
                    rd = request.getRequestDispatcher("/manageHairdressing.jsp");
                    break;
                case GET_EMPLOYEES_AJAX:
                    response.setContentType("text/html;charset=UTF-8");

                    String idHairdressing = request.getParameter("idHairdressing");
                    getListEmployeesToJSON(response, idHairdressing);
                    break;
                case SAVE_HOLIDAYS_EMPLOYEE_AJAX:
                    response.setContentType("text/html;charset=UTF-8");
                    
                    String idHairdressing2 = request.getParameter("idHairdressing");
                    String idEmployee = request.getParameter("idEmployee");
                    String selectedHolidays = request.getParameter("selectedHolidays");
                    
                    saveHolidaysEmployee(idHairdressing2, idEmployee, selectedHolidays);
                    break;
                default:
                    sendListEmployees(request, activeUser);
                    rd = request.getRequestDispatcher("/manageHairdressing.jsp");
                    break;
            }

            if (rd != null) {
                
                rd.forward(request, response);
            }
        } catch (ServletException ex) {
            Logger.getLogger(AccessManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AccessManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(ManageHairdressingManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Employee> getListEmployees(String idHairdressing) {
        return firebaseDAO.getAllEmployeesByHairdressing(idHairdressing);
    }

    public void addEmployee(HttpServletRequest request, User activeUser) {
        //-------------------------------------------------
        // TODO : Controlar si el dni no es existente en dicha peluqueria
        //-------------------------------------------------
        String name = (String) request.getParameter("name") != null ? request.getParameter("name") : "";
        String surname = (String) request.getParameter("surname") != null ? request.getParameter("surname") : "";
        String nationalIdentity = (String) request.getParameter("nationalIdentity") != null ? request.getParameter("nationalIdentity") : "";
        String age = (String) request.getParameter("age") != null ? request.getParameter("age") : "";
        String address = (String) request.getParameter("address") != null ? request.getParameter("address") : "";
        String phoneNumber = (String) request.getParameter("phoneNumber") != null ? request.getParameter("phoneNumber") : "";

        Employee newEmployee = new Employee(name, surname, nationalIdentity, age, address, phoneNumber, activeUser.getUID());
        firebaseDAO.insertEmployee(newEmployee);
    }

    public void editEmployee(HttpServletRequest request, User activeUser) {
        //Supongamos que todos los campos han pasado el control 
        // Controlar : Si el dni no es existente en dicha peluqueria

        String name = (String) request.getParameter("name") != null ? request.getParameter("name") : "";
        String surname = (String) request.getParameter("surname") != null ? request.getParameter("surname") : "";
        String nationalIdentity = (String) request.getParameter("nationalIdentity") != null ? request.getParameter("nationalIdentity") : "";
        String age = (String) request.getParameter("age") != null ? request.getParameter("age") : "";
        String address = (String) request.getParameter("address") != null ? request.getParameter("address") : "";
        String phoneNumber = (String) request.getParameter("phoneNumber") != null ? request.getParameter("phoneNumber") : "";

        if (activeUser != null) {
            Employee newEmployee = new Employee(name, surname, nationalIdentity, age, address, phoneNumber, activeUser.getUID());
            firebaseDAO.modifyEmployee(newEmployee);
        }

    }

    public void deleteEmployee(HttpServletRequest request, User activeUser) {
        String nationalIdentity = (String) request.getParameter("natIdenEmployee") != null ? request.getParameter("natIdenEmployee") : "";

        if (activeUser != null) {
            firebaseDAO.deleteEmployee(nationalIdentity, activeUser.getUID());
        }
    }

    public void sendListEmployees(HttpServletRequest request, User activeUser) {
        if (activeUser != null) {
            request.setAttribute("employees", getListEmployees(activeUser.getUID()));
        }
        
    }

    public void getListEmployeesToJSON(HttpServletResponse response, String idHairdressing) throws IOException {
        List<Employee> listEmployees = getListEmployees(idHairdressing);
        
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        try ( PrintWriter out = response.getWriter()) {

            LinkedHashMap<String, Object> jsonOrderedMap;
            for (Employee employee : listEmployees) {
                jsonOrderedMap = new LinkedHashMap<>();

                jsonOrderedMap.put("nationalIdentity", employee.getNationalIdentity());
                jsonOrderedMap.put("name", employee.getName());

                JSONObject member = new JSONObject(jsonOrderedMap);
                array.put(member);
            }

            try {
                json.put("jsonArray", array);
            } catch (JSONException ex) {
                Logger.getLogger(IndexManager.class.getName()).log(Level.SEVERE, null, ex);
            }

            out.print(!array.isNull(0) ? json : "");
        }
    }
    
    public void saveHolidaysEmployee(String idHairdressing, String idEmployee, String holidays) throws ParseException {
        Map<String, Object> docData = new HashMap<>();

        ArrayList<Date> listHolidays = new ArrayList<>();
        String[] arrayHolidays = holidays.contains(",") ? holidays.split(",") : new String[0];
        
        if (arrayHolidays.length > 0) {
            for (String holiday : arrayHolidays) {
                Date date = new SimpleDateFormat("dd/MM/yyyy").parse(holiday);
                listHolidays.add(date);
            }
        }
        docData.put("holidays", listHolidays);
        
        firebaseDAO.insertHolidaysEmployee(idHairdressing, idEmployee, docData);
    }
}
