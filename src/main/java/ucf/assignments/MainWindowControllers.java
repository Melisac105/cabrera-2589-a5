package ucf.assignments;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

public class MainWindowControllers implements Initializable {

    private static InventoryList myList;

    @FXML
    MenuItem newInventory;
    @FXML
    MenuItem quit;

    @FXML
    TableView<Item> tableView;
    @FXML
    TableColumn<Item, String> serialNumber;
    @FXML
    TableColumn<Item, String> name;
    @FXML
    TableColumn<Item, String> price;

    @FXML
    ImageView removeButton;
    @FXML
    ImageView addButton;
    @FXML
    ImageView searchButton;

    @FXML
    TextField remainingCapacity;



    public void createNewInventory(ActionEvent actionEvent) {
    }

    public void quitButtonClicked(ActionEvent actionEvent) {
    }

    public void removeButtonClicked(MouseEvent mouseEvent) {
    }

    @FXML
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
        // the method will be called on the initialization of MainWindowController's object

        myList = new InventoryList("My List", 100); // initializing todolist

        // getting all the tasks from 'data.txt' file and adding them to todolist one by one
        try {
            File inputFile = new File("files/data.txt");
            Scanner fileScanner = new Scanner(inputFile);
            while(fileScanner.hasNext()){
                String line = fileScanner.nextLine();
                String[] lineParts = line.split(",");
                myList.addItem(new Item(lineParts[0],lineParts[1],lineParts[2]));
            }
        } catch(Exception e){
            e.printStackTrace();
        }

        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        serialNumber.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));

        tableView.getItems().setAll(myList.getItems());

        name.setCellFactory(TextFieldTableCell.forTableColumn());
        name.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Item, String>>() {
                    // this method will be called when user try to edit any cell in the taskName column
                    @Override
                    public void handle(TableColumn.CellEditEvent<Item, String> t) {
                        // setting new name to the selected cell in the taskName column
                        ( t.getTableView().getItems().get(t.getTablePosition().getRow())).setName(t.getNewValue());

                        // getting that item from the table into Item object
                        Item temp = ( t.getTableView().getItems().get(t.getTablePosition().getRow()));

                        myList.editName(temp, t.getNewValue()); // updating that item's new name into todolist
                    }
                }
        );

        serialNumber.setCellFactory(TextFieldTableCell.forTableColumn());
        serialNumber.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Item, String>>() {
                    // this method will be called when user try to edit any cell in the description column
                    @Override
                    public void handle(TableColumn.CellEditEvent<Item, String> t) {
                        // setting new description to the selected cell in the description column
                        ( t.getTableView().getItems().get(t.getTablePosition().getRow())).setSerialNumber(t.getNewValue());

                        // getting that item from the table into Item object
                        Item temp = ( t.getTableView().getItems().get(t.getTablePosition().getRow()));

                        myList.editSerialNumber(temp, t.getNewValue()); // updating that item's new description into todolist
                    }
                }
        );

        price.setCellFactory(TextFieldTableCell.forTableColumn());
        price.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Item, String>>() {
                    // this method will be called when user try to edit any cell in the dueDate column
                    @Override
                    public void handle(TableColumn.CellEditEvent<Item, String> t) {
                        // setting new dueDate to the selected cell in the description column
                        ( t.getTableView().getItems().get(t.getTablePosition().getRow())).setPrice(t.getNewValue());

                        // getting that item from the table into Item object
                        Item temp = ( t.getTableView().getItems().get(t.getTablePosition().getRow()));

                        myList.editPrice(temp, t.getNewValue()); // updating that item's new dueDate into todolist
                    }
                }
        );
    }

    public static ArrayList<Item> getTasks(){
        return myList.getItems();
    }
}
