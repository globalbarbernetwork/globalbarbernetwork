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

import com.google.cloud.Timestamp;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.globalbarbernetwork.entities.Employee;
import org.globalbarbernetwork.entities.Reserve;
import org.globalbarbernetwork.entities.Service;
import org.globalbarbernetwork.entities.User;
import org.globalbarbernetwork.firebase.FirebaseDAO;
import org.globalbarbernetwork.interfaces.ManagerInterface;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static org.globalbarbernetwork.constants.Constants.*;

/**
 *
 * @author Grup 3
 */
public class ManageHairdressingManager extends Manager implements ManagerInterface {

    final static String LOAD_LISTS_TO_MANAGE = "loadListsToManage";

    final static String ADD_EMPLOYEE = "addEmployee";
    final static String EDIT_EMPLOYEE = "editEmployee";
    final static String DELETE_EMPLOYEE = "deleteEmployee";
    final static String CHECK_EMPLOYEE_AJAX = "checkEmployeeAjax";
    final static String CHECK_EMPLOYEE_RESERVE_AJAX = "checkEmployeeHasReserveAjax";

    final static String GET_EMPLOYEES_AJAX = "getEmployeesAjax";
    final static String GET_HOLIDAYS_EMPLOYEE_AJAX = "getHolidaysEmployeeAjax";
    final static String SAVE_HOLIDAYS_EMPLOYEE_AJAX = "saveHolidaysEmployeeAjax";

    final static String ADD_SERVICE = "addService";
    final static String EDIT_SERVICE = "editService";
    final static String DELETE_SERVICE = "deleteService";
    final static String GET_SERVICES_AJAX = "getServicesAjax";

    final static String UPDATE_SCHEDULE = "updateSchedule";
    final static String UPDATE_HOLIDAYS = "updateHolidays";

    final static String GET_DISABLED_DAYS_HAIDRESSING = "getDisableDaysHairdressing";

    private final FirebaseDAO firebaseDAO = new FirebaseDAO();

    @Override

    /**
     * This method will be executed on load ManageHairdressingManager
     *
     * @param request the request
     * @param response the response
     * @param action the action
     */
    public void execute(HttpServletRequest request, HttpServletResponse response, String action) {

        try {
            RequestDispatcher rd = null;
            User activeUser = (User) request.getSession().getAttribute("user");
            request.setAttribute("incrementMin", INCREMENT_MINUTES);

            switch (action) {
                case LOAD_LISTS_TO_MANAGE:
                    rd = request.getRequestDispatcher("/" + MANAGE_HAIRDRESSING_JSP);
                    break;
                case ADD_EMPLOYEE:
                    if ("POST".equals(request.getMethod())) {
                        addEmployee(request, activeUser);
                    }
                    rd = request.getRequestDispatcher("/" + MANAGE_HAIRDRESSING_JSP);
                    break;
                case EDIT_EMPLOYEE:
                    if ("POST".equals(request.getMethod())) {
                        editEmployee(request, activeUser);
                    }
                    rd = request.getRequestDispatcher("/" + MANAGE_HAIRDRESSING_JSP);
                    break;
                case DELETE_EMPLOYEE:
                    if ("POST".equals(request.getMethod())) {
                        deleteEmployee(request, activeUser);
                    }
                    rd = request.getRequestDispatcher("/" + MANAGE_HAIRDRESSING_JSP);
                    break;
                case CHECK_EMPLOYEE_AJAX:
                    response.setContentType("application/json");
                    checkIfEmployeeExistsInHairdressing(request, response, activeUser);
                    break;
                case CHECK_EMPLOYEE_RESERVE_AJAX:
                    response.setContentType("application/json");
                    checkIfEmployeeHasReserves(request, response, activeUser);
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
                    rd = request.getRequestDispatcher("/" + MANAGE_HAIRDRESSING_JSP);
                    break;
                case EDIT_SERVICE:
                    if ("POST".equals(request.getMethod())) {
                        editService(request, activeUser);
                    }
                    rd = request.getRequestDispatcher("/" + MANAGE_HAIRDRESSING_JSP);
                    break;
                case DELETE_SERVICE:
                    if ("POST".equals(request.getMethod())) {
                        deleteService(request, activeUser);
                    }
                    rd = request.getRequestDispatcher("/" + MANAGE_HAIRDRESSING_JSP);
                    break;
                case GET_SERVICES_AJAX:
                    response.setContentType("application/json");

                    String idHairdressing4 = request.getParameter("idHairdressing");

                    getServicesHairdressingToJSON(response, idHairdressing4);
                    break;
                case UPDATE_SCHEDULE:
                    this.updateSchedule(request, activeUser);
                    rd = request.getRequestDispatcher("/" + MANAGE_HAIRDRESSING_JSP);
                    break;
                case UPDATE_HOLIDAYS:
                    if (request.getMethod().equals(POST)) {
                        this.updateHolidays(request, activeUser);
                    }
                    rd = request.getRequestDispatcher("/" + MANAGE_HAIRDRESSING_JSP);
                    break;
                case GET_DISABLED_DAYS_HAIDRESSING:
                    response.setContentType("application/json");

                    String idHairdressing5 = request.getParameter("idHairdressing");

                    getDisabledDaysToJSON(response, idHairdressing5);
                    break;
            }

            if (rd != null) {
                activeTab(request, action);
                sendListServices(request, activeUser);
                sendListEmployees(request, activeUser);
                loadSchedule(request, activeUser);
                loadHolidays(request, activeUser);
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

    /**
     * This method will return the active tab in the screen
     *
     * @param request the request
     * @param action the action
     */
    public void activeTab(HttpServletRequest request, String action) {
        String[] actions = {"service", "schedule", "holiday", "employee"};
        Map<String, Boolean> controlActiveTabs = new HashMap<>();
        for (int i = 0; i < actions.length; i++) {
            if (action.equalsIgnoreCase(LOAD_LISTS_TO_MANAGE) && i == 0) {
                controlActiveTabs.put(actions[i], true);
            } else if (action.toLowerCase().contains(actions[i])) {
                controlActiveTabs.put(actions[i], true);
            } else {
                controlActiveTabs.put(actions[i], false);
            }
        }

        request.setAttribute("selectedTab", controlActiveTabs);
    }

    /**
     * This method will get the list of all employees for a hairdressing
     *
     * @param idHairdressing the id hairdressing
     * @return the list employees
     */
    public List<Employee> getListEmployees(String idHairdressing) {
        return firebaseDAO.getAllEmployees(idHairdressing);
    }

    /**
     * This method will create an employee in Firebase DB
     *
     * @param request the request
     * @param activeUser the active user
     */
    public void addEmployee(HttpServletRequest request, User activeUser) {
        String name = (String) request.getParameter("name") != null ? request.getParameter("name") : "";
        String surname = (String) request.getParameter("surname") != null ? request.getParameter("surname") : "";
        String idNumber = (String) request.getParameter("idNumber") != null ? request.getParameter("idNumber") : "";
        String contractIni = (String) request.getParameter("contractIniDate") != null ? request.getParameter("contractIniDate") : "";
        String contractEnd = (String) request.getParameter("contractEndDate") != null ? request.getParameter("contractEndDate") : "";
        String phoneNumber = (String) request.getParameter("phoneNumber") != null ? request.getParameter("phoneNumber") : "";

        if (activeUser != null) {
            Employee employee = firebaseDAO.getEmployeeByIDNumber(activeUser.getUID(), idNumber);
            if (employee == null) {
                Employee newEmployee = new Employee(name, surname, idNumber, parseStringToDate(contractIni), parseStringToDate(contractEnd), phoneNumber, activeUser.getUID());
                firebaseDAO.insertEmployee(newEmployee);
            }
        }
    }

    /**
     * This method will edit employee data in Firebase DB
     *
     * @param request the request
     * @param activeUser the active user
     */
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

    /**
     * This method will delete an employee and all his data recursively
     *
     * @param request the request
     * @param activeUser the active user
     */
    public void deleteEmployee(HttpServletRequest request, User activeUser) {
        String idHairdressing = activeUser.getUID();
        String idNumber = (String) request.getParameter("idNumberEmployeeToDelete") != null ? request.getParameter("idNumberEmployeeToDelete") : "";

        if (activeUser != null) {
            firebaseDAO.deleteEmployee(idNumber, idHairdressing);
            firebaseDAO.deleteHolidaysEmployee(idHairdressing, idNumber);
            firebaseDAO.deleteReservesEmployee(idHairdressing, idNumber);
        }
    }

    /**
     * This method will send a list of employees to the request
     *
     * @param request the request
     * @param activeUser the active user
     */
    public void sendListEmployees(HttpServletRequest request, User activeUser) {
        if (activeUser != null) {
            try {
                Thread.sleep(200);
                request.setAttribute("employees", getListEmployees(activeUser.getUID()));
            } catch (InterruptedException ex) {
                Logger.getLogger(ManageHairdressingManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * This method check if employee exist in a hairdressing, thats it an API
     * call.
     *
     * @param request the request
     * @param response the response
     * @param activeUser the active user
     */
    public void checkIfEmployeeExistsInHairdressing(HttpServletRequest request, HttpServletResponse response, User activeUser) {
        String idNumberEmployee = request.getParameter("idNumberEmployee");
        Employee employee = firebaseDAO.getEmployeeByIDNumber(activeUser.getUID(), idNumberEmployee);
        boolean idNumberExistInHaird = employee != null;

        JSONObject json = null;
        try ( PrintWriter out = response.getWriter()) {
            LinkedHashMap<String, Object> jsonOrderedMap = new LinkedHashMap<>();
            jsonOrderedMap.put("idNumberExistInHairdressing", idNumberExistInHaird);
            json = new JSONObject(jsonOrderedMap);
            out.print(json);
        } catch (IOException ex) {
            Logger.getLogger(ScheduleManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method check if an employee has reserves, that it an API call.
     *
     * @param request the request
     * @param response the response
     * @param activeUser the active user
     */
    public void checkIfEmployeeHasReserves(HttpServletRequest request, HttpServletResponse response, User activeUser) {
        String idNumberEmployee = request.getParameter("idNumberEmployee");
        List<Reserve> reserves = firebaseDAO.getAllReservesEmployee(activeUser.getUID(), idNumberEmployee);;
        boolean employeeHasReserves = reserves != null && !reserves.isEmpty();

        JSONObject json = null;
        try ( PrintWriter out = response.getWriter()) {
            LinkedHashMap<String, Object> jsonOrderedMap = new LinkedHashMap<>();
            jsonOrderedMap.put("employeeHasReserves", employeeHasReserves);
            json = new JSONObject(jsonOrderedMap);
            out.print(json);
        } catch (IOException ex) {
            Logger.getLogger(ScheduleManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method will retrieve list of employees in a hairdressing in JSON,
     * this is an AJAX call
     *
     * @param response the response
     * @param idHairdressing the id hairdressing
     * @throws IOException
     */
    public void getListEmployeesToJSON(HttpServletResponse response, String idHairdressing) throws IOException {
        List<Employee> listEmployees = getListEmployees(idHairdressing);

        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        try ( PrintWriter out = response.getWriter()) {

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

    /**
     * This method will insert the holidays for an employee in the hairdressing
     * employee holidays collection
     *
     * @param idHairdressing the id hairdressing
     * @param idEmployee the id employee
     * @param holidays the holidays
     * @throws ParseException
     */
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

    /**
     * This method will retrieve all the employee holidays in JSON, that's it an
     * API call.
     *
     * @param response the response
     * @param idHairdressing the id hairdressing
     * @param idEmployee the id employee
     * @throws IOException
     * @throws JSONException
     */
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

    /**
     * This method will return all the disabled days in JSON format, that's it
     * an AJAX call
     *
     * @param response the response
     * @param idHairdressing the id hairdressing
     * @throws IOException
     * @throws JSONException
     */
    public void getDisabledDaysToJSON(HttpServletResponse response, String idHairdressing) throws IOException, JSONException {
        List<Timestamp> holidaysHairdressing = firebaseDAO.getHolidays(idHairdressing);
        Map<String, Object> nonWorkingDaysOfWeek = firebaseDAO.getScheduleHairdressing(idHairdressing);

        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        try ( PrintWriter out = response.getWriter()) {
            for (Timestamp holiday : holidaysHairdressing) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String dateHoliday = sdf.format(holiday.toDate());

                array.put(dateHoliday);
            }
            json.put("jsonArrayHolidaysHairdressing", array);

            array = new JSONArray();
            for (Map.Entry<String, Object> entry : nonWorkingDaysOfWeek.entrySet()) {
                Integer dayOfWeek = Integer.parseInt(entry.getKey());
                Map<String, Object> rangesHours = (Map<String, Object>) entry.getValue();

                if (((String) ((Map<String, Object>) rangesHours.get("rangeHour1")).get("startHour")).isEmpty()
                        && ((String) ((Map<String, Object>) rangesHours.get("rangeHour1")).get("endHour")).isEmpty()
                        && ((String) ((Map<String, Object>) rangesHours.get("rangeHour2")).get("startHour")).isEmpty()
                        && ((String) ((Map<String, Object>) rangesHours.get("rangeHour2")).get("endHour")).isEmpty()) {

                    array.put(dayOfWeek.equals(7) ? 0 : dayOfWeek);
                }
            }
            json.put("jsonArrayNonWorkingDaysOfWeek", array);

            out.print(json);
        }
    }

    /**
     * This method will get all the hairdressing services and will return the
     * data in JSON, that's it a AJAX call
     *
     * @param response the response
     * @param idHairdressing the id hairdressing
     * @throws IOException
     * @throws JSONException
     */
    public void getServicesHairdressingToJSON(HttpServletResponse response, String idHairdressing) throws IOException, JSONException {
        List<Service> listServices = getListServices(idHairdressing);

        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        try ( PrintWriter out = response.getWriter()) {

            LinkedHashMap<String, Object> jsonOrderedMap;
            for (Service service : listServices) {
                jsonOrderedMap = new LinkedHashMap<>();

                jsonOrderedMap.put("idService", service.getId());
                jsonOrderedMap.put("descService", service.getName() + " - " + service.obtainPriceFormatted() + " €");

                JSONObject member = new JSONObject(jsonOrderedMap);
                array.put(member);
            }
            json.put("jsonArray", array);

            out.print(json);
        }
    }

    /**
     * This method will parse an String to Date with an especific format
     *
     * @param dateInFormatString the date in format string
     * @return Date
     */
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

    /**
     * This method will send the list of services to a request variable
     *
     * @param request the request
     * @param activeUser the active user
     */
    public void sendListServices(HttpServletRequest request, User activeUser) {
        if (activeUser != null) {
            try {
                Thread.sleep(200);
                request.setAttribute("services", getListServices(activeUser.getUID()));
            } catch (InterruptedException ex) {
                Logger.getLogger(ManageHairdressingManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * This method will retrieve all stored services for a hairdressing
     *
     * @param idHairdressing the id hairdressing
     * @return the list services
     */
    public List<Service> getListServices(String idHairdressing) {
        return firebaseDAO.getAllServices(idHairdressing);
    }

    /**
     * This method will create a service, this will write in Firebase DB
     *
     * @param request the request
     * @param activeUser the active user
     */
    public void addService(HttpServletRequest request, User activeUser) {

        String name = (String) request.getParameter("nameService") != null ? request.getParameter("nameService") : "";
        String durationService = (String) request.getParameter("durationService") != null ? request.getParameter("durationService") : "";
        String priceService = (String) request.getParameter("priceService") != null ? request.getParameter("priceService") : "";

        if (activeUser != null) {
            Service service = new Service(name, convertDurationToMin(durationService), Double.parseDouble(priceService.replace(",", ".")));
            firebaseDAO.insertService(activeUser, service);
        }
    }

    /**
     * This method will edit a service, gets the submited data and save it in
     * Firebase
     *
     * @param request the request
     * @param activeUser the active user
     */
    public void editService(HttpServletRequest request, User activeUser) {
        String id = (String) request.getParameter("idServiceToUpdate") != null ? request.getParameter("idServiceToUpdate") : "";
        String name = (String) request.getParameter("nameService") != null ? request.getParameter("nameService") : "";
        String durationService = (String) request.getParameter("durationService") != null ? request.getParameter("durationService") : "";
        String priceService = (String) request.getParameter("priceService") != null ? request.getParameter("priceService") : "";

        if (activeUser != null) {
            Service service = new Service(id, name, convertDurationToMin(durationService), Double.parseDouble(priceService.replace(",", ".")));
            firebaseDAO.updateService(activeUser, service);
        }

    }

    /**
     * This method will delete the submited service
     *
     * @param request the request
     * @param activeUser the active user
     */
    public void deleteService(HttpServletRequest request, User activeUser) {
        String id = (String) request.getParameter("idServiceToDelete") != null ? request.getParameter("idServiceToDelete") : "";

        if (activeUser != null) {
            firebaseDAO.deleteService(activeUser, id);
        }
    }

    /**
     * This method covnerts the duration in minuts
     *
     * @param duration the duration
     * @return int
     */
    public int convertDurationToMin(String duration) {
        String[] tmpDuration = duration.split(":");
        return Integer.parseInt(tmpDuration[0]) * 60 + Integer.parseInt(tmpDuration[1]);
    }

    /**
     * This method will load the hairdressing schedule
     *
     * @param request the request
     * @param activeUser the active user
     */
    public void loadSchedule(HttpServletRequest request, User activeUser) {
        if (activeUser != null) {
            Map<String, Object> data = new HashMap<>();
            data = firebaseDAO.getScheduleHairdressing(activeUser.getUID());

            request.setAttribute("schedule", data);
            request.setAttribute("daysOfWeek", this.getDaysOfWeek());
        }
    }

    /**
     * This method will update shcedule range hours for a hairdressing
     *
     * @param request the request
     * @param activeUser the active user
     */
    public void updateSchedule(HttpServletRequest request, User activeUser) {
        Map<String, Object> schedule = new HashMap<>();
        schedule = this.getScheduleFromRequest(request);
        firebaseDAO.updateSchedule(schedule, activeUser);
    }

    /**
     * This method receives all the submitted parameters and treats them to
     * build a map with the defined schedule
     *
     * @param request the request
     * @return the schedule from request
     */
    private Map<String, Object> getScheduleFromRequest(HttpServletRequest request) {
        HashMap<String, Object> data = new HashMap<>();

        for (int i = 1; i < 8; i++) {
            HashMap<String, String> rangeHoursValues = new HashMap<>();
            HashMap<String, Map<String, String>> rangeHours = new HashMap<>();
            String range1StartValue = new String(), range1EndValue = new String(), range2StartValue = new String(), range2EndValue = new String();

            range1StartValue = Objects.isNull(request.getParameter("range1-start-day" + i)) ? "" : request.getParameter("range1-start-day" + i);
            range1EndValue = Objects.isNull(request.getParameter("range1-end-day" + i)) ? "" : request.getParameter("range1-end-day" + i);
            rangeHoursValues.put("startHour", range1StartValue);
            rangeHoursValues.put("endHour", range1EndValue);

            rangeHours.put("rangeHour1", rangeHoursValues);
            data.put(Integer.toString(i), rangeHours);

            rangeHoursValues = new HashMap<>();

            range2StartValue = Objects.isNull(request.getParameter("range2-start-day" + i)) ? "" : request.getParameter("range2-start-day" + i);
            range2EndValue = Objects.isNull(request.getParameter("range2-end-day" + i)) ? "" : request.getParameter("range2-end-day" + i);
            rangeHoursValues.put("startHour", range2StartValue);
            rangeHoursValues.put("endHour", range2EndValue);

            rangeHours.put("rangeHour2", rangeHoursValues);
            data.put(Integer.toString(i), rangeHours);
        }

        return data;

    }

    /**
     * This method will return the number of days in a week formatted in a Map
     *
     * @return the days of week
     */
    private Map<String, String> getDaysOfWeek() {

        Map<String, String> daysOfWeek = new HashMap<>();

        daysOfWeek.put("1", "Dilluns");
        daysOfWeek.put("2", "Dimarts");
        daysOfWeek.put("3", "Dimecres");
        daysOfWeek.put("4", "Dijous");
        daysOfWeek.put("5", "Divendres");
        daysOfWeek.put("6", "Dissabte");
        daysOfWeek.put("7", "Diumenge");

        return daysOfWeek;
    }

    /**
     * This method will load all the holidays for a hairdressing, for show it in
     * the calendar
     *
     * @param request the request
     * @param activeUser the active user
     */
    public void loadHolidays(HttpServletRequest request, User activeUser) {
        if (activeUser != null) {
            JSONArray jsonArray = new JSONArray();
            List<String> tableData = new ArrayList<>();
            List<Timestamp> holidays = firebaseDAO.getHolidays(activeUser.getUID());

            Collections.sort(holidays);
            //this.buildCustomizedSymbols()

            for (Timestamp holiday : holidays) {
                String date = new SimpleDateFormat("dd/MM/yyyy").format(holiday.toDate());
                String tableDate = new SimpleDateFormat("EEEE d 'de' MMMM 'del' yyyy", this.buildCustomizedSymbols()).format(holiday.toDate());
                jsonArray.put(date);
                tableData.add(tableDate);
            }

            request.setAttribute("holidays", jsonArray);
            request.setAttribute("tableData", tableData);
        }
    }

    /**
     * This method will add a list of holidays to a hairdressing
     *
     * @param request the request
     * @param activeUser the active user
     */
    public void updateHolidays(HttpServletRequest request, User activeUser) {
        String holidays = request.getParameter("selectedHairdressingHolidaysDates");
        Map<String, Object> docData = new HashMap<>();

        ArrayList<Date> listHolidays = new ArrayList<>();
        String[] arrayHolidays = !holidays.split(",")[0].equals("") ? holidays.split(",") : new String[0];

        try {
            if (arrayHolidays.length > 0) {
                for (String holiday : arrayHolidays) {
                    Date date = new SimpleDateFormat("dd/MM/yyyy").parse(holiday);
                    listHolidays.add(date);
                }
            }
        } catch (ParseException ex) {
            Logger.getLogger(ManageHairdressingManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        docData.put("holidays", listHolidays);
        firebaseDAO.updateHolidays(activeUser, docData);
    }

    /**
     * This method will create a customized DateFormatSymbols, to set capital
     * letter in the days and months
     *
     * @return DateFormatSymbols
     */
    private DateFormatSymbols buildCustomizedSymbols() {
        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(Locale.forLanguageTag("ca-ES"));
        dateFormatSymbols.setWeekdays(new String[]{
            "",
            "Diumenge",
            "Dilluns",
            "Dimarts",
            "Dimecres",
            "Dijous",
            "Divendres",
            "Dissabte"});
        dateFormatSymbols.setMonths(new String[]{
            "Gener",
            "Febrer",
            "Març",
            "Abril",
            "Maig",
            "Juny",
            "Juliol",
            "Agost",
            "Septembre",
            "Octubre",
            "Novembre",
            "Desembre"});
        return dateFormatSymbols;
    }

}
