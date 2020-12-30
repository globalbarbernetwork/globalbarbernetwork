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
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Integer.parseInt;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.globalbarbernetwork.bo.ReserveBO;
import org.globalbarbernetwork.firebase.FirebaseDAO;
import org.globalbarbernetwork.interfaces.ManagerInterface;
import org.json.JSONObject;
import org.globalbarbernetwork.entities.Reserve;
import org.globalbarbernetwork.entities.Service;
import org.globalbarbernetwork.entities.Client;
import org.json.JSONArray;
import org.json.JSONException;
import static org.globalbarbernetwork.constants.Constants.*;
import org.globalbarbernetwork.entities.Employee;
import org.globalbarbernetwork.entities.User;

/**
 *
 * @author Grup 3
 */
public class ScheduleManager extends Manager implements ManagerInterface {

    private final FirebaseDAO firebaseDAO = new FirebaseDAO();
    
    private final String LOAD_MANAGE_RESERVES = "loadManageReserves";
    private final String GET_TIMETABLE_AJAX = "getTimetableAjax";
    private final String GET_AVAILABLE_HOURS_AJAX = "getAvailableHoursAjax";
    private final String ADD_RESERVE_AJAX = "addReserveAjax";
    private final String RESERVE_HISTORICAL = "loadClientHistorical";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, String action) {
        try {
            RequestDispatcher rd = null;
            User activeUser = (User) request.getSession().getAttribute("user");

            switch (action) {
                case LOAD_MANAGE_RESERVES:
                    JSONObject json = getTimetableToJSON2(activeUser.getUID());
                    // Recoger holidays hairdressing para setearlas como disabled
                    JSONObject json2 = getAllReservesToJSON(activeUser.getUID());
                    
                    request.setAttribute("businessHoursJSON", json.toString());
                    request.setAttribute("reservesEventsJSON", json2.toString());
                    
                    rd = request.getRequestDispatcher("/" + MANAGE_RESERVES_JSP);
                    break;
                case GET_TIMETABLE_AJAX:
                    response.setContentType("application/json");

                    String idHairdressing = request.getParameter("idHairdressing");
            
                    try ( PrintWriter out = response.getWriter()) {
                        out.print(getTimetableToJSON(idHairdressing));
                    }
                    break;
                case GET_AVAILABLE_HOURS_AJAX:
                    response.setContentType("application/json");

                    String idHairdressing2 = request.getParameter("idHairdressingSelected");
                    String idHairdresser = request.getParameter("idHairdresserSelected");
                    String idService = request.getParameter("idServiceSelected");

                    DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate date = LocalDate.parse(request.getParameter("selectedDate"), pattern);

                    ArrayList<LocalTime> rangeHoursComplete = getListAvailableHours(idHairdressing2, idHairdresser, idService, date);

                    getListAvailableHoursToJSON(response, rangeHoursComplete);
                    break;
                case ADD_RESERVE_AJAX:
                    response.setContentType("application/json");

                    String idHairdressing3 = request.getParameter("idHairdressing");
                    String idHairdresser2 = request.getParameter("idHairdresser");
                    String idService2 = request.getParameter("idService");
                    DateTimeFormatter pattern2 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate date2 = LocalDate.parse(request.getParameter("date"), pattern2);
                    Integer time = Integer.valueOf(request.getParameter("time"));

                    addReserve(response, activeUser, idHairdressing3, idHairdresser2, idService2, date2, time);
                    break;
                case RESERVE_HISTORICAL:
                    if (activeUser != null) {
                        HashMap<String, ArrayList> historical = (HashMap<String, ArrayList>) getClientHistorical(activeUser);
                        request.setAttribute("historical", historical);
                        rd = request.getRequestDispatcher("/" + HISTORICAL_CLIENT_JSP);
                    } else {
                        rd = request.getRequestDispatcher("/" + LOGIN_JSP);
                    }
                    break;
            }

            try {
                if (rd != null) {
                    this.buildMenuOptions(request, response);
                    rd.forward(request, response);
                }
            } catch (ServletException ex) {
                Logger.getLogger(AccessManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(AccessManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(ScheduleManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(ScheduleManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private JSONObject getTimetableToJSON2(String idHairdressing) throws JSONException {
        Map<String, Object> timetable = firebaseDAO.getScheduleHairdressing(idHairdressing);

        JSONObject json = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        if (timetable != null) {
            LinkedHashMap<String, Object> jsonOrderedMap;
            for (Map.Entry<String, Object> entry : timetable.entrySet()) {
                Integer dayOfWeek = parseInt(entry.getKey());
                Map<String, Map<String, String>> rangesHours = (Map<String, Map<String, String>>) entry.getValue();

                jsonOrderedMap = new LinkedHashMap<>();
                String rangeHour1Start = (String) ((Map<String, String>) rangesHours.get("rangeHour1")).get("startHour");
                String rangeHour1End = (String) ((Map<String, String>) rangesHours.get("rangeHour1")).get("endHour");
                String rangeHour2Start = (String) ((Map<String, String>) rangesHours.get("rangeHour2")).get("startHour");
                String rangeHour2End = (String) ((Map<String, String>) rangesHours.get("rangeHour2")).get("endHour");

                if (!rangeHour1Start.isEmpty() && !rangeHour1End.isEmpty() || !rangeHour2Start.isEmpty() && !rangeHour2End.isEmpty()) {
                    jsonOrderedMap.put("dayOfWeek", dayOfWeek);
                    jsonOrderedMap.put("rangeHourInit", !rangeHour1Start.isEmpty() ? rangeHour1Start : rangeHour2Start);
                    jsonOrderedMap.put("rangeHourEnd", !rangeHour2End.isEmpty() ? rangeHour2End : rangeHour1End);

                    JSONObject member = new JSONObject(jsonOrderedMap);
                    jsonArray.put(member);
                }
            }
            json.put("jsonArray", jsonArray);
        }

        return json;
    }
    
    private JSONObject getAllReservesToJSON(String idHairdressing) throws JSONException {
        ArrayList<Reserve> listReserves = firebaseDAO.getReserves2(idHairdressing);
        
        JSONObject json = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        if (listReserves != null) {
            LinkedHashMap<String, Object> jsonOrderedMap;
            for (Reserve reserve : listReserves) {
                jsonOrderedMap = new LinkedHashMap<>();
                
                Client client = firebaseDAO.getClient(reserve.getIdClient());

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

                jsonOrderedMap.put("title", client.getName() + " " + client.getSurname());
                jsonOrderedMap.put("startDateTime",  reserve.obtainTimeInitLocalDate().format(formatter));
                jsonOrderedMap.put("endDateTime", reserve.obtainTimeFinalLocalDate().format(formatter));
                
                JSONObject member = new JSONObject(jsonOrderedMap);
                jsonArray.put(member);
            }
            
            json.put("jsonArray", jsonArray);
        }
        
        return json;
    }

    private JSONObject getTimetableToJSON(String idHairdressing) {
        Map<String, Object> timetable = firebaseDAO.getScheduleHairdressing(idHairdressing);
        
        JSONObject json = null;
        if (timetable != null) {
            LinkedHashMap<String, Object> jsonOrderedMap = new LinkedHashMap<>();
            for (Map.Entry<String, Object> entry : timetable.entrySet()) {
                String dayOfWeek = entry.getKey();
                Map<String, Map<String, String>> rangesHours = (Map<String, Map<String, String>>) entry.getValue();

                LinkedHashMap<String, Object> jsonOrderedMap2 = new LinkedHashMap<>();
                jsonOrderedMap2.put("dayOfWeek", getNameOfDayOfWeek(dayOfWeek) + ":");
                jsonOrderedMap2.put("rangesHours", formatTimetable(rangesHours));

                jsonOrderedMap.put(dayOfWeek, new JSONObject(jsonOrderedMap2));
            }
            json = new JSONObject(jsonOrderedMap);
        }

        return json;
    }

    private String formatTimetable(Map<String, Map<String, String>> timetable) {
        String range1Start = timetable.get("rangeHour1").get("startHour");
        String range1End = timetable.get("rangeHour1").get("endHour");
        String range2Start = timetable.get("rangeHour2").get("startHour");
        String range2End = timetable.get("rangeHour2").get("endHour");

        String range;
        if (range1Start.equals("") && range1End.equals("") && range2Start.equals("") && range2End.equals("")) {
            range = "tancat";
        } else if (range1Start.equals("") && range1End.equals("")) {
            range = range2Start + "-" + range2End;
        } else if (range2Start.equals("") && range2End.equals("")) {
            range = range1Start + "-" + range1End;
        } else {
            range = range1Start + "-" + range1End + " / " + range2Start + "-" + range2End;
        }

        return range;
    }

    private String getNameOfDayOfWeek(String dayOfWeek) {
        String[] namesOfDays = new String[]{"Dilluns", "Dimarts", "Dimecres", "Dijous", "Divendres", "Dissabte", "Diumenge"};

        return namesOfDays[Integer.parseInt(dayOfWeek) - 1];
    }

    private int convertDurationToMin(String duration) {
        String[] tmpDuration = duration.split(":");
        return Integer.parseInt(tmpDuration[0]) * 60 + Integer.parseInt(tmpDuration[1]);
    }

    private ArrayList<LocalTime> getListSplitHours(Map<String, Object> rangeHour) {
        boolean continueWhile = true;
        ArrayList<LocalTime> rangeHourSplit = new ArrayList();
        LocalTime iniHour = !((String) rangeHour.get("startHour")).isEmpty() ? LocalTime.parse((CharSequence) rangeHour.get("startHour")) : null;
        LocalTime endHour = !((String) rangeHour.get("endHour")).isEmpty() ? LocalTime.parse((CharSequence) rangeHour.get("endHour")) : null;

        if (iniHour != null && endHour != null) {
            while (continueWhile) {
                rangeHourSplit.add(iniHour);
                iniHour = iniHour.plusMinutes(Long.valueOf(INCREMENT_MINUTES));
                if (iniHour.equals(endHour)) {
                    continueWhile = false;
                }
            }
        }
        return rangeHourSplit;
    }

    private ArrayList<LocalTime> getListAvailableHours(String idHairdressing, String idHairdresser, String idService, LocalDate date) {
        Map<String, Object> timetableHairdressing = firebaseDAO.getScheduleHairdressing(idHairdressing);
        String dayOfWeek = String.valueOf(date.getDayOfWeek().getValue());

        Map<String, Object> timetableDay = (Map<String, Object>) timetableHairdressing.get(dayOfWeek);

        ArrayList<LocalTime> rangeHour1 = getListSplitHours((Map<String, Object>) timetableDay.get("rangeHour1"));
        ArrayList<LocalTime> rangeHour2 = getListSplitHours((Map<String, Object>) timetableDay.get("rangeHour2"));

        ArrayList<LocalTime> rangeHoursComplete;
        rangeHoursComplete = (ArrayList<LocalTime>) rangeHour1.clone();
        rangeHoursComplete.addAll(rangeHour2);

        Service selectedService = firebaseDAO.getServiceById(idHairdressing, idService);
        DateTimeFormatter formatterWithDash = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDateString = date.format(formatterWithDash);
        Map<LocalTime, Integer> timeToDrop = new HashMap<>();
        LocalTime rangeHour1Final = !((String) ((Map<String, Object>) timetableDay.get("rangeHour1")).get("endHour")).isEmpty()
                ? LocalTime.parse((CharSequence) ((Map<String, Object>) timetableDay.get("rangeHour1")).get("endHour")) : null;
        LocalTime rangeHour2Final = !((String) ((Map<String, Object>) timetableDay.get("rangeHour2")).get("endHour")).isEmpty()
                ? LocalTime.parse((CharSequence) ((Map<String, Object>) timetableDay.get("rangeHour2")).get("endHour")) : null;
        LocalDateTime ldtNow = LocalDateTime.now();

        // Excluir horas de reservas del peluquero seleccionado
        if (idHairdresser != null && !idHairdresser.isEmpty()) {
            ArrayList<Reserve> listReservesEmployee = firebaseDAO.getReservesEmployeeByStatePending(idHairdressing, String.valueOf(date.getYear()),
                    String.valueOf(date.getMonthValue()), formattedDateString, idHairdresser);

            for (Iterator it = rangeHoursComplete.iterator(); it.hasNext();) {
                LocalTime time = (LocalTime) it.next();
                LocalTime ltFinalTmpAfter = time.plusMinutes(selectedService.getDuration());
                boolean existOverlap = false;

                for (Reserve reserve : listReservesEmployee) {
                    LocalTime ltInit = reserve.obtainTimeInitLocalDate().toLocalTime();
                    LocalTime ltFinal = reserve.obtainTimeFinalLocalDate().toLocalTime();
                    LocalTime ltInitTmpBefore = ltInit.minusMinutes(selectedService.getDuration());
                    LocalTime ltFinalTmpBefore = ltInit;

                    // Eliminamos las horas que coincidan con las reservas pendientes.
                    if ((ltInit.equals(time) || time.isAfter(ltInit) && time.isBefore(ltFinal))
                            || (time.isAfter(ltInitTmpBefore) && time.isBefore(ltFinalTmpBefore))) {
                        existOverlap = true;
                        break;
                    }
                }

                // Eliminamos las horas que coincidan con posible reserva en base al servicio seleccionado.
                if ((rangeHour1Final != null && ltFinalTmpAfter.isAfter(rangeHour1Final)
                        || rangeHour2Final != null && ltFinalTmpAfter.isAfter(rangeHour2Final))) {
                    existOverlap = true;
                }

                if (existOverlap || ldtNow.toLocalDate().equals(date) && time.isBefore(ldtNow.toLocalTime())) {
                    it.remove();
                }
            }
        } else { // Excluir todas las horas con reserva siempre y cuando esten reservadas ocupando a todos los empleados.
            List<Employee> listEmployees = firebaseDAO.getAllEmployees(idHairdressing);
            int num = 0;
            for (Employee employee : listEmployees) {
                ArrayList<Reserve> listReservesEmployee = firebaseDAO.getReservesEmployeeByStatePending(idHairdressing, String.valueOf(date.getYear()),
                        String.valueOf(date.getMonthValue()), formattedDateString, employee.getIdNumber());
                num++;
                for (LocalTime time : rangeHoursComplete) {
                    LocalTime ltFinalTmpAfter = time.plusMinutes(selectedService.getDuration());

                    for (Reserve reserve : listReservesEmployee) {
                        LocalTime ltInit = reserve.obtainTimeInitLocalDate().toLocalTime();
                        LocalTime ltFinal = reserve.obtainTimeFinalLocalDate().toLocalTime();
                        LocalTime ltInitTmp = ltInit.minusMinutes(selectedService.getDuration());
                        LocalTime ltFinalTmp = ltInit;

                        // Eliminamos las horas que coincidan con las reservas pendientes.
                        if ((ltInit.equals(time) || time.isAfter(ltInit) && time.isBefore(ltFinal))
                                || (time.isAfter(ltInitTmp) && time.isBefore(ltFinalTmp))) {
                            if (timeToDrop.get(time) != null && timeToDrop.get(time) != num) {
                                timeToDrop.put(time, timeToDrop.get(time) + 1);
                            } else if (timeToDrop.get(time) == null) {
                                timeToDrop.put(time, 1);
                            }
                        }
                    }

                    // Eliminamos las horas que coincidan con posible reserva en base al servicio seleccionado.
                    if ((rangeHour1Final != null && ltFinalTmpAfter.isAfter(rangeHour1Final)
                            || rangeHour2Final != null && ltFinalTmpAfter.isAfter(rangeHour2Final))) {
                        if (timeToDrop.get(time) != null && timeToDrop.get(time) != num) {
                            timeToDrop.put(time, timeToDrop.get(time) + 1);
                        } else if (timeToDrop.get(time) == null) {
                            timeToDrop.put(time, 1);
                        }
                    }

                    if (ldtNow.toLocalDate().equals(date) && time.isBefore(ldtNow.toLocalTime())) {
                        timeToDrop.put(time, listEmployees.size());
                    }
                }
            }

            for (Map.Entry<LocalTime, Integer> entry : timeToDrop.entrySet()) {
                LocalTime time = entry.getKey();
                Integer occurrencesNotAvailableTime = entry.getValue();

                if (occurrencesNotAvailableTime.equals(listEmployees.size())) {
                    rangeHoursComplete.remove(time);
                }
            }
        }

        return rangeHoursComplete;
    }

    private void getListAvailableHoursToJSON(HttpServletResponse response, ArrayList<LocalTime> rangeHourComplete) throws IOException {
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        try (PrintWriter out = response.getWriter()) {
            LinkedHashMap<String, Object> jsonOrderedMap;

            for (LocalTime time : rangeHourComplete) {
                jsonOrderedMap = new LinkedHashMap<>();

                jsonOrderedMap.put("timeInFormat", time.toString());
                jsonOrderedMap.put("timeInMinutes", convertDurationToMin(time.toString()));

                JSONObject member = new JSONObject(jsonOrderedMap);
                array.put(member);
            }

            json.put("jsonArray", array);

            out.print(json);
        } catch (IOException ex) {
            Logger.getLogger(ScheduleManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(ScheduleManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addReserve(HttpServletResponse response, User activeUser, String idHairdressing, String idHairdresser, String idService, LocalDate date, Integer time) throws IOException {
        Service selectedService = firebaseDAO.getServiceById(idHairdressing, idService);
        DateTimeFormatter formatterWithDash = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDateString = date.format(formatterWithDash);
        LocalTime ltInitReserveTmp = LocalTime.parse("00:00").plusMinutes(Long.valueOf(time));
        LocalTime ltFinalReserveTmp = ltInitReserveTmp.plusMinutes(Long.valueOf(selectedService.getDuration()));
        boolean existOverlap = false;
        String idEmployeeFree = "";

        if (idHairdresser != null && !idHairdresser.isEmpty()) {
            ArrayList<Reserve> listReservesEmployee = firebaseDAO.getReservesEmployeeByStatePending(idHairdressing, String.valueOf(date.getYear()),
                    String.valueOf(date.getMonthValue()), formattedDateString, idHairdresser);

            for (Reserve reserve : listReservesEmployee) {
                LocalTime ltInit = reserve.obtainTimeInitLocalDate().toLocalTime();
                LocalTime ltFinal = reserve.obtainTimeFinalLocalDate().toLocalTime();

                if (ltInitReserveTmp.equals(ltInit)
                        || (ltInitReserveTmp.isAfter(ltInit) && ltInitReserveTmp.isBefore(ltFinal))
                        || (ltFinalReserveTmp.isAfter(ltInit) && ltFinalReserveTmp.isBefore(ltFinal))
                        || (ltFinalReserveTmp.equals(ltFinal))) {
                    existOverlap = true;
                    break;
                }
            }

            if (!existOverlap) {
                idEmployeeFree = idHairdresser;
            }

        } else {
            List<Employee> listEmployees = firebaseDAO.getAllEmployees(idHairdressing);

            for (Employee employee : listEmployees) {
                ArrayList<Reserve> listReservesEmployee = firebaseDAO.getReservesEmployeeByStatePending(idHairdressing, String.valueOf(date.getYear()),
                        String.valueOf(date.getMonthValue()), formattedDateString, employee.getIdNumber());

                for (Reserve reserve : listReservesEmployee) {
                    LocalTime ltInit = reserve.obtainTimeInitLocalDate().toLocalTime();
                    LocalTime ltFinal = reserve.obtainTimeFinalLocalDate().toLocalTime();

                    if (ltInitReserveTmp.equals(ltInit)
                            || (ltInitReserveTmp.isAfter(ltInit) && ltInitReserveTmp.isBefore(ltFinal))
                            || (ltFinalReserveTmp.isAfter(ltInit) && ltFinalReserveTmp.isBefore(ltFinal))
                            || (ltFinalReserveTmp.equals(ltFinal))) {
                        existOverlap = true;
                        break;
                    } else if (!existOverlap && listReservesEmployee.get(listReservesEmployee.size() - 1).getId().equals(reserve.getId())) {
                        idEmployeeFree = employee.getIdNumber();
                        break;
                    }
                }

                if (!existOverlap && !idEmployeeFree.isEmpty()) {
                    break;
                }
            }
        }

        JSONObject json;
        LinkedHashMap<String, Object> jsonOrderedMap = new LinkedHashMap();

        if (!existOverlap) {
            // Metodo para guardar
            Reserve reserve = new Reserve(activeUser.getUID(), idHairdressing, idEmployeeFree, idService, STATE_PENDING);
            LocalDateTime ldtReserve = LocalDateTime.of(date, ltInitReserveTmp);
            reserve.modifyTimeInitDate(LocalDateTime.of(date, ltInitReserveTmp));
            reserve.modifyTimeFinalDate(LocalDateTime.of(date, ltFinalReserveTmp));

            String reserveRef = firebaseDAO.insertReserve(reserve, String.valueOf(ldtReserve.getYear()), String.valueOf(ldtReserve.getMonthValue()), formattedDateString);
            
            // Se guardan la referencia de la reserva para eliminarlo facilmente
            String reserveClientRef = firebaseDAO.insertReserveClient(activeUser.getUID(), reserveRef);
            
            // Se guardan la referencia de la reserva y la referencia de la tabla de la reserva cliente para eliminarlo facilmente
            String reserveEmployeeRef = firebaseDAO.insertReserveEmployee(reserve.getIdHairdressing(), reserve.getIdEmployee(), reserveRef, reserveClientRef);
            
            // Se añade la referencia de la reserva empleado creada anteriormente para eliminarlo facilmente
            firebaseDAO.updateReserveClient(activeUser.getUID(), reserveClientRef, reserveRef, reserveEmployeeRef);          

            // Devolver datos para printar "Reserva realitzada per el día X a l'hora Y".
            DateTimeFormatter formatter;
            if (ldtReserve.getHour() == 13 || ldtReserve.getHour() == 01) {
                formatter = DateTimeFormatter.ofPattern("EEEE, d MMMM 'del' yyyy 'a la' HH:mm", Locale.forLanguageTag("ca-ES"));
            } else {
                formatter = DateTimeFormatter.ofPattern("EEEE, d MMMM 'del' yyyy 'a les' HH:mm", Locale.forLanguageTag("ca-ES"));
            }

            jsonOrderedMap.put("message", "Reserva realitzada pel " + ldtReserve.format(formatter));

            json = new JSONObject(jsonOrderedMap);
        } else {
            // Devolver el error de solapamiento.
            response.sendError(409, "Hi ha hagut, un solapament amb un altre reserva, si us plau realitza de nou la reserva per les hores disponibles.");
            jsonOrderedMap.put("messageError", "Hi ha hagut, un solapament amb un altre reserva, si us plau realitza de nou la reserva per les hores disponibles.");
            json = new JSONObject(jsonOrderedMap);
        }

        try (PrintWriter out = response.getWriter()) {
            out.print(json);
        }
    }

    private Map getClientHistorical(User activeUser) {
        Map<String, ArrayList> historical = new HashMap<>();
        Map<String, Boolean> isOnRange = new HashMap<>();
        ArrayList<Map> completedReserves = new ArrayList<>();
        ArrayList<Map> pendingReserves = new ArrayList<>();
        Map<String, Object> pendingReserve = new HashMap<>();
        Map<String, Object> completedReserve = new HashMap<>();
        ReserveBO reserveBO = new ReserveBO();

        List<Reserve> reserves = firebaseDAO.getClientHistorical(activeUser);
        for (Reserve reserve : reserves) {
            pendingReserve = new HashMap<>();
            completedReserve = new HashMap<>();

            isOnRange = dateIsOnRange(reserve.getTimeInit());
            if (!isOnRange.isEmpty()) {
                if (isOnRange.get("before") != null && isOnRange.get("before").equals(Boolean.TRUE)) {
                    completedReserve.put("hairdressing", reserveBO.getHairdressing(reserve.getIdHairdressing()));
                    completedReserve.put("reserve", reserve);
                    completedReserve.put("service", reserveBO.getService(reserve.getIdHairdressing(), reserve.getIdService()));
                    completedReserve.put("employee", reserveBO.getEmployee(reserve.getIdHairdressing(), reserve.getIdEmployee()));
                    completedReserves.add(completedReserve);
                } else if (isOnRange.get("after") != null && isOnRange.get("after").equals(Boolean.TRUE)) {
                    pendingReserve.put("hairdressing", reserveBO.getHairdressing(reserve.getIdHairdressing()));
                    pendingReserve.put("reserve", reserve);
                    pendingReserve.put("service", reserveBO.getService(reserve.getIdHairdressing(), reserve.getIdService()));
                    pendingReserve.put("employee", reserveBO.getEmployee(reserve.getIdHairdressing(), reserve.getIdEmployee()));
                    pendingReserves.add(pendingReserve);
                }
            }

            historical.put("pendingReserves", pendingReserves);
            historical.put("completedReserves", completedReserves);

        }

        return historical;
    }

    private Map<String, Boolean> dateIsOnRange(Date reserveDate) {
        Map<String, Boolean> isOnRange = new HashMap<>();
        LocalDateTime currentDate = LocalDateTime.now();

        LocalDateTime date = reserveDate.toInstant()
                .atZone(ZoneId.of("Europe/Madrid"))
                .toLocalDateTime();

        LocalDateTime currentDateMaxValue = currentDate.plus(Period.ofMonths(2));
        LocalDateTime currentDateMinValue = currentDate.minus(Period.ofMonths(2));

        if (date.isBefore(currentDate) && date.isAfter(currentDateMinValue)) {
            isOnRange.put("before", Boolean.TRUE);
        } else if (date.isAfter(currentDate) && date.isBefore(currentDateMaxValue)) {
            isOnRange.put("after", Boolean.TRUE);
        } else {
            return null;
        }

        return isOnRange;
    }

}
