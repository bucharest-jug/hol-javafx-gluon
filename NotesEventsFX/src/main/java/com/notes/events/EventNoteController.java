/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.notes.events;

import com.notes.events.database.ConnectOperationsDB;
import com.notes.events.model.CurrentEvents;
import com.notes.events.model.CurrentEventsSingleton;
import com.notes.events.model.EventModel;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

/**
 *
 * @author mihael.buzdugan
 */
public class EventNoteController implements Initializable {
    
    
    @FXML
    private Label label;
    
    @FXML
    private Button addNewEventButton;
    
    //@FXML
    //private Button quitButton;
    
    private List <EventModel> eventsRegister;
    
    private ObservableList<EventModel> eventsObservableList;
    
    private ObservableList <EventModel> selectedItems;
    
    @FXML
    private TableView <EventModel> tableCurrentEvents;
    
    @FXML
    private TableColumn titleCol;
    @FXML
    private TableColumn aboutCol;
    @FXML
    private TableColumn dateTimeCol;
    @FXML
    private TableColumn importanceCol;
    
    private ScheduledExecutorService scheduler;
 
    private ConnectOperationsDB connectOperationsDB;
    
    private final static String passwordDB = "test";
    
    private final static String alarmSoundResourceDesktop = "src" + File.separator + "desktop" +
                    File.separator + "resources" + File.separator + "alarm" + File.separator + "beep.wav" ;
    
