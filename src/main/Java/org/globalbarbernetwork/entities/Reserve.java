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
package org.globalbarbernetwork.entities;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 *
 * @author Grup 3
 */
public class Reserve {
    private String id;
    private String idClient;
    private String idHairdressing;
    private String idEmployee;
    private String idService;
    private Date timeInit;
    private Date timeFinal;
    private String state;

    public Reserve() {
    }

    public Reserve(String id, String idClient, String idHairdressing, String idEmployee, String idService, Date timeIni, Date timeFinal, String state) {
        this.id = id;
        this.idClient = idClient;
        this.idHairdressing = idHairdressing;
        this.idEmployee = idEmployee;
        this.idService = idService;
        this.timeInit = timeIni;
        this.timeFinal = timeFinal;
        this.state = state;
    }

    public Reserve(String idClient, String idHairdressing, String idEmployee, String idService, String state) {
        this.idClient = idClient;
        this.idHairdressing = idHairdressing;
        this.idEmployee = idEmployee;
        this.idService = idService;
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public String getIdHairdressing() {
        return idHairdressing;
    }

    public void setIdHairdressing(String idHairdressing) {
        this.idHairdressing = idHairdressing;
    }

    public String getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(String idEmployee) {
        this.idEmployee = idEmployee;
    }

    public String getIdService() {
        return idService;
    }

    public void setIdService(String idService) {
        this.idService = idService;
    }

    public Date getTimeInit() {
        return timeInit;
    }

    public void setTimeInit(Date timeIni) {
        this.timeInit = timeIni;
    }

    public Date getTimeFinal() {
        return timeFinal;
    }

    public void setTimeFinal(Date timeFinal) {
        this.timeFinal = timeFinal;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    
    public LocalDateTime obtainTimeInitLocalDate() {
        return this.timeInit.toInstant()
                .atZone(ZoneId.of("Europe/Madrid"))
                .toLocalDateTime();
    }
    
    public LocalDateTime obtainTimeFinalLocalDate() {
        return this.timeFinal.toInstant()   
                .atZone(ZoneId.of("Europe/Madrid"))
                .toLocalDateTime();
    }
    
    public void modifyTimeInitDate(LocalDateTime timeIni) {
        this.timeInit = Date.from(timeIni.atZone(ZoneId.of("Europe/Madrid")).toInstant());
    }
    
    public void modifyTimeFinalDate(LocalDateTime timeFinal) {
        this.timeFinal = Date.from(timeFinal.atZone(ZoneId.of("Europe/Madrid")).toInstant());
    }
}
