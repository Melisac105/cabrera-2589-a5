/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Melissa Cabrera
 */

package ucf.assignments;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;

public class InventoryTrackingApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            primaryStage.setScene(scene);
            primaryStage.setTitle("MainWindow");
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop(){
        //get all tasks from Main Controller class
        //write all tasks to file
        //as the data and changes should be saved
        try{
            FileWriter writeFile = new FileWriter("files/data.txt");
            writeFile.write("Serial Number\tName\t\tPrice\r\n");
            for(Item i : MainWindowControllers.getItems()){
                writeFile.write(i.toTSV()+"\r\n");
            }
            writeFile.flush();
            writeFile.close();
        } catch(Exception e) {
            System.out.println("Data Exception"+e);
        }
    }
}
