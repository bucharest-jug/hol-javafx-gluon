package com.notes.events;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Class that contains the method by which application is started
 * @author mihael.buzdugan
 */
public class NoteEvents extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/EventNoteFXML.fxml"));
        Scene scene = new Scene(root, 580, 420);

        stage.getIcons().add(new Image(NoteEvents.class.getResourceAsStream("/notebook.png")));

        stage.setScene(scene);
        stage.show();
    }

}
