package ucf.assignments;

public class Item {

        private String serialNumber;
        private String price;

        String name;

        public Item(String serialNumber, String name, String price) {
            this.name = name;
            this.serialNumber = serialNumber;
            this.price = price; // format as shared
        }

        //getter method for item description
        public String getSerialNumber() {
            return serialNumber;
        }

        //setter method for item description using "this."
        public void setSerialNumber(String serialNumber) {
            this.serialNumber = serialNumber;
        }

        //getter method for price
        public String getPrice() {
            return price;
        }

        //setter method for price using "this."
        public void setPrice(String price) {
            this.price = price;
        }

        //getter method for name
        public String getName() {
            return name;
        }

        //setter method for name
        public void setName(String name) {
            this.name = name;
        }

        public String toString(){
            return this.serialNumber+","+this.name +","+this.price;
        }

}
