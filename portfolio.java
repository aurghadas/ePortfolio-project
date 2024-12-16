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
