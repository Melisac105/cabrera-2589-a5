/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Melissa Cabrera
 */

package ucf.assignments;

public class Html {
    public static String generateHeaderHtml() {
        //html header format
        return """
                <!DOCTYPE html>
                <html>
                    <head>
                        <style>
                            table {
                                border-spacing: 0 15px;
                                border-collapse: collapse;
                            }
                            th, td {
                                width: 250px;
                                text-align: center;
                                border: 1px solid black;
                            }
                        </style>
                    </head>
                    <body>
                        <table>
                            <thead>
                                <tr>
                                    <th>Serial Number</th>
                                    <th>Name</th>
                                    <th>Price</th>
                                </tr>
                            </thead>
                        </table>
                    <body>
                </html>
                """;
    }

    public static String generateBodyHtml(String serialNum, String name, String price) {
        //html body format
        return """
                <!DOCTYPE html>
                <html>
                    <table>
                        <tr>
                            <td>%s</td>
                            <td>%s</td>
                            <td>%s</td>
                        </tr>
                    </table>
                </html>
                """.formatted(serialNum, name, price);
    }
}
