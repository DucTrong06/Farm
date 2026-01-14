// Main graphical user interface (GUI)
package smartfarm.view;

import smartfarm.controller.FarmController;
import smartfarm.exception.GameException;
import smartfarm.model.*;
import smartfarm.model.plant.*;
import smartfarm.model.weather.Weather;
import smartfarm.util.Constants;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FarmFrame extends JFrame {
    
    private FarmController controller;
    private GameState gameState;
    // Store the position of the currently selected land cell
    private int selectedRow = -1;
    private int selectedCol = -1;

    // UI Components
    private JLabel lblStats;
    private JLabel lblWeather;
    private JPanel gridPanel;
    private JButton[][] cellButtons;
    private JLabel lblCellInfo;
    private JLabel lblResources;
    private JLabel lblMessage;
    
    private JButton btnPlant, btnWater, btnFertilize, btnHarvest, btnTreatPest;
    private JButton btnBuyWater, btnBuyFertilizer, btnBuyPesticide;
    private JButton btnNextDay, btnHelp, btnBackToMenu;

    public FarmFrame() {
        this.gameState = new GameState(Constants.DEFAULT_FARM_ROWS, Constants.DEFAULT_FARM_COLS);
        this.controller = new FarmController(gameState, this);
        
        initComponents();
        setupLayout();
        setupListeners();
        refreshUI();
    }

    private void initComponents() {
        // Set up window properties, create labels, build the land grid (gridPanel), and create buttons
        setTitle("Smart Farm - Farm Management");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        // Game stats labels (Day, Money)
        lblStats = new JLabel("Day 1 | Money: $1000");
        lblStats.setFont(new Font("Arial", Font.BOLD, 16));
        lblStats.setForeground(Color.WHITE);
        
        lblWeather = new JLabel("Sunny");
        lblWeather.setFont(new Font("Arial", Font.BOLD, 16));
        lblWeather.setForeground(Color.WHITE);

        // Create the panel that contains the farm grid
        Farm farm = gameState.getFarm();
        gridPanel = new JPanel(new GridLayout(farm.getRows(), farm.getCols(), 5, 5));
        gridPanel.setBackground(new Color(141, 110, 99));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Initialize the button array and call the grid creation method
        cellButtons = new JButton[farm.getRows()][farm.getCols()];
        createFarmGrid();
        
        lblCellInfo = new JLabel("<html>No cell selected<br>Click a cell to view info</html>");
        lblCellInfo.setFont(new Font("Arial", Font.PLAIN, 12));
        
        lblResources = new JLabel("<html>Water: 100<br>Fertilizer: 60<br>Pesticide: 20</html>");
        lblResources.setFont(new Font("Arial", Font.PLAIN, 12));
        
        lblMessage = new JLabel("<html>Welcome to Smart Farm!<br>Start farming now.</html>");
        lblMessage.setFont(new Font("Arial", Font.PLAIN, 12));
        
        // Create action buttons with distinct colors
        btnPlant = createActionButton("Plant", new Color(139, 195, 74));
        btnWater = createActionButton("Water", new Color(3, 169, 244));
        btnFertilize = createActionButton("Fertilize", new Color(255, 152, 0));
        btnHarvest = createActionButton("Harvest", new Color(76, 175, 80));
        btnTreatPest = createActionButton("Treat Pest", new Color(244, 67, 54));
        
        // Purchase button
        btnBuyWater = createActionButton("Buy Water x10 ($10)", new Color(0, 188, 212));
        btnBuyFertilizer = createActionButton("Buy Fertilizer x10 ($20)", new Color(255, 152, 0));
        btnBuyPesticide = createActionButton("Buy Pesticide x5 ($40)", new Color(244, 67, 54));
        
        btnNextDay = createActionButton("Next Day", new Color(156, 39, 176));
        btnNextDay.setFont(new Font("Arial", Font.PLAIN, 12));
        
        btnHelp = createActionButton("Help", new Color(33, 150, 243));
        btnBackToMenu = createActionButton("Back to Menu", new Color(96, 125, 139));
    }

    private JButton createActionButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFont(new Font("Arial", Font.PLAIN, 12));
        btn.addMouseListener(new MouseAdapter() {
            // Hover color change effect
            public void mouseEntered(MouseEvent e) { btn.setBackground(bgColor.darker()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(bgColor); }
        });
        return btn;
    }

    private void createFarmGrid() {
        Farm farm = gameState.getFarm();
        for (int row = 0; row < farm.getRows(); row++) {
            for (int col = 0; col < farm.getCols(); col++) {
                final int r = row;
                final int c = col;
                JButton cellBtn = new JButton();
                cellBtn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
                cellBtn.setPreferredSize(new Dimension(80, 80));
                cellBtn.setBackground(new Color(141, 110, 99));
                cellBtn.setBorder(new LineBorder(new Color(93, 64, 55), 2));
                cellBtn.setFocusPainted(false);
                cellBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                cellBtn.addActionListener(e -> selectCell(r, c));
                cellButtons[r][c] = cellBtn;
                gridPanel.add(cellBtn);
            }
        }
    }

    private void setupLayout() {
        setLayout(new BorderLayout(0, 0));
        //top
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));
        topPanel.setBackground(new Color(76, 175, 80));
        topPanel.add(lblStats);
        topPanel.add(lblWeather);
        
        //center
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(232, 245, 233));
        centerPanel.add(gridPanel, BorderLayout.CENTER);
        
        //right
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(new Color(255, 249, 196));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        rightPanel.setPreferredSize(new Dimension(280, 0));
        addRightPanelComponents(rightPanel);
        
        //bottom
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(new Color(255, 245, 157));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bottomPanel.add(lblMessage);
        
        // Add everything to the Frame
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void addRightPanelComponents(JPanel panel) {
        // Function to add components to the right-side Panel
        JLabel lblTitle = new JLabel("CONTROLS");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(new Color(121, 85, 72));
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblTitle);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(createInfoPanel("Cell Info:", lblCellInfo));
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(createSectionLabel("Actions:"));
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        addButtonToPanel(panel, btnPlant);
        addButtonToPanel(panel, btnWater);
        addButtonToPanel(panel, btnFertilize);
        addButtonToPanel(panel, btnHarvest);
        addButtonToPanel(panel, btnTreatPest);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(new JSeparator());
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(createSectionLabel("Shop:"));
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        addButtonToPanel(panel, btnBuyWater);
        addButtonToPanel(panel, btnBuyFertilizer);
        addButtonToPanel(panel, btnBuyPesticide);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(new JSeparator());
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(createSectionLabel("Time:"));
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        addButtonToPanel(panel, btnNextDay);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        addButtonToPanel(panel, btnHelp);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        addButtonToPanel(panel, btnBackToMenu);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(createInfoPanel("Resources:", lblResources));
        panel.add(Box.createVerticalGlue());
    }

    private JPanel createInfoPanel(String title, JLabel content) {
        // Function to create a sub-panel for displaying information
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 12));
        lblTitle.setForeground(new Color(102, 102, 102));
        panel.add(lblTitle);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(content);
        return panel;
    }

    private JLabel createSectionLabel(String text) {
        // Function to create a title label for each group of functions
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.BOLD, 14));
        lbl.setForeground(new Color(121, 85, 72));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private void addButtonToPanel(JPanel panel, JButton button) {
        // Function to add a button to the panel and align it to the left
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        panel.add(button);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
    }

    private void setupListeners() {
        // Function to set up ActionListeners (click events) for all buttons
        btnPlant.addActionListener(e -> handlePlant());
        
        btnWater.addActionListener(e -> controller.waterCrop(selectedRow, selectedCol));
        btnFertilize.addActionListener(e -> controller.fertilizeCrop(selectedRow, selectedCol));
        btnHarvest.addActionListener(e -> controller.harvestCrop(selectedRow, selectedCol));
        btnTreatPest.addActionListener(e -> controller.treatPest(selectedRow, selectedCol));
        
        btnBuyWater.addActionListener(e -> controller.buyResource("WATER", 10));
        btnBuyFertilizer.addActionListener(e -> controller.buyResource("FERTILIZER", 10));
        btnBuyPesticide.addActionListener(e -> controller.buyResource("PESTICIDE", 5));
        
        btnNextDay.addActionListener(e -> controller.advanceToNextDay());
        
        btnHelp.addActionListener(e -> showHelpDialog());
        
        btnBackToMenu.addActionListener(e -> controller.backToMenu());
    }

    private void handlePlant() {
        // Function to handle the logic when the "Plant" button is clicked
        if (!checkSelection()) return;
        
        String[] options = {
            "Wheat - $5 (Profit: +$45)",
            "Corn - $10 (Profit: +$70)",
            "Tomato - $15 (Profit: +$105)",
            "Potato - $8 (Profit: +$52)"
        };
        
        String selected = (String) JOptionPane.showInputDialog(this, "Choose crop to plant:", "Plant Crop",
            JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        
        if (selected != null) {
            controller.plantCrop(selectedRow, selectedCol, selected);
        }
    }

    private void selectCell(int row, int col) {
        // Function to handle when the user clicks on a land cell in the grid
        selectedRow = row;
        selectedCol = col;
        highlightSelectedCell();
        updateCellInfo();
        setMessage("Selected cell [" + row + "," + col + "]");
    }

    private void highlightSelectedCell() {
        Farm farm = gameState.getFarm();
        try {
            for (int r = 0; r < farm.getRows(); r++) {
                for (int c = 0; c < farm.getCols(); c++) {
                    if (r == selectedRow && c == selectedCol) {
                        cellButtons[r][c].setBorder(new LineBorder(Color.YELLOW, 4));
                    } else {
                        cellButtons[r][c].setBorder(new LineBorder(new Color(93, 64, 55), 2));
                    }
                }
            }
        } catch (Exception e) {}
    }

    private boolean checkSelection() {
        if (selectedRow < 0 || selectedCol < 0) {
            setMessage("Please select a cell first!");
            return false;
        }
        return true;
    }
    
    
    public void updateCellInfo() {
        // Update the lblCellInfo content based on the currently selected land cell
        if (selectedRow >= 0 && selectedCol >= 0) {
            try {
                String info = gameState.getFarm().getCell(selectedRow, selectedCol).getInfo();
                lblCellInfo.setText("<html>" + info.replace("\n", "<br>") + "</html>");
            } catch (GameException e) {
            }
        }
    }

    public void setMessage(String msg) {
        lblMessage.setText("<html>" + msg.replace("\n", "<br>") + "</html>");
    }
    
    public void showError(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }


    public void refreshUI() {
        Farm farm = gameState.getFarm();
        
        lblStats.setText(gameState.getStats());

        // Update the weather
        Weather weather = farm.getCurrentWeather();
        lblWeather.setText(weather != null ? weather.getName() : "Sunny");

        // Update the resource inventory
        lblResources.setText(String.format("<html>Water: %d<br>Fertilizer: %d<br>Pesticide: %d</html>",
            gameState.getWaterStock(), gameState.getFertilizerStock(), gameState.getPesticideStock()));
        
        try {
            // Redraw the land cell grid
            for (int r = 0; r < farm.getRows(); r++) {
                for (int c = 0; c < farm.getCols(); c++) {
                    Cell cell = farm.getCell(r, c);
                    JButton btn = cellButtons[r][c];
                    btn.setText(cell.getDisplayEmoji());
                    
                    if (!cell.isEmpty() && cell.getPlant() != null) {
                        Plant plant = cell.getPlant();
                        
                    }
                }
            }
        } catch (GameException e) {}
        
        if (selectedRow >= 0 && selectedCol >= 0) highlightSelectedCell();
    }
    
    private void showHelpDialog() {
        // Display the Help dialog
        JDialog helpDialog = new JDialog(this, "Game Guide", true);
        helpDialog.setSize(700, 600);
        helpDialog.setLocationRelativeTo(this);
        JTextArea txtHelp = new JTextArea();
        txtHelp.setEditable(false);
        txtHelp.setFont(new Font("Arial", Font.PLAIN, 13));
        txtHelp.setLineWrap(true);
        txtHelp.setWrapStyleWord(true);
        txtHelp.setMargin(new Insets(15, 15, 15, 15));
        txtHelp.setText("OBJECTIVE:\n\n" +
                "- Manage your smart farm\n" +
                "- Plant and care for crops through their growth stages\n" +
                "- Manage resources\n" +
                "- Deal with weather and pest attacks\n" +
                "- Earn money from harvests and expand your farm\n\n" +
                
                "===============================================\n\n" +
                
                "HOW TO PLAY:\n\n" +
                "1. Click on a soil cell to select the cell you want to interact with\n" +
                "2. Use buttons on the right side to perform actions:\n" +
                "   - Plant: Sow seeds\n" +
                "   - Water: Provide water to crops\n" +
                "   - Fertilize: Provide nutrients\n" +
                "   - Harvest: Collect money when crops are ripe\n" +
                "   - Treat Pest: Cure pest infections\n" +
                "3. Click 'Next Day' to advance time\n" +
                "4. Buy additional resources when needed\n\n" +
                
                "===============================================\n\n" +
                
                "CROP GROWTH STAGES:\n\n" +
                "Seed - Just planted\n" +
                "Seedling - Sprouting, needs good care\n" +
                "Mature - Almost ready for harvest, continue caring\n" +
                "Harvest - Ready to harvest! Click to collect money\n" +
                
                "===============================================\n\n" +
                
                "CROP TYPES:\n\n" +
                "WHEAT:\n" +
                "  - Seed price: $5\n" +
                "  - Harvest value: $50\n" +
                "  - Profit: +$45\n" +
                "  - Speed: Fast (3 days)\n" +
                "  - Suitable for: Starting the game\n\n" +
                
                "CORN:\n" +
                "  - Seed price: $10\n" +
                "  - Harvest value: $80\n" +
                "  - Profit: +$70\n" +
                "  - Speed: Medium (6 days)\n" +
                "  - Suitable for: Balanced approach\n\n" +
                
                "TOMATO:\n" +
                "  - Seed price: $15\n" +
                "  - Harvest value: $120\n" +
                "  - Profit: +$105 (highest!)\n" +
                "  - Speed: Slow (9 days)\n" +
                "  - Suitable for: When you have capital\n\n" +
                
                "POTATO:\n" +
                "  - Seed price: $8\n" +
                "  - Harvest value: $60\n" +
                "  - Profit: +$52\n" +
                "  - Speed: Fast (3 days)\n" +
                "  - Suitable for: Easy to grow, low requirements\n\n" +
                
                "===============================================\n\n" +
                
                "WEATHER AND EFFECTS:\n\n" +
                "SUNNY:\n" +
                "   - Crops lose water faster\n" +
                "   - Need to water more frequently\n\n" +
                
                "RAINY:\n" +
                "   - Free automatic watering!\n" +
                "   - Save resources\n\n" +
                
                "CLOUDY:\n" +
                "   - Normal, stable conditions\n" +
                
                "DROUGHT:\n" +
                "   - VERY DANGEROUS!\n" +
                "   - Rapid water and health loss\n" +
                "   - Requires careful management\n\n" +
                
                "STORM:\n" +
                "   - Can damage crops\n" +
                "   - Reduces crop health\n\n" +
                
                "===============================================\n\n" +
                
                "RESOURCE MANAGEMENT:\n\n" +
                "WATER: $1 per unit\n" +
                "  - Essential for crop growth\n" +
                "  - Consumed daily\n\n" +
                
                "FERTILIZER: $2 per unit\n" +
                "  - Provides nutrients\n" +
                "  - Improves soil quality\n\n" +
                
                "PESTICIDE: $8 per bottle\n" +
                "  - Cures pest infections\n" +
                "  - Restores crop health\n\n" +
                
                "===============================================\n\n" +
                
                "PEST ATTACKS:\n\n" +
                "- Appear randomly each day (20% chance)\n" +
                "- Rapidly decrease crop health\n" +
                "- Treatment: Use pesticide immediately\n" +
                "- Consequences: Crop dies if not treated\n\n" +
                
                "===============================================\n\n" +
                
                "PLAYING TIPS:\n\n" +
                "* Start with WHEAT to accumulate capital quickly\n" +
                "* Always keep enough water and fertilizer in stock\n" +
                "* Monitor weather to water on time\n" +
                "* Treat pests IMMEDIATELY when detected\n" +
                "* Diversify crops to reduce risk\n" +
                "* Harvest on time, don't let crops wither\n" +
                "* Watch crop health (HP), fertilize when low\n" +
                "* Rain = free water, take advantage to save money\n" +
                "* Plant TOMATO when you have stable capital\n" +
                "* Manage money wisely, don't spend recklessly\n\n" +
                
                "===============================================\n\n" +
                
                "IMPORTANT NOTES:\n\n" +
                "- Crops need WATER and FERTILIZER every day\n" +
                "- Lack of resources reduces health leads to death\n" +
                "- Each crop type has different requirements\n" +
                "- Weather greatly affects your farm\n" +
                "- No auto-save, be careful!\n\n" +
                
                "===============================================\n\n" +
                
                "Good luck with your farming!"); 
        JScrollPane scrollPane = new JScrollPane(txtHelp);
        JButton btnClose = new JButton("Close");
        btnClose.setBackground(new Color(76, 175, 80));
        btnClose.setForeground(Color.BLACK);
        btnClose.addActionListener(e -> helpDialog.dispose());
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnClose);
        helpDialog.add(scrollPane, BorderLayout.CENTER);
        helpDialog.add(bottomPanel, BorderLayout.SOUTH);
        helpDialog.setVisible(true);
    }
}