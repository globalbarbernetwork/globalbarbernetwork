/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.xtec.ioc.entity;

/**
 *
 * @author Adrian
 */
public class Holidays {
    
    private Integer id;
    private Integer idHairdressing;
    private Date time;

    public Holidays() {
    }

    public Holidays(Integer id, Integer idHairdressing, Date time) {
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
