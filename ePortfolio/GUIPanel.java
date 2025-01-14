import javax.swing.*;
import java.awt.*;

/**
 * This class represents a GUI Panel.
 * It is used to display and organize GUI components.
 * It provides methods for adding and removing components, as well as for layout and styling.
 */
public class GUIPanel {

    /**
     * Displays the GUI for the user to interact with the portfolio.
     * It sets the size of the frame, the menu bar, the default close operation, and makes the frame visible.
     */
    public void displayGUI(){
        ePortfolioFrame.setJMenuBar(optionsBar);
        ePortfolioFrame.setSize(1000, 600);
        ePortfolioFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        greetingPanel();
        ePortfolioFrame.setVisible(true);
    }

    private Portfolio portfolio = null;
    private String fileName = null;

    /**
     * Constructs a new GUIPanel object with a default portfolio.
     * 
     * The default portfolio is created internally and is empty.
     */
    public GUIPanel() {
        portfolio = new Portfolio();
    }

    /**
     * Constructs a new GUIPanel object with a portfolio and file name.
     * 
     * @param portfolio the portfolio to be associated with this GUIPanel
     * @param fileName the file name to be associated with this GUIPanel
     */
    public GUIPanel(Portfolio portfolio, String fileName) {
        this.portfolio = portfolio;
        this.fileName = fileName;
    }

    private JFrame ePortfolioFrame = new JFrame("ePortfolio");
    private JMenuBar optionsBar = addOptionsToBar();
    private int Index = 0;

    /**
     * Creates the menu bar with all the options for the user to interact with the portfolio.
     * @return the JMenuBar with all the options
     */
    private JMenuBar addOptionsToBar() {
        JMenuBar optionsBar = new JMenuBar();

        JMenu optionsMenu = new JMenu("Commands");
        JMenuItem buyingInvestment = new JMenuItem("Buying an investment");
        buyingInvestment.addActionListener(e -> buyNewInvestment());
        JMenuItem sellingInvestment = new JMenuItem("Selling an investment");
        sellingInvestment.addActionListener(e -> sellExistingInvestment());
        JMenuItem updatingInvestment = new JMenuItem("Updating investments");
        updatingInvestment.addActionListener(e -> updateInvestments());
        JMenuItem totalGain = new JMenuItem("Getting total gain");
        totalGain.addActionListener(e -> getTotalGain());
        JMenuItem searchingInvestments = new JMenuItem("Searching Investments");
        searchingInvestments.addActionListener(e -> searchInvestments());
        JMenuItem quit = new JMenuItem("Quit");
        quit.addActionListener(e -> quitCommand());

        optionsMenu.add(buyingInvestment);
        optionsMenu.add(sellingInvestment);
        optionsMenu.add(updatingInvestment);
        optionsMenu.add(totalGain);
        optionsMenu.add(searchingInvestments);
        optionsMenu.add(quit);
        optionsBar.add(optionsMenu);

        return optionsBar;
    }

/**
 * Displays a GUI form for buying a new investment. The form allows the user to 
 * input the type of investment (Stock or MutualFund), along with details such 
 * as symbol, name, quantity, and price. It includes a reset button to clear 
 * inputs and a buy button to attempt purchasing the investment. The buy 
 * operation validates inputs and displays messages for errors or success. 
 * The form is updated within the main application frame.
 */
    private void buyNewInvestment() {
        SwingUtilities.invokeLater(() -> {
            JPanel registerPanel = new JPanel(new BorderLayout());

            JPanel form = new JPanel(new GridBagLayout());
            GridBagConstraints constraints = new GridBagConstraints();
            registerPanel.add(form, BorderLayout.CENTER);

            //Constraints for components
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.anchor = GridBagConstraints.NORTH;
            constraints.insets = new Insets(5, 5, 5, 5); //Add some margin

            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.gridwidth = 2;
            form.add(new JLabel("Buying an investment"), constraints);
            //Type Label and JComboBox
            constraints.gridx = 0;
            constraints.gridy = 1;
            constraints.gridwidth = 1;
            form.add(new JLabel("Type:"), constraints);

            constraints.gridx = 1;
            constraints.gridy = 1;
            String[] investmentTypes = { "Stock", "Mutualfund" }; //Contains two types of investments
            JComboBox<String> typeInput = new JComboBox<>(investmentTypes);
            typeInput.setSelectedIndex(0); //Default to "stock"
            form.add(typeInput, constraints);

            //Symbol Label and Text Field
            constraints.gridx = 0;
            constraints.gridy = 2;
            form.add(new JLabel("Symbol:"), constraints);

            constraints.gridx = 1;
            constraints.gridy = 2;
            JTextField symbolInput = new JTextField(15);
            form.add(symbolInput, constraints);

            //Name Label and Text Field
            constraints.gridx = 0;
            constraints.gridy = 3;
            form.add(new JLabel("Name:"), constraints);

            constraints.gridx = 1;
            constraints.gridy = 3;
            JTextField nameInput = new JTextField(15);
            form.add(nameInput, constraints);

            //Quantity Label and Text Field
            constraints.gridx = 0;
            constraints.gridy = 4;
            form.add(new JLabel("Quantity:"), constraints);

            constraints.gridx = 1;
            constraints.gridy = 4;
            JTextField quantityInput = new JTextField(15);
            form.add(quantityInput, constraints);

            //Price Label and Text Field
            constraints.gridx = 0;
            constraints.gridy = 5;
            form.add(new JLabel("Price:"), constraints);

            constraints.gridx = 1;
            constraints.gridy = 5;
            JTextField priceInput = new JTextField(15);
            form.add(priceInput, constraints);

            //Reset Button setup
            constraints.gridx = 8;
            constraints.gridy = 2;
            JButton resetButton = new JButton("Reset");
            form.add(resetButton, constraints);

            //Buy Button setup
            constraints.gridx = 8;
            constraints.gridy = 4;
            JButton buyButton = new JButton("Buy");
            form.add(buyButton, constraints);

            //Panel for messages with a label on top
            JPanel messagesPanel = new JPanel(new BorderLayout());
            JLabel messagesLabel = new JLabel("Messages:");
            messagesPanel.add(messagesLabel, BorderLayout.NORTH);

            JTextArea registerMessages = new JTextArea(10, 20);
            registerMessages.setEditable(false);
            messagesPanel.add(new JScrollPane(registerMessages), BorderLayout.CENTER);
            registerPanel.add(messagesPanel, BorderLayout.SOUTH);

            //Reset button action
            resetButton.addActionListener(e -> {
                symbolInput.setText("");
                nameInput.setText("");
                quantityInput.setText("");
                priceInput.setText("");
                registerMessages.setText("");
            });

            //Buy button action 
            buyButton.addActionListener(e -> {

                try {
                    String type = typeInput.getSelectedItem().toString();
                    String symbol = symbolInput.getText().trim();
                    String name = nameInput.getText().trim();
                    String quantityString = quantityInput.getText().trim();
                    String priceString = priceInput.getText().trim();
                    int quantity;
                    double price;

                    //Check if any fields are empty
                    if (type.isEmpty() || symbol.isEmpty() || name.isEmpty() || quantityString.isEmpty()
                            || priceString.isEmpty()) {
                        registerMessages.append("Error: Empty fields detected. Please fill in all fields.\n");
                        return;
                    }

                    try {
                        quantity = Integer.parseInt(quantityInput.getText().trim());
                        if (quantity <= 0) {
                            throw new IllegalArgumentException("Quantity must be positive.");
                        }
                    } catch (NumberFormatException ex) {
                        throw new IllegalArgumentException("Invalid quantity: Must be a valid number.");
                    }

                    try {
                        price = Double.parseDouble(priceInput.getText().trim());
                        if (price <= 0) {
                            throw new IllegalArgumentException("Price must be positive.");
                        }
                    } catch (NumberFormatException ex) {
                        throw new IllegalArgumentException("Invalid price: Must be a valid number.");
                    }

                    if (symbol.isEmpty() || name.isEmpty()) {
                        registerMessages.append("Symbol and Name cannot be empty.\n");
                        return;
                    }

                    String message = portfolio.buy(type, symbol, name, quantity, price);
                    registerMessages.append(message + "\n");

                } catch (IllegalArgumentException ex) {
                    registerMessages.append("Input Error: " + ex.getMessage() + "\n");
                } catch (Exception ex) {
                    registerMessages.append(ex.getMessage() + "\n");
                }

            });

            //Update the main frame
            ePortfolioFrame.getContentPane().removeAll();
            ePortfolioFrame.getContentPane().add(registerPanel);
            ePortfolioFrame.revalidate();
            ePortfolioFrame.repaint();
        });
    }

/**
         * Prompts the user to enter the symbol of the investment and the quantity to sell, and then calls the
         * sell method on the portfolio object. If the investment is not found, prints an error message.
         * If the quantity entered is more than the quantity available, prints an error message.
         * If selling is successful, prints a success message.
         */
    private void sellExistingInvestment() {
        SwingUtilities.invokeLater(() -> {
            JPanel registerPanel = new JPanel(new BorderLayout());

            JPanel form = new JPanel(new GridBagLayout());
            GridBagConstraints constraints = new GridBagConstraints();
            registerPanel.add(form, BorderLayout.CENTER);

            //Constraints for components
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.anchor = GridBagConstraints.NORTH;
            constraints.insets = new Insets(5, 5, 5, 5); // Add some margin

            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.gridwidth = 2;
            form.add(new JLabel("Selling an investment"), constraints);
            //Symbol Label and Text Field
            constraints.gridx = 0;
            constraints.gridy = 1;
            constraints.gridwidth = 1;
            form.add(new JLabel("Symbol:"), constraints);

            constraints.gridx = 1;
            constraints.gridy = 1;
            JTextField symbolInput = new JTextField(15);
            form.add(symbolInput, constraints);

            //Quantity Label and Text Field
            constraints.gridx = 0;
            constraints.gridy = 2;
            form.add(new JLabel("Quantity:"), constraints);

            constraints.gridx = 1;
            constraints.gridy = 2;
            JTextField quantityInput = new JTextField(15);
            form.add(quantityInput, constraints);

            //Price Label and Text Field
            constraints.gridx = 0;
            constraints.gridy = 3;
            form.add(new JLabel("Price:"), constraints);

            constraints.gridx = 1;
            constraints.gridy = 3;
            JTextField priceInput = new JTextField(15);
            form.add(priceInput, constraints);

            //Reset Button setup
            constraints.gridx = 4;
            constraints.gridy = 1;
            JButton resetButton = new JButton("Reset");
            form.add(resetButton, constraints);

            //Sell Button setup
            constraints.gridx = 4;
            constraints.gridy = 2;
            JButton sellButton = new JButton("Sell");
            form.add(sellButton, constraints);

            //Panel for messages with a label on top
            JPanel messagesPanel = new JPanel(new BorderLayout());
            JLabel messagesLabel = new JLabel("Messages:");
            messagesPanel.add(messagesLabel, BorderLayout.NORTH);

            JTextArea registerMessages = new JTextArea(10, 20);
            registerMessages.setEditable(false);
            messagesPanel.add(new JScrollPane(registerMessages), BorderLayout.CENTER);
            registerPanel.add(messagesPanel, BorderLayout.SOUTH);

            //Reset button action
            resetButton.addActionListener(e -> {
                symbolInput.setText("");
                quantityInput.setText("");
                priceInput.setText("");
                registerMessages.setText("");
            });

            //Sell button action
            sellButton.addActionListener(e -> {

                try {
                    String symbol = symbolInput.getText().trim();
                    String quantityString = quantityInput.getText().trim();
                    String priceString = priceInput.getText().trim();
                    int quantity;
                    double price;

                    //Check if any fields are empty
                    if (symbol.isEmpty() || quantityString.isEmpty() || priceString.isEmpty()) {
                        registerMessages.append("Error: Empty fields detected. Please fill in all fields.\n");
                        return;
                    }

                    try {
                        quantity = Integer.parseInt(quantityInput.getText().trim());
                        if (quantity <= 0) {
                            throw new IllegalArgumentException("Quantity must be positive.");
                        }
                    } catch (NumberFormatException ex) {
                        throw new IllegalArgumentException("Invalid quantity: Must be a valid number.");
                    }

                    try {
                        price = Double.parseDouble(priceInput.getText().trim());
                        if (price <= 0) {
                            throw new IllegalArgumentException("Price must be positive.");
                        }
                    } catch (NumberFormatException ex) {
                        throw new IllegalArgumentException("Invalid price: Must be a valid number.");
                    }

                    if (symbol.isEmpty()) {
                        registerMessages.append("Symbol cannot be empty.\n");
                        return;
                    }

                    String message = portfolio.sell(symbol, quantity, price);
                    registerMessages.append(message + "\n");
                } catch (IllegalArgumentException ex) {
                    registerMessages.append("Input Error: " + ex.getMessage() + "\n");
                } catch (Exception ex) {
                    registerMessages.append(ex.getMessage() + "\n");
                }

            });

            //Update the main frame
            ePortfolioFrame.getContentPane().removeAll();
            ePortfolioFrame.getContentPane().add(registerPanel);
            ePortfolioFrame.revalidate();
            ePortfolioFrame.repaint();
        });
    }

/**
 * Displays a GUI form for updating an investment. The form shows the symbol and name of the investment at the current
 * index, and allows the user to input a new price and name. The form includes previous and next buttons to navigate
 * through the list of investments, and a save button to apply the changes. If the user attempts to go out of bounds of
 * the list, the buttons will be disabled. The form is updated within the main application frame.
 */
    private void updateInvestments() {
        SwingUtilities.invokeLater(() -> {
            JPanel registerPanel = new JPanel(new BorderLayout());

            JPanel form = new JPanel(new GridBagLayout());
            GridBagConstraints constraints = new GridBagConstraints();
            registerPanel.add(form, BorderLayout.CENTER);

            //Constraints for components
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.anchor = GridBagConstraints.NORTH;
            constraints.insets = new Insets(5, 5, 5, 5); // Add some margin

            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.gridwidth = 2;
            form.add(new JLabel("Updating investments"), constraints);
            //Symbol Label and Text Field
            constraints.gridx = 0;
            constraints.gridy = 1;
            constraints.gridwidth = 1;
            form.add(new JLabel("Symbol:"), constraints);

            constraints.gridx = 1;
            constraints.gridy = 1;
            JTextField symbolInput = new JTextField(15);
            symbolInput.setEditable(false);
            //Check to see if there are any investments then set the text field
            if (portfolio.investments.size() > 0){
                symbolInput.setText(portfolio.investments.get(Index).getSymbol());
            }
            form.add(symbolInput, constraints);

            //Name Label and Text Field
            constraints.gridx = 0;
            constraints.gridy = 2;
            form.add(new JLabel("Name:"), constraints);

            constraints.gridx = 1;
            constraints.gridy = 2;
            JTextField nameInput = new JTextField(15);
            nameInput.setEditable(false);
            //Check to see if there are any investments then set the text field
            if (portfolio.investments.size() > 0) {
                nameInput.setText(portfolio.investments.get(Index).getName());
            }
            form.add(nameInput, constraints);

            //Price Label and Text Field
            constraints.gridx = 0;
            constraints.gridy = 3;
            form.add(new JLabel("Price:"), constraints);

            constraints.gridx = 1;
            constraints.gridy = 3;
            JTextField priceInput = new JTextField(15);
            form.add(priceInput, constraints);

            //Previous Button setup
            constraints.gridx = 4;
            constraints.gridy = 1;
            JButton previousButton = new JButton("Prev");
            //Check to determine if the previous button should be enabled or not
            if (Index == 0) {
                previousButton.setEnabled(false);
            } else {
                previousButton.setEnabled(true);
            }

            form.add(previousButton, constraints);

            //Next Button set up
            constraints.gridx = 4;
            constraints.gridy = 2;
            JButton nextButton = new JButton("Next");
            if (portfolio.investments.size() > 1) {
                nextButton.setEnabled(true);
            }
            form.add(nextButton, constraints);

            //Save Button setup
            constraints.gridx = 4;
            constraints.gridy = 3;
            JButton saveButton = new JButton("Save");
            form.add(saveButton, constraints);

            //Panel for messages with a label on top
            JPanel messagesPanel = new JPanel(new BorderLayout());
            JLabel messagesLabel = new JLabel("Messages:");
            messagesPanel.add(messagesLabel, BorderLayout.NORTH);

            JTextArea registerMessages = new JTextArea(10, 20);
            registerMessages.setEditable(false);
            messagesPanel.add(new JScrollPane(registerMessages), BorderLayout.CENTER);
            registerPanel.add(messagesPanel, BorderLayout.SOUTH);

            //Previous button action
            previousButton.addActionListener(e -> {
                if (portfolio.investments.size() > 0) {
                    Index = Math.max(0, Index - 1); // Decrease index but ensure it doesn't go below 0
                    symbolInput.setText(portfolio.investments.get(Index).getSymbol());
                    nameInput.setText(portfolio.investments.get(Index).getName());

                    // Enable/disable buttons based on updated index
                    previousButton.setEnabled(Index > 0);
                    nextButton.setEnabled(Index < portfolio.investments.size() - 1);
                }
            });

            //Next button action
            nextButton.addActionListener(e -> {
                if (portfolio.investments.size() > 0) {
                    Index = Math.min(portfolio.investments.size() - 1, Index + 1); //Increasing the index but ensure it doesn't go above the last index
                    symbolInput.setText(portfolio.investments.get(Index).getSymbol());
                    nameInput.setText(portfolio.investments.get(Index).getName());

                    //Enable/disable buttons based on updated index
                    previousButton.setEnabled(Index > 0);
                    nextButton.setEnabled(Index < portfolio.investments.size() - 1);
                }
            });

            //Save button action
            saveButton.addActionListener(e -> {
                if (portfolio.investments.size() == 0) {
                    messagesLabel.setText("You have no investments to update.");
                    return;
                }

                try{
                    String symbol = symbolInput.getText().trim();
                    String name = nameInput.getText().trim();
                    double price;

                    try {
                        price = Double.parseDouble(priceInput.getText().trim());
                        if (price <= 0) {
                            throw new IllegalArgumentException("Price must be positive.");
                        }
                    } catch (NumberFormatException ex) {
                        throw new IllegalArgumentException("Invalid price: Must be a valid number.");
                    }
                    

                    String result = portfolio.update(symbol, price, name);
                    registerMessages.append(result + "\n");
                } catch (IllegalArgumentException ex) {
                    registerMessages.append("Error: " + ex.getMessage() + "\n");
                }catch (Exception ex) {
                    registerMessages.append(ex.getMessage() + "\n");
                }   
                
            });

            //Update the main frame
            ePortfolioFrame.getContentPane().removeAll();
            ePortfolioFrame.getContentPane().add(registerPanel);
            ePortfolioFrame.revalidate();
            ePortfolioFrame.repaint();
        });
    }

/**
     * Displays a GUI form for getting the total gain of all investments in the
     * portfolio. The form includes a label and text field for the total gain, and a
     * panel for displaying individual gains for each investment. Individual gains
     * are displayed in a text area with a label on top. The form is updated within
     * the main application frame.
     */
    private void getTotalGain() {
        SwingUtilities.invokeLater(() -> {
            JPanel registerPanel = new JPanel(new BorderLayout());

            JPanel form = new JPanel(new GridBagLayout());
            GridBagConstraints constraints = new GridBagConstraints();
            registerPanel.add(form, BorderLayout.CENTER);

            //Constraints for components
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.anchor = GridBagConstraints.NORTH;
            constraints.insets = new Insets(5, 5, 5, 5); 

            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.gridwidth = 2;
            form.add(new JLabel("Getting total gain"), constraints);
            //Total Gain Label and Text Field
            constraints.gridx = 0;
            constraints.gridy = 1;
            constraints.gridwidth = 1;
            form.add(new JLabel("Total gain:"), constraints);

            constraints.gridx = 1;
            constraints.gridy = 1;
            JTextField totalGain = new JTextField(15);
            totalGain.setEditable(false); 
            form.add(totalGain, constraints);
            totalGain.setText(portfolio.calculateGain());

            //Panel for individual gains with a label on top
            JPanel messagesPanel = new JPanel(new BorderLayout());
            JLabel messagesLabel = new JLabel("Individual gains:");
            messagesPanel.add(messagesLabel, BorderLayout.NORTH);

            JTextArea registerMessages = new JTextArea(20, 20);
            registerMessages.setEditable(false);
            String[] individualGains = portfolio.calculateIndividualInvestmentGain();
            //Loop to display individual gains
            for (int i = 0; i < individualGains.length; i++) {
                registerMessages.append(individualGains[i] + "\n");
            }
            messagesPanel.add(new JScrollPane(registerMessages), BorderLayout.CENTER);
            registerPanel.add(messagesPanel, BorderLayout.SOUTH);

            //Update the main frame
            ePortfolioFrame.getContentPane().removeAll();
            ePortfolioFrame.getContentPane().add(registerPanel);
            ePortfolioFrame.revalidate();
            ePortfolioFrame.repaint();
        });
    }

        /**
         * Displays a GUI form for searching investments in the portfolio. The form
         * includes text fields for symbol, name keywords, low price, and high price.
         * The user can enter any of these fields to search for the relevant
         * investments. The search results are displayed in a text area with a label
         * on top. The form is updated within the main application frame.
         */
    private void searchInvestments() {
        SwingUtilities.invokeLater(() -> {
            JPanel registerPanel = new JPanel(new BorderLayout());

            JPanel form = new JPanel(new GridBagLayout());
            GridBagConstraints constraints = new GridBagConstraints();
            registerPanel.add(form, BorderLayout.CENTER);

            //Constraints for components
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.anchor = GridBagConstraints.NORTH;
            constraints.insets = new Insets(5, 5, 5, 5); // Add some margin

            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.gridwidth = 2;
            form.add(new JLabel("Searching investments"), constraints);
            //Symbol Label and Text Field
            constraints.gridx = 0;
            constraints.gridy = 1;
            constraints.gridwidth = 1;
            form.add(new JLabel("Symbol:"), constraints);

            constraints.gridx = 1;
            constraints.gridy = 1;
            JTextField symbolInput = new JTextField(15);
            form.add(symbolInput, constraints);

            //Name Keywords Label and Text Field
            constraints.gridx = 0;
            constraints.gridy = 2;
            form.add(new JLabel("Name keywords:"), constraints);

            constraints.gridx = 1;
            constraints.gridy = 2;
            JTextField nameKeywordsInput = new JTextField(15);
            form.add(nameKeywordsInput, constraints);

            //Low Price Label and Text Field
            constraints.gridx = 0;
            constraints.gridy = 3;
            form.add(new JLabel("Low price:"), constraints);

            constraints.gridx = 1;
            constraints.gridy = 3;
            JTextField lowPriceInput = new JTextField(15);
            form.add(lowPriceInput, constraints);

            //High Price Label and Text Field
            constraints.gridx = 0;
            constraints.gridy = 4;
            form.add(new JLabel("High price:"), constraints);

            constraints.gridx = 1;
            constraints.gridy = 4;
            JTextField highPriceInput = new JTextField(15);
            form.add(highPriceInput, constraints);

            //Reset Button setup
            constraints.gridx = 4;
            constraints.gridy = 1;
            JButton resetButton = new JButton("Reset");
            form.add(resetButton, constraints);

            //Search Button setup
            constraints.gridx = 4;
            constraints.gridy = 3;
            JButton searchButton = new JButton("Search");
            form.add(searchButton, constraints);

            //Panel for Search results with a label on top
            JPanel messagesPanel = new JPanel(new BorderLayout());
            JLabel messagesLabel = new JLabel("Search results:");
            messagesPanel.add(messagesLabel, BorderLayout.NORTH);

            JTextArea registerMessages = new JTextArea(10, 20);
            registerMessages.setEditable(false);
            messagesPanel.add(new JScrollPane(registerMessages), BorderLayout.CENTER);
            registerPanel.add(messagesPanel, BorderLayout.SOUTH);

            //Reset button action
            resetButton.addActionListener(e -> {
                symbolInput.setText("");
                nameKeywordsInput.setText("");
                lowPriceInput.setText("");
                highPriceInput.setText("");
                registerMessages.setText("");
            });

            //Search button action
            searchButton.addActionListener(e -> {
                

                try{
                    String symbol = symbolInput.getText().trim();
                    String nameKeywords = nameKeywordsInput.getText().trim();

                    String lowPrice = lowPriceInput.getText().trim();
                    String highPrice = highPriceInput.getText().trim();

                    if(!lowPrice.isEmpty()){
                        try {
                            double lPrice = Double.parseDouble(lowPriceInput.getText().trim());
                            if (lPrice <= 0) {
                                throw new IllegalArgumentException("Lower price must be positive.");
                            }
                        } catch (NumberFormatException ex) {
                            throw new IllegalArgumentException("Invalid lower price: Must be a valid number.");
                        }
                    }

                    if(!highPrice.isEmpty()){
                        try {
                            double hPrice = Double.parseDouble(highPriceInput.getText().trim());
                            if (hPrice <= 0) {
                                throw new IllegalArgumentException("Higher price must be positive.");
                            }
                        } catch (NumberFormatException ex) {
                            throw new IllegalArgumentException("Invalid higher price: Must be a valid number.");
                        }
                    }
                    
                    
                    String[] results = portfolio.searchOperation(symbol, lowPrice, highPrice, nameKeywords);
                    for (String result : results){
                        registerMessages.append(result + "\n");
                    }
                }catch (IllegalArgumentException ex) {
                    registerMessages.append("Error: " + ex.getMessage() + "\n");
                }catch (Exception ex) {
                    registerMessages.append(ex.getMessage() + "\n");
                }   
                
            });

            //Update the main frame
            ePortfolioFrame.getContentPane().removeAll();
            ePortfolioFrame.getContentPane().add(registerPanel);
            ePortfolioFrame.revalidate();
            ePortfolioFrame.repaint();
        });
    }

/**
 * Exits the application after saving the current state of the portfolio to a file.
 * If a specific file name is provided, the portfolio is saved with that name.
 * Otherwise, it is saved with a default file name "cis2430".
 */
    private void quitCommand() {
        if (fileName != null) {
            portfolio.saveInvestments(fileName);  //Case when filename is provided

        }
        else{
            portfolio.saveInvestments("cis2430");  //Case when filename is not provided
        }
        System.exit(0);
    }

/**
 * Displays a welcome message and provides instructions for using the
 * application. This method is called when the application is first
 * launched.
 */
    private void greetingPanel() {

        JPanel registerPanel = new JPanel();
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        registerPanel.add(form, BorderLayout.CENTER);

        //Constraints for components
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.insets = new Insets(5, 5, 5, 5);

        //Adding labels for the welcome message
        constraints.gridx = 5;
        constraints.gridy = 5;
        constraints.gridwidth = 2;
        form.add(new JLabel("Welcome to ePortfolio.\n"), constraints);

        constraints.gridx = 5;
        constraints.gridy = 10;
        constraints.gridwidth = 2;
        form.add(new JLabel("Choose a command from the \"Commands\" menu to buy or sell"), constraints);

        constraints.gridx = 5;
        constraints.gridy = 11;
        constraints.gridwidth = 2;
        form.add(new JLabel("an investment, update prices for all investments, get gain for the"), constraints);

        constraints.gridx = 5;
        constraints.gridy = 12;
        constraints.gridwidth = 2;
        form.add(new JLabel("portfolio, search for relevant investments, or quit the program."), constraints);


        //Update the main frame
        ePortfolioFrame.getContentPane().removeAll();
        ePortfolioFrame.getContentPane().add(registerPanel);
        ePortfolioFrame.revalidate();
        ePortfolioFrame.repaint();
    }

}
