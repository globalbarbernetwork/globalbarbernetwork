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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Integer.parseInt;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VAlarm;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Action;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Contact;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.RandomUidGenerator;
import net.fortuna.ical4j.util.UidGenerator;
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
import org.globalbarbernetwork.entities.Hairdressing;
import org.globalbarbernetwork.entities.User;
import org.globalbarbernetwork.services.SmtpService;

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

    /**
     * This method will be executed on load ScheduleManager
     *
     * @param request the request
     * @param response the response
     * @param action the action
     */
    public void execute(HttpServletRequest request, HttpServletResponse response, String action) {

        try {
            RequestDispatcher rd = null;
            User activeUser = (User) request.getSession().getAttribute("user");

            switch (action) {
                case LOAD_MANAGE_RESERVES:
                    JSONObject json = getTimetableToJSON2(activeUser.getUID());
                    // Recoger holidays hairdressing para setearlas como disabled
                    JSONObject json2 = getAllReservesAndHolidaysToJSON(activeUser.getUID());

                    request.setAttribute("businessHoursJSON", json.toString());
                    request.setAttribute("reservesEventsJSON", json2.toString());

                    rd = request.getRequestDispatcher("/" + MANAGE_RESERVES_JSP);
                    break;
                case GET_TIMETABLE_AJAX:
                    response.setContentType("application/json");

                    String idHairdressing = request.getParameter("idHairdressing");

                    try (PrintWriter out = response.getWriter()) {
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

                    addReserveAndSendEmailReserveConfirmation(response, activeUser, idHairdressing3, idHairdresser2, idService2, date2, time);
                    break;
                case RESERVE_HISTORICAL:
                    if (activeUser != null) {
                        HashMap<String, ArrayList> historical = (HashMap<String, ArrayList>) getClientHistorical(activeUser);
                        request.setAttribute("historical", historical);
                        rd = request.getRequestDispatcher("/" + HISTORICAL_CLIENT_JSP);
                    } else {
                        request.setAttribute("newUserCreated", false);
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

    /**
     * This method will return all the reserves for a hairdressing in JSON
     * format
     *
     * @param idHairdressing the id hairdressing
     * @return the timetable to JSO n2
     * @throws JSONException
     */
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

    /**
     * This method will return all the reserves and holidays for a hairdressing,
     * in JSON format
     *
     * @param idHairdressing the id hairdressing
     * @return the all reserves to JSON
     * @throws JSONException
     */
    private JSONObject getAllReservesAndHolidaysToJSON(String idHairdressing) throws JSONException {
        ArrayList<Reserve> listReserves = firebaseDAO.getReserves(idHairdressing);
        ArrayList<Timestamp> listHolidays = firebaseDAO.getHolidays(idHairdressing);
        List<Employee> listEmployees = firebaseDAO.getAllEmployees(idHairdressing);

        JSONObject json = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        LinkedHashMap<String, Object> jsonOrderedMap;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime ldtNow = LocalDateTime.now();

        for (Reserve reserve : listReserves) {
            jsonOrderedMap = new LinkedHashMap<>();

            Client client = firebaseDAO.getClient(reserve.getIdClient());

            jsonOrderedMap.put("title", client.getName() + " " + client.getSurname());
            jsonOrderedMap.put("description", "Descripció llarga");
            jsonOrderedMap.put("startDateTime", reserve.obtainTimeInitLocalDate().format(formatter));
            jsonOrderedMap.put("endDateTime", reserve.obtainTimeFinalLocalDate().format(formatter));
            jsonOrderedMap.put("classNames", "reservePending");
            if (reserve.getState().equals(STATE_COMPLETED) || reserve.obtainTimeFinalLocalDate().isBefore(ldtNow) && !reserve.getState().equals(STATE_ANNULLED)) {
                jsonOrderedMap.put("classNames", "reserveCompleted");
            } else if (reserve.getState().equals(STATE_ANNULLED)) {
                jsonOrderedMap.put("classNames", "reserveAnnulled");
            }

            JSONObject member = new JSONObject(jsonOrderedMap);
            jsonArray.put(member);
        }

        for (Timestamp holiday : listHolidays) {
            jsonOrderedMap = new LinkedHashMap<>();

            LocalDateTime ldtHoliday = holiday.toDate().toInstant()
                    .atZone(ZoneId.of("Europe/Madrid"))
                    .toLocalDateTime();

            jsonOrderedMap.put("title", "Festiu");
            jsonOrderedMap.put("allDay", true);
            jsonOrderedMap.put("startDateTime", ldtHoliday.format(formatter));
            jsonOrderedMap.put("endDateTime", ldtHoliday.format(formatter));
            jsonOrderedMap.put("display", "background");
            jsonOrderedMap.put("classNames", "festiu");

            JSONObject member = new JSONObject(jsonOrderedMap);
            jsonArray.put(member);
        }

        for (Employee employee : listEmployees) {
            ArrayList<Timestamp> holidays = firebaseDAO.getHolidaysEmployee(idHairdressing, employee.getIdNumber());

            for (Timestamp holiday : holidays) {
                jsonOrderedMap = new LinkedHashMap<>();

                LocalDateTime ldtHoliday = holiday.toDate().toInstant()
                        .atZone(ZoneId.of("Europe/Madrid"))
                        .toLocalDateTime();

                jsonOrderedMap.put("title", "Festiu treballador");
                jsonOrderedMap.put("allDay", true);
                jsonOrderedMap.put("startDateTime", ldtHoliday.format(formatter));
                jsonOrderedMap.put("endDateTime", ldtHoliday.format(formatter));
                jsonOrderedMap.put("classNames", "festiuTreballador");

                JSONObject member = new JSONObject(jsonOrderedMap);
                jsonArray.put(member);
            }
        }

        json.put("jsonArray", jsonArray);
        return json;
    }

    /**
     * This method will return the hairdressing timetable in JSON format
     *
     * @param idHairdressing the id hairdressing
     * @return the timetable to JSON
     */
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

    /**
     * This method will return the timetable formatted
     *
     * @param the
     * @param timetable the timetable
     * @return String
     */
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

    /**
     * This method will return the name of the week
     *
     * @param dayOfWeek the day of week
     * @return the name of day of week
     */
    private String getNameOfDayOfWeek(String dayOfWeek) {
        String[] namesOfDays = new String[]{"Dilluns", "Dimarts", "Dimecres", "Dijous", "Divendres", "Dissabte", "Diumenge"};

        return namesOfDays[Integer.parseInt(dayOfWeek) - 1];
    }

    /**
     * This method will convert the duration to min
     *
     * @param duration the duration
     * @return int
     */
    private int convertDurationToMin(String duration) {
        String[] tmpDuration = duration.split(":");
        return Integer.parseInt(tmpDuration[0]) * 60 + Integer.parseInt(tmpDuration[1]);
    }

    /**
     * This method will return a range of hours splitted in a range of minuts
     *
     * @param the
     * @param rangeHour the range hour
     * @return the list split hours
     */
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

    /**
     * This method return a list of available hours for a hairdressing, service
     * and date
     *
     * @param idHairdressing the id hairdressing
     * @param idHairdresser the id hairdresser
     * @param idService the id service
     * @param date the date
     * @return the list available hours
     */
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
        LocalTime rangeHour1Init = !((String) ((Map<String, Object>) timetableDay.get("rangeHour1")).get("startHour")).isEmpty()
                ? LocalTime.parse((CharSequence) ((Map<String, Object>) timetableDay.get("rangeHour1")).get("endHour")) : null;
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
                if ((rangeHour1Final != null && ltFinalTmpAfter.isAfter(rangeHour1Final) && !time.equals(rangeHour1Init) && !time.isAfter(rangeHour1Init)
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
                    if ((rangeHour1Final != null && ltFinalTmpAfter.isAfter(rangeHour1Final) && !time.equals(rangeHour1Init) && !time.isAfter(rangeHour1Init)
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

    /**
     * This method return the available hours in JSON format, this is an API
     * call
     *
     * @param response the response
     * @param rangeHourComplete the range hour complete
     * @throws IOException
     */
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

    /**
     * This method add a new reserve for a hairdressingin Firebase DB and send email reserve confirmation to client
     *
     * @param response the response
     * @param activeUser the active user
     * @param idHairdressing the id hairdressing
     * @param idHairdresser the id hairdresser
     * @param idService the id service
     * @param date the date
     * @param time the time
     * @throws IOException
     */
    public void addReserveAndSendEmailReserveConfirmation(HttpServletResponse response, User activeUser, String idHairdressing, String idHairdresser, String idService, LocalDate date, Integer time) throws IOException {
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
                existOverlap = false;

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

                if (listReservesEmployee.isEmpty()) {
                    idEmployeeFree = employee.getIdNumber();
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
                formatter = DateTimeFormatter.ofPattern("EEEE, d MMMM 'del' yyyy 'a la' HH:mm a", Locale.forLanguageTag("ca-ES"));
            } else {
                formatter = DateTimeFormatter.ofPattern("EEEE, d MMMM 'del' yyyy 'a les' HH:mm a", Locale.forLanguageTag("ca-ES"));
            }
            
            // Información para extraer
            Hairdressing hairdressing = firebaseDAO.getHairdressing(idHairdressing);
            Employee employee = firebaseDAO.getEmployeeByIDNumber(idHairdressing, idEmployeeFree);

            jsonOrderedMap.put("message", "Reserva realitzada a " + hairdressing.getCompanyName()  +  " pel <br/>" + ldtReserve.format(formatter));

            json = new JSONObject(jsonOrderedMap);
            
            //Generación archivo ICS
            String eventTitle = "Cita en " + hairdressing.getCompanyName();
            String description = "Servicio: " + selectedService.getName() + "\nEmpleado: " + employee.getName() + " " + employee.getSurname();
            
            File archiveICS = generateICS(reserve.obtainTimeInitLocalDate(), reserve.obtainTimeFinalLocalDate(), eventTitle, description, hairdressing.getAddress(), hairdressing.getPhoneNumber());
            
            // Envio mail confirmación de reserva
            sendEmailReserveConfirmation(activeUser.getEmail(), hairdressing.getCompanyName(), activeUser.getDisplayName(), 
                                        ldtReserve.format(formatter), selectedService.getName(), employee.getName() + " " + employee.getSurname(), 
                                hairdressing.getAddress(), archiveICS);
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
    
    /**
     * This method will generate archive ICS
     *
     * @param dateEvent the reserve date
     * @param eventTitle the reserve title
     * @param description the description
     * @param address the hairdressing address
     * @param phoneNumber the hairdressing phone number
     */
    private File generateICS(LocalDateTime startDateEvent, LocalDateTime finalDateEvent, String eventTitle, String description, String address, String phoneNumber) {
        // Se crea una alarma que saltara 1 día antes del evento
        VAlarm reminderAlarm = new VAlarm(Duration.ofDays(-1));
        reminderAlarm.add(Action.DISPLAY);

        // Se crea una segunda alarma que saltara 2 horas antes del evento
        VAlarm reminderAlarm2 = new VAlarm(Duration.ofHours(-2));
        reminderAlarm2.add(Action.DISPLAY);

        // Se genera un id unico para el evento
        UidGenerator ug = new RandomUidGenerator();
        Uid uid = ug.generateUid();
        
        // Se crea el objeto evento con fecha inicio, titulo evento y su uid
        VEvent event = new VEvent(startDateEvent, finalDateEvent,eventTitle);
        event.add(uid);
        event.add(new Description(description));
        event.add(new Location(address));
        event.add(new Contact(phoneNumber));
        event.add(reminderAlarm);
        event.add(reminderAlarm2);

        // Se crea un calendario
        Calendar calendar = new Calendar();
        calendar.add(new ProdId("Global Barber Network"));
        calendar.add(Version.VERSION_2_0);
        calendar.add(CalScale.GREGORIAN);

        // Se añade el evento al calendario
        calendar.add(event);

        // Se crea el fichero ics
        File archiveICS = new File("appointment.ics");
        try (FileOutputStream fos = new FileOutputStream(archiveICS)) {
            CalendarOutputter outputter = new CalendarOutputter();
            outputter.output(calendar, fos);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ScheduleManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ScheduleManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return archiveICS;
    }

    /**
     * This method will generate reserve confirmation email.
     *
     * @param email the client's email
     * @param companyName the company name
     * @param displayName the display name
     * @param timeInit the time formatted
     * @param serviceName the service name
     * @param employeeName the employee name
     * @param hairdressingAddress the hairdressing address
     */
    private void sendEmailReserveConfirmation(String email, String companyName, String displayName, String timeInit, String serviceName, 
                                                String employeeName, String hairdressingAddress, File archiveICS) {
        String subjectMailReserveConfirmation = SUBJECT_MAIL_RESERVE_CONFIRMATION.replace("%COMPANY_NAME%", companyName);
        String bodyMailReserveConfirmation = BODY_MAIL_RESERVE_CONFIRMATION.replace("%DISPLAY_NAME%", displayName).replace("%COMPANY_NAME%", companyName)
                                                .replace("%TIME_INIT%", timeInit).replace("%SERVICE_NAME%", serviceName)
                                                .replace("%EMPLOYEE_NAME%", employeeName).replace("%HAIRDRESSING_ADDRESS%", hairdressingAddress);
        
        SmtpService stmpService = new SmtpService();
        stmpService.sendEmailWithAttachment(email, subjectMailReserveConfirmation, bodyMailReserveConfirmation, archiveICS);
    }

    /**
     * This method return the reserve historical of client
     *
     * @param activeUser the active user
     * @return the client historical
     */
    private Map getClientHistorical(User activeUser) {
        Map<String, ArrayList> historical = new HashMap<>();
        Map<String, Boolean> isOnRange = new HashMap<>();
        ArrayList<Map<String, Object>> completedReserves = new ArrayList<>();
        ArrayList<Map<String, Object>> pendingReserves = new ArrayList<>();
        Map<String, Object> pendingReserve = new HashMap<>();
        Map<String, Object> completedReserve = new HashMap<>();
        ReserveBO reserveBO = new ReserveBO();

        List<Reserve> reserves = firebaseDAO.getClientHistorical(activeUser);
        for (Reserve reserve : reserves) {
            pendingReserve = new HashMap<>();
            completedReserve = new HashMap<>();

            isOnRange = dateIsOnRange(reserve.obtainTimeFinalLocalDate());
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

        }

        Collections.sort(pendingReserves, new Comparator<Map<String, Object>>() {
            @Override

            /**
             * This method is for compare dates and sort it
             *
             * @param the
             * @param o1 the o1
             * @param o2 the o2
             * @return int
             */
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {

                Reserve reserve1 = (Reserve) o1.get("reserve");
                Reserve reserve2 = (Reserve) o2.get("reserve");
                return reserve1.obtainTimeInitLocalDate().compareTo(reserve2.obtainTimeInitLocalDate());
            }
        });

        Collections.sort(completedReserves, new Comparator<Map<String, Object>>() {
            @Override

            /**
             * This method is for compare dates and sort it
             *
             * @param the
             * @param o1 the o1
             * @param o2 the o2
             * @return int
             */
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                Reserve reserve1 = (Reserve) o1.get("reserve");
                Reserve reserve2 = (Reserve) o2.get("reserve");
                return reserve1.obtainTimeInitLocalDate().compareTo(reserve2.obtainTimeInitLocalDate());
            }
        });

        historical.put(
                "pendingReserves", pendingReserves);
        historical.put(
                "completedReserves", completedReserves);

        return historical;
    }

    /**
     * This method check if a date is on range to show it in the client
     * historical
     *
     * @param reserveDate the reserve date
     * @return Boolean>
     */
    private Map<String, Boolean> dateIsOnRange(LocalDateTime reserveDate) {
        Map<String, Boolean> isOnRange = new HashMap<>();
        LocalDateTime currentDate = LocalDateTime.now();

        LocalDateTime currentDateMaxValue = currentDate.plus(Period.ofMonths(2));
        LocalDateTime currentDateMinValue = currentDate.minus(Period.ofMonths(2));

        if (reserveDate.isBefore(currentDate) && reserveDate.isAfter(currentDateMinValue)) {
            isOnRange.put("before", Boolean.TRUE);
        } else if (reserveDate.isAfter(currentDate) && reserveDate.isBefore(currentDateMaxValue)) {
            isOnRange.put("after", Boolean.TRUE);
        } else {
            return null;
        }

        return isOnRange;
    }

}
