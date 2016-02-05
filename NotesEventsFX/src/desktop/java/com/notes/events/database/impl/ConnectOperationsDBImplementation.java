package com.notes.events.database.impl;

import com.notes.events.database.ConnectOperationsDB;
import com.notes.events.model.EventModel;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.mapdb.BTreeMap;
import org.mapdb.DB;
import org.mapdb.DBMaker;

/**
 * Implementation of interface for database operations
 * It is used MapDB
 * http://www.mapdb.org/
 * @author mihael.buzdugan
 */
public class ConnectOperationsDBImplementation implements ConnectOperationsDB{

    private DB db;
    
    private static final Path pathFileDB = Paths.get("src" + File.separator + "desktop"+ File.separator 
            + "resources" + File.separator + "db" + File.separator + "db.map");
    
    @Override
    public void createNewBackup(String nameBackup, List <EventModel> list) {
        
        BTreeMap<String, List<EventModel> > map = db.treeMapCreate(nameBackup).makeOrGet();
                                                  
        map.put(nameBackup, list);
        
        db.commit();
    }

    @Override
    public List<EventModel> retrieveRecords(String nameBackup) {
        BTreeMap<String, List<EventModel> > map = db.treeMap(nameBackup);
        
        List <EventModel> list = map.get(nameBackup);
        return list;
    }

    @Override
    public void connect(String password) {
        db = DBMaker.fileDB(pathFileDB.toFile())
        .closeOnJvmShutdown()
        .encryptionEnable(password)
        .make();     
    }

    @Override
    public void disconnect() {
        if(!db.isClosed())
            db.close();
    }

    @Override
    public void testingString(){
        System.out.println("Testing for desktop");
    }   
    
}
