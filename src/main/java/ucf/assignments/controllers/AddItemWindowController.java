package ucf.assignments.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ucf.assignments.util.InventoryList;
import ucf.assignments.util.Item;

import java.io.FileWriter;
import java.io.IOException;


public class AddItemWindowController {

    static final char[] LETTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    static final char[] SPECIAL_CHARACTERS = "!@#%^&*()_+".toCharArray();

    public Button submit;

    public TextField itemName;
    public TextField serialNum;
    public TextField itemPrice;


    @FXML
    public void submitButtonClicked()  {
        String name = itemName.getText(); // get Task Name
        String serialNumber = serialNum.getText(); // get description
        String price = itemPrice.getText(); // get description


        //if statement to check if description has more than 256 characters
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
            // getting list of all task from mainwindow and writing it to file named 'data.txt'
            FileWriter writeFile = new FileWriter("files/data.txt");
            for (Item i : MainWindowControllers.getItems()) {
                writeFile.write(i.toString() + "\r\n");
            }
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

        Stage stagePrevious = (Stage) submit.getScene().getWindow();
        stagePrevious.close();
        stage.show();
    }
}
