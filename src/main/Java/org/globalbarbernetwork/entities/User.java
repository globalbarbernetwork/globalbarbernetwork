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
public class User {

    private String UID;
    private String email;
    private String phoneNumber;
    private String displayName;
    private String type;


/** 
 * It is a constructor without params. 
 */
    public User() { 
    }


/** 
 * It is a constructor with params. 
 *
 * @param UID  the  UID
 * @param email  the email
 * @param phoneNumber  the phone number
 * @param displayName  the display name
 * @param type  the type
 */
    public User(String UID, String email, String phoneNumber, String displayName, String type) { 
        this.UID = UID;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.displayName = displayName;
        this.type = type;
    }


/** 
 * Gets the UID
 *
 * @return the  UID
 */
    public String getUID() { 
        return UID;
    }


/** 
 * Sets the UID
 *
 * @param UID  the  UID
 */
    public void setUID(String UID) { 
        this.UID = UID;
    }


/** 
 * Gets the email
 *
 * @return the email
 */
    public String getEmail() { 
        return email;
    }


/** 
 * Sets the email
 *
 * @param email  the email
 */
    public void setEmail(String email) { 
        this.email = email;
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
 * @param phoneNumber  the phone number
 */
    public void setPhoneNumber(String phoneNumber) { 
        this.phoneNumber = phoneNumber;
    }


/** 
 * Gets the display name
 *
 * @return the display name
 */
    public String getDisplayName() { 
        return displayName;
    }


/** 
 * Sets the display name
 *
 * @param displayName  the display name
 */
    public void setDisplayName(String displayName) { 
        this.displayName = displayName;
    }


/** 
 * Gets the type
 *
 * @return the type
 */
    public String getType() { 
        return type;
    }


/** 
 * Sets the type
 *
 * @param type  the type
 */
    public void setType(String type) { 
        this.type = type;
    }
}
