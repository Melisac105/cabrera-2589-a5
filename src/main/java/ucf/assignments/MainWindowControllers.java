package ucf.assignments;

import javafx.stage.FileChooser;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.URL;
import java.util.*;

public class MainWindowControllers implements Initializable {

    private InventoryList myInventory;

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
    Label capacity;

    @FXML
    TextField searchField;

    @FXML
    public void quitButtonClicked() {
        System.exit(0);
    }

    @FXML
    public void removeButtonClicked() {
        // getting selected item from the tableview into Item object
        Item selectedItem = tableView.getSelectionModel().getSelectedItem();

        // removing selected item from the table view
        tableView.getItems().remove(selectedItem);

        // setting remaining capacity of the todolist
        myInventory.removeItem(selectedItem);

        capacity.setText("Capacity: " + myInventory.getCapacity());
    }

    @FXML
    public void addButtonClicked() throws IOException {
        //open a new window when add button is clicked
        // if the remaining capacity of the todolist is 0 then it will just show a message dialog box to the user with a message
        // otherwise load a new screen using fxml loader that's AddTaskWindow.fxml to add new task
        if (myInventory.getCapacity() <= 0) {
            new Alert(Alert.AlertType.INFORMATION, "The list is full, delete some item").show();
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ucf/assignments/AddItemWindow.fxml"));
            Parent root;
            root = fxmlLoader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);

            stage.setResizable(false);
            Stage stagePrevious = (Stage) addButton.getScene().getWindow();
            stagePrevious.close();
            stage.show();
        }
    }

    @FXML
    public void saveAsTSV()  {
        // create object of DirectoryChooser
        DirectoryChooser directoryChooser = new DirectoryChooser();

        // set the title
        directoryChooser.setTitle("Choose a folder to save file");

        // call showDialog method of directoryChooser to show it on screen
        // File object will get selected directory from directoryChooser dialog
        File file = directoryChooser.showDialog(null);
        System.out.println(file);

        // file writer will write all the todotasks to the 'list.txt' file in the selected directory
        //show a message dialog box when all the tasks will have been saved into the file
        try {
            FileWriter writeFile = new FileWriter(file.toString() + "\\list1.txt");
            writeFile.write("Serial Number\t" + "Name\t\t" + "Price\t\n");
            for (Item i : InventoryList.getItems()) {
                writeFile.write( i.toTSV() + "\r\n");
            }
            writeFile.flush();
            writeFile.close();
            new Alert(Alert.AlertType.INFORMATION, "This list has been saved in your folder").show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void saveAsHTML() {
        // create object of DirectoryChooser
        DirectoryChooser directoryChooser = new DirectoryChooser();

        // set the title
        directoryChooser.setTitle("Choose a folder to save file");

        // call showDialog method of directoryChooser to show it on screen
        // File object will get selected directory from directoryChooser dialog
        File file = directoryChooser.showDialog(null);
        System.out.println(file);

        try {
            File f = new File(file.toString() + "\\source.html");
            if(!f.exists()) {
                f.createNewFile();
            }
            BufferedWriter bw = null;
            bw = new BufferedWriter(new FileWriter(f));
            String htmlHeaderFileContent = Html.generateHeaderHtml();
            bw.write(htmlHeaderFileContent);
            for (Item i : InventoryList.getItems()) {
                String htmlBodyFileContent = Html.generateBodyHtml(i.getSerialNumber(), i.getName(), i.getPrice());
                bw.write(htmlBodyFileContent);
            }
            bw.close();
            new Alert(Alert.AlertType.INFORMATION, "This list has been saved in your folder").show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void saveAsJSON() {
        JSONArray jsonArray = new JSONArray();

        //add inventory data to JSONObject
        for (Item i : InventoryList.getItems()) {

            JSONObject productObject = new JSONObject();
            JSONObject productDetails = new JSONObject();
            productDetails.put("serial", i.getSerialNumber());
            productDetails.put("name", i.getName());
            productDetails.put("price", i.getPrice());
            productObject.put("product", productDetails);
            jsonArray.add(productObject);
        }
        // create object of DirectoryChooser
        DirectoryChooser directoryChooser = new DirectoryChooser();

        // set the title
        directoryChooser.setTitle("Choose a folder to save file");

        // call showDialog method of directoryChooser to show it on screen
        // File object will get selected directory from directoryChooser dialog
        File f = directoryChooser.showDialog(null);
        System.out.println(f);

        // file writer will write all the todotasks to the 'todolistdownloaded.json' file in the selected directory
        //show a message dialog box when all the tasks will have been saved into the file
        try {
            FileWriter file = new FileWriter(f.toString() + "\\products1.json");
            file.write(jsonArray.toJSONString());
            file.flush();
            new Alert(Alert.AlertType.INFORMATION, "This list has been saved in your folder").show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void openInventory() {
        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("TSV", "*.txt"),
                new FileChooser.ExtensionFilter("HTML", "*.html"),
                new FileChooser.ExtensionFilter("JSON", "*.json"));

        // set title for file chooser dialog
        fileChooser.setTitle("Open Resource File");

        // show file chooser dialog on screen to select a file and assigning selected file to the File object
        File selectedFile = fileChooser.showOpenDialog(null);

        System.out.println(selectedFile.getName());
        // if user has selected any file then it will clear existing todolist first and then add all the tasks
        // from the file into that todolist one by one using a loop
        // finally it will show all the items in the tableview and update the Remaining capacity of the todolist
        // else show a message dialog box with a message.
        if (selectedFile != null) {
            // myInventory.clearAll();
            Scanner fileScanner = null;
            try {
                fileScanner = new Scanner(selectedFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            while (fileScanner.hasNext()) {
                String line = fileScanner.nextLine();
                String[] lineParts = line.split(",");

                myInventory.addItem(new Item(lineParts[0], lineParts[1], lineParts[2]));
            }
            tableView.getItems().setAll(InventoryList.getItems());
            capacity.setText("Capacity: " + myInventory.getCapacity());
        } else {
            new Alert(Alert.AlertType.INFORMATION, "Invalid File OR File not chosen").show();
        }

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // the method will be called on the initialization of MainWindowController's object

        myInventory = new InventoryList("My List", 0); // initializing todolist

        // getting all the tasks from 'data.txt' file and adding them to todolist one by one
        try {
            File inputFile = new File("files/data.txt");
            Scanner fileScanner = new Scanner(inputFile);
            while (fileScanner.hasNext()) {
                String line = fileScanner.nextLine();
                String[] lineParts = line.split(",");
                myInventory.addItem(new Item(lineParts[0], lineParts[1], lineParts[2]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        name.setSortType(TableColumn.SortType.ASCENDING);

        serialNumber.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));
        serialNumber.setSortType(TableColumn.SortType.ASCENDING);

        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        price.setSortType(TableColumn.SortType.ASCENDING);

        tableView.getItems().setAll(InventoryList.getItems());

        capacity.setText("Capacity: " + myInventory.getCapacity());

        name.setCellFactory(TextFieldTableCell.forTableColumn());
        name.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Item, String>>() {
                    // this method will be called when user try to edit any cell in the taskName column
                    @Override
                    public void handle(TableColumn.CellEditEvent<Item, String> t) {
                        // setting new name to the selected cell in the taskName column
                        (t.getTableView().getItems().get(t.getTablePosition().getRow())).setName(t.getNewValue());

                        // getting that item from the table into Item object
                        Item temp = (t.getTableView().getItems().get(t.getTablePosition().getRow()));

                        myInventory.editName(temp, t.getNewValue()); // updating that item's new name into todolist
                    }
                }
        );

        serialNumber.setCellFactory(TextFieldTableCell.forTableColumn());
        serialNumber.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Item, String>>() {
                    // this method will be called when user try to edit any cell in the description column
                    @Override
                    public void handle(TableColumn.CellEditEvent<Item, String> t) {

                        for (int i = 0; i < InventoryList.getItems().size(); i++) {
                            if (InventoryList.getItems().get(i).getSerialNumber().equals(t.getTableView().getItems().get(i).getSerialNumber())) {
                                new Alert(Alert.AlertType.INFORMATION, "This serial number already exists").show();
                                return;
                            }
                        }
                        // setting new description to the selected cell in the description column
                        (t.getTableView().getItems().get(t.getTablePosition().getRow())).setSerialNumber(t.getNewValue());

                        // getting that item from the table into Item object
                        Item temp = (t.getTableView().getItems().get(t.getTablePosition().getRow()));

                        myInventory.editSerialNumber(temp, t.getNewValue()); // updating that item's new description into todolist
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
                        (t.getTableView().getItems().get(t.getTablePosition().getRow())).setPrice(t.getNewValue());

                        // getting that item from the table into Item object
                        Item temp = (t.getTableView().getItems().get(t.getTablePosition().getRow()));

                        myInventory.editPrice(temp, t.getNewValue()); // updating that item's new dueDate into todolist
                    }
                }
        );

        //observable list in a filtered list with all items
        FilteredList<Item> filteredData = new FilteredList(tableView.getItems(), b -> true);

        //set filter predicate when filterData changes
        searchField.textProperty().addListener((observable, oldData, newData) -> {
            filteredData.setPredicate(item -> {

                //if searchBox is empty, show all items
                if (newData == null || newData.isEmpty()) {
                    return true;
                }

                String toLowerCase = newData.toLowerCase();

                if (item.getName().toLowerCase().contains(toLowerCase)) {
                    return true; //filter matches name
                } else if (item.getSerialNumber().toLowerCase().contains(toLowerCase)) {
                    return true; //filter matches serialNumber
                } else {
                    return false; //not match
                }
            });
            SortedList<Item> sortData = new SortedList<>(filteredData); //filterList in a sortList

            //bind sortList comparator to tableView comparator
            sortData.comparatorProperty().bind(tableView.comparatorProperty());

            tableView.setItems(sortData); //add sorted/filtered data to the table
        });


    }

    public static ArrayList<Item> getItems() {
        return InventoryList.getItems();
    }
}
