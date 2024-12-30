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


/**
     * Buys additional quantities of the stock at a specified price.
     * If the user chooses to buy the first stock, the book value is initialized
     * based on the quantity and price provided, including the commission fee.
     * If the user chooses to buy more shares of the same stock at the same price,
     * the book value is updated by adding the new purchase cost to the existing
     * book value. If the user chooses to buy more shares at a different price,
     * the book value is updated by adding the new purchase cost to the existing
     * book value and the price is updated to the new price.
     * 
     * @param quantity the number of shares to buy
     * @param price the price per share to use for the purchase
     * @return a string indicating the success of the purchase and the updated details of the stock
     */
    @Override
    public String buy(int quantity, double price){
        this.updatePrice(price); //Updating the price first
        this.updateQuantity(quantity);  //Updating the quantity

        if(this.quantity == 0){  //Case when buying the first stock
            this.bookValue = (quantity * price) + comission;
        }else{
            if(this.price == price){  //Case when updated price is same as before
                this.bookValue += (quantity * price + comission);
                return "Same price! Adding to the book value for the following stock:" + "\n\n" +
                        "Symbol: " + this.getSymbol() + "\n" +
                        "Name: " + this.getName() + "\n" +
                        "Quantity: " + this.getQuantity() + "\n" +
                        "Price: " + this.getPrice() + "\n" +
                        "Book Value: " + this.getBookValue();
            }else{  //Case when updated price is new
                this.bookValue = this.bookValue + (quantity * price + comission);
                this.price = price;
                return "New price! Updating the price and adjusting book value for the following stock:" + "\n\n" +
                        "Symbol: " + this.getSymbol() + "\n" +
                        "Name: " + this.getName() + "\n" +
                        "Quantity: " + this.getQuantity() + "\n" +
                        "Price: " + this.getPrice() + "\n" +
                        "Book Value: " + this.getBookValue();
            }
        }
        this.quantity += quantity;  //Updating the quantity according to the purchase amount
        return "Purchased successfully!";
    }


/**
     * Sells a portion of the investment.
     * 
     * This method updates the price of the investment, calculates the payment, and removes the investment from the list if the entire quantity is sold.
     * 
     * @param investments the list of investments to remove from if the entire quantity is sold
     * @param Symbol the symbol of the investment to sell
     * @param quantity the number of shares to sell
     * @param price the price at which to sell
     * @return a string indicating the result of the sale, including the payment received and the updated investment details
     */
    @Override
    public String sell(ArrayList<Investment> investments, String Symbol, int quantity, double price){
        
        this.updatePrice(price);  //Updating the price

        if(this.getQuantity() == quantity){  //Case when updated price is same as before
            double payment = (this.price * this.quantity) - comission;

            investments.remove(this);  //Removing the stock from the list if user chooses to sell total
            return "Payment received: " + payment + "\n" +     
                    "This stock is sold and removed from our list:" + "\n" +
                    "Symbol: " + Symbol + "\n" +
                    "Name: " + this.getName() + "\n" +
                    "Quantity: " + this.getQuantity() + "\n" +
                    "Price: " + this.getPrice() + "\n" +
                    "Book value: " + this.getBookValue() + "\n";
        } else if(this.getQuantity() > quantity){

            double bookValueNew = (quantity * price) + comission;  //Calculating the new book value using the given formula
            double payment = bookValueNew - comission;  //Calculating the payment using the given formula

            int remainingQuantity = this.quantity - quantity;  //Calculating the remaining quantity
            double bookValueRemaining = ((this.getQuantity() * this.getPrice()) + comission) - bookValueNew;

            //Updating the quantity and book value
            this.quantity = remainingQuantity;
            this.bookValue = bookValueRemaining;
            return "Payment received for selling " + quantity + " shares: " + payment + "\n" + "For the following stock:" + "\n\n" +
                    "Symbol: " + Symbol + "\n" +
                    "Name: " + this.getName() + "\n" +
                    "Remaining quantity: " + this.getQuantity() + "\n" +
                    "Remaining book value: " + this.getBookValue() + "\n";
        }else{
            return "Opps! You don't have enough shares to sell. Please try again.";
        }

    }

    /**
     * Calculates the gain for the stock and returns it. The gain is the difference
     * between the payment received after selling the stock and the book value of the
     * stock. The payment received is the current price of the stock multiplied by the
     * quantity of the stock, minus the commission fee. The book value is the total
     * value of the stock at the time of purchase.
     * @return the gain for the stock
     */
    @Override
    public double calculateGain(){
        double gain = ((this.getPrice() * this.quantity) + comission) - ((this.getPreviousPrice() * this.quantity) + comission) - comission;  //Calculating the payment using the given formula
        return gain;
    }

    
}

