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
public class WorkSchedule {
    private String id;
    private String idHairdressing;
    private String uidEmployee;
    private Map<String, Date> rangeHour1;
    private Map<String, Date> rangeHour2;
    

/** 
 * It is a constructor without params. 
 */
    public WorkSchedule() { 
    }


/** 
 * It is a constructor with params. 
 *
 * @param id  the id
 * @param idHairdressing  the id hairdressing
 * @param uidEmployee  the uid employee
 * @param rangeHour1  the range hour1
 * @param rangeHour2  the range hour2
 */
    public WorkSchedule(String id, String idHairdressing, String uidEmployee, Map<String, Date> rangeHour1, Map<String, Date> rangeHour2) { 
        this.id = id;
        this.idHairdressing = idHairdressing;
        this.uidEmployee = uidEmployee;
        this.rangeHour1 = rangeHour1;
        this.rangeHour2 = rangeHour2;
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
 * @param id  the id
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
 * @param idHairdressing  the id hairdressing
 */
    public void setIdHairdressing(String idHairdressing) { 
        this.idHairdressing = idHairdressing;
    }


/** 
 * Gets the uid employee
 *
 * @return the uid employee
 */
    public String getUidEmployee() { 
        return uidEmployee;
    }


/** 
 * Sets the uid employee
 *
 * @param uidEmployee  the uid employee
 */
    public void setUidEmployee(String uidEmployee) { 
        this.uidEmployee = uidEmployee;
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
 * @param rangeHour1  the range hour1
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
 * @param rangeHour2  the range hour2
 */
    public void setRangeHour2(Map<String, Date> rangeHour2) { 
        this.rangeHour2 = rangeHour2;
    }
}
