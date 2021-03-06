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
public class Client extends User {
   
    private String name;
    private String surname;

    /**
     * It is a constructor without params.
     */
    public Client() {}

    /**
     * It is a constructor with params.
     *
     * @param name the name
     * @param surname the surname
     * @param UID the UID
     * @param email the email
     * @param phoneNumber the phone number
     * @param displayName the display name
     * @param type the type     
     */
    public Client(String name, String surname, String UID, String email, String phoneNumber, String displayName, String type) {
        super(UID, email, phoneNumber, displayName, type);
        this.name = name;
        this.surname = surname;
    }

    /**
     *
     * Gets the name
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * Sets the name
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * Gets the surname
     *
     * @return the surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     *
     * Sets the surname
     *
     * @param surname the surname
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }
}
