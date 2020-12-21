/*
 * Copyright (C) 2020 IOC DAW
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

import java.time.Duration;
import java.time.LocalTime;

/**
 *
 * @author IOC DAW
 */
public class Service {

    private Integer id;
    private String name;
    private Integer duration;

    public Service() {
    }

    public Service(Integer id, String name, Integer duration) {
        this.id = id;
        this.name = name;
        this.duration = duration;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String convertMinToFormat(int minutes) {
        Long minute = new Long(minutes);
        return LocalTime.MIN.plus(Duration.ofMinutes(minute)).toString();
    }
}
