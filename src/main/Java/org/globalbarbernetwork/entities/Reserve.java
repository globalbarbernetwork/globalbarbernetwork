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

import java.util.Date;

/**
 *
 * @author Grup 3
 */
public class Reserve {
    private String id;
    private Integer idHairdressing;
    private Date timeIni;
    private Date timeFinal;
    private String uidEmployee;
    private Boolean active;
    private String uid_client;

    public Reserve() {
    }

    public Reserve(String id, Integer idHairdressing, Date timeIni, Date timeFinal, String uidEmployee, Boolean active, String uid_client) {
        this.id = id;
        this.idHairdressing = idHairdressing;
        this.timeIni = timeIni;
        this.timeFinal = timeFinal;
        this.uidEmployee = uidEmployee;
        this.active = active;
        this.uid_client = uid_client;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getIdHairdressing() {
        return idHairdressing;
    }

    public void setIdHairdressing(Integer idHairdressing) {
        this.idHairdressing = idHairdressing;
    }

    public Date getTimeIni() {
        return timeIni;
    }

    public void setTimeIni(Date timeIni) {
        this.timeIni = timeIni;
    }

    public Date getTimeFinal() {
        return timeFinal;
    }

    public void setTimeFinal(Date timeFinal) {
        this.timeFinal = timeFinal;
    }

    public String getUidEmployee() {
        return uidEmployee;
    }

    public void setUidEmployee(String uidEmployee) {
        this.uidEmployee = uidEmployee;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getUid_client() {
        return uid_client;
    }

    public void setUid_client(String uid_client) {
        this.uid_client = uid_client;
    }
}