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
package org.globalbarbernetwork.jobs;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import org.globalbarbernetwork.entities.Hairdressing;
import org.globalbarbernetwork.entities.Reserve;
import org.globalbarbernetwork.firebase.FirebaseDAO;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author Grup 3
 */
public class StateReserveJob implements Job {

    private final FirebaseDAO firebaseDAO = new FirebaseDAO();

    private void changeStateReserve() {
        List<Hairdressing> hairdressings = firebaseDAO.getAllHairdressings();
        Date date = new Date();
        LocalDateTime now = date.toInstant().atZone(ZoneId.of("Europe/Madrid")).toLocalDateTime();
        DateTimeFormatter formatterWithDash = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDateString = now.format(formatterWithDash);

        for (Hairdressing hairdressing : hairdressings) {
            List<Reserve> reserves = firebaseDAO.getReservesByStatePending(hairdressing.getUID(), String.valueOf(now.getYear()), String.valueOf(now.getMonthValue()), formattedDateString);

            for (Reserve reserve : reserves) {
                if (reserve.obtainTimeFinalLocalDate().isBefore(now)) {
                    reserve.setState("C");
                    firebaseDAO.updateStateReserve(reserve, String.valueOf(now.getYear()), String.valueOf(now.getMonthValue()), formattedDateString);                    
                }
            }
        }
    }

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        changeStateReserve();
    }

}
