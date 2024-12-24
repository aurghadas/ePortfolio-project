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



    /**
     * Buys a specified quantity of an investment at a given price.
     * If the investment does not exist in the portfolio, a new one is created.
     * If the investment already exists, the quantity is updated.
     * 
     * @param type the type of investment (Stock or MutualFund)
     * @param symbol the symbol of the investment
     * @param name the name of the investment
     * @param quantity the number of units to buy
     * @param price the price per unit of the investment
     * @return a string indicating the success of the purchase
     */
    public String buy(String type, String symbol, String name, int quantity, double price) { // Made changes
        Investment currentInvestment = null;

        //Check to see if any investment with the same symbol already exists or not
        if (type.equalsIgnoreCase("Stock")) {
            for (int i = 0; i < investments.size(); i++) {
                if (investments.get(i).getSymbol().equals(symbol) && !investments.get(i).isStock()) {
                    return "Symbol exists as MutualFund, not as Stock!";
                }
            }
        }else if (type.equalsIgnoreCase("MutualFund")) {
            for (int i = 0; i < investments.size(); i++) {
                if (investments.get(i).getSymbol().equals(symbol) && investments.get(i).isStock()) {
                    return "Symbol exists as Stock, not as MutualFund!";
                }
            }
        }

        //Looking for the investment in the existing list
        for (int i = 0; i < investments.size(); i++) {
            if (investments.get(i).getSymbol().equals(symbol)) {
                currentInvestment = investments.get(i);
                break;
            }
        }

        if (currentInvestment == null) { //If the investment is not found, create a new one based on its type

            if (type.equalsIgnoreCase("Stock")) {
                currentInvestment = new Stock(symbol, name, quantity, price);
                investments.add(currentInvestment);
                return "Following Stock added successfully!" + "\n\n" +
                        "Symbol: " + currentInvestment.getSymbol() + "\n" +
                        "Name: " + currentInvestment.getName() + "\n" +
                        "Quantity: " + currentInvestment.getQuantity() + "\n" +
                        "Price: " + currentInvestment.getPrice() + "\n";

            } else if (type.equalsIgnoreCase("MutualFund")) {
                currentInvestment = new MutualFund(symbol, name, quantity, price);
                investments.add(currentInvestment);
                return "Following MutualFund added successfully!" + "\n\n" +
                        "Symbol: " + currentInvestment.getSymbol() + "\n" +
                        "Name: " + currentInvestment.getName() + "\n" +
                        "Quantity: " + currentInvestment.getQuantity() + "\n" +
                        "Price: " + currentInvestment.getPrice() + "\n";
            }

        } else {
            String message = currentInvestment.buy(quantity, price); // If the investment is found in the existing list, just buy it
            return message;
        }

        return "";  
    }


    /**
     * Sells a specified quantity of an investment at a given price.
     * Iterates through the investments arraylist to find the investment object
     * with the given symbol. If the investment is found, the sell method is
     * called to update the quantity and book value of the investment.
     * If the investment is not found, an error message is printed.
     * 
     * @param symbol the symbol of the investment
     * @param quantity the number of units to sell
     * @param price the price per unit of the investment
     * @return a string indicating the success of the sale
     */
    public String sell(String symbol, int quantity, double price) {

        boolean found = false;

        // Iterating through the investments arraylist to find the investment object with the given symbol
        for (int i = 0; i < investments.size(); i++) {
            Investment currentInvestment = investments.get(i);

            //Checking if the investment is a stock or a mutual fund and selling it accordingly
            if (currentInvestment.getSymbol().equalsIgnoreCase(symbol) && currentInvestment instanceof Stock) {
                String message = ((Stock) currentInvestment).sell(investments, symbol, quantity, price);
                found = true;

                return message;

            } else if (currentInvestment.getSymbol().equalsIgnoreCase(symbol) && currentInvestment instanceof MutualFund) {
                String message = ((MutualFund) currentInvestment).sell(investments, symbol, quantity, price);
                found = true;

                return message;
            }
        }

        //Printing error message if the investment was not found
        if (found == false) {
            return "No investment found with the symbol: (" + symbol + ")";
        }

        return "";
    }


    /**
     * Updates the price of the investment with the given symbol and name to the given price.
     * Iterates through the investments arraylist and updates the price for all existing objects
     * with the given symbol and name.
     * Returns a string indicating the success of the update and the updated details of the investment.
     * If the investment is not found, an empty string is returned.
     *
     * @param Symbol the symbol of the investment to update
     * @param price the new price to set for the investment
     * @param name the name of the investment to update
     * @return a string indicating the success of the update
     */
    public String update(String Symbol, Double price, String name) {

        //Iterating through all the investments int the arraylist and update price for all existing objects
        for (int i = 0; i < investments.size(); i++) {
            Investment currentInvestment = investments.get(i);

            if (currentInvestment.getSymbol().equalsIgnoreCase(Symbol) && currentInvestment.getName().equalsIgnoreCase(name)) {
                currentInvestment.updatePrice(price);
                return "The following investment price has been updated successfully:" + "\n\n" +
                        "Symbol: " + currentInvestment.getSymbol() + "\n" +
                        "Name: " + currentInvestment.getName() + "\n" +
                        "Price: " + currentInvestment.getPrice() + "\n";
            }

        }
        return " ";
    }

    /**
     * Calculates the total gain from all investments.
     * 
     * This method iterates through the investments arraylist and calls the `calculateGain` method of each investment.
     * The sum of all gains is returned as a string.
     * 
     * @return a string representing the total gain from all investments, in dollars
     */
    public String calculateGain() {
        double sum = 0;

        // Iterating through the investments arraylist and accumulating total gain
        for (int i = 0; i < investments.size(); i++) {
            Investment currentInvestment = investments.get(i);
            sum = sum + currentInvestment.calculateGain();
        }

        return "$" + sum;
    }

    /**
     * Calculates the gain for each investment in the portfolio and returns a string array
     * containing the gain for each investment. The gain for each investment is the difference
     * between the current value of the investment and its book value. It iterates through the
     * list of investments, calls the calculateGain method on each investment object, and
     * accumulates the gain. Finally, it returns a string array with the gain for each investment.
     *
     * @return a string array containing the gain for each investment
     */
    public String[] calculateIndividualInvestmentGain() {
        String[] result = new String[investments.size()];

        //Iterating through the investments arraylist and accumulating total gain
        for (int i = 0; i < investments.size(); i++) {
            Investment currentInvestment = investments.get(i);
            double sum = currentInvestment.calculateGain();
            result[i] = "Symbol: " + currentInvestment.getSymbol() + "\n" +
                    "Name: " + currentInvestment.getName() + "\n" +
                    "Gain: $" + sum + "\n";
        }

        return result;
    }


    /**
     * Searches for investments that match the given criteria and returns an array of strings
     * containing the details of the matching investments. The search criteria includes the symbol
     * of the investment, the price range, and keywords in the name of the investment. If no
     * matching investments are found, a message is displayed indicating that no investments were
     * found.
     * 
     * @param symbol the symbol of the investment to search for
     * @param minPrice the minimum price of the investment
     * @param maxPrice the maximum price of the investment
     * @param keywords the keywords to search for in the name of the investment
     * @return an array of strings containing the details of the matching investments
     */
    public String[] searchOperation(String symbol, String minPrice, String maxPrice, String keywords) {
        List<String> resultList = new ArrayList<>(); //Use a List to collect the results
        myHashMap.clear(); //Clearing the HashMap

        // Iterating through the investments arraylist and adding words to the HashMap
        for (int i = 0; i < investments.size(); i++) {

            String symbolAndName = (investments.get(i).getSymbol() + " " + investments.get(i).getName()).toLowerCase(); //Combining symbol and name
            String[] parts = symbolAndName.split("\\s+"); //Splitting the combined string based on spaces

            for (int j = 0; j < parts.length; j++) {
                String part = parts[j];
                myHashMap.computeIfAbsent(part, k -> new ArrayList<>()).add(i); //Mapping the word to its list of indexes
            }
        }

        //Getting input from the user for the search operation
        String searchSymbol = symbol.trim();

        String priceInput = minPrice + "-" + maxPrice;

        //initializing the lower and upper price
        double lowerPrice = 0;
        double upperPrice = Double.MAX_VALUE;

        //Processing user input for price
        if (priceInput.length() > 1) {
            String[] prices = priceInput.split("-");
            lowerPrice = parseToDouble(prices[0].trim(), 0);

            if (prices.length > 1) {
                upperPrice = parseToDouble(prices[1].trim(), Double.MAX_VALUE);
            } else {
                upperPrice = Double.MAX_VALUE;
            }
        }

        keywords = keywords.trim();

        List<Integer> filteredIndexes = new ArrayList<>(); //ArrayList for storing filtered indexes

        if (keywords.isEmpty() == false) {

            String[] words = keywords.toLowerCase().split("\\s+");
            Set<Integer> matchedIndexes = new HashSet<>(); //Creating a set for storing matched indexes
            boolean isFirst = true;

            //Iterating through the words in order to get the list of indexes
            for (int i = 0; i < words.length; i++) {

                String word = words[i];
                List<Integer> positions = myHashMap.get(word); // Getting the list of indexes for the word from the HashMap

                if (positions == null) { //Case where the word is not found
                    filteredIndexes.clear();
                    break;
                } else if (isFirst == true) { //Case where the first valid keyword is found
                    matchedIndexes.addAll(positions);
                    isFirst = false;
                } else { //Case where the first valid keyword is not found
                    matchedIndexes.retainAll(positions);
                }
            }
            filteredIndexes.addAll(matchedIndexes); //Adding the matched indexes to the filteredIndexes

        } else {
            //Case where no keywords are provided by the user
            for (int i = 0; i < investments.size(); i++) {
                filteredIndexes.add(i);
            }
        }

        boolean matchFound = false;  //Flag to indicate if a match is found not not

        // Iterating through the filtered indexes to find matching investments
        for (int i = 0; i < filteredIndexes.size(); i++) {

            int j = filteredIndexes.get(i);
            Investment currentInvestment = investments.get(j);
            boolean matchesSymbol = (searchSymbol.isEmpty() || currentInvestment.getSymbol().equalsIgnoreCase(searchSymbol)); // Checking if the current investment matches the symbol given by the user
            boolean withinPriceRange = (lowerPrice == 0.0 && upperPrice == 0.0) || (currentInvestment.getPrice() >= lowerPrice && currentInvestment.getPrice() <= upperPrice); // Checking if the current investment is within the given price range

            //Print investment details if it matches the given criteria
            if (matchesSymbol == true && withinPriceRange == true) {

                resultList.add(currentInvestment.printDetails());
                matchFound = true;
            }
        }

        if (matchFound == false) { //Case where no matching investments are found
            resultList.add("No investments found matching the given criteria.");
        }

        return resultList.toArray(new String[0]);
    }


/**
 * Parses the given string as a double. If parsing fails due to an invalid format,
 * the specified fallback value is returned.
 *
 * @param value the string to be parsed as a double
 * @param fallback the value to return if parsing fails
 * @return the parsed double value, or the fallback value if parsing fails
 */
    private double parseToDouble(String value, double fallback) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

}
