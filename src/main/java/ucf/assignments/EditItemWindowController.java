/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Melissa Cabrera
 */

package ucf.assignments;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;


public class EditItemWindowController {

    static final char[] LETTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    static final char[] SPECIAL_CHARACTERS = "!@#%^&*()_+".toCharArray();

    @FXML
    TextField itemName;
    @FXML
    TextField serialNum;
    @FXML
    TextField itemPrice;

    @FXML
    Button saveChanges;
    @FXML
    Button back;


    @FXML
    public void saveChangesClicked()  {
        String name = itemName.getText(); // get Task Name
        String serialNumber = serialNum.getText(); // get description
        String price = itemPrice.getText(); // get description


        //if statements to validate user inputs
        if (name.length() > 256) {
            new Alert(Alert.AlertType.INFORMATION, "The name should have between 2 and 256 characters").show();
            return;
        } else if (name.length() < 2) {
            new Alert(Alert.AlertType.INFORMATION, "The name should have between 2 and 256 characters").show();
            return;
        } else if (name.isBlank()) {
            new Alert(Alert.AlertType.INFORMATION, "Please enter name").show();
            return;
        }

        if (serialNumber.length() == 0) {
            new Alert(Alert.AlertType.INFORMATION, "Please enter serial number").show();
            return;
        }

        if (price.length() == 0) {
            new Alert(Alert.AlertType.INFORMATION, "Please enter price").show();
            return;
        } else if (price.length() < 5) {
            new Alert(Alert.AlertType.INFORMATION, "The price must have the format $X.XX").show();
            return;
        }

        for (char c : LETTERS) {
            String letter = c + "";
            if (price.toLowerCase().contains(letter)) {
                new Alert(Alert.AlertType.INFORMATION, "The price must contain only numbers").show();
                return;
            }
        }

        for (char c : SPECIAL_CHARACTERS) {
            String letter = c + "";
            if (price.toLowerCase().contains(letter)) {
                new Alert(Alert.AlertType.INFORMATION, "The price must contain only numbers").show();
                return;
            }
        }

        for (int i = 0; i < InventoryList.getItems().size(); i++) {
            if (InventoryList.getItems().get(i).getSerialNumber().equals(serialNumber)) {
                new Alert(Alert.AlertType.INFORMATION, "This serial number already exists").show();
                return;
            }
        }

        // creating object for new Item
        Item newItem = new Item(serialNumber, name, price);

        // adding this new item to the mainwindows item's list
        MainWindowControllers.getItems().add(newItem);

        try {
            FileWriter writeFile = new FileWriter("files/data.txt");
            writeFile.write("Serial Number\tName\t\tPrice\r\n");
            for (Item i : MainWindowControllers.getItems()) {
                writeFile.write(i.toTSV() + "\r\n");
            }
            writeFile.flush();
            writeFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // loading new window using fxml loader that's MainWindow
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        Stage stagePrevious = (Stage) saveChanges.getScene().getWindow();
        stagePrevious.close();
        stage.show();
        new Alert(Alert.AlertType.INFORMATION, "This item has been modify!").show();
    }

    @FXML
    public void backButtonClicked(){
        //load and open MainWindow when back button clicked
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        Stage stagePrevious = (Stage) back.getScene().getWindow();
        stagePrevious.close();
        stage.show();
    }
}