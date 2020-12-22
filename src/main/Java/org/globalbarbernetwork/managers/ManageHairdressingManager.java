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

import com.google.cloud.Timestamp;
import java.io.IOException;
import java.io.PrintWriter;
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

import static org.globalbarbernetwork.constants.Constants.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.globalbarbernetwork.entities.Employee;
import org.globalbarbernetwork.entities.Service;
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

    final static String LOAD_LISTS_TO_MANAGE = "loadListsToManage";

    final static String ADD_EMPLOYEE = "addEmployee";
    final static String EDIT_EMPLOYEE = "editEmployee";
    final static String DELETE_EMPLOYEE = "deleteEmployee";
    final static String CHECK_EMPLOYEE_AJAX = "checkEmployeeAjax";
    
    final static String GET_EMPLOYEES_AJAX = "getEmployeesAjax";
    final static String GET_HOLIDAYS_EMPLOYEE_AJAX = "getHolidaysEmployeeAjax";
    final static String SAVE_HOLIDAYS_EMPLOYEE_AJAX = "saveHolidaysEmployeeAjax";
    
    final static String ADD_SERVICE = "addService";
    final static String EDIT_SERVICE = "editService";
    final static String DELETE_SERVICE = "deleteService";
    final static String GET_SERVICES_AJAX = "getServicesAjax";

    private final FirebaseDAO firebaseDAO = new FirebaseDAO();
    
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, String action) {
        try {
            RequestDispatcher rd = null;
            User activeUser = (User) request.getSession().getAttribute("user");

            switch (action) {
                case LOAD_LISTS_TO_MANAGE:
                    rd = request.getRequestDispatcher("/"+MANAGE_HAIRDRESSING_JSP);
                    break;
                case ADD_EMPLOYEE:
                    if ("POST".equals(request.getMethod())) {
                        addEmployee(request, activeUser);
                    }
                    rd = request.getRequestDispatcher("/"+MANAGE_HAIRDRESSING_JSP);
                    break;
                case EDIT_EMPLOYEE:
                    if ("POST".equals(request.getMethod())) {
                        editEmployee(request, activeUser);
                    }
                    rd = request.getRequestDispatcher("/"+MANAGE_HAIRDRESSING_JSP);
                    break;
                case DELETE_EMPLOYEE:
                    if ("POST".equals(request.getMethod())) {
                        deleteEmployee(request, activeUser);
                    }
                    rd = request.getRequestDispatcher("/"+MANAGE_HAIRDRESSING_JSP);
                    break;
                case CHECK_EMPLOYEE_AJAX:
                    response.setContentType("application/json");
                    checkIfEmployeeExistsInHairdressing(request, response, activeUser);
                    break;
                case GET_EMPLOYEES_AJAX:
                    response.setContentType("text/html;charset=UTF-8");

                    String idHairdressing = request.getParameter("idHairdressing");
                    getListEmployeesToJSON(response, idHairdressing);
                    break;
                case GET_HOLIDAYS_EMPLOYEE_AJAX:
                    response.setContentType("application/json");

                    String idHairdressing3 = request.getParameter("idHairdressing");
                    String idEmployee2 = request.getParameter("idEmployee");

                    getHolidaysEmployeeToJSON(response, idHairdressing3, idEmployee2);
                    break;
                case SAVE_HOLIDAYS_EMPLOYEE_AJAX:
                    String idHairdressing2 = request.getParameter("idHairdressing");
                    String idEmployee = request.getParameter("idEmployee");
                    String selectedHolidays = request.getParameter("selectedHolidays");

                    saveHolidaysEmployee(idHairdressing2, idEmployee, selectedHolidays);
                    break;
                case ADD_SERVICE:
                    if ("POST".equals(request.getMethod())) {
                        addService(request, activeUser);
                    }
                    rd = request.getRequestDispatcher("/"+MANAGE_HAIRDRESSING_JSP);
                    break;
                case EDIT_SERVICE:
                    if ("POST".equals(request.getMethod())) {
                        editService(request, activeUser);
                    }
                    rd = request.getRequestDispatcher("/"+MANAGE_HAIRDRESSING_JSP);
                    break;
                case DELETE_SERVICE:
                    if ("POST".equals(request.getMethod())) {
                        deleteService(request, activeUser);
                    }
                    rd = request.getRequestDispatcher("/"+MANAGE_HAIRDRESSING_JSP);
                    break;
                case GET_SERVICES_AJAX:
                    response.setContentType("application/json");
                    
                    String idHairdressing4 = request.getParameter("idHairdressing");
                    
                    getServicesHairdressingToJSON(response, idHairdressing4);
                    break;
            }

            if (action.toLowerCase().contains("employee")) {
                request.setAttribute("selectedTab", false);
            } else {
                request.setAttribute("selectedTab", true);
            }

            if (rd != null) {
                sendListServices(request, activeUser);
                sendListEmployees(request, activeUser);
                this.buildMenuOptions(request, response);
                rd.forward(request, response);
            }
        } catch (ServletException ex) {
            Logger.getLogger(AccessManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AccessManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(ManageHairdressingManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(ManageHairdressingManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Employee> getListEmployees(String idHairdressing) {
        return firebaseDAO.getAllEmployeesByHairdressing(idHairdressing);
    }

    public void addEmployee(HttpServletRequest request, User activeUser) {
        String name = (String) request.getParameter("name") != null ? request.getParameter("name") : "";
        String surname = (String) request.getParameter("surname") != null ? request.getParameter("surname") : "";
        String idNumber = (String) request.getParameter("idNumber") != null ? request.getParameter("idNumber") : "";
        String contractIni = (String) request.getParameter("contractIniDate") != null ? request.getParameter("contractIniDate") : "";
        String contractEnd = (String) request.getParameter("contractEndDate") != null ? request.getParameter("contractEndDate") : "";
        String phoneNumber = (String) request.getParameter("phoneNumber") != null ? request.getParameter("phoneNumber") : "";

        Employee employee = firebaseDAO.getEmployeeByIDNumber(activeUser.getUID(), idNumber);
        if (activeUser != null && employee == null) {
            Employee newEmployee = new Employee(name, surname, idNumber, parseStringToDate(contractIni), parseStringToDate(contractEnd), phoneNumber, activeUser.getUID());
            firebaseDAO.insertEmployee(newEmployee);
        }
    }

    public void editEmployee(HttpServletRequest request, User activeUser) {

        String name = (String) request.getParameter("name") != null ? request.getParameter("name") : "";
        String surname = (String) request.getParameter("surname") != null ? request.getParameter("surname") : "";
        String idNumber = (String) request.getParameter("idNumberEmployeeToEdit") != null ? request.getParameter("idNumberEmployeeToEdit") : "";
        String contractIni = (String) request.getParameter("contractIniDate") != null ? request.getParameter("contractIniDate") : "";
        String contractEnd = (String) request.getParameter("contractEndDate") != null ? request.getParameter("contractEndDate") : "";
        String phoneNumber = (String) request.getParameter("phoneNumber") != null ? request.getParameter("phoneNumber") : "";

        if (activeUser != null) {
            Employee newEmployee = new Employee(name, surname, idNumber, parseStringToDate(contractIni), parseStringToDate(contractEnd), phoneNumber, activeUser.getUID());
            firebaseDAO.updateEmployee(newEmployee);
        }

    }

    public void deleteEmployee(HttpServletRequest request, User activeUser) {
        //-------------------------------------------------
        // TODO : En el momento que se elimine la identidad nacional de un empleado, habrá que eliminar todos los registros de todas las tablas dodne se encuentre dicho empleado
        //-------------------------------------------------
        String idHairdressing = activeUser.getUID();
        String idNumber = (String) request.getParameter("idNumberEmployeeToDelete") != null ? request.getParameter("idNumberEmployeeToDelete") : "";

        if (activeUser != null) {
            firebaseDAO.deleteEmployee(idNumber, idHairdressing);
            firebaseDAO.deleteHolidaysEmployee(idHairdressing, idNumber);
        }
    }

    public void sendListEmployees(HttpServletRequest request, User activeUser) {
        if (activeUser != null) {
            request.setAttribute("employees", getListEmployees(activeUser.getUID()));
        }
    }

    public void checkIfEmployeeExistsInHairdressing(HttpServletRequest request, HttpServletResponse response, User activeUser) {
        String idNumberEmployee = request.getParameter("idNumberEmployee");
        Employee employee = firebaseDAO.getEmployeeByIDNumber(activeUser.getUID(), idNumberEmployee);
        boolean idNumberExistInHaird = employee != null;

        JSONObject json = null;
        try (PrintWriter out = response.getWriter()) {
            LinkedHashMap<String, Object> jsonOrderedMap = new LinkedHashMap<>();
            jsonOrderedMap.put("idNumberExistInHairdressing", idNumberExistInHaird);
            json = new JSONObject(jsonOrderedMap);
            out.print(json);
        } catch (IOException ex) {
            Logger.getLogger(ScheduleManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getListEmployeesToJSON(HttpServletResponse response, String idHairdressing) throws IOException {
        List<Employee> listEmployees = getListEmployees(idHairdressing);

        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        try (PrintWriter out = response.getWriter()) {

            LinkedHashMap<String, Object> jsonOrderedMap;
            for (Employee employee : listEmployees) {
                jsonOrderedMap = new LinkedHashMap<>();

                jsonOrderedMap.put("idNumber", employee.getIdNumber());
                jsonOrderedMap.put("nameSurname", employee.getName() + " " + employee.getSurname());

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
        String[] arrayHolidays = !holidays.split(",")[0].equals("") ? holidays.split(",") : new String[0];

        if (arrayHolidays.length > 0) {
            for (String holiday : arrayHolidays) {
                Date date = new SimpleDateFormat("dd/MM/yyyy").parse(holiday);
                listHolidays.add(date);
            }
        }
        docData.put("holidays", listHolidays);

        firebaseDAO.insertHolidaysEmployee(idHairdressing, idEmployee, docData);
    }

    public void getHolidaysEmployeeToJSON(HttpServletResponse response, String idHairdressing, String idEmployee) throws IOException, JSONException {
        List<Timestamp> listHolidays = firebaseDAO.getHolidaysEmployee(idHairdressing, idEmployee);

        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        try ( PrintWriter out = response.getWriter()) {
            for (Timestamp holiday : listHolidays) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String dateHoliday = sdf.format(holiday.toDate());

                array.put(dateHoliday);
            }
            json.put("jsonArray", array);
            
            out.print(json);
        }
    }
    
    public void getServicesHairdressingToJSON(HttpServletResponse response, String idHairdressing) throws IOException, JSONException {
        List<Service> listServices = getListServices(idHairdressing);
        
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        try (PrintWriter out = response.getWriter()) {
            
            LinkedHashMap<String, Object> jsonOrderedMap;
            for (Service service : listServices) {
                jsonOrderedMap = new LinkedHashMap<>();
                
                jsonOrderedMap.put("idService", service.getId());
                jsonOrderedMap.put("nameAndDuration", service.getName() + " (" + service.convertMinToFormat(service.getDuration()) + " h)");
                
                JSONObject member = new JSONObject(jsonOrderedMap);
                array.put(member);
            }
            json.put("jsonArray", array);

            out.print(json);
        }
    }

    public Date parseStringToDate(String dateInFormatString) {
        Date dateInFormatDate = null;
        if (!"".equals(dateInFormatString)) {
            try {
                dateInFormatDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateInFormatString);
            } catch (ParseException ex) {
                Logger.getLogger(ManageHairdressingManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return dateInFormatDate;
    }

    public void sendListServices(HttpServletRequest request, User activeUser) {
        if (activeUser != null) {
            request.setAttribute("services", getListServices(activeUser.getUID()));
        }
    }

    public List<Service> getListServices(String idHairdressing) {
        return firebaseDAO.getAllServicesByHairdressing(idHairdressing);
    }

    public void addService(HttpServletRequest request, User activeUser) {
        String name = (String) request.getParameter("nameService") != null ? request.getParameter("nameService") : "";
        String durationService = (String) request.getParameter("durationService") != null ? request.getParameter("durationService") : "";

        int autoIncrementalID = 1;

        List<Service> services = getListServices(activeUser.getUID());
        if (services.size() > 0) {
            Service tmpService = services.get(services.size() - 1);
            autoIncrementalID = tmpService.getId() + 1;
        }

        Service service = new Service(autoIncrementalID, name, convertDurationToMin(durationService));
        firebaseDAO.insertService(activeUser, service);
    }

    public void editService(HttpServletRequest request, User activeUser) {
        String id = (String) request.getParameter("idServiceToUpdate") != null ? request.getParameter("idServiceToUpdate") : "";
        String name = (String) request.getParameter("nameService") != null ? request.getParameter("nameService") : "";
        String durationService = (String) request.getParameter("durationService") != null ? request.getParameter("durationService") : "";

        if (activeUser != null) {
            Service service = new Service(Integer.valueOf(id), name, convertDurationToMin(durationService));
            firebaseDAO.updateService(activeUser, service);
        }

    }

    public void deleteService(HttpServletRequest request, User activeUser) {
        String id = (String) request.getParameter("idServiceToDelete") != null ? request.getParameter("idServiceToDelete") : "";

        if (activeUser != null) {
            firebaseDAO.deleteService(activeUser, Integer.valueOf(id));
        }
    }

    public int convertDurationToMin(String duration) {
        String[] tmpDuration = duration.split(":");
        return Integer.parseInt(tmpDuration[0]) * 60 + Integer.parseInt(tmpDuration[1]);
    }
}
