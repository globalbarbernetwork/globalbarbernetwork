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

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Grup 3
 */
public class Employee {

    private String name;
    private String surname;
    private String idNumber;
    private Date contractIniDate;
    private Date contractEndDate;
    private String phoneNumber;
    private String idHairdressing;

    /**    
     * It is a constructor without params.
     */
    public Employee() {

    }

    /**     
     * It is a constructor with params.
     *
     * @param name the name
     * @param surname the surname
     * @param idNumber the id number
     * @param contractIniDate the contract ini date
     * @param contractEndDate the contract end date
     * @param phoneNumber the phone number
     * @param idHairdressing the id hairdressing
     */
    public Employee(String name, String surname, String idNumber, Date contractIniDate, Date contractEndDate, String phoneNumber, String idHairdressing) {
        this.name = name;
        this.surname = surname;
        this.idNumber = idNumber;
        this.contractIniDate = contractIniDate;
        this.contractEndDate = contractEndDate;
        this.phoneNumber = phoneNumber;
        this.idHairdressing = idHairdressing;
    }

    /**
     * Gets the name
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the surname
     *
     * @return the surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Sets the surname
     *
     * @param surname the surname
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Gets the identifier number
     *
     * @return the identifier number
     */
    public String getIdNumber() {
        return idNumber;
    }

    /**
     * Sets the identifier number
     *
     * @param idNumber the id number
     */
    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    /**
     * Gets the contract ini date
     *
     * @return the contract ini date
     */
    public Date getContractIniDate() {
        return contractIniDate;
    }

    /**
     * Sets the contract ini date
     *
     * @param contractIniDate the contract ini date
     */
    public void setContractIniDate(Date contractIniDate) {
        this.contractIniDate = contractIniDate;
    }

    /**
     * Gets the contract end date
     *
     * @return the contract end date
     */
    public Date getContractEndDate() {
        return contractEndDate;
    }

    /**
     * Sets the contract end date
     *
     * @param contractEndDate the contract end date
     */
    public void setContractEndDate(Date contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    /**
     * Gets the phone number
     *
     * @return the phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phone number
     *
     * @param phoneNumber the phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
     * Date to string
     *
     * @param date the date
     * @return String
     */
    public String dateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }

}
