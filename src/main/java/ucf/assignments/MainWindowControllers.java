package ucf.assignments;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowControllers implements Initializable {


    public MenuItem newInventory;
    public MenuItem quit;
    public TableColumn serialNumber;
    public TableColumn name;
    public TableColumn price;
    public ImageView removeButton;
    public ImageView addButton;
    public ImageView searchButton;
    public TextField remainingCapacity;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }

    public void createNewInventory(ActionEvent actionEvent) {
    }

    public void quitButtonClicked(ActionEvent actionEvent) {
    }

    public void removeButtonClicked(MouseEvent mouseEvent) {
    }

    public void addButtonClicked(MouseEvent mouseEvent) {
    }

    public void searchButtonClicked(MouseEvent mouseEvent) {
    }
}
