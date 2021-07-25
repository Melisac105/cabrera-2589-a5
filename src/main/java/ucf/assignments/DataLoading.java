/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Melissa Cabrera
 */

package ucf.assignments;

import java.io.FileWriter;
import java.io.IOException;

public class DataLoading {

     static void loadData() throws IOException {

        //load save data when app is open again
        FileWriter writeFile = new FileWriter("files/data.txt");
        writeFile.write("Serial Number\tName\t\tPrice\r\n");
        for (Item i : InventoryList.getItems()) {
            writeFile.write(i.toTSV() + "\r\n");
        }
        writeFile.flush();
        writeFile.close();
    }
}
