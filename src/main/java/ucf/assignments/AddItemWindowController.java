package ucf.assignments;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

public class AddItemWindowController {

    public Button submit;

    public TextField itemName;
    public TextField serialNum;
    public TextField itemPrice;

    @FXML
    public void submitButtonClicked() {
        String name = itemName.getText(); // get Task Name
        String serialNumber = serialNum.getText(); // get description
        String price = itemPrice.getText(); // get description

        //if statement to check if description has more than 256 characters
        if(name.length()>256){
            JOptionPane.showMessageDialog(null, "Description is more than 256 characters","Description", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // creating object for new Item
        Item newTask = new Item(serialNumber, name, price);

        // adding this new item to the mainwindows item's list
        MainWindowControllers.getTasks().add(newTask);

        try{
            // getting list of all task from mainwindow and writing it to file named 'data.txt'
            FileWriter writeFile = new FileWriter("files/data.txt");
            for(Item i : MainWindowControllers.getTasks()){
                writeFile.write(i.toString()+"\r\n");
            }
            writeFile.close();
        } catch(Exception e){
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
