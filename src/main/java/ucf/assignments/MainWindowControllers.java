package ucf.assignments;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowControllers implements Initializable {

    private static InventoryList myList;

    public MenuItem newInventory;
    public MenuItem quit;
    public TableColumn serialNumber;
    public TableColumn name;
    public TableColumn price;
    public ImageView removeButton;
    public ImageView addButton;
    public ImageView searchButton;
    public TextField remainingCapacity;



    public void createNewInventory(ActionEvent actionEvent) {
    }

    public void quitButtonClicked(ActionEvent actionEvent) {
    }

    public void removeButtonClicked(MouseEvent mouseEvent) {
    }

    public void addButtonClicked(MouseEvent mouseEvent) throws IOException {
        //open a new window when add button is clicked
        // if the remaining capacity of the todolist is 0 then it will just show a message dialog box to the user with a message
            // otherwise load a new screen using fxml loader that's AddTaskWindow.fxml to add new task
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddItemWindow.fxml"));
            Parent root = null;
            root = fxmlLoader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);

            stage.setResizable(false);
            Stage stagePrevious = (Stage) addButton.getScene().getWindow();
            stagePrevious.close();
            stage.show();
    }

    public void searchButtonClicked(MouseEvent mouseEvent) {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
