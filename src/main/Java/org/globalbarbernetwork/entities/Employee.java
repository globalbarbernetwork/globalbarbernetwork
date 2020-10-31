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

/**
 *
 * @author Grup 3
 */
public class Employee {
    private String uid;
    private String name;
    private String idHairdressing;

    public Employee() {
    }

    public Employee(String uid, String name, String id_hairDressing) {
        this.uid = uid;
        this.name = name;
        this.idHairdressing = id_hairDressing;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdHairdressing() {
        return idHairdressing;
    }

    public void setIdHairdressing(String idHairdressing) {
        this.idHairdressing = idHairdressing;
    }
}
