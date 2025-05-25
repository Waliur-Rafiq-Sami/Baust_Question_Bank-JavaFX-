/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package qbank_project;

import java.util.Date;

/**
 *
 * @author waliu
 */
public class UsersData {

    private Integer id;
    private Integer QuestionId;
    private Double total;
    private Date date;
    private String emUsername;

    public UsersData(Integer id, Integer customerID, Double total,
            Date date, String emUsername) {
        this.id = id;
        this.QuestionId = customerID;
        this.total = total;
        this.date = date;
        this.emUsername = emUsername;
    }

    public Integer getId() {
        return id;
    }

    public Integer getCustomerID() {
        return QuestionId;
    }

    public Double getTotal() {
        return total;
    }

    public Date getDate() {
        return date;
    }

    public String getEmUsername() {
        return emUsername;
    }

}
