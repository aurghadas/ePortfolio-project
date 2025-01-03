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

