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

    private String name;
    private String surname;
    private String idNumber;
    private String age;
    private String address;
    private String phoneNumber;
    private String idHairdressing;

    public Employee() {
    }

    public Employee(String name, String surname, String idNumber, String age, String address, String phoneNumber, String idHairdressing) {
        this.name = name;
        this.surname = surname;
        this.idNumber = idNumber;
        this.age = age;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.idHairdressing = idHairdressing;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIdHairdressing() {
        return idHairdressing;
    }

    public void setIdHairdressing(String idHairdressing) {
        this.idHairdressing = idHairdressing;
    }

}
