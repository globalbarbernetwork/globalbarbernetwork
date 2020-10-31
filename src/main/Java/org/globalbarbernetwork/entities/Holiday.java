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

    public Holiday() {
    }

    public Holiday(Integer id, Integer idHairdressing, Date time) {
        this.id = id;
        this.idHairdressing = idHairdressing;
        this.time = time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdHairdressing() {
        return idHairdressing;
    }

    public void setIdHairdressing(Integer idHairdressing) {
        this.idHairdressing = idHairdressing;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
        
}
