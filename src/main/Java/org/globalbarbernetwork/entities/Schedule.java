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
import java.util.Map;

/**
 *
 * @author Grup 3
 */
public class Schedule {
    
    private String id;
    private String idHairdressing;
    private Integer year;
    private Map<String, Date> rangeHour1;
    private Map<String, Date> rangeHour2;
    private Boolean indBaja;

    public Schedule() {
    }
    
    public Schedule(String id, String idHairdressing, Integer year, Map<String, Date> rangeHour1, Map<String, Date> rangeHour2, Boolean indBaja) {
        this.id = id;
        this.idHairdressing = idHairdressing;
        this.year = year;
        this.rangeHour1 = rangeHour1;
        this.rangeHour2 = rangeHour2;
        this.indBaja = indBaja;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdHairdressing() {
        return idHairdressing;
    }

    public void setIdHairdressing(String idHairdressing) {
        this.idHairdressing = idHairdressing;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Map<String, Date> getRangeHour1() {
        return rangeHour1;
    }

    public void setRangeHour1(Map<String, Date> rangeHour1) {
        this.rangeHour1 = rangeHour1;
    }

    public Map<String, Date> getRangeHour2() {
        return rangeHour2;
    }

    public void setRangeHour2(Map<String, Date> rangeHour2) {
        this.rangeHour2 = rangeHour2;
    }

    public Boolean getIndBaja() {
        return indBaja;
    }

    public void setIndBaja(Boolean indBaja) {
        this.indBaja = indBaja;
    }

    
    
}
