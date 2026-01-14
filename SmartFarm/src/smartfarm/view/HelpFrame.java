package smartfarm.view;

import javax.swing.*;

import smartfarm.controller.HelpController;

import java.awt.*;

public class HelpFrame extends JFrame {
    
    private HelpController controller; 
    private JButton btnBack;
    private JTextArea txtHelp;

    public HelpFrame() {

        this.controller = new HelpController();
        this.controller.setView(this);
        
        initComponents();
        setupLayout();
        setupListeners();
    }

    private void initComponents() {
        setTitle("Game Guide - Smart Farm");
        setSize(800, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        
        // Configure the Back button
        btnBack = new JButton("Back to Menu");
        btnBack.setBackground(new Color(76, 175, 80));
        btnBack.setForeground(Color.BLACK);
        btnBack.setFont(new Font("Arial", Font.PLAIN, 14));
        btnBack.setFocusPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Configure the text display area (JTextArea)
        txtHelp = new JTextArea();
        txtHelp.setEditable(false);
        txtHelp.setFont(new Font("Arial", Font.PLAIN, 14));
        txtHelp.setLineWrap(true);
        txtHelp.setWrapStyleWord(true);
        txtHelp.setMargin(new Insets(15, 15, 15, 15));
        txtHelp.setText(getHelpContent());
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(255, 253, 231));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel lblHeader = new JLabel("GAME GUIDE");
        lblHeader.setFont(new Font("Arial", Font.BOLD, 28));
        lblHeader.setForeground(new Color(121, 85, 72));
        headerPanel.add(lblHeader);
        
        JScrollPane scrollPane = new JScrollPane(txtHelp);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel footerPanel = new JPanel();
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        footerPanel.add(btnBack);
        
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    private void setupListeners() {
        btnBack.addActionListener(e -> controller.backToMenu());
    }
    
    private String getHelpContent() {
        return "OBJECTIVE:\n\n" +
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
               
               "Good luck with your farming!";
    }
}