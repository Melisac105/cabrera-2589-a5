package ucf.assignments;

import java.util.ArrayList;

public class InventoryList {

    private String name;
    private int capacity;

    public int count;

    public static ArrayList<Item> items;

    public InventoryList(String name, int capacity) {
        //initialize all fields
        this.name = name;
        this.capacity = capacity;
        count = 0; //set count = 0
        items = new ArrayList<>(); //make a new list for items
    }

    public String getName() {
        return name; //returns the title of the todolist
    }

    public void setName(String name) {
        this.name = name; //set new title of the todolist using "this."
    }

    public int getRemainingCapacity() {
        return capacity - count; //return the remaining capacity of the todolist
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity; //setter method for capacity using "this."
    }

    public void updateName(String newName) {
        this.name = newName; //edit title using "this.title"
    }

    public void addItem(Item i) {
        items.add(i); //use .add to add new task in the list
        count++; //increase counter by 1
    }

    public void removeItem(Item i) {
        items.remove(i); //use .remove to delete item in the list
        count--; //decrease counter by 1
    }

    public void editName(Item i, String str) {
        int index;
        //loop to find task from the list and record the index for passed task
        for (index = 0; index < count; index++) {
            //if name are the same, then break
            if (i.equals(getItems().get(index))) {
                break;
            }
        }
        getItems().get(index).setName(str); //update the passed string as new one
    }

    public void editSerialNumber(Item i, String str) {
        int index;
        //loop to find task from the list and record the index for passed task
        for (index = 0; index < count; index++) {
            //if descriptions are the same, then break
            if (i.equals(getItems().get(index))) {
                break;
            }
        }
        getItems().get(index).setSerialNumber(str); //update the passed string as new description
    }

    public void editPrice(Item i, String str) {
        int index;
        //loop to find task from the list and record the index for passed task
        for (index = 0; index < count; index++) {
            //if dates are the same, then break
            if (i.equals(getItems().get(index))) {
                break;
            }
        }
        getItems().get(index).setPrice(str); //update the passed string as new due date
    }


    //this is a getter method for getting items inside a todolist
    public static ArrayList<Item> getItems() {
        return items;
    }

}
