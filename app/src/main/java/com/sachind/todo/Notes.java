package com.sachind.todo;

import android.util.Log;

import java.io.Serializable;

public class Notes implements Serializable {
    public String title;
    public String description;
    public String dateOfCreation;

    public String getTitle(){
        return this.title;
    }

    public String getDescription(){
        return this.description;
    }

    public String getDateOfCreation(){
        return this.dateOfCreation;
    }

    public void setTitle(String title){
        Log.d("Notes Class", "setTitle: "+title);
        this.title = title;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setDateOfCreation(String dateOfCreation){
        this.dateOfCreation = dateOfCreation;
    }
}
