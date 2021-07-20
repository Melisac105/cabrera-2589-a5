package ucf.assignments.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    Item item = new Item("HFT54YH76","lamp","$9.99");

    @Test
    void getSerialNumber() {
        assertEquals("HFT54YH76", item.getSerialNumber());
    }

    @Test
    void setSerialNumber() {
        item.setSerialNumber("658HTF");
        assertEquals("658HTF", item.getSerialNumber());
    }

    @Test
    void getPrice() {
        assertEquals("$9.99", item.getPrice());
    }

    @Test
    void setPrice() {
        item.setPrice("$10.99");
        assertEquals("$10.99", item.getPrice());
    }

    @Test
    void getName() {
        assertEquals("lamp", item.getName());
    }

    @Test
    void setName() {
        item.setName("book");
        assertEquals("book", item.getName());
    }

    @Test
    void testToString() {
        assertEquals("HFT54YH76,lamp,$9.99", item.toString());
    }

    @Test
    void toTSV() {
        assertEquals("HFT54YH76\tlamp\t$9.99", item.toTSV());
    }
}