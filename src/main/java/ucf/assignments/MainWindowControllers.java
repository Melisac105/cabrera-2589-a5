/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Melissa Cabrera
 */

package ucf.assignments;

import javafx.stage.FileChooser;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class MainWindowControllers implements Initializable {

    private InventoryList myInventory;
    private Item itemEdited;

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
    ImageView editButton;

    @FXML
    Label capacity;

    @FXML
    TextField searchField;


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
        //load a new screen using fxml loader that's AddTaskWindow.fxml to add new task
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

    @FXML
    public void editButtonClicked() {
        //open a new window when edit button is clicked
        // if the item is not selected show a message dialog box to the user with a message
        // otherwise load a new screen using fxml loader that's EditItemWindow.fxml to edit item
        //all item information is set in the EditWindow
        //when open EditWindow, the item is deleted from myInventory list/
        //and when the window is close by the user the item modified is added again to the list
        if (tableView.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.INFORMATION, "Please select an item to edit!").show();
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EditItemWindow.fxml"));
            Parent root = null;
            try {
                root = fxmlLoader.load();
                EditItemWindowController controller = fxmlLoader.getController();
                controller.itemName.setText(tableView.getSelectionModel().getSelectedItem().getName());
                controller.itemPrice.setText(tableView.getSelectionModel().getSelectedItem().getPrice());
                controller.serialNum.setText(tableView.getSelectionModel().getSelectedItem().getSerialNumber());
                itemEdited = tableView.getSelectionModel().getSelectedItem();
                myInventory.removeItem(itemEdited);
                tableView.getItems().remove(itemEdited);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setOnCloseRequest(event -> {
                myInventory.addItem(itemEdited);
            });
            stage.setScene(scene);
            Stage stagePrevious = (Stage) editButton.getScene().getWindow();
            stagePrevious.close();
            stage.show();
        }
    }

    @FXML
    public void sortLowToHigh(){
        //convert prices to double type and add them to created ArrayList
        //put items into HashMap
        //clear tableView
        //sort prices and add them to tableView

        ArrayList<Double> prices = new ArrayList<>();
        ArrayList<Item> items = new ArrayList<>();
        Map<Double, Item> mp = new HashMap<>();

        for (Item i : tableView.getItems()) {
            items.add(i);
            String newString = i.getPrice().substring(1);
            prices.add(Double.parseDouble(newString));
        }

        int index = 0;
        double[] dbArr = new double[prices.size()];

        for(double a : prices){
            dbArr[index] = a;
            index++;
        }

        index = 0;

        for(Item i : items) {
            mp.put(dbArr[index], i);
            index++;
        }
        tableView.getItems().clear();
        Arrays.sort(dbArr);
        for (double d: dbArr){
            tableView.getItems().add(mp.get(d));
        }
    }

    @FXML
    public void saveAsTSV()  {

        FileChooser fileChooser = new FileChooser(); //create fileChooser object

        fileChooser.setTitle("Choose a folder to save file"); //set tittle for fileChooser dialog

        //add extension
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TSV", "*.txt"));

        // show file chooser dialog on screen to select a file and assigning selected file to the File object
        File f = fileChooser.showSaveDialog(new Stage());

        //write list with TSV format
        try {
            FileWriter writeFile = new FileWriter(f.toString());
            writeFile.write("Serial Number\tName\t\tPrice\r\n");
            for (Item i : InventoryList.getItems()) {
                writeFile.write(i.toTSV() + "\r\n");
            }
            writeFile.flush();
            writeFile.close();
            new Alert(Alert.AlertType.INFORMATION, "This list has been saved in your folder").show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void saveAsHTML() throws IOException {

        FileChooser fileChooser = new FileChooser(); // create object of FileChooser

        fileChooser.setTitle("Choose a folder to save file"); //set tittle for fileChooser dialog

        //add extension
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("HTML", "*.html"));

        // show file chooser dialog on screen to select a file and assigning selected file to the File object
        File f = fileChooser.showSaveDialog(new Stage());

        DataLoading.loadData(); //load inventory data

        //write list with HTML format
        try {
            File file = new File(f.toString() );
            if(!file.exists()) {
                file.createNewFile();
            }
            BufferedWriter bw;
            bw = new BufferedWriter(new FileWriter(f));
            String htmlHeaderFileContent = Html.generateHeaderHtml();
            bw.write(htmlHeaderFileContent);
            for (Item i : InventoryList.getItems()) {
                String htmlBodyFileContent = Html.generateBodyHtml(i.getSerialNumber(), i.getName(), i.getPrice());
                bw.write(htmlBodyFileContent);
            }
            bw.write(
                    """
                        </body>
                        </html>
                        """);
            bw.close();
            new Alert(Alert.AlertType.INFORMATION, "This list has been saved in your folder").show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void saveAsJSON() throws IOException {

        DataLoading.loadData(); //load inventory data

        JSONArray jsonArray = new JSONArray();

        //add items into JSONArray
        for (Item i : InventoryList.getItems()) {
            JSONObject productDetails = new JSONObject();
            productDetails.put("serial", i.getSerialNumber());
            productDetails.put("name", i.getName());
            productDetails.put("price", i.getPrice());
            jsonArray.add(productDetails);
        }

        // create object of FileChooser
        FileChooser fileChooser = new FileChooser();

        // set the title
        fileChooser.setTitle("Choose a folder to save file");

        //add extension
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));

        // call showDialog method of fileChooser to show it on screen
        File f = fileChooser.showSaveDialog(new Stage());


        String str = jsonArray.toJSONString(); //convert JSONArray to JSONString
        String [] str2 = str.split("(?<=},)");

        //write list with JSON format
        try {
            FileWriter file = new FileWriter(f.toString());
            for(String i : str2) {
                file.write(i);
            }
            file.flush();
            file.close();
            new Alert(Alert.AlertType.INFORMATION, "This list has been saved in your folder").show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @FXML
    public void openInventory() {
        FileChooser fileChooser = new FileChooser(); //create fileChooser object

        //add extensions
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("TSV", "*.txt"),
                new FileChooser.ExtensionFilter("HTML", "*.html"),
                new FileChooser.ExtensionFilter("JSON", "*.json"));

        // set title for file chooser dialog
        fileChooser.setTitle("Open Resource File");

        // show file chooser dialog on screen to select a file and assigning selected file to the File object
        File selectedFile = fileChooser.showOpenDialog(null);

        if(selectedFile != null) {
            String name = selectedFile.getName();
            String extension = name.substring(name.lastIndexOf(".") + 1, selectedFile.getName().length());

            //find in cases what type of extension is
            //when know which case, do require process to open file
            switch (extension) {
                case "html", "HTML" -> {
                    String html = "";
                    Path path = Paths.get(selectedFile.toURI());
                    try {
                        html = String.valueOf(Files.readAllLines(path));
                        if (!tableView.getItems().isEmpty()) {
                            tableView.getItems().clear();
                        }
                        myInventory = new InventoryList("HTML", 0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Document doc = Jsoup.parse(html);
                    Elements theWholeTable = doc.select("table");

                    //rows of table
                    Elements allTheRows = theWholeTable.select(":not(thead) tr");
                    for (Element row : allTheRows) {
                        Elements rowItems = row.select("td");
                        if (rowItems.size() == 3) {
                            if (!myInventory.contains(rowItems.get(0).text())) {
                                myInventory.addItem(new Item(rowItems.get(0).text(),
                                        rowItems.get(1).text(),
                                        rowItems.get(2).text()));
                            }
                        }
                    }
                    tableView.getItems().setAll(InventoryList.getItems());
                    capacity.setText("Remaining Capacity: " + myInventory.getCapacity());
                }
                case "json", "JSON" -> {
                    FileReader fr;
                    try {
                        fr = new FileReader(selectedFile);
                        JSONParser jsonParser = new JSONParser();
                        JSONArray arr = (JSONArray) jsonParser.parse(fr);
                        if (!(tableView.getItems()).isEmpty()) {
                            tableView.getItems().clear();
                        }
                        myInventory = new InventoryList("JSON", 0);
                        for (Object obj : arr) {
                            JSONObject o = (JSONObject) obj;
                            System.out.println(o.get("serial").toString() + " " + o.get("name").toString() + " " + o.get("price").toString());
                            if (!myInventory.contains(o.get("serial").toString())) {
                                myInventory.addItem(new Item(o.get("serial").toString(), o.get("name").toString(), o.get("price").toString()));
                            }
                        }
                    } catch (ParseException | IOException e) {
                        e.printStackTrace();
                    }
                    tableView.getItems().setAll(InventoryList.getItems());
                    capacity.setText("Remaining Capacity: " + myInventory.getCapacity());
                }
                case "txt", "TXT" -> {
                    Scanner fileScanner = null;
                    try {
                        fileScanner = new Scanner(selectedFile);
                        if (!tableView.getItems().isEmpty()) {
                            tableView.getItems().clear();
                        }
                        myInventory = new InventoryList("TSV", 0);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (fileScanner.hasNext()) {
                        fileScanner.nextLine();
                    }
                    while (fileScanner.hasNext()) {
                        String line = fileScanner.nextLine();
                        String[] lineParts = line.split("\\s+");
                        if (!myInventory.contains(lineParts[0])) {
                            myInventory.addItem(new Item(lineParts[0], lineParts[1], lineParts[2]));
                        }
                    }
                    tableView.getItems().setAll(InventoryList.getItems());
                    capacity.setText("Remaining Capacity: " + myInventory.getCapacity());
                }
                default -> System.out.println("not recognized!");
            }
        }
        else
        {
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
            if(inputFile.exists()) {
                Scanner fileScanner = new Scanner(inputFile);
                if(fileScanner.hasNext()) {
                    System.out.println(fileScanner.nextLine());
                }
                while (fileScanner.hasNext()) {
                    String line = fileScanner.nextLine();
                    String[] lineParts = line.split("\\s+");
                    myInventory.addItem(new Item(lineParts[0], lineParts[1], lineParts[2]));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        name.setSortType(TableColumn.SortType.ASCENDING);

        serialNumber.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));
        serialNumber.setSortType(TableColumn.SortType.ASCENDING);

        price.setCellValueFactory(new PropertyValueFactory<>("price"));

        tableView.getItems().setAll(InventoryList.getItems());

        capacity.setText("Capacity: " + myInventory.getCapacity());

        name.setCellFactory(TextFieldTableCell.forTableColumn());

        serialNumber.setCellFactory(TextFieldTableCell.forTableColumn());

        price.setCellFactory(TextFieldTableCell.forTableColumn());

        //observable list in a filtered list with all items
        FilteredList<Item> filteredData = new FilteredList(tableView.getItems(), b -> true);

        //set filter predicate when filterData changes
        searchField.textProperty().addListener((observable, oldDate, newData) -> {
            filteredData.setPredicate(item -> {

                boolean result = true;

                //if statement to find items containing same name or serial number
                if (newData != null && !newData.isEmpty()) {
                    if (!item.getName().toLowerCase().contains(newData.toLowerCase())) {
                        result = item.getSerialNumber().toLowerCase().contains(newData.toLowerCase());
                    }
                }
                return result;
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
