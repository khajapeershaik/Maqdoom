package com.project.maqdoom.ui.services;

import java.io.Serializable;

public class ServicesChecklistItems implements Serializable {

    String name;
    boolean isSelected;

    public ServicesChecklistItems(String name,  boolean isSelected) {
        this.name = name;
        this.isSelected = isSelected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
