package ucf.assignments;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class MainWindowControllersTest {

    InventoryList myInventory = new InventoryList("My Inventory",100);

    private void loadItems() throws FileNotFoundException {
        File inputFile = new File("files/testdata.txt");
        Scanner fileScanner = new Scanner(inputFile);
        while(fileScanner.hasNext()){
            String line = fileScanner.nextLine();
            String[] lineParts = line.split(",");
            myInventory.addItem(new Item(lineParts[0],lineParts[1],lineParts[2]));
        }
    }

    @Test
    void quitButtonClicked() throws FileNotFoundException {
        loadItems();
    }

    @Test
    void removeButtonClicked() throws FileNotFoundException {
        loadItems();
        Item temp = new Item("GGT54GH","car","$89.99");
        myInventory.removeItem(temp);
        assertEquals(97, myInventory.getRemainingCapacity());

    }

    @Test
    void addButtonClicked() throws FileNotFoundException {
        loadItems();
    }

    @Test
    void getItems() throws FileNotFoundException {
        loadItems();
    }
}