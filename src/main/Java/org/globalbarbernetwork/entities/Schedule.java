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

    /**
     * It is a constructor without params.
     */
    public Schedule() {
    }

    /**
     * It is a constructor with params.
     *
     * @param id the id
     * @param idHairdressing the id hairdressing
     * @param year the year
     * @param rangeHour1 the range hour1
     * @param rangeHour2 the range hour2
     * @param indBaja the ind baja
     */
    public Schedule(String id, String idHairdressing, Integer year, Map<String, Date> rangeHour1, Map<String, Date> rangeHour2, Boolean indBaja) {
        this.id = id;
        this.idHairdressing = idHairdressing;
        this.year = year;
        this.rangeHour1 = rangeHour1;
        this.rangeHour2 = rangeHour2;
        this.indBaja = indBaja;
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
     * Gets the year
     *
     * @return the year
     */
    public Integer getYear() {
        return year;
    }

    /**
     * Sets the year
     *
     * @param year the year
     */
    public void setYear(Integer year) {
        this.year = year;
    }

    /**
     * Gets the range hour1
     *
     * @return the range hour1
     */
    public Map<String, Date> getRangeHour1() {
        return rangeHour1;
    }

    /**
     * Sets the range hour1
     *
     * @param rangeHour1 the range hour1
     */
    public void setRangeHour1(Map<String, Date> rangeHour1) {
        this.rangeHour1 = rangeHour1;
    }

    /**
     * Gets the range hour2
     *
     * @return the range hour2
     */
    public Map<String, Date> getRangeHour2() {
        return rangeHour2;
    }

    /**
     * Sets the range hour2
     *
     * @param rangeHour2 the range hour2
     */
    public void setRangeHour2(Map<String, Date> rangeHour2) {
        this.rangeHour2 = rangeHour2;
    }

    /**
     * Gets the ind baja
     *
     * @return the ind baja
     */
    public Boolean getIndBaja() {
        return indBaja;
    }

    /**
     * Sets the ind baja
     *
     * @param indBaja the ind baja
     */
    public void setIndBaja(Boolean indBaja) {
        this.indBaja = indBaja;
    }

}
