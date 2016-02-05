package com.simplerectangle;

import javafx.animation.FillTransition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SimpleRectangle extends Application {
    
    //left corner of the rectangle coordinates
    private final static int X_RECTANGLE = 160;
    private final static int Y_RECTANGLE = 160;
    
    @Override
    public void start(Stage stage) {
        Group group = new Group();

        double width = 80; //initial width
        double height = 80;//initial height
        
        Rectangle rectangle = new Rectangle(X_RECTANGLE, Y_RECTANGLE, width , height);
    
        rectangle.setFill(Color.AZURE);
        rectangle.setStroke(Color.BLACK);
        group.getChildren().add(rectangle);
    
        Scene scene = new Scene(group, 400, 400, Color.CORAL);
               
        rectangle.addEventFilter(MouseEvent.DRAG_DETECTED, new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent ev) {
                            double x = ev.getSceneX();
                            double y = ev.getSceneY();
                            
                            double width = rectangle.getWidth();
                            double height = rectangle.getHeight();
                            
                             //if is in perimeter of rectangle                          
                            if( x >= X_RECTANGLE && x <= X_RECTANGLE + width )
                                if( y >= Y_RECTANGLE && y <= X_RECTANGLE + height ){
                                    //add an event filter if mouse is in perimeter of rectangle
                                    rectangle.addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
                                        @Override
                                        public void handle(MouseEvent ev) {
                                            //calculate variation of dimension of rectangle after position of mpuse
                                            //that drag into direction
                                            double var_x = ev.getSceneX() - x;
                                            double var_y = ev.getSceneY() - y;
                                            double new_x = width + var_x;
                                            double new_y = height + var_y;
                                            //modify width and height of rectangle in a certain range
                                            if(new_x > 10 && new_x < 240)
                                                rectangle.setWidth(new_x);
                                            if(new_y >10 && new_y < 240)
                                                rectangle.setHeight(new_y);
                                        }
                                    });
                                        
                                    
                                        FillTransition ft = new FillTransition(Duration.millis(30), rectangle, null, Color.AZURE);
                                        ft.setCycleCount(4);
                                        ft.setAutoReverse(true);
                                        ft.play();
                                        ev.consume(); 
                                    }
                                                                       
                                }
                    });        
        
       
        /*
        //add filter with lambda expression 
        rectangle.addEventFilter(MouseEvent.DRAG_DETECTED, (MouseEvent ev) -> {
            double x = ev.getSceneX();
            double y = ev.getSceneY();
            double width1 = rectangle.getWidth();
            double height1 = rectangle.getHeight();
            if (x >= X_RECTANGLE && x <= X_RECTANGLE + width1) {
                if (y >= Y_RECTANGLE && y <= X_RECTANGLE + height1) {
                    rectangle.addEventFilter(MouseEvent.MOUSE_DRAGGED, (MouseEvent ev1) -> {
                        double var_x = ev1.getSceneX() - x;
                        double var_y = ev1.getSceneY() - y;
                        double new_x = width1 + var_x;
                        double new_y = height1 + var_y;
                        if(new_x > 10 && new_x < 240)
                            rectangle.setWidth(new_x);
                        if(new_y >10 && new_y < 240)
                            rectangle.setHeight(new_y);
                    });
                    FillTransition ft = new FillTransition(Duration.millis(30), rectangle, null, Color.AZURE);
                    ft.setCycleCount(4);
                    ft.setAutoReverse(true);
                    ft.play();
                    ev.consume();
                }
            }
        }); 
        */
        //event filter to change aspect of mouse coursor
        rectangle.addEventFilter(MouseEvent.ANY, new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent ev) {
                            double x = ev.getSceneX();
                            double y = ev.getSceneY();
                            scene.setCursor(Cursor.DEFAULT);
                            if(x >= X_RECTANGLE && x <= X_RECTANGLE + rectangle.getWidth())
                                if(y >= Y_RECTANGLE && y <= X_RECTANGLE + rectangle.getHeight() )
                                    if(ev.isPrimaryButtonDown())
                                        scene.setCursor(Cursor.NW_RESIZE);
                                    else
                                        scene.setCursor(Cursor.OPEN_HAND);
                                
                        }
        });
        
        stage.getIcons().add(new Image(SimpleRectangle.class.getResourceAsStream("/icon.png")));
        stage.setTitle("Dreptunghi");
        stage.setScene(scene);
        stage.show();
    }

}
