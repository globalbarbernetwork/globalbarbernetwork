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
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
import static org.globalbarbernetwork.constants.Constants.*;
import org.json.JSONArray;
import org.json.JSONException;

/**
 *
 * @author Grup 3
 */
public class ScheduleManager implements ManagerInterface {

    private final FirebaseDAO firebaseDAO = new FirebaseDAO();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, String action) {
        try {
            RequestDispatcher rd = null;

            switch (action) {
                case "timetable":
                    response.setContentType("application/json");

                    String uid = request.getParameter("uidHairdressing");
                    Map<String, Object> timetable = firebaseDAO.getTimetableHairdressing(uid);

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
                    break;
                case "getAvailableHoursAjax":
                    // Hay dos casos :
                    // Calcular las horas disponibles teniendo un peluquero en concreto
                    // Calcular las horas disponibles con todos los peluqueros
                    response.setContentType("application/json");

                    String idHairdressing = request.getParameter("idHairdressingSelected");
                    String idHairdresser = request.getParameter("idHairdresserSelected");
                    String idService = request.getParameter("idServiceSelected");
                                        
                    DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate date = LocalDate.parse(request.getParameter("selectedDate"), pattern);
                    
                    System.out.println(date.getDayOfWeek().getValue());
                    System.out.println(date.getDayOfMonth());
                    System.out.println(date.getMonthValue());
                    System.out.println(date.getYear());
                    
                    

                    Map<String, Object> timetableHairdressing = firebaseDAO.getTimetableHairdressing(idHairdressing);
                    String dayOfWeek = String.valueOf(date.getDayOfWeek().getValue());

                    Map<String, Object> timetableDay = (Map<String, Object>) timetableHairdressing.get(dayOfWeek);

                    ArrayList<LocalTime> rangeHour1 = getListSplitHours((Map<String, Object>) timetableDay.get("rangeHour1"));
                    ArrayList<LocalTime> rangeHour2 = getListSplitHours((Map<String, Object>) timetableDay.get("rangeHour2"));

                    ArrayList<LocalTime> rangeHourComplete;
                    rangeHourComplete = (ArrayList<LocalTime>) rangeHour1.clone();
                    rangeHourComplete.addAll(rangeHour2);
                    
                    
                    
                    // Excluir horas de reservas del peluquero seleccionado
                    if (idHairdresser != null) {
                        //Reservas peluquero del día, activas.
                    } else { // Excluir todas las horas con reserva
                        // Recoger empleados disponibles para el dia solicitado                       
                        // Recoger reservas de cada empleado disponible para el dia solicitado
                        // Mirar si hay alguno disponible para el dia y hora escogido.
                    }
                    
                    //comprobaciones segun servicio
                    

                    getListAvailableHoursToJSON(response, rangeHourComplete);
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

    private String formatTimetable(Map<String, Map<String, String>> timetable) {
        String range1Start = timetable.get("rangeHour1").get("startHour");
        String range1End = timetable.get("rangeHour1").get("endHour");
        String range2Start = timetable.get("rangeHour2").get("startHour");
        String range2End = timetable.get("rangeHour2").get("endHour");

        String range;
        if (range1Start.equals("00:00") && range1End.equals("00:00") && range2Start.equals("00:00") && range2End.equals("00:00")) {
            range = "tancat";
        } else if (range1Start.equals("00:00") && range1End.equals("00:00")) {
            range = range2Start + "-" + range2End;
        } else if (range2Start.equals("00:00") && range2End.equals("00:00")) {
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
        LocalTime iniHour = LocalTime.parse((CharSequence) rangeHour.get("startHour"));
        LocalTime endHour = LocalTime.parse((CharSequence) rangeHour.get("endHour"));

        while (continueWhile) {
            rangeHourSplit.add(iniHour);
            iniHour = iniHour.plusMinutes(INCREMENT_MINUTES);
            if (iniHour.equals(endHour)) {
                rangeHourSplit.add(iniHour);
                continueWhile = false;
            }
        }
        return rangeHourSplit;
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
}
