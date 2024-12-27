import java.util.ArrayList;

/**
 * This class holds the attributes which are only applicable to Stocks.
 * It extends the abstract class Investment.
 */
public class Stock extends Investment{
    
    private static final double comission = 9.99;  //Fixed comission fee for stocks

    /**
     * Constructs a new Stock object with specified details.
     *
     * @param symbol the symbol representing the stock
     * @param name the name of the stock
     * @param quantity the number of shares initially purchased
     * @param price the initial price per share
     * @param bookValue the initial book value of the stock
     */
    public Stock(String symbol, String name, int quantity, double price, double bookValue){
        super(symbol, name, quantity, price);
        this.bookValue = bookValue;
    }

    /**
     * Constructs a new Stock object with specified details. The book value is initialized
     * based on the quantity and price provided, including the commission fee.
     *
     * @param symbol the symbol representing the stock
     * @param name the name of the stock
     * @param quantity the number of shares initially purchased
     * @param price the initial price per share
     */
    public Stock(String symbol, String name, int quantity, double price){
        super(symbol, name, quantity, price);
        this.bookValue = (quantity * price) + comission; //Initializing the book value for the initial purchase
    }

    /**
     * Default constructor for Stock, initializes a stock with placeholder values.
     */
    public Stock(){
        super(null, null, 0, 0.0); // Default constructor with placeholder values
    }

    /**
     * Checks if the investment is a stock or not.
     * 
     * @return true if the investment is a stock, false otherwise
     */
    @Override
    public boolean isStock(){
        return true;
    }

