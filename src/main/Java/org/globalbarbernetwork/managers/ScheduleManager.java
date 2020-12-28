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

import com.google.common.collect.HashBiMap;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.globalbarbernetwork.firebase.FirebaseDAO;
import org.globalbarbernetwork.interfaces.ManagerInterface;
import org.json.JSONObject;
import org.globalbarbernetwork.entities.Reserve;
import org.globalbarbernetwork.entities.Service;
import org.json.JSONArray;
import org.json.JSONException;
import static org.globalbarbernetwork.constants.Constants.*;
import org.globalbarbernetwork.entities.Employee;
import org.globalbarbernetwork.entities.User;

/**
 *
 * @author Grup 3
 */
public class ScheduleManager implements ManagerInterface {

    private final FirebaseDAO firebaseDAO = new FirebaseDAO();
    private final String GET_TIMETABLE_AJAX = "getTimetableAjax";
    private final String GET_AVAILABLE_HOURS_AJAX = "getAvailableHoursAjax";
    private final String ADD_RESERVE_AJAX = "addReserveAjax";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, String action) {
        try {
            RequestDispatcher rd = null;
            User activeUser = (User) request.getSession().getAttribute("user");
            
            switch (action) {
                case GET_TIMETABLE_AJAX:
                    response.setContentType("application/json");

                    String idHairdressing = request.getParameter("idHairdressing");
                    getTimetableToJSON(response, idHairdressing);
                    break;
                case GET_AVAILABLE_HOURS_AJAX:
                    response.setContentType("application/json");

                    String idHairdressing2 = request.getParameter("idHairdressingSelected");
                    String idHairdresser = request.getParameter("idHairdresserSelected");
                    Integer idService = Integer.valueOf(request.getParameter("idServiceSelected"));

                    DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate date = LocalDate.parse(request.getParameter("selectedDate"), pattern);

                    ArrayList<LocalTime> rangeHoursComplete = getListAvailableHours(idHairdressing2, idHairdresser, idService, date);

                    getListAvailableHoursToJSON(response, rangeHoursComplete);
                    break;
                case ADD_RESERVE_AJAX:
                    response.setContentType("application/json");

                    String idHairdressing3 = request.getParameter("idHairdressing");
                    String idHairdresser2 = request.getParameter("idHairdresser");
                    Integer idService2 = Integer.valueOf(request.getParameter("idService"));
                    DateTimeFormatter pattern2 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate date2 = LocalDate.parse(request.getParameter("date"), pattern2);
                    Integer time = Integer.valueOf(request.getParameter("time"));

                    addReserve(response, activeUser, idHairdressing3, idHairdresser2, idService2, date2, time);
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
        } catch (IOException ex) {
            Logger.getLogger(ScheduleManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getTimetableToJSON(HttpServletResponse response, String idHairdressing) {
        Map<String, Object> timetable = firebaseDAO.getTimetableHairdressing(idHairdressing);

        JSONObject json = null;
        try ( PrintWriter out = response.getWriter()) {
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
            out.print(json);
        } catch (IOException ex) {
            Logger.getLogger(ScheduleManager.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    private ArrayList<LocalTime> getListAvailableHours(String idHairdressing, String idHairdresser, Integer idService, LocalDate date) {
        Map<String, Object> timetableHairdressing = firebaseDAO.getTimetableHairdressing(idHairdressing);
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

        // Excluir horas de reservas del peluquero seleccionado
        if (idHairdresser != null && !idHairdresser.isEmpty()) {
            ArrayList<Reserve> listReservesEmployee = firebaseDAO.getReservesEmployee(idHairdressing, String.valueOf(date.getYear()),
                    String.valueOf(date.getMonthValue()), formattedDateString, idHairdresser);

            for (Reserve reserve : listReservesEmployee) {
                LocalTime ltInit = reserve.getTimeInitLocalDate().toLocalTime();
                LocalTime ltFinal = reserve.getTimeFinalLocalDate().toLocalTime();
                LocalTime ltInitTmpBefore = ltInit.minusMinutes(selectedService.getDuration());
                LocalTime ltFinalTmpBefore = ltInit;

                for (Iterator it = rangeHoursComplete.iterator(); it.hasNext();) {
                    LocalTime time = (LocalTime) it.next();
                    LocalTime ltFinalTmpAfter = time.plusMinutes(selectedService.getDuration());

                    // Eliminamos las horas que coincidan con las reservas pendientes.
                    // Eliminamos las horas que coincidan con posible reserva en base al servicio seleccionado.
                    if ((ltInit.equals(time) || time.isAfter(ltInit) && time.isBefore(ltFinal))
                            || (time.isAfter(ltInitTmpBefore) && time.isBefore(ltFinalTmpBefore))
                            || (rangeHour1Final != null && ltFinalTmpAfter.isAfter(rangeHour1Final)
                            || rangeHour2Final != null && ltFinalTmpAfter.isAfter(rangeHour2Final))) {
                        it.remove();
                    }
                }
            }
        } else { // Excluir todas las horas con reserva siempre y cuando esten reservadas ocupando a todos los empleados.
            List<Employee> listEmployees = firebaseDAO.getAllEmployees(idHairdressing);
            int num = 0;
            for (Employee employee : listEmployees) {
                ArrayList<Reserve> listReservesEmployee = firebaseDAO.getReservesEmployee(idHairdressing, String.valueOf(date.getYear()),
                        String.valueOf(date.getMonthValue()), formattedDateString, employee.getIdNumber());
                num++;
                for (Reserve reserve : listReservesEmployee) {
                    LocalTime ltInit = reserve.getTimeInitLocalDate().toLocalTime();
                    LocalTime ltFinal = reserve.getTimeFinalLocalDate().toLocalTime();
                    LocalTime ltInitTmp = ltInit.minusMinutes(selectedService.getDuration());
                    LocalTime ltFinalTmp = ltInit;

                    for (LocalTime time : rangeHoursComplete) {
                        LocalTime ltFinalTmpAfter = time.plusMinutes(selectedService.getDuration());

                        // Eliminamos las horas que coincidan con las reservas pendientes.
                        // Eliminamos las horas que coincidan con posible reserva en base al servicio seleccionado.
                        if ((ltInit.equals(time) || time.isAfter(ltInit) && time.isBefore(ltFinal))
                                || (time.isAfter(ltInitTmp) && time.isBefore(ltFinalTmp))
                                || (rangeHour1Final != null && ltFinalTmpAfter.isAfter(rangeHour1Final)
                                || rangeHour2Final != null && ltFinalTmpAfter.isAfter(rangeHour2Final))) {
                            if (timeToDrop.get(time) != null && timeToDrop.get(time) != num) {
                                timeToDrop.put(time, timeToDrop.get(time) + 1);
                            } else if (timeToDrop.get(time) == null) {
                                timeToDrop.put(time, 1);
                            }
                        }
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
        try ( PrintWriter out = response.getWriter()) {
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

    public void addReserve(HttpServletResponse response, User activeUser, String idHairdressing, String idHairdresser, Integer idService, LocalDate date, Integer time) throws IOException {
        Service selectedService = firebaseDAO.getServiceById(idHairdressing, idService);
        DateTimeFormatter formatterWithDash = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDateString = date.format(formatterWithDash);
        LocalTime ltInitReserveTmp = LocalTime.parse("00:00").plusMinutes(Long.valueOf(time));
        LocalTime ltFinalReserveTmp = ltInitReserveTmp.plusMinutes(Long.valueOf(selectedService.getDuration()));
        boolean existOverlap = false;
        String idEmployeeFree = "";

        if (idHairdresser != null && !idHairdresser.isEmpty()) {
            ArrayList<Reserve> listReservesEmployee = firebaseDAO.getReservesEmployee(idHairdressing, String.valueOf(date.getYear()),
                    String.valueOf(date.getMonthValue()), formattedDateString, idHairdresser);

            for (Reserve reserve : listReservesEmployee) {
                LocalTime ltInit = reserve.getTimeInitLocalDate().toLocalTime();
                LocalTime ltFinal = reserve.getTimeFinalLocalDate().toLocalTime();

                if (ltInitReserveTmp.equals(ltInit)
                        || (ltInitReserveTmp.isAfter(ltInit) && ltInitReserveTmp.isBefore(ltFinal))
                        || (ltFinalReserveTmp.isAfter(ltInit) && ltFinalReserveTmp.isBefore(ltFinal))
                        || (ltFinalReserveTmp.equals(ltFinal))) {
                    existOverlap = true;
                    break;
                }
            }
            
            if (!existOverlap){
                idEmployeeFree = idHairdresser;
            }

        } else {
            List<Employee> listEmployees = firebaseDAO.getAllEmployees(idHairdressing);
             
            for (Employee employee : listEmployees) {
                ArrayList<Reserve> listReservesEmployee = firebaseDAO.getReservesEmployee(idHairdressing, String.valueOf(date.getYear()),
                        String.valueOf(date.getMonthValue()), formattedDateString, employee.getIdNumber());

                for (Reserve reserve : listReservesEmployee) {
                    LocalTime ltInit = reserve.getTimeInitLocalDate().toLocalTime();
                    LocalTime ltFinal = reserve.getTimeFinalLocalDate().toLocalTime();

                    if (ltInitReserveTmp.equals(ltInit)
                            || (ltInitReserveTmp.isAfter(ltInit) && ltInitReserveTmp.isBefore(ltFinal))
                            || (ltFinalReserveTmp.isAfter(ltInit) && ltFinalReserveTmp.isBefore(ltFinal))
                            || (ltFinalReserveTmp.equals(ltFinal))) {
                        existOverlap = true;
                        break;
                    } else if (!existOverlap && listReservesEmployee.get(listReservesEmployee.size() - 1).getId().equals(reserve.getId())){
                        idEmployeeFree = employee.getIdNumber();
                        break;
                    }
                }
                
                if (!existOverlap && !idEmployeeFree.isEmpty()){
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
            reserve.setTimeInitDate(LocalDateTime.of(date, ltInitReserveTmp));
            reserve.setTimeFinalDate(LocalDateTime.of(date, ltFinalReserveTmp));
            
            firebaseDAO.insertReserve(reserve, String.valueOf(ldtReserve.getYear()), String.valueOf(ldtReserve.getMonthValue()), formattedDateString);
            // Devolver datos para printar "Reserva realitzada per el día X a l'hora Y".
            
            DateTimeFormatter formatter;
            if (ldtReserve.getHour() == 13 || ldtReserve.getHour() == 01 ) {
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
}
