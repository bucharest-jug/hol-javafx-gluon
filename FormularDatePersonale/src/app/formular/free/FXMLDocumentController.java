/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.formular.free;

import app.record.model.AlertMessage;
import app.record.model.RecordModel;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author mihael.buzdugan
 */
public class FXMLDocumentController implements Initializable {
   
    private List <RecordModel> personalDataRecords;
    
    private File destination;
    private FileWriter file;
    
    @FXML
    private TextField name;
    @FXML
    private TextField famillyName;
    @FXML
    private TextField address;
    @FXML
    private TextField phoneNumber;
    //@FXML
    //private Button putRecord;
    
    //@FXML
    //private Button saveInFile;
    @FXML
    private Button exit;
    @FXML
    private ListView listViewRecords;
    
    private ObservableList<String> itemsListRecords;
    
    /**
     * Method to add record
     */
    @FXML
    public void addRecord(){
        
        String nameCapture = name.getText().trim();
        String famillyNameCapture = famillyName.getText().trim();
        String addressCapture = address.getText().trim();
        String phoneNumberCapture = phoneNumber.getText().trim();
        
        AlertMessage alertMessage;
        
        //input validation
        if(nameCapture.length() < 3 ||
                Character.isDigit(nameCapture.charAt(0))){
            alertMessage = new AlertMessage((byte) 1, "Numele are doar " + nameCapture.length() + " caractere");      
            showAlert(alertMessage);
        }
        else if(famillyNameCapture.length() < 3 ||
                Character.isDigit(famillyNameCapture.charAt(0))){
            alertMessage = new AlertMessage((byte) 2, "Prenumele are doar " + nameCapture.length() + " caractere");            
            showAlert(alertMessage); 
        }
        else if(addressCapture.length() < 8 || Character.isDigit(addressCapture.charAt(0))){
            alertMessage = new AlertMessage((byte) 2, "Adresa are doar " + nameCapture.length() + " caractere");
            showAlert(alertMessage); 
        }
        else if(phoneNumberCapture.length() < 8 || phoneNumberCapture.length() > 12
                || !phoneNumberCapture.matches("[0-9]+")){
            alertMessage = new AlertMessage((byte) 2, "Numarul de telefon este incorect introdus");
            showAlert(alertMessage);
        }
        else{
            //if validation is pass record is added to internal list and to observable list 
            RecordModel record = new RecordModel(nameCapture, famillyNameCapture,
                    addressCapture, phoneNumberCapture);          
            
            personalDataRecords.add(record);
            
            itemsListRecords.add(record.toString());
            
            listViewRecords.setItems(itemsListRecords);
        }
        
        cleanTextFields();
    }
    
    /**
     * Clean all fields
     */
    private void cleanTextFields(){
        name.clear();
        famillyName.clear();
        address.clear();
        phoneNumber.clear();
    } 
    
    /**
     * Displays a warning if certain conditions are not met
     * @param alertMessage 
     */
    private void showAlert(AlertMessage alertMessage){
        Alert alert = new Alert(AlertType.ERROR);
        String errorString = getTitleError(alertMessage.getErrorCode());
        alert.setTitle(errorString);
        alert.setResizable(false);
        String version = System.getProperty("java.version");
        String content = String.format(alertMessage.getMessage(), version);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    /**
     * Returns error title by the code
     */
    private String getTitleError(byte errorCode){
        switch (errorCode) {
            case 1:
                return "Eroare nume";
            case 2:
                return "Eroare prenume";
            case 3:
                return "Eroare adresa";
            case 4:
                return "Eroare numar de telefon";
            case 5:
                return "Eroare numar inregistrari";
            default:
                return null;
        }
    }
    
    /**
     * Save in file
     */
    @FXML
    public void saveInFilePressed(){
        //diplay error alert if no records have been added
        if(personalDataRecords.isEmpty()){
            AlertMessage alertMessage = new AlertMessage((byte) 5, "Nu au fost adaugate inregistrari de la inceputul aplicatiei");
            showAlert(alertMessage);
        }
        else{
            for(RecordModel rec : personalDataRecords){
                String str = rec.getName() + ',' + rec.getFamillyName() +
                    ',' + rec.getAddress() + ',' + rec.getPhoneNumber() + '\n';
            
                try {    
                    //write in file
                    file.write(str);
                    file.flush();
                } 
                catch (IOException ex) {}
               
            }
            //after saving clear internal list and displayed records
            listViewRecords.getItems().clear();
            personalDataRecords.clear();
        }
    }
    
    /**
     * Exit from application
     * @throws IOException 
     */
    @FXML
    public void exitApp() throws IOException{
        file.close();
        exitFromApp();
    }
    
    /**
     * Exit from application by closing stage
     */
    private void exitFromApp(){
       // get a handle to the stage
        Stage stage = (Stage) exit.getScene().getWindow();
        // do what you have to do
        stage.close(); 
    }
    
    /**
     * Initialize application before starting
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ResourceBundle resourceBndl = ResourceBundle.getBundle("bundles.ResourceSetupBundle",
                new Locale("en", "EN"));
        //read file name from bundle file
        String fileName = resourceBndl.getString("name_file");
         
        System.out.println(fileName);
        destination = new File(fileName);
        //create if not exist
        if(!destination.exists()) {
            try {
                //create new file if not exist
                destination.createNewFile();
               
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
                exitFromApp();
            }
        } 
        
        try {
            file = new FileWriter(destination.getName(),true);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            exitFromApp();
        }
        
        personalDataRecords = new ArrayList <> ();
        itemsListRecords = FXCollections.observableArrayList();
    }    
    
}
