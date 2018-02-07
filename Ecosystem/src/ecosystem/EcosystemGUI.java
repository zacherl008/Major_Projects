/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ecosystem;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @author kzacherl
 */

//event listeners, event handlers, set text and get text
//panes, buttons, sliders
//pane to scene, scene to stage, show stage
public class EcosystemGUI extends Application {
    
    GridPane acreGrid;
    World myWorld = new World();
    TextArea acreField;
    @Override
    public void start(Stage primaryStage) throws Exception {
        int rows = 3;
        int cols = 3;
        int currRow = 0;
        int currCol = 0;
        int acre = 0;
        BorderPane outer = new BorderPane();
        
        acreGrid = new GridPane();
        //space since currRow is declared outside of here
        for( ; currRow < rows; currRow++){
            for( ; currCol < cols; currCol++){
                TextArea currTA = new TextArea();
                currTA.setEditable(false);
                currTA.setFont(new Font("Arial", 20));
                currTA.setText("Acre #" + acre + "\nGrass: " + myWorld.getAcreArray()[currRow][currCol].numGrass() +"\nSheep: " + 
                        myWorld.getAcreArray()[currRow][currCol].numSheep() + "\nWolves: " + 
                        myWorld.getAcreArray()[currRow][currCol].numWolves());
                currTA.setMinSize(100, 100);
                currTA.setMaxSize(130, 130);
                acreGrid.add(currTA, currCol, currRow);
                acre++;
            }
            currCol = 0;
        }
        //get children produces the things inside
        outer.setCenter(acreGrid);        
        Button cycle = new Button("Cycle");
        
        cycle.setOnAction(getCycleHandler());
        cycle.setAlignment(Pos.CENTER);
        FlowPane fp = new FlowPane();
        fp.setAlignment(Pos.CENTER);
        fp.getChildren().add(cycle);
        outer.setBottom(fp);
        
        Scene myScene = new Scene(outer);
        primaryStage.setTitle("Ecosystem");
        primaryStage.setScene(myScene);
        primaryStage.show();
    }
    
    private EventHandler getCycleHandler(){   
        EventHandler handler = new EventHandler(){
            @Override
            //Integer parse int since it needs a string
            //Event event
            //function automatically called when a button is pressed
            //run through acres and update grid
            //setBackground or setFont(color parameter)
            
            public void handle(Event event){
                myWorld.cycle();
                int acre = 0;
                for(int i = 0; i < myWorld.getAcreArray().length; i++){
                    for(int j = 0; j < myWorld.getAcreArray()[i].length; j++){
                        //((TextArea)(acreGrid.getChildren().get(i))).setText(i + "" + j);
                        //print cycle num!!
                        acreField = (TextArea)(acreGrid.getChildren().get(acre));
                        acreField.setText("Acre #" + acre + "\nGrass: " + myWorld.getAcreArray()[i][j].numGrass() +"\nSheep: " + 
                        myWorld.getAcreArray()[i][j].numSheep() + "\nWolves: " + 
                        myWorld.getAcreArray()[i][j].numWolves());
                        acre++;
                    }
                }  
            }
        };
        return handler;
    }
    
}
