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
public class Date {
    
    private Integer id;
    private Integer idHairdressing;
    private Integer idEmployee;  
    private Date time;    

    public Date() {
    }

    public Date(Integer id, Integer idHairdressing, Integer idEmployee, Date time) {
        this.id = id;
        this.idHairdressing = idHairdressing;
        this.idEmployee = idEmployee;
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

    public Integer getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(Integer idEmployee) {
        this.idEmployee = idEmployee;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
    
    
    
}
