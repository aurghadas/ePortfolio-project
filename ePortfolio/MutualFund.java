import java.util.ArrayList;

/**
 * This class holds the attributes which are only applicable to mutual funds.
 * It extends the abstract class Investment.
 */
public class MutualFund extends Investment{

    private static final double redemptionFee = 45.00;  //Fixed redemption fee for mutual funds

    /**
     * Constructs a new MutualFund object with specified details.
     *
     * @param symbol the symbol representing the mutual fund
     * @param name the name of the mutual fund
     * @param quantity the number of shares initially purchased
     * @param price the initial price per share
     * @param bookValue the total value of the mutual fund at the time of purchase
     */
    public MutualFund(String symbol, String name, int quantity, double price, double bookValue){
        super(symbol, name, quantity, price);
        this.bookValue = bookValue;
    }
    
    /**
     * Constructs a new MutualFund object with specified details. The book value is initialized
     * based on the quantity and price provided.
     *
     * @param symbol the symbol representing the mutual fund
     * @param name the name of the mutual fund
     * @param quantity the number of shares initially purchased
     * @param price the initial price per share
     */
    public MutualFund(String symbol, String name, int quantity, double price){
        super(symbol, name, quantity, price);
        this.bookValue = (quantity * price);  //Initializing the book value for the initial purchase
    }

    /**
     * Default constructor for MutualFund, initializes a mutual fund with placeholder values.
     */
    public MutualFund(){
        super(null, null, 0, 0.0);  //Default constructor with placeholder values
    }

    
    /**
     * Returns false as this is a MutualFund and not a Stock
     * @return false
     */
    @Override
    public boolean isStock() {
        return false;
    }
  
    
    /**
     * Purchases a specified quantity of mutual fund at a given price.
     * Updates the mutual fund's price and adjusts the book value accordingly.
     * If the mutual fund is being bought for the first time, initializes the book value to be the
     * product of quantity and price. If the price remains unchanged,
     * the book value is incremented by the new purchase cost.
     * If the price changes, updates the price and adds the new purchase cost to the book value.
     * Finally, increments the mutual fund's quantity by the purchased amount.
     * 
     * @param quantity the number of shares to purchase
     * @param price the price per share to use for the purchase
     * @return a string indicating the success of the purchase and the updated details of the mutual fund
     */
    @Override
    public String buy(int quantity, double price) {
        this.updatePrice(price); //Updating the price first
        this.updateQuantity(quantity);  //Updating the quantity

        if(this.quantity == 0){  //Case when buying the first mutual fund
            this.bookValue = quantity * price;
        }else{
            if(this.price == price){  //Case when updated price is same as before
                this.bookValue = this.bookValue + quantity * price;
                return "Same price! Adding to the book value for the following mutual fund:" + "\n\n" +
                        "Symbol: " + this.getSymbol() + "\n" +
                        "Name: " + this.getName() + "\n" +
                        "Quantity: " + this.getQuantity() + "\n" +
                        "Price: " + this.getPrice() + "\n" +
                        "Book Value: " + this.getBookValue();
            }else{  //Case when updated price is new
                this.bookValue = this.bookValue + quantity * price;
                this.price = price;
                return "New price! Updating the price and adjusting book value for the following mutual fund:" + "\n\n" +
                        "Symbol: " + this.getSymbol() + "\n" +
                        "Name: " + this.getName() + "\n" +
                        "Quantity: " + this.getQuantity() + "\n" +
                        "Price: " + this.getPrice() + "\n" +
                        "Book Value: " + this.getBookValue();
            }
        }
        this.quantity = this.quantity + quantity;  //Updating the quantity according to the purchase amount
        return "Purchased successfully!";
    }


