package com.hoangduy.sewingapp.dto;

import java.io.Serializable;

/**
 * Created by hoangduy on 2/13/18.
 */

public class History implements Serializable {
    private String name;
    private String date;
    private String description;

    public History() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
