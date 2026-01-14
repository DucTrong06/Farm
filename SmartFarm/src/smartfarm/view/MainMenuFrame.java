package smartfarm.view;

import javax.swing.*;

import smartfarm.controller.MenuController;

import java.awt.*;

public class MainMenuFrame extends JFrame {
    
    private MenuController controller;
    private JButton btnStart;
    private JButton btnHelp;
    private JButton btnQuit;


    public MainMenuFrame(MenuController controller) {
        this.controller = controller;
        initComponents();
        setupLayout();
        setupListeners();
    }

    private void initComponents() {
        setTitle("Smart Farm Simulator");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        btnStart = createStyledButton("Start Farming", new Color(76, 175, 80));
        btnHelp = createStyledButton("Help", new Color(33, 150, 243));
        btnQuit = createStyledButton("Quit", new Color(244, 67, 54));
        
        btnStart.setFont(new Font("Arial", Font.BOLD, 18));
        btnHelp.setFont(new Font("Arial", Font.BOLD, 18));
        btnQuit.setFont(new Font("Arial", Font.BOLD, 18));
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        Dimension size = new Dimension(300, 50); 
        btn.setPreferredSize(size);
        btn.setMaximumSize(size);
        // Change the cursor to a hand when hovering over
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            // Add Hover effect (change color when mouse hovers over)
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor);
            }
        });
        
        return btn;
    }

    private void setupLayout() {
        // Method to arrange components on the interface (Layout)
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(139, 195, 74));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        JLabel lblTitle = new JLabel("SMART FARM");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 50));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblSubtitle = new JLabel("Crop Growth & Resource Management");
        lblSubtitle.setFont(new Font("Arial", Font.PLAIN, 16));
        lblSubtitle.setForeground(Color.WHITE);
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblVersion = new JLabel("Version 1.0 | OOP Project");
        lblVersion.setFont(new Font("Arial", Font.PLAIN, 12));
        lblVersion.setForeground(new Color(255, 255, 255, 180));
        lblVersion.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Center-align the buttons
        btnStart.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnHelp.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnQuit.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(lblTitle);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(lblSubtitle);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        mainPanel.add(btnStart);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(btnHelp);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(btnQuit);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanel.add(lblVersion);
        mainPanel.add(Box.createVerticalGlue());
        
        add(mainPanel);
    }

    private void setupListeners() {
        // Method to assign actions (ActionListener) to the buttons
        btnStart.addActionListener(e -> controller.startNewGame());
        btnHelp.addActionListener(e -> controller.openHelp());
        btnQuit.addActionListener(e -> controller.quitGame());
    }
}