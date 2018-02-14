package com.hoangduy.sewingapp.dto;

import java.io.Serializable;

/**
 * Created by hoangduy on 2/13/18.
 */

public class Schedule implements Serializable {
    private String date;
    private String customerName;
    private String description;

    public Schedule() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
