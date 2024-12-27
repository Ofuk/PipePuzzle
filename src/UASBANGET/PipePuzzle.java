package UASBANGET;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class PipePuzzle extends JFrame {
    private int gridSize = 4; // Default grid size
    private JButton[][] buttons;
    private Map<Point, ImageIcon> imageMap = new HashMap<>(); // To store images based on coordinates
    private Map<Point, Integer> rotationCountMap = new HashMap<>(); // To store rotation count for each image

    private JPanel startPanel, gamePanel; // Panels for start screen and game
    private CardLayout cardLayout; // To switch between panels

    public PipePuzzle() {
        setTitle("Pipe Puzzle Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);

        cardLayout = new CardLayout();
        setLayout(cardLayout);

        createStartPanel();
        createGamePanel();

        add(startPanel, "START");
        add(gamePanel, "GAME");

        setVisible(true);
    }

    private void createStartPanel() {
        startPanel = new JPanel();
        startPanel.setLayout(new BorderLayout());
        startPanel.setBackground(new Color(50, 150, 250));

        JLabel welcomeLabel = new JLabel("Welcome To PipePuzzle", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setForeground(Color.WHITE);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1));

        JButton level1Button = new JButton("Level 1");
        level1Button.setFont(new Font("Arial", Font.BOLD, 18));
        level1Button.setBackground(Color.ORANGE);
        level1Button.setForeground(Color.BLACK);
        level1Button.addActionListener(e -> startGame(4, "level1"));

        JButton level2Button = new JButton("Level 2");
        level2Button.setFont(new Font("Arial", Font.BOLD, 18));
        level2Button.setBackground(Color.ORANGE);
        level2Button.setForeground(Color.BLACK);
        level2Button.addActionListener(e -> startGame(4, "level2"));

        JButton level3Button = new JButton("Level 3");
        level3Button.setFont(new Font("Arial", Font.BOLD, 18));
        level3Button.setBackground(Color.ORANGE);
        level3Button.setForeground(Color.BLACK);
        level3Button.addActionListener(e -> startGame(4, "level3"));

        buttonPanel.add(level1Button);
        buttonPanel.add(level2Button);
        buttonPanel.add(level3Button);

        startPanel.add(welcomeLabel, BorderLayout.CENTER);
        startPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void createGamePanel() {
        gamePanel = new JPanel();
        gamePanel.setBackground(new Color(200, 220, 240));
        gamePanel.setLayout(new BorderLayout());

        JPanel gridPanel = new JPanel();
        gridPanel.setBackground(new Color(200, 220, 240));
        gamePanel.add(gridPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2));

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 18));
        backButton.setBackground(Color.RED);
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> cardLayout.show(getContentPane(), "START"));

        JButton nextButton = new JButton("Next");
        nextButton.setFont(new Font("Arial", Font.BOLD, 18));
        nextButton.setBackground(Color.GREEN);
        nextButton.setForeground(Color.WHITE);
        nextButton.addActionListener(e -> goToNextLevel());

        buttonPanel.add(backButton);
        buttonPanel.add(nextButton);

        gamePanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    // Method to go to the next level
    private void goToNextLevel() {
        String currentLevel = getCurrentLevel();
        int nextLevel = Integer.parseInt(currentLevel.substring(currentLevel.length() - 1)) + 1;
        String nextLevelStr = "level" + nextLevel;

        if (nextLevel <= 3) {
            startGame(gridSize, nextLevelStr);
        } else {
            JOptionPane.showMessageDialog(this, "Anda telah menyelesaikan semua level!");
            cardLayout.show(getContentPane(), "START");
        }
    }

    // Helper method to get the current level
    private String getCurrentLevel() {
        for (Component comp : getContentPane().getComponents()) {
            if (comp.isVisible() && comp instanceof JPanel) {
                for (Component innerComp : ((JPanel) comp).getComponents()) {
                    if (innerComp instanceof JButton) {
                        JButton button = (JButton) innerComp;
                        if (button.getActionListeners().length > 0) {
                            ActionListener listener = button.getActionListeners()[0];
                            if (listener instanceof AbstractAction) {
                                AbstractAction action = (AbstractAction) listener;
                                String command = action.getValue(Action.ACTION_COMMAND_KEY).toString();
                                if (command.startsWith("level")) {
                                    return command;
                                }
                            }
                        }
                    }
                }
            }
        }
        return "level1";
    }
  private void startGame(int size, String level) {
        this.gridSize = size;
        this.buttons = new JButton[gridSize][gridSize];
        this.imageMap.clear();
        this.rotationCountMap.clear();
        JPanel gridPanel = (JPanel) gamePanel.getComponent(0);
        gridPanel.removeAll();
        gridPanel.setLayout(new GridLayout(gridSize, gridSize));

        ImageIcon[][] images = loadImagesForLevel(level);

        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                JButton button = new JButton("");
                button.setBackground(new Color(200, 220, 240));

                // Add image to button if available
                if (images[row][col] != null) {
                    button.setIcon(images[row][col]);
                    imageMap.put(new Point(row, col), images[row][col]);
                    rotationCountMap.put(new Point(row, col), 0); // Initialize rotation count
                }

                final int r = row, c = col;
                button.addActionListener(e -> rotateImage(r, c, level));

                buttons[row][col] = button;
                gridPanel.add(button);
            }
        }

        gridPanel.revalidate();
        gridPanel.repaint();
        cardLayout.show(getContentPane(), "GAME");
    }

    private ImageIcon[][] loadImagesForLevel(String level) {
        ImageIcon[][] images = new ImageIcon[gridSize][gridSize];
        String basePath = "src/Images/";
        switch (level) {
            case "level1":
                images[0][0] = loadImage(basePath + "pipe-win0,0.jpg");
                images[0][1] = loadImage(basePath + "kecoh0,1.jpg");
                images[0][2] = loadImage(basePath + "kecoh0,2.jpg");
                images[0][3] = loadImage(basePath + "kecoh3,1.0,3.jpg");
                images[1][0] = loadImage(basePath + "pipe-win1,0.2,3.jpg");
                images[1][1] = loadImage(basePath + "pipe-win1,1.1,2.jpg");
                images[1][2] = loadImage(basePath + "pipe-win1,1.1,2.jpg");
                images[1][3] = loadImage(basePath + "pipe-win1,3.jpg");
                images[2][0] = loadImage(basePath + "kecoh2,0.jpg");
                images[2][1] = loadImage(basePath + "kecoh2,1.jpg");
                images[2][2] = loadImage(basePath + "kecoh2,2.jpg");
                images[2][3] = loadImage(basePath + "pipe-win1,0.2,3.jpg");
                images[3][0] = loadImage(basePath + "kecoh3,0.jpg");
                images[3][1] = loadImage(basePath + "kecoh3,1.0,3.jpg");
                images[3][2] = loadImage(basePath + "kecoh3,2.jpg");
                images[3][3] = loadImage(basePath + "kecoh3,3.jpg");
                break;
            case "level2":
                images[0][0] = loadImage(basePath + "pipee0,0.1,2.jpg");
                images[0][1] = loadImage(basePath + "pipee0,1.1,3.jpg");
                images[0][2] = loadImage(basePath + "pipee0,2.jpg");
                images[0][3] = loadImage(basePath + "kecohh0,3.jpg");
                images[1][0] = loadImage(basePath + "kecoh2,0.jpg");
                images[1][1] = loadImage(basePath + "kecoh2,1.jpg");
                images[1][2] = loadImage(basePath + "pipee0,0.1,2.jpg");
                images[1][3] = loadImage(basePath + "pipee0,1.1,3.jpg");
                images[2][0] = loadImage(basePath + "kecoh3,0.jpg");
                images[2][1] = loadImage(basePath + "kecoh2,1.jpg");
                images[2][2] = loadImage(basePath + "kecoh2,0.jpg");
                images[2][3] = loadImage(basePath + "kecoh2,1.jpg");
                images[3][0] = loadImage(basePath + "kecoh3,0.jpg");
                images[3][1] = loadImage(basePath + "pipe-win1,1.1,2.jpg");
                images[3][2] = loadImage(basePath + "kecoh2,1.jpg");
                images[3][3] = loadImage(basePath + "kecohh3,3.jpg");
                break;
            case "level3":
                images[0][0] = loadImage(basePath + "pipeee0,0.1,0.2,0.jpg");
                images[0][1] = loadImage(basePath + "kecoh0,1.jpg");
                images[0][2] = loadImage(basePath + "kecoh2,0.jpg");
                images[0][3] = loadImage(basePath + "kecoh2,1.jpg");
                images[1][0] = loadImage(basePath + "pipeee0,0.1,0.2,0.jpg");
                images[1][1] = loadImage(basePath + "kecoh2,1.jpg");
                images[1][2] = loadImage(basePath + "kecoh2,0.jpg");
                images[1][3] = loadImage(basePath + "kecoh0,1.jpg");
                images[2][0] = loadImage(basePath + "pipeee0,0.1,0.2,0.jpg");
                images[2][1] = loadImage(basePath + "kecoh0,1.jpg");
                images[2][2] = loadImage(basePath + "kecoh3,0.jpg");
                images[2][3] = loadImage(basePath + "kecoh2,1.jpg");
                images[3][0] = loadImage(basePath + "pipeee3,0.jpg");
                images[3][1] = loadImage(basePath + "pipeee3,1.3,2.3,3.jpg");
                images[3][2] = loadImage(basePath + "pipeee3,1.3,2.3,3.jpg");
                images[3][3] = loadImage(basePath + "pipeee3,1.3,2.3,3.jpg");
                break;
        }

        return images;
    }

    // Helper function to load images
    private ImageIcon loadImage(String fileName) {
        try {
            ImageIcon icon = new ImageIcon(fileName);
            Image scaledImage = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH); // Scale images to fit buttons
            return new ImageIcon(scaledImage);
        } catch (Exception e) {
            System.out.println("Error loading image: " + fileName);
            return null;
        }
    }

    private void rotateImage(int row, int col, String level) {
        ImageIcon originalIcon = imageMap.get(new Point(row, col));
        if (originalIcon != null) {
            Image rotatedImage = rotateImageIcon(originalIcon.getImage(), 90);
            Image scaledImage = rotatedImage.getScaledInstance(buttons[row][col].getWidth(), buttons[row][col].getHeight(), Image.SCALE_SMOOTH);
            ImageIcon newIcon = new ImageIcon(scaledImage);
            buttons[row][col].setIcon(newIcon);
            imageMap.put(new Point(row, col), newIcon);

            int rotationCount = rotationCountMap.get(new Point(row, col)) + 1;
            rotationCountMap.put(new Point(row, col), rotationCount);

            if (level.equals("level1") && rotationCount == 1) {
                boolean allRotatedOnce = true;
                Point[] pointsToCheck = {
                        new Point(0, 0), new Point(1, 0), new Point(1, 1),
                        new Point(1, 2), new Point(1, 3), new Point(2, 3)
                };
                for (Point point : pointsToCheck) {
                    if (rotationCountMap.get(point) != 1) {
                        allRotatedOnce = false;
                        break;
                    }
                }
                if (allRotatedOnce) {
                    JOptionPane.showMessageDialog(this, "Anda Menang!");
                }
            }
            if (level.equals("level2") && rotationCount == 1) {
                boolean allRotatedOnce = true;
                Point[] pointsToCheck = {
                        new Point(0, 0), new Point(0, 1), new Point(0, 2),
                        new Point(1, 2), new Point(1, 3)
                };
                for (Point point : pointsToCheck) {
                    if (rotationCountMap.get(point) != 1) {
                        allRotatedOnce = false;
                        break;
                    }
                }
                if (allRotatedOnce) {
                    JOptionPane.showMessageDialog(this, "Anda Menang!");
                }
            }
            if (level.equals("level3") && rotationCount == 1) {
                boolean allRotatedOnce = true;
                Point[] pointsToCheck = {
                        new Point(0, 0), new Point(1, 0), new Point(2, 0),
                        new Point(3, 0), new Point(3, 1), new Point(3, 2), new Point(3, 3)
                };
                for (Point point : pointsToCheck) {
                    if (rotationCountMap.get(point) != 1) {
                        allRotatedOnce = false;
                        break;
                    }
                }
                if (allRotatedOnce) {
                    JOptionPane.showMessageDialog(this, "Anda Menang!");
                }
            }
        }
    }

    // Helper function to rotate image
    private Image rotateImageIcon(Image srcImg, double angle) {
        int w = srcImg.getWidth(null);
        int h = srcImg.getHeight(null);

        BufferedImage rotated = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotated.createGraphics();

        g2d.rotate(Math.toRadians(angle), w / 2.0, h / 2.0);
        g2d.drawImage(srcImg, 0, 0, null);
        g2d.dispose();

        return rotated;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PipePuzzle::new);
    }
}