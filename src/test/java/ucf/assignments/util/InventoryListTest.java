package ucf.assignments.util;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class InventoryListTest {

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
    void getName() throws FileNotFoundException {
        loadItems();
        assertEquals("My Inventory", myInventory.getName());
    }

    @Test
    void setName() throws FileNotFoundException {
        loadItems();
        myInventory.setName("My Inventory new name");
        assertEquals("My Inventory new name", myInventory.getName());
    }

    @Test
    void getRemainingCapacity() throws FileNotFoundException {
        loadItems();
        assertEquals(96, myInventory.getRemainingCapacity());
    }

    @Test
    void setCapacity() throws FileNotFoundException {
        loadItems();
        myInventory.setCapacity(150);
        assertEquals(146, myInventory.getRemainingCapacity());
    }

    @Test
    void editName() throws FileNotFoundException {
        loadItems();
        myInventory.updateName("new name");
        assertEquals("new name", myInventory.getName());
    }

    @Test
    void addItem() throws FileNotFoundException {
        loadItems();

        Item temp = new Item("JYGB65TB", "mouse", "$20.99");
        myInventory.addItem(temp);

        assertEquals(temp, myInventory.getItems().get(myInventory.count-1));
    }

    @Test
    void removeItem() throws FileNotFoundException {
        loadItems();

        Item temp = new Item("JYGB65TB", "mouse", "$20.99");
        myInventory.addItem(temp);

        int capacityBeforeDelete = myInventory.getRemainingCapacity();

        myInventory.removeItem(temp);

        int capacityAfterDelete = myInventory.getRemainingCapacity();
        assertEquals(capacityBeforeDelete,capacityAfterDelete-1);
    }

    @Test
    void updateName() throws FileNotFoundException {
        loadItems();
        Item temp = myInventory.getItems().get(0);
        myInventory.updateName(temp,"test");
        assertEquals("test", myInventory.getItems().get(0).getName());
    }

    @Test
    void editSerialNumber() throws FileNotFoundException {
        loadItems();
        Item temp = myInventory.getItems().get(0);
        myInventory.editSerialNumber(temp, "TG76JNN");
        assertEquals("TG76JNN", myInventory.getItems().get(0).getSerialNumber());
    }

    @Test
    void editPrice() throws FileNotFoundException {
        loadItems();
        Item temp = myInventory.getItems().get(0);
        myInventory.editPrice(temp, "$5.99");
        assertEquals("$5.99", myInventory.getItems().get(0).getPrice());

    }

    @Test
    void getItems() throws FileNotFoundException {
        loadItems();
        ArrayList<Item> items = myInventory.getItems();
        assertEquals(4, items.size());
    }
}