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
public class Client {
    
    private String name;
    private String surname;
    private String passwword;
    private Integer phoneNumber;
    private String email;
    private String city;
    private Integer zipcode;

    public Client() {
    }

    public Client(String name, String surname, String passwword, Integer phoneNumber, String email, String city, Integer zipcode) {
        this.name = name;
        this.surname = surname;
        this.passwword = passwword;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.city = city;
        this.zipcode = zipcode;
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

    public String getPasswword() {
        return passwword;
    }

    public void setPasswword(String passwword) {
        this.passwword = passwword;
    }

    public Integer getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Integer phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getZipcode() {
        return zipcode;
    }

    public void setZipcode(Integer zipcode) {
        this.zipcode = zipcode;
    }
        
   
}
