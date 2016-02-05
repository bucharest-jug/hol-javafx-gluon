/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.notes.events.model;


import java.util.Stack;

/**
 * Class with internal stack to pass EventModel object from controller that add new event to main controller
 * @author mihael.buzdugan
 */
public class CurrentEvents {
    
    private final Stack<EventModel> currentEvents;
    
    public CurrentEvents(){
        this.currentEvents =  new Stack<> ();
    }

    public EventModel getLastEvent() {
        if(currentEvents.isEmpty())
            return null;
        return currentEvents.pop();
    }

    public void addNewEvent(EventModel newEvent){
        if(newEvent != null)
            currentEvents.push(newEvent);
    }
}
