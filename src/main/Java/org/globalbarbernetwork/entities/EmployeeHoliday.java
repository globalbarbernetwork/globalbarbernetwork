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
public class EmployeeHoliday {

    private String idEmployee;
    private Date timeDate;

    /**
     * It is a constructor without params.
     */
    public EmployeeHoliday() {}

    /**
     * It is a constructor with params.
     *
     * @param idEmployee the id employee
     * @param timeDate the time date
     */
    public EmployeeHoliday(String idEmployee, Date timeDate) {
        this.idEmployee = idEmployee;
        this.timeDate = timeDate;
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
     * Gets the time date
     *
     * @return the time date
     */
    public Date getTimeDate() {
        return timeDate;
    }

    /**
     * Sets the time date
     *
     * @param timeDate the time date
     */
    public void setTimeDate(Date timeDate) {
        this.timeDate = timeDate;
    }

}
