import java.util.Scanner;
import java.util.Set;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * This class holds the attributes which are only applicable to stocks and
 * mutual funds. It extends the abstract class Investment.
 */
public class Portfolio {

    /**
     * The list of investments.
     * 
     * This field stores all the investments in the portfolio.
     */
    protected ArrayList<Investment> investments = new ArrayList<>();
    private HashMap<String, List<Integer>> myHashMap = new HashMap<>(); 

    /**
     * The main method of the program. It creates a new portfolio, loads from a file if provided, and then starts the GUI.
     * If no filename is provided, it starts with a blank portfolio and saves it to "cis2430.portfolio".
     * @param args the command line arguments, of which the first argument is the filename to load the portfolio from
     */
    public static void main(String[] args) {

        //Creating an object of Portfolio class
        Portfolio obj = new Portfolio();


        if (args.length > 0) {

            String filename = args[0];

            if (obj.loadInvestments(filename) == true) {
                System.out.println("Portfolio loaded successfully from file: " + filename);
                GUIPanel gui = new GUIPanel(obj, filename);  //Creating an object of GUIPanel
                gui.displayGUI();  //Displaying the GUI
            } else {
                System.out.println("Failed to load Portfolio from file. Starting with a blank portfolio.");
                obj.saveInvestments(filename);
                GUIPanel gui = new GUIPanel(obj, filename);
                gui.displayGUI();
            }

        } else {
            System.out.println("No file provided. Starting with a blank Portfolio.");
            obj.saveInvestments("cis2430");
            GUIPanel gui = new GUIPanel(obj, "cis2430");
            gui.displayGUI();
        }


    }

    /**
     * Saves the current state of the portfolio to a file with the given name in the
     * "portfolio" directory.
     * The file is written in a format that can be read by the loadInvestments
     * method.
     * 
     * @param filename the name of the file to write to
     */
    public void saveInvestments(String filename) {

        // Checking if the directory exist otherwise create it
        if (!new File("portfolio").exists()) {
            new File("portfolio").mkdir();
        }

        File file = new File("portfolio/" + filename + ".portfolio"); //Creating the file object

        try {
            PrintWriter writer = new PrintWriter(file); //Creating the PrintWriter object for writing data in the file

            //Loop to iterate through whole investments arraylist
            for (int i = 0; i < investments.size(); i++) {

                if (investments.get(i).isStock()) { //Writing data of stocks in the file
                    writer.printf(
                            "Type = \"Stock\"\nSymbol = \"%s\"\nName = \"%s\"\nQuantity = %d\nPrice = %.2f\nBookValue = %.2f\n\n",
                            investments.get(i).getSymbol(), investments.get(i).getName(),
                            investments.get(i).getQuantity(),
                            investments.get(i).getPrice(), investments.get(i).getBookValue());
                } else { //Writing data of mutual funds in the file
                    writer.printf(
                            "Type = \"MutualFund\"\nSymbol = \"%s\"\nName = \"%s\"\nQuantity = %d\nPrice = %.2f\nBookValue = %.2f\n\n",
                            investments.get(i).getSymbol(), investments.get(i).getName(),
                            investments.get(i).getQuantity(),
                            investments.get(i).getPrice(), investments.get(i).getBookValue());

                }
            }
            writer.close(); //Closing the PrintWriter

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads investment data from a specified file in the "portfolio" directory. The
     * file should be
     * formatted in a specific way that aligns with the saveInvestments method's
     * output. The method
     * reads each investment's type, symbol, name, quantity, price, and book value,
     * then creates and
     * adds the corresponding Stock or MutualFund object to the investments list.
     * Returns true if the
     * file exists and is successfully read; otherwise, returns false.
     * 
     * @param filename the name of the file (without extension) to load the
     *                 investments from
     * @return boolean indicating whether the investments were successfully loaded
     */
    public boolean loadInvestments(String filename) {
        //Creating the file object
        File file = new File("portfolio", filename + ".portfolio");

        if (file.exists()) {

            try (Scanner reader = new Scanner(file)) {

                while (reader.hasNextLine()) { //Reading data from the file line by line

                    if (reader.hasNextLine() == false) { //Exiting the loop when the file ends
                        break;
                    }

                    String typeLine = getNextLine(reader);
                    if (typeLine.contains("=") == false) { //Skip when the line does not contain "="
                        continue;
                    }

                    String type = performSplit(typeLine, "\"", 1); //Getting the investment type from the file

                    String symbolLine = getNextLine(reader);
                    String symbol = performSplit(symbolLine, "\"", 1); //Getting the investment symbol from the file

                    String nameLine = getNextLine(reader);
                    String name = performSplit(nameLine, "\"", 1); //Getting the investment name from the file

                    String quantityLine = getNextLine(reader);
                    int quantity = parseToInt(performSplit(quantityLine, "=", 1)); //Getting the investment quantity from the file

                    String priceLine = getNextLine(reader);
                    double price = parseToDouble(performSplit(priceLine, "=", 1)); //Getting the investment price from the file

                    String bookValueLine = getNextLine(reader);
                    double bookValue = parseToDouble(performSplit(bookValueLine, "=", 1)); //Getting the investment book value from the file

                    //Adding the investments to the investments arraylist
                    if (type.equalsIgnoreCase("Stock")) {
                        investments.add(new Stock(symbol, name, quantity, price, bookValue));
                    } else if (type.equalsIgnoreCase("MutualFund")) {
                        investments.add(new MutualFund(symbol, name, quantity, price, bookValue));
                    }
                }
                return true;

            } catch (FileNotFoundException e) {
                System.err.println("File not found: " + e.getMessage());
                return false;
            }
        }
        return false;
    }

    /**
     * Retrieves the next line from the provided Scanner object if available.
     * If no line is available, returns an empty string.
     *
     * @param reader the Scanner object used to read lines from an input source
     * @return the next line as a String if available, otherwise an empty string
     */
    private String getNextLine(Scanner reader) {
        if (reader.hasNextLine()) {
            return reader.nextLine();
        } else {
            return "";
        }
    }

    /**
     * Splits a line of text into parts using a regular expression, and returns
     * the part at the given index. If the line is not split into enough parts
     * to have a part at the given index, an empty string is returned.
     *
     * @param line  the line of text to be split
     * @param regex the regular expression to be used for splitting
     * @param i     the index of the part to be returned
     * @return the part at the given index, or an empty string if no such part
     *         exists
     */
    private String performSplit(String line, String regex, int i) {
        String[] parts = line.split(regex);
        if (parts.length > i) {
            return parts[i];
        } else {
            return "";
        }
    }

    /**
     * Parses the given string as an integer, or returns 0 if parsing fails
     * (for example, if the string does not represent a valid integer)
     *
     * @param number the string to be parsed
     * @return the parsed integer, or 0 if parsing fails
     */
    private int parseToInt(String number) {
        try {
            return Integer.parseInt(number.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Parses the given string as a double, or returns 0.0 if parsing fails
     * (for example, if the string does not represent a valid double)
     *
     * @param number the string to be parsed
     * @return the parsed double, or 0.0 if parsing fails
     */
    private double parseToDouble(String number) {
        try {
            return Double.parseDouble(number.trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
    
