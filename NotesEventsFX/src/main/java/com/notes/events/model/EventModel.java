/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.notes.events.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Model class for event record
 * @author mihael.buzdugan
 */
public final class EventModel implements Serializable{
    
    private String title;
    private String description;    
    private LocalDateTime dateTime;
    private byte importance;
    private String dateTimeString;
    //if event is marked then alert is not shown for this
    private boolean marked;
    
    //format for date time
    private final static DateTimeFormatter formatter =
                      DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");
    
    public EventModel() { 
        setTitle("unknown");
        setDescription("TODO");
        setDateTime(LocalDateTime.now());
        setImportance((byte) 1);
        marked = false;
    }

    public EventModel(String title, String description, LocalDateTime dateTime, byte importance) {      
        setTitle(title);
        setDescription(description);
        setDateTime(dateTime);
        setImportance(importance);
        marked = false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
        
        this.dateTimeString = this.dateTime.format(formatter);
    }
    
    public byte getImportance() {
        return importance;
    }

    public void setImportance(byte importance) {
        this.importance = importance;
    }

    public String getDateTimeString() {
        return dateTimeString;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }
    
}
