/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.notes.events.database;

import com.notes.events.model.EventModel;
import java.util.List;

/**
 * Interface for method which are used for backup and retrieve data from database
 * @author mihael.buzdugan
 */
public interface ConnectOperationsDB {
    
    public void connect(String password);
    public void disconnect();
    public void createNewBackup(String nameBackup, List <EventModel> list);
    public List<EventModel> retrieveRecords(String nameBackup);
    public void testingString();
}
