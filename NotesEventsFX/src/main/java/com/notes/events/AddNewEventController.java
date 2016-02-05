/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.notes.events;

import com.notes.events.model.CurrentEvents;
import com.notes.events.model.CurrentEventsSingleton;
import com.notes.events.model.EventModel;
import com.notes.events.resource.DateTimePicker;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 * Controller class for adding new event
 * 
 * @author mihael.buzdugan
 */
public class AddNewEventController implements Initializable {

    private final String DESCRIPTION_IMPORTANCE [] = {"very important", "medium importance", "little importance", "insignificant"};
    
    @FXML 
    private TextField titleTextField;
    
    @FXML 
    private TextArea descriptionTextArea;
            
    @FXML
    private Slider eventImportanceSlider;
    
    @FXML
    private DateTimePicker eventDateTimePicker;
    
    @FXML
    private Button addEventButton;
    
    private EventModel newEvent;
    
    private boolean startCompleteTitle = false;
    private boolean startCompleteDescription = false;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        newEvent = new EventModel();
        
        titleTextField.setText("Put  title for this event");
        descriptionTextArea.setText("Description of this event");
        eventDateTimePicker.setShowWeekNumbers(true);
        
        initEventImportanceSlider();
    }    
    
    private void initEventImportanceSlider(){       
        titleTextField.textProperty().addListener((observable, oldValue, newValue) -> {            
            newEvent.setTitle(titleTextField.getText());
        });
        
        descriptionTextArea.textProperty().addListener((observable, oldValue, newValue) -> {           
            newEvent.setDescription(descriptionTextArea.getText());
        });
        
        eventImportanceSlider.setMin(0);
        eventImportanceSlider.setMax(3);
        eventImportanceSlider.setValue(2);
        eventImportanceSlider.setMinorTickCount(0);
        eventImportanceSlider.setMajorTickUnit(1);
        eventImportanceSlider.setSnapToTicks(true);
        eventImportanceSlider.setShowTickMarks(true);
        eventImportanceSlider.setShowTickLabels(true);
        
        //fix how Slider works and related text
        eventImportanceSlider.setLabelFormatter(new StringConverter<Double>() {
            @Override
            public String toString(Double n) {
                if (n < 0.5) return DESCRIPTION_IMPORTANCE[3];
                if (n < 1.5) return DESCRIPTION_IMPORTANCE[2];
                if (n < 2.5) return DESCRIPTION_IMPORTANCE[1];

                return DESCRIPTION_IMPORTANCE[0];
            }

            @Override
            public Double fromString(String str) {
                if(str.equals(DESCRIPTION_IMPORTANCE[0]))
                    return 0d;
                else if (str.equals(DESCRIPTION_IMPORTANCE[1]))
                    return 1d;
                else if(str.equals(DESCRIPTION_IMPORTANCE[2]))
                    return 2d;
                else if(str.equals(DESCRIPTION_IMPORTANCE[3]))
                    return 3d;
                return -1d;
            }
        });  
    }
    
    /**
     * clear title field
     */
    @FXML
    public void clearTitleArea() {
        if(!startCompleteTitle){
            titleTextField.clear();
            startCompleteTitle = true;
        }
    }
    
    /**
     * Clear description area
     */
    @FXML
    public void clearDescriptionArea(){
        if(!startCompleteDescription){
            descriptionTextArea.clear();
            startCompleteDescription = true;
        }
    }
    
    /**
     * The method invoked by button that add a new event
     * @throws IOException 
     */
    @FXML
    public void addNewEvent() throws IOException{
        
        List <String> campuriIncomplete = new ArrayList <>();
        
        String title = titleTextField.getText();
        
        //As certain conditions are not met by validation conditions a list is completed      
        if(title == null || title.length() == 0 || !startCompleteTitle){
            campuriIncomplete.add("Titlul ");
        }
        
        String description = descriptionTextArea.getText();
        
        if(title == null || title.length() == 0 || !startCompleteDescription){
            campuriIncomplete.add("Descrierea ");
        }
        
        ZonedDateTime dateTime = eventDateTimePicker.getDateTimeValue();
        
        
        if(dateTime == null || dateTime.toLocalDateTime().isBefore(LocalDateTime.now())){
            campuriIncomplete.add("Evenimentul trebuie completat dupa cel prezent");
        }
        
        byte importance = (byte) eventImportanceSlider.getValue();
        
        newEvent = new EventModel(title, description, dateTime.toLocalDateTime(), importance);
        if(!campuriIncomplete.isEmpty())
            alertOption(campuriIncomplete);
        else
            exitStage(true);
    }
    
    /**
     * It created an alert window for incomplete or blank fields
     * If you have clicked button "Renunta" window is closing without the added a new event
     * If you have clicked button "Continua" can continue to edit or it can be corrected certain data 
     * @param campuriIncomplete Array with incomplete fields
     */
    private void alertOption(List <String> campuriIncomplete){
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Date incomplete");
        alert.setHeaderText("Continua completarea sau renunta");
        String message = "Completeaza/Corecteaza campul sau campurile " + '\n';
        for(int i = 0; i < campuriIncomplete.size(); i++)
            message += campuriIncomplete.get(i) + ' ' + '\n';
        alert.setContentText(message);
        ButtonType buttonLeft = new ButtonType("Renunta", ButtonData.CANCEL_CLOSE);
        ButtonType buttonRight = new ButtonType("Continua");
        
        alert.getButtonTypes().setAll(buttonLeft, buttonRight);
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonLeft){           
            exitStage(false);
        }
    }
    
    /**
     * Method to close this stage
     * check that was completed a new event
     * if a new event is completed it will be added by singleton a class
     * @param added true if is added new event
     */
    private void exitStage(boolean added){       
        
        Stage stage = (Stage) addEventButton.getScene().getWindow();
        if(added){
           CurrentEvents currentEvents = CurrentEventsSingleton.getInstance();
           currentEvents.addNewEvent(newEvent);
        }
        //fire WINDOW_CLOSE_REQUEST event for closing window action
        stage.fireEvent(new WindowEvent(stage,WindowEvent.WINDOW_CLOSE_REQUEST));
        stage.close();
    }
    
}
