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

import com.google.cloud.firestore.GeoPoint;

/**
 *
 * @author Grup 3
 */
public class Hairdressing extends User {

    private String companyName;
    private String website;
    private String city;
    private String address;
    private String country;
    private String province;
    private String zipCode;
    private GeoPoint coordinates;
    private String instagram;
    private String description;


/** 
 * It is a constructor without params.
 */
    public Hairdressing() { 
    }


/** 
 * It is a constructor with params.
 *
 * @param companyName  the company name
 * @param website  the website
 * @param city  the city
 * @param address  the address
 * @param country  the country
 * @param province  the province
 * @param zipCode  the zip code
 * @param coordinates  the coordinates
 * @param instagram  the instagram
 * @param description  the description
 * @param UID  the  UID
 * @param email  the email
 * @param phoneNumber  the phone number
 * @param displayName  the display name
 * @param type  the type
 */
    public Hairdressing(String companyName, String website, String city, String address, String country, String province, String zipCode, GeoPoint coordinates, String instagram, String description, String UID, String email, String phoneNumber, String displayName, String type) { 
        super(UID, email, phoneNumber, displayName, type);
        this.companyName = companyName;
        this.website = website;
        this.city = city;
        this.address = address;
        this.country = country;
        this.province = province;
        this.zipCode = zipCode;
        this.coordinates = coordinates;
        this.instagram = instagram;
        this.description = description;
    }


/** 
 *
 * Gets the company name
 *
 * @return the company name
 */
    public String getCompanyName() { 

        return companyName;
    }


/**
 * Sets the company name
 *
 * @param companyName  the company name
 */
    public void setCompanyName(String companyName) { 
        this.companyName = companyName;
    }


/** 
 * Gets the website
 *
 * @return the website
 */
    public String getWebsite() { 
        return website;
    }


/** 
 * Sets the website
 *
 * @param website  the website
 */
    public void setWebsite(String website) { 
        this.website = website;
    }


/** 
 * Gets the city
 *
 * @return the city
 */
    public String getCity() { 
        return city;
    }


/** 
 * Sets the city
 *
 * @param city  the city
 */
    public void setCity(String city) { 
        this.city = city;
    }


/** 
 * Gets the address
 *
 * @return the address
 */
    public String getAddress() { 
        return address;
    }


/** 
 * Sets the address
 *
 * @param address  the address
 */
    public void setAddress(String address) { 
        this.address = address;
    }


/** 
 * Gets the country
 *
 * @return the country
 */
    public String getCountry() { 
        return country;
    }


/** 
 * Sets the country
 *
 * @param country  the country
 */
    public void setCountry(String country) { 
        this.country = country;
    }


/** 
 * Gets the province
 *
 * @return the province
 */
    public String getProvince() { 
        return province;
    }


/** 
 * Sets the province
 *
 * @param province  the province
 */
    public void setProvince(String province) { 
        this.province = province;
    }


/** 
 * Gets the zip code
 *
 * @return the zip code
 */
    public String getZipCode() { 
        return zipCode;
    }


/** 
 * Sets the zip code
 *
 * @param zipCode  the zip code
 */
    public void setZipCode(String zipCode) { 
        this.zipCode = zipCode;
    }


/** 
 * Gets the coordinates
 *
 * @return the coordinates
 */
    public GeoPoint getCoordinates() { 
        return coordinates;
    }


/** 
 * Sets the coordinates
 *
 * @param coordinates  the coordinates
 */
    public void setCoordinates(GeoPoint coordinates) { 
        this.coordinates = coordinates;
    }


/** 
 * Gets the instagram
 *
 * @return the instagram
 */
    public String getInstagram() { 
        return instagram;
    }


/** 
 * Sets the instagram
 *
 * @param instagram  the instagram
 */
    public void setInstagram(String instagram) { 
        this.instagram = instagram;
    }


/** 
 * Gets the description
 *
 * @return the description
 */
    public String getDescription() { 
        return description;
    }


/** 
 * Sets the description
 *
 * @param description  the description
 */
    public void setDescription(String description) { 
        this.description = description;
    }
}
