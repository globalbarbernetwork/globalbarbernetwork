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
import static org.globalbarbernetwork.constants.Constants.*;

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

    /**
     * It is a constructor without params.
     */
    public Reserve() {
    }

    /**
     *
     * It is a constructor.
     *
     * @param id the id
     * @param idClient the id client
     * @param idHairdressing the id hairdressing
     * @param idEmployee the id employee
     * @param idService the id service
     * @param timeIni the time ini
     * @param timeFinal the time final
     * @param state the state
     */
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

    /**
     * It is a constructor with params.
     *
     * @param idClient the id client
     * @param idHairdressing the id hairdressing
     * @param idEmployee the id employee
     * @param idService the id service
     * @param state the state
     */
    public Reserve(String idClient, String idHairdressing, String idEmployee, String idService, String state) {
        this.idClient = idClient;
        this.idHairdressing = idHairdressing;
        this.idEmployee = idEmployee;
        this.idService = idService;
        this.state = state;
    }

    /**
     * Gets the identifier
     *
     * @return the identifier
     */
    public String getId() {

        return id;
    }

    /**
     * Sets the identifier
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the identifier client
     *
     * @return the identifier client
     */
    public String getIdClient() {
        return idClient;
    }

    /**
     * Sets the identifier client
     *
     * @param idClient the id client
     */
    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    /**
     * Gets the identifier hairdressing
     *
     * @return the identifier hairdressing
     */
    public String getIdHairdressing() {
        return idHairdressing;
    }

    /**
     * Sets the identifier hairdressing
     *
     * @param idHairdressing the id hairdressing
     */
    public void setIdHairdressing(String idHairdressing) {
        this.idHairdressing = idHairdressing;
    }

    /**
     * Gets the identifier employee
     *
     * @return the identifier employee
     */
    public String getIdEmployee() {
        return idEmployee;
    }

    /**
     * Sets the identifier employee
     *
     * @param idEmployee the id employee
     */
    public void setIdEmployee(String idEmployee) {
        this.idEmployee = idEmployee;
    }

    /**
     * Gets the identifier service
     *
     * @return the identifier service
     */
    public String getIdService() {
        return idService;
    }

    /**
     * Sets the identifier service
     *
     * @param idService the id service
     */
    public void setIdService(String idService) {
        this.idService = idService;
    }

    /**
     * Gets the time init
     *
     * @return the time init
     */
    public Date getTimeInit() {
        return timeInit;
    }

    /**
     * Sets the time init
     *
     * @param timeIni the time ini
     */
    public void setTimeInit(Date timeIni) {
        this.timeInit = timeIni;
    }

    /**
     * Gets the time final
     *
     * @return the time final
     */
    public Date getTimeFinal() {
        return timeFinal;
    }

    /**
     * Sets the time final
     *
     * @param timeFinal the time final
     */
    public void setTimeFinal(Date timeFinal) {
        this.timeFinal = timeFinal;
    }

    /**
     * Gets the state
     *
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the state
     *
     * @param state the state
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Obtain time init local date format
     *
     * @return LocalDateTime
     */
    public LocalDateTime obtainTimeInitLocalDate() {
        return this.timeInit.toInstant()
                .atZone(ZoneId.of("Europe/Madrid"))
                .toLocalDateTime();
    }

    /**
     * Obtain time final local date format
     *
     * @return LocalDateTime
     */
    public LocalDateTime obtainTimeFinalLocalDate() {
        return this.timeFinal.toInstant()
                .atZone(ZoneId.of("Europe/Madrid"))
                .toLocalDateTime();
    }

    /**
     * Modify time init date
     *
     * @param timeIni the time ini
     */
    public void modifyTimeInitDate(LocalDateTime timeIni) {
        this.timeInit = Date.from(timeIni.atZone(ZoneId.of("Europe/Madrid")).toInstant());
    }

    /**
     * Modify time final date
     *
     * @param timeFinal the time final
     */
    public void modifyTimeFinalDate(LocalDateTime timeFinal) {
        this.timeFinal = Date.from(timeFinal.atZone(ZoneId.of("Europe/Madrid")).toInstant());
    }
    
    public String obtainLargeState() {
        switch (this.state) {
            case STATE_PENDING:
                return "Pendent";
            case STATE_COMPLETED:
                return "Completada";
            case STATE_ANNULLED:
                return "Anul·lada";
            default:
                return "";
        }
    }
}
