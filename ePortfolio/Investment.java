import java.util.ArrayList;

/**
 * An abstract class Investment which contains the common attributes of stock and mutual fund investments.
 */
public abstract class Investment {
    /**
     * The symbol of the investment.
     */
    protected String symbol; 

    /**
     * The name of the investment.
     */
    protected String name; 
    /**
     * The quantity of the investment.
     */
    protected int quantity;  
    /**
     * The price per unit of the investment.
     */
    protected double price;  
    /**
     * The book value of the investment, which is the total value of the
     * investment at the time of purchase.
     */
    protected double bookValue;  

    /**
     * The previous price of the investment.
     */
    protected double previousPrice;

    /**
     * Constructs a new Investment object with specified details.
     *
     * @param symbol the stock symbol of the investment
     * @param name the name of the investment
     * @param quantity the quantity of the investment
     * @param price the price per unit of the investment
     */
    public Investment(String symbol, String name, int quantity, double price) {
        this.symbol = symbol;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.previousPrice = price;
    }

    /**
     * Returns the symbol of the investment.
     * @return the symbol of the investment
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Returns the name of the investment.
     * @return the name of the investment
     */
    public String getName() {
        return name;
    }

/**
 * Returns the quantity of the investment.
 * @return the quantity of the investment
 */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Returns the price of the investment.
     * @return the price of the investment
     */
    public double getPrice() {
        return price;
    }

    /**
     * Returns the previous price of the investment.
     * 
     * @return the previous price of the investment
     */
    public double getPreviousPrice() {
        return previousPrice;
    }

    /**
     * Returns the book value of the investment, which is the total value of the
     * investment at the time of purchase.
     * @return the book value of the investment
     */
    public double getBookValue() {
        return bookValue;
    }
  
