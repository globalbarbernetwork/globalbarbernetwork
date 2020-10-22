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
public class WorkSchedule {
    
    private Integer id;
    private Integer idHairdressing;
    private Integer idEmployee;
    private Date timeStart;
    private Date timeEnd;

    public WorkSchedule() {
    }

    public WorkSchedule(Integer id, Integer idHairdressing, Integer idEmployee, Date timeStart, Date timeEnd) {
        this.id = id;
        this.idHairdressing = idHairdressing;
        this.idEmployee = idEmployee;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
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

    public Date getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }
    
    
    
}
