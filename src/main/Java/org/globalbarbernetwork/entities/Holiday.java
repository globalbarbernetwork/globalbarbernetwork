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
public class Holiday {
    private Integer id;
    private Integer idHairdressing;
    private Date time;


/** 
 * It is a constructor without params. 
 */
    public Holiday() { 
    }


/** 
 * It is a constructor with params. 
 *
 * @param id  the id
 * @param idHairdressing  the id hairdressing
 * @param time  the time
 */
    public Holiday(Integer id, Integer idHairdressing, Date time) { 
        this.id = id;
        this.idHairdressing = idHairdressing;
        this.time = time;
    }


/** 
 * Gets the identifier
 *
 * @return the identifier
 */
    public Integer getId() { 
        return id;
    }


/** 
 * Sets the identifier
 *
 * @param id  the id
 */
    public void setId(Integer id) { 
        this.id = id;
    }


/** 
 * Gets the identifier hairdressing
 *
 * @return the identifier hairdressing
 */
    public Integer getIdHairdressing() { 
        return idHairdressing;
    }


/** 
 * Sets the identifier hairdressing
 *
 * @param idHairdressing  the id hairdressing
 */
    public void setIdHairdressing(Integer idHairdressing) { 
        this.idHairdressing = idHairdressing;
    }


/** 
 * Gets the time
 *
 * @return the time
 */
    public Date getTime() { 
        return time;
    }


/** 
 * Sets the time
 *
 * @param time  the time
 */
    public void setTime(Date time) { 
        this.time = time;
    }
        
}
