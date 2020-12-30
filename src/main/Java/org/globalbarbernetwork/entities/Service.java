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

    private String id;
    private String name;
    private Integer duration;
    private Double price;


/** 
 * It is a constructor without params. 
 */
    public Service() { 
    }


/** 
 * It is a constructor with 3 params. 
 *
 * @param id  the id
 * @param name  the name
 * @param duration  the duration
 * @param price  the price
 */
    public Service(String id, String name, Integer duration, Double price) { 
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.price = price;
    }
    

/** 
 * It is a constructor with 4 params. 
 *
 * @param name  the name
 * @param duration  the duration
 * @param price  the price
 */
    public Service(String name, Integer duration, Double price) { 
        this.name = name;
        this.duration = duration;
        this.price = price;
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
 * @param name  the name
 */
    public void setName(String name) { 
        this.name = name;
    }


/** 
 * Gets the duration
 *
 * @return the duration
 */
    public Integer getDuration() { 
        return duration;
    }


/** 
 * Sets the duration
 *
 * @param duration  the duration
 */
    public void setDuration(Integer duration) { 
        this.duration = duration;
    }


/** 
 * Gets the price
 *
 * @return the price
 */
    public Double getPrice() { 
        return price;
    }


/** 
 * Sets the price
 *
 * @param price  the price
 */
    public void setPrice(Double price) { 
        this.price = price;
    }


/** 
 * Obtain duration formatted
 *
 * @return String
 */
    public String obtainDurationFormatted() { 
        return LocalTime.MIN.plus(Duration.ofMinutes(new Long(this.duration))).toString();
    }
    

/** 
 * Obtain price formatted
 *
 * @return String
 */
    public String obtainPriceFormatted() { 
        return String.format("%.2f", this.price).replace(".", ",");
    }
}
