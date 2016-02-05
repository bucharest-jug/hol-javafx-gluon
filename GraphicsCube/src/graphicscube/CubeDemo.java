/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphicscube;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Modified from the example of http://www.javafxapps.in/tutorial/Creating-3D-Cube-in-javafx.html
 * For drawing an 3D cube can be use primitive Box, as shown in http://skidrunner.blogspot.ro/2015/03/simple-javafx-cube-3d.html
 */
public class CubeDemo extends Application {

    private Timeline animation;

    private final static double SIZE =75;
    private final static Color COLOR = Color.AZURE;
    
    @Override
    public void start(Stage primaryStage) {

        Group cube = new Group();
        
        Rectangle backFace = new Rectangle();
        Rectangle bottomFace = new Rectangle();
        Rectangle rightFace = new Rectangle();
        Rectangle leftFace = new Rectangle();
        Rectangle topFace = new Rectangle();
        Rectangle frontFace = new Rectangle();
        
        backFace.setWidth(SIZE);
        backFace.setHeight(SIZE);
        bottomFace.setWidth(SIZE);
        bottomFace.setHeight(SIZE);
        rightFace.setWidth(SIZE);
        rightFace.setHeight(SIZE);
        leftFace.setWidth(SIZE);
        leftFace.setHeight(SIZE);
        topFace.setWidth(SIZE);
        topFace.setHeight(SIZE);
        frontFace.setWidth(SIZE);
        frontFace.setHeight(SIZE);
        
        backFace.setFill(COLOR.deriveColor(0.0, 1.0, (1 - 0.5 * 1), 1.0));
        bottomFace.setFill(COLOR.deriveColor(0.0, 1.0, (1 - 0.4 * 1), 1.0));
        rightFace.setFill(COLOR.deriveColor(0.0, 1.0, (1 - 0.3 * 1), 1.0));
        leftFace.setFill(COLOR.deriveColor(0.0, 1.0, (1 - 0.2 * 1), 1.0));
        topFace.setFill(COLOR.deriveColor(0.0, 1.0, (1 - 0.1 * 1), 1.0));
        frontFace.setFill(COLOR);
        
        backFace.setTranslateX(-0.5 * SIZE);
        backFace.setTranslateY(-0.5 * SIZE);
        backFace.setTranslateZ(0.5 * SIZE);
        
        bottomFace.setTranslateX(-0.5 * SIZE);
        bottomFace.setTranslateY(0);
        bottomFace.setRotationAxis(Rotate.X_AXIS);
        bottomFace.setRotate(90);
        
        rightFace.setTranslateX(-1 * SIZE);
        rightFace.setTranslateY(-0.5 * SIZE);
        rightFace.setRotationAxis(Rotate.Y_AXIS);
        rightFace.setRotate(90);
        
        leftFace.setTranslateX(0);
        leftFace.setTranslateY(-0.5 * SIZE);       
        leftFace.setRotationAxis(Rotate.Y_AXIS);
        leftFace.setRotate(90);
        
        topFace.setTranslateX(-0.5 * SIZE);
        topFace.setTranslateY(-1 * SIZE);
        topFace.setRotationAxis(Rotate.X_AXIS);
        topFace.setRotate(90);
               
        frontFace.setTranslateX(-0.5 * SIZE);
        frontFace.setTranslateY(-0.5 * SIZE);
        frontFace.setTranslateZ(-0.5 * SIZE);
        
        cube.getChildren().addAll(
                backFace,
                bottomFace,
                rightFace,
                leftFace,
                topFace,
                frontFace);
        
        cube.getTransforms().addAll(new Rotate(45, Rotate.X_AXIS), new Rotate(45, Rotate.Y_AXIS));
        StackPane root = new StackPane();
        //add cube to root 
        root.getChildren().add(cube);
        //animate the created cube
        animation = new Timeline();
        animation.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO,
                new KeyValue(cube.rotationAxisProperty(), Rotate.Z_AXIS),
                new KeyValue(cube.rotateProperty(), 0d)),
                new KeyFrame(Duration.seconds(4),
                new KeyValue(cube.rotationAxisProperty(), Rotate.Z_AXIS),
                new KeyValue(cube.rotateProperty(), 360d)));
        
        animation.setCycleCount(Animation.INDEFINITE);
        animation.play();
        Scene scene = new Scene(root, 400, 400, true);
        scene.setCamera(new PerspectiveCamera());
        primaryStage.setResizable(false);
        primaryStage.setTitle("Cub rotatie");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
