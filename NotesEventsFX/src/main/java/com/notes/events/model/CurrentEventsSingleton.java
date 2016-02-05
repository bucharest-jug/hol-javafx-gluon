/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.notes.events.model;

/**
 * Singleton for access current events stack
 * @author mihael.buzdugan
 */
public class CurrentEventsSingleton {
  
    private static CurrentEvents currentEventsInstance = null;
    
    private CurrentEventsSingleton() {}
    
    public static CurrentEvents getInstance() {
        if(currentEventsInstance == null) {
            synchronized(CurrentEventsSingleton.class) {
                if(currentEventsInstance == null) {
                    currentEventsInstance = new CurrentEvents();
                }
            }
        }
        return currentEventsInstance;
    }
}

