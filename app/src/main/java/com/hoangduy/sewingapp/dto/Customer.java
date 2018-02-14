package com.hoangduy.sewingapp.dto;

import java.io.Serializable;

/**
 * Created by hoangduy on 2/13/18.
 */

public class Customer implements Serializable {

    private String name;
    private String phone;
    private String description;

    public Customer() {
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
