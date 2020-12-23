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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
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
                    if (timetable != null) {
                        timetable.remove("uid");
                    }

                    JSONObject json = null;
                    try (PrintWriter out = response.getWriter()) {
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
                    Map<String, Object> timetableHairdressing = firebaseDAO.getTimetableHairdressing(idHairdressing);

                    String idService = request.getParameter("idServiceSelected");

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = sdf.parse(request.getParameter("selectedDate"));

                    String idHairdresser = request.getParameter("idHairdresserSelected");

                    ArrayList<String> rangeHours1 = new ArrayList();
                    ArrayList<String> rangeHours2 = new ArrayList();
                    
                    if (idHairdresser != null) {
                        String dayOfWeek = String.valueOf(date.getDay());

                        Map<String, Object> timetableDay = (Map<String, Object>) timetableHairdressing.get(dayOfWeek);
                        Map<String, Object> rangeHour1 = (Map<String, Object>) timetableDay.get("rangeHour1");
                        Map<String, Object> rangeHour2 = (Map<String, Object>) timetableDay.get("rangeHour2");

                        rangeHours1.add((String) rangeHour1.get("startHour"));
                        rangeHours1.add((String) rangeHour1.get("endHour"));

                        rangeHours2.add((String) rangeHour2.get("startHour"));
                        rangeHours2.add((String) rangeHour2.get("endHour"));
                        
                        boolean continueWhile = true;
                        ArrayList<LocalTime> rangeHour1Split = new ArrayList();
                        ArrayList<LocalTime> rangeHour2Split = new ArrayList();
                        LocalTime iniHour1 = LocalTime.parse((CharSequence) rangeHours1.get(0));
                        LocalTime endHour1 = LocalTime.parse((CharSequence) rangeHours1.get(1));

                        LocalTime iniHour2 = LocalTime.parse((CharSequence) rangeHours2.get(0));
                        LocalTime endHour2 = LocalTime.parse((CharSequence) rangeHours2.get(1));

                        while(continueWhile){
                            rangeHour1Split.add(iniHour1);
                            iniHour1 = iniHour1.plusMinutes(INCREMENT_MINUTES);
                            if (iniHour1.equals(endHour1)) {
                                rangeHour1Split.add(iniHour1);
                                continueWhile = false;
                            }
                        }
                        
                        continueWhile = true;
                        while(continueWhile){
                            rangeHour2Split.add(iniHour2);
                            iniHour2 = iniHour2.plusMinutes(INCREMENT_MINUTES);
                            if (iniHour2.equals(endHour2)) {
                                rangeHour2Split.add(iniHour2);
                                continueWhile = false;
                            }
                        }
                        

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
        } catch (ParseException ex) {
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
        String[] namesOfDays = new String[]{"Diumenge", "Dilluns", "Dimarts", "Dimecres", "Dijous", "Divendres", "Dissabte"};

        return namesOfDays[Integer.parseInt(dayOfWeek)];
    }
}
