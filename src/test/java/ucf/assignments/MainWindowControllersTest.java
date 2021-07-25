package ucf.assignments;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;


class MainWindowControllersTest {

    InventoryList myInventory = new InventoryList("My Inventory",0);
    int no_of_duplicates;

    private void loadItems() throws FileNotFoundException {
        Scanner fileScanner = new Scanner("files/testdata.txt");
        if(fileScanner.hasNext()) {
            fileScanner.nextLine();
        }
        while (fileScanner.hasNext()) {
            String line = fileScanner.nextLine();
            String[] lineParts = line.split("\\s+");
            if(!myInventory.contains(lineParts[0])) {
                myInventory.addItem(new Item(lineParts[0], lineParts[1], lineParts[2]));
            }
        }
    }

    @Test
    void numericPrices() {
        ArrayList<Double> dbArr = new ArrayList<>();
        openAsHTML();
        for(Item i : InventoryList.getItems()){
            dbArr.add(Double.parseDouble(i.getPrice().substring(1)));
        }
        openAsJSON();
        for(Item i : InventoryList.getItems()){
            dbArr.add(Double.parseDouble(i.getPrice().substring(1)));
        }
        openAsTSV();
        for(Item i : InventoryList.getItems()){
            dbArr.add(Double.parseDouble(i.getPrice().substring(1)));
        }
    }

    @Test
    void saveAsTSV() {
        try {
            loadItems();
            FileWriter writeFile = new FileWriter("files/Test_As_tsv.txt");
            writeFile.write("Serial Number\tName\t\tPrice\r\n");
            for (Item i : InventoryList.getItems()) {
                writeFile.write(i.toTSV() + "\r\n");
            }
            writeFile.flush();
            writeFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void saveAsHTML() {
        try {
            loadItems();
            File file = new File("files/Test_As_html.html");
            if(!file.exists()) {
                file.createNewFile();
            }
            BufferedWriter bw;
            bw = new BufferedWriter(new FileWriter(file));
            String htmlHeaderFileContent = Html.generateHeaderHtml();
            bw.write(htmlHeaderFileContent);
            for (Item i : InventoryList.getItems()) {
                String htmlBodyFileContent = Html.generateBodyHtml(i.getSerialNumber(), i.getName(), i.getPrice());
                bw.write(htmlBodyFileContent);
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void saveAsJSON() throws FileNotFoundException {
        loadItems();
        JSONArray jsonArray = new JSONArray();
        for (Item i : InventoryList.getItems()) {

            JSONObject productDetails = new JSONObject();
            productDetails.put("serial", i.getSerialNumber());
            productDetails.put("name", i.getName());
            productDetails.put("price", i.getPrice());
            jsonArray.add(productDetails);

        }
        String str = jsonArray.toJSONString();
        String [] str2 = str.split("(?<=},)");

        try {
            FileWriter file = new FileWriter("files/Test_As_json.json");
            for(String a : str2) {
                file.write(a);
            }
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void openAsTSV() {
        Scanner fileScanner;
        fileScanner = new Scanner("files/Test_As_tsv.txt");
        myInventory = new InventoryList("TSV", 0);
        if(fileScanner.hasNext())
            fileScanner.nextLine();
        while (fileScanner.hasNext()) {
            String line = fileScanner.nextLine();
            String[] lineParts = line.split("\\s+");
            if(!myInventory.contains(lineParts[0]))
                myInventory.addItem(new Item(lineParts[0], lineParts[1], lineParts[2]));
        }
    }

    @Test
    void openAsHTML() {
        String html = "";
        Path path = Paths.get("files/Test_As_html.html");
        try {
            html = String.valueOf(Files.readAllLines(path));
            myInventory = new InventoryList("HTML", 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Document doc = Jsoup.parse(html);
        Elements theWholeTable = doc.select("table");
        Elements allTheRows = theWholeTable.select(":not(thead) tr");
        for (Element row : allTheRows) {
            Elements rowItems = row.select("td");
            if (rowItems.size() == 3) {
                if (!myInventory.contains(rowItems.get(0).text()))
                    myInventory.addItem(new Item(rowItems.get(0).text(),
                            rowItems.get(1).text(),
                            rowItems.get(2).text()));
            }
        }
        System.out.println(myInventory.toString());
    }

    @Test
    void openAsJSON() {
        FileReader fr;
        try {
            fr = new FileReader("files/Test_As_json.json");
            JSONParser jsonParser = new JSONParser();
            JSONArray arr = (JSONArray) jsonParser.parse(fr);
            myInventory = new InventoryList("JSON", 0);
            for(Object obj : arr){
                JSONObject o = (JSONObject) obj;
                if(!myInventory.contains(o.get("serial").toString()))
                    myInventory.addItem(new Item(o.get("serial").toString(), o.get("name").toString(), o.get("price").toString()));
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void noDuplicateSerialNumber(){
        openAsHTML();
        no_of_duplicates = 0;
        for(Item i : InventoryList.getItems()){
            if (!myInventory.contains(i.getSerialNumber()))
                no_of_duplicates++;
        }
        System.out.println("Number of Duplicates in HTML "+no_of_duplicates);

        openAsJSON();
        no_of_duplicates = 0;
        for(Item i : InventoryList.getItems()){
            if (!myInventory.contains(i.getSerialNumber()))
                no_of_duplicates++;
        }
        System.out.println("Number of Duplicates in JSON "+no_of_duplicates);
        openAsTSV();
        no_of_duplicates = 0;
        for(Item i : InventoryList.getItems()){
            if (!myInventory.contains(i.getSerialNumber()))
                no_of_duplicates++;
        }
        System.out.println("Number of Duplicates in JSON "+no_of_duplicates);
    }

}