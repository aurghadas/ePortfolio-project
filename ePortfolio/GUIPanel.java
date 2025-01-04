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