    private String alarmSoundResource;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {       
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yy");
        LocalDate date = LocalDate.now();
        String dateStr = date.format(dtf);
        int dayOfMonth = date.getDayOfMonth();
        Background background1 = new Background(new BackgroundFill(Color.BISQUE, CornerRadii.EMPTY, Insets.EMPTY));
        Background background2 = new Background(new BackgroundFill(Color.AQUAMARINE, CornerRadii.EMPTY, Insets.EMPTY));
        if( dayOfMonth % 2 == 0)
            label.setBackground(background1);
        else
            label.setBackground(background2);
        
        label.setText(label.getText() + " pornita la data " + dateStr);
        
        eventsRegister = new ArrayList<> ();
        
        titleCol.setCellValueFactory(new PropertyValueFactory<EventModel, String>("title"));
        aboutCol.setCellValueFactory(new PropertyValueFactory<EventModel, String>("description"));
        
        aboutCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<EventModel ,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<EventModel , String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getDescription());
            }
        });
        
        aboutCol.setCellFactory(new Callback<TableColumn<String,String>, TableCell<String,String>>() {
            @Override
            public TableCell<String, String> call(TableColumn<String, String> param) {
                return new ContentCell();
            }
        });
        
        dateTimeCol.setCellValueFactory(new PropertyValueFactory<EventModel, String>("dateTimeString"));
        importanceCol.setCellValueFactory(new PropertyValueFactory<EventModel, String>("importance"));
        
        eventsObservableList = FXCollections.observableArrayList();
        
        tableCurrentEvents.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableCurrentEvents.setItems(eventsObservableList);  
        
        scheduler = Executors.newScheduledThreadPool(1);
        
        //scheduller to verify proximity programming of events
        scheduler.scheduleAtFixedRate(new AlertEvents(), 1, 1, TimeUnit.MINUTES);
              
        String platform = System.getProperty("javafx.platform", "desktop");
        if(platform.equals("desktop")) {
            //String path = "desktop.java.com.notes.events.database.impl.ConnectOperationsDBImplementation";
            String path = "com.notes.events.database.impl.ConnectOperationsDBImplementation";
            
            try {

                connectOperationsDB = (ConnectOperationsDB) Class.forName(path).newInstance();
                connectOperationsDB.testingString();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                System.err.println("Clasa pentru desktop nu a fost gasita sau nu a fost instantiata");
                exitApp();
            }
            
            alarmSoundResource = alarmSoundResourceDesktop;
        }
        else{
            exitApp();
        }
        
        connectOperationsDB.connect(passwordDB);
    }   
    
    @FXML
    public void handleAddNewEventButton(ActionEvent event) throws IOException {
        //a stage for adding new event can be start if not exist other open
        if(!addNewEventButton.isDisabled()){
            try {
            //block button          
                Stage newStage = new Stage();
                //stage without buttons for exit and resize
                newStage.initStyle(StageStyle.UNDECORATED);
                
                String newEventFXMLResource = "/fxml/FXMLNewEvent.fxml";
                AnchorPane page;
                
                page = (AnchorPane) FXMLLoader.load(EventNoteController.class.getResource(newEventFXMLResource));
                Scene scene = new Scene(page);
                newStage.setScene(scene);
                newStage.setResizable(false);
                page.setEventDispatcher(null);
                
                //handle closing action for completing an event
                newStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

                    @Override
                    public void handle(WindowEvent event) {
                       
                        CurrentEvents currentEventsAdded= CurrentEventsSingleton.getInstance();
                        EventModel newEvent = currentEventsAdded.getLastEvent();
                        if(newEvent != null){
                            
                            eventsRegister.add(newEvent);
                            eventsObservableList.add(newEvent);
                            
                        } 
                        //button is activated
                        //it allows adding a new event
                        addNewEventButton.setDisable(false); 
                    }  
                });
                
                newStage.setX(200);
                newStage.setY(220);
            
                newStage.show();
                //button is disabled until it completes the data for a new event
                addNewEventButton.setDisable(true); 
            } 
            catch (IOException ex) {
                System.err.println("Error" + ex.getMessage());
                exitApp();
            }  
        }
    }
    
    /**
     * Exit from application
     * @param event 
     */
    @FXML
    public void handleQuitButtonAction(ActionEvent event) {
        exitApp();
    }
    
    /**
     * Close resource and exit
     */
    private void exitApp(){
        connectOperationsDB.disconnect();
        scheduler.shutdownNow();
        Platform.exit();
    }       
    
    /**
     * Delete all added events
     * @param event 
     */
    @FXML
    public void deleteAllEventsAction(ActionEvent event){
        
        if(eventsRegister.isEmpty())
            return;
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirma stergere");
        alert.setContentText("Stergi toate evenimentele curente inregistrate?");
        ButtonType buttonLeft = new ButtonType("Da");
        ButtonType buttonRight = new ButtonType("Nu", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonLeft, buttonRight);
        
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.get() == buttonLeft){           
            eventsRegister.clear();
            eventsObservableList.clear();
        }      
    }
    
    /**
     * It allows selecting multiple records
     * @param event 
     */
    @FXML
    public void handleMultipleSelectionTextView(MouseEvent event){
        tableCurrentEvents.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        selectedItems = tableCurrentEvents.getSelectionModel().getSelectedItems();      
    }
    
    /**
     * Delete all selected events
     * @param event 
     */
    @FXML
    public void deleteSelectedItemsAction(ActionEvent event){
        if(selectedItems != null && !selectedItems.isEmpty()){
           tableCurrentEvents.getItems().removeAll(selectedItems);
            eventsRegister.clear();
            for(int i = 0; i < tableCurrentEvents.getItems().size(); i++){
                EventModel eventItem = tableCurrentEvents.getItems().get(i);
                eventsRegister.add(eventItem); 
            }        
        }
    }
    
    
    /**
     * Class to start an alert for some event if certain conditions were met
     */
    private class AlertEvents implements Runnable {
        @Override
        public void run() {
            
            int timeDifference = 3;
            for(int i =0; i < eventsRegister.size(); i++){
                EventModel event = eventsRegister.get(i);
                //if event is not marked yet
                if(!event.isMarked()){
                    if(limitFromNow(event.getDateTime(), timeDifference)){
                        event.setMarked(true);
                        //start display an alert (with playing sound)
                        Platform.runLater(new ShowAlertEvent(event));
                    }
                }
            }
        }
    }
    
    private class ShowAlertEvent implements Runnable {
        private final EventModel event;
        
        public ShowAlertEvent(EventModel event){
            this.event = event;
        }
        
        @Override
        public void run(){
            showAlertEvent(event);
        }
        
        private void showAlertEvent(EventModel eventNew){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alerta eveniment");
            alert.setHeaderText("Alerta");
            
            alert.setContentText("Eveniment " + eventNew.getTitle()+ ": " + eventNew.getDescription() +
               " programat pentru " + eventNew.getDateTime().toString());
            alert.show();
            playAlarmSound(alarmSoundResource);           
    }  
  }
    
    /**
     * Play alarm sound from resource file
     * @param pathFileSound Path to resource file
     */
    private void playAlarmSound(String pathFileSound){
        Media media = new Media(Paths.get(pathFileSound).toUri().toString()); 
        MediaPlayer player = new MediaPlayer(media);
        player.play(); 
    }
    
    private class ContentCell extends TableCell<String, String> {
        @Override
        protected void updateItem(String content, boolean empty) {
            super.updateItem(content, empty);
            this.setText(content);
            this.setTooltip((empty || content == null) ? null : new Tooltip(content));
        }
    }
      
    private boolean limitFromNow(LocalDateTime dateTime, int numberMinutesLimit){
        long minutes = ChronoUnit.MINUTES.between(LocalDateTime.now(), dateTime);
        return minutes <= numberMinutesLimit;
    }
    
    /**
     * Method for backup action
     * @param event 
     */
    @FXML
    public void backupAction(ActionEvent event){
        TextInputDialog inputBackup = new TextInputDialog("");
        inputBackup.setTitle("Backup");
        inputBackup.setHeaderText("Completarea textului de identificare pentru backup");
        Optional<String> nameBackup = inputBackup.showAndWait();
        if (nameBackup.isPresent()) {
            this.connectOperationsDB.createNewBackup(nameBackup.get(), eventsRegister);
        }
    }
    
    /**
     * Method for restore action
     * @param event 
     */
    @FXML
    public void restoreAction(ActionEvent event){
        TextInputDialog inputRestore = new TextInputDialog("");
        inputRestore.setTitle("Restore");
        inputRestore.setHeaderText("Completarea textului de identificare pentru restore");
        Optional<String> nameRestore = inputRestore.showAndWait();
        List <EventModel> backupEvents = null;
        if (nameRestore.isPresent()) {
            backupEvents = this.connectOperationsDB.retrieveRecords(nameRestore.get());
        }
        if(backupEvents != null){
           eventsRegister.addAll(backupEvents);
           eventsObservableList.addAll(backupEvents); 
        }
        
    }
}
