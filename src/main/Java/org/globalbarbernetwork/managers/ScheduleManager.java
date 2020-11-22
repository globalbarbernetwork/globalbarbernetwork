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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Grup 3
 */
public class ScheduleManager implements ManagerInterface {

    private final FirebaseDAO firebaseDAO = new FirebaseDAO();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, String action) {
        RequestDispatcher rd = null;

        switch (action) {
            case "timetable":
                response.setContentType("text/html;charset=UTF-8");
                String uid = request.getParameter("uidHairdressing");
                Map<String, Object> timetable = firebaseDAO.getTimetableHairdressing(uid);
                if (timetable != null) {
                    timetable.remove("UID");
                }

                JSONObject json = new JSONObject();
                JSONArray array = new JSONArray();

                LinkedHashMap<String, Object> jsonOrderedMap = new LinkedHashMap<>();
                try ( PrintWriter out = response.getWriter()) {
                    if (timetable != null) {
                        for (Map.Entry<String, Object> entry : timetable.entrySet()) {
                            String dayOfWeek = entry.getKey();
                            Map<String, Map<String, String>> rangeHours = (Map<String, Map<String, String>>) entry.getValue();

                            jsonOrderedMap.put(dayOfWeek, formatTimetable(dayOfWeek, rangeHours));
                        }
                    }
                    JSONObject member = new JSONObject(jsonOrderedMap);
                    array.put(member);
                    try {
                        json.put("jsonArray", array);
                    } catch (JSONException ex) {
                        Logger.getLogger(IndexManager.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    out.print(json);
                } catch (IOException ex) {
                    Logger.getLogger(ScheduleManager.class.getName()).log(Level.SEVERE, null, ex);
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

    private String formatTimetable(String dayOfWeek, Map<String, Map<String, String>> timetable) {
        String nameOfDay = getNameOfDayOfWeek(dayOfWeek);
        String space = "";
        for (int i = nameOfDay.length(); i <= 10; i++) {
            space += "&nbsp;";
        }

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

        return nameOfDay + ":" + space + range;
    }

    private String getNameOfDayOfWeek(String dayOfWeek) {
        String[] namesOfDays = new String[]{"Diumenge", "Dilluns", "Dimarts", "Dimecres", "Dijous", "Divendres", "Dissabte"};

        return namesOfDays[Integer.parseInt(dayOfWeek)];
    }
}
