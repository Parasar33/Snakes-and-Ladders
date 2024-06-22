package org.dev;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.JLabel;
import javax.swing.Timer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Snake_ladders extends JFrame {

    private static final int ROLL_INTERVAL = 100;
    private static final int ROLL_DURATION = 1000;

    private static final long serialVersionUID = 1L;
    private JButton[][] boardButtons = new JButton[10][10];

    private static int players;
    private int currentPlayer = 0, previousPlayer = 0;
    private int[] playerPositions;
    int newPosition;
    private HashMap<Integer,int[]> previous = new HashMap<>();

    // BOARD 1 DETAILS
    private int[] B1_snakeStarts = {17, 54, 62, 64, 87, 93, 94, 98};
    private int[] B1_snakeEnds = {7, 34, 19, 60, 36, 73, 75, 79};
    private int[] B1_ladderStarts = {1, 4, 9, 21, 28, 51, 72, 80};
    private int[] B1_ladderEnds = {38, 14, 31, 42, 84, 67, 91, 99};
    
    
    //Game Logo
    static ImageIcon logo = new ImageIcon("G:\\workspace\\Ludo\\src\\res\\snakes&ladders.png");
    
    //Game player Icons
    ImageIcon blue_icon = new ImageIcon("G:\\workspace\\Ludo\\src\\res\\blue-piece.png");
    ImageIcon yellow_icon = new ImageIcon("G:\\workspace\\Ludo\\src\\res\\yellow-piece.png");
    ImageIcon green_icon = new ImageIcon("G:\\workspace\\Ludo\\src\\res\\green-piece.png");
    ImageIcon red_icon = new ImageIcon("G:\\workspace\\Ludo\\src\\res\\red-piece.png");
    ImageIcon pink_icon = new ImageIcon("G:\\workspace\\Ludo\\src\\res\\pink-piece.png");
    ImageIcon cyan_icon = new ImageIcon("G:\\workspace\\Ludo\\src\\res\\cyan-piece.png");
    ImageIcon orange_icon = new ImageIcon("G:\\workspace\\Ludo\\src\\res\\orange-piece.png");
    ImageIcon olive_icon = new ImageIcon("G:\\workspace\\Ludo\\src\\res\\olive-piece.png");

    ImageIcon[] pieces = {red_icon, blue_icon, green_icon, yellow_icon, pink_icon, cyan_icon, orange_icon, olive_icon};

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Snake_ladders frame = new Snake_ladders();
                    frame.setTitle("SNAKES & LADDERS - by rishav parasar");
                    frame.setIconImage(logo.getImage());
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    static int index = 0;

    public Snake_ladders() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1085, 705);
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);

        JPanel board = new JPanel() {
            private static final long serialVersionUID = 1L;
            private Image backgroundImage;

            {
                try {
                    backgroundImage = ImageIO.read(new File("G:\\workspace\\Ludo\\src\\res\\game2.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        board.setBounds(85, 10, 650, 650);
        getContentPane().add(board);
        board.setLayout(new GridLayout(10, 10));

        JPanel operations = new JPanel();
        operations.setBounds(745, 10, 326, 140);
        getContentPane().add(operations);
        operations.setLayout(null);

        JComboBox<Integer> playerCountComboBox = new JComboBox<>();
        playerCountComboBox.setBounds(300, 30, 20, 20);
        for (int i = 1; i <= 8; i++) {
            playerCountComboBox.addItem(i);
        }
        playerCountComboBox.setSelectedIndex(0);
        operations.add(playerCountComboBox);

        JTextPane player_count = new JTextPane();
        player_count.setEditable(false);
        player_count.setFont(new Font("Zig", Font.ITALIC, 13));
        player_count.setText("Choose the number of Players");
        player_count.setBounds(0, 30, 326, 20);
        operations.add(player_count);

        JButton playButton = new JButton("PLAY");
        playButton.setFont(new Font("Zig", Font.ITALIC, 14));
        playButton.setBounds(43, 60, 100, 30);
        operations.add(playButton);

        JButton restartButton = new JButton("RESTART");
        restartButton.setFont(new Font("Zig", Font.ITALIC, 14));
        restartButton.setBounds(175, 60, 127, 30);
        operations.add(restartButton);

        JPanel panel = new JPanel();
        panel.setBounds(10, 143, 65, 517);
        getContentPane().add(panel);
        panel.setLayout(null);

        JPanel updates = new JPanel();
        updates.setBounds(745, 150, 326, 510);
        getContentPane().add(updates);
        updates.setLayout(null);

        JLabel snaked = new JLabel("");
        snaked.setBounds(10, 10, 306, 256);
        ImageIcon gifIcon = new ImageIcon("G:\\workspace\\Ludo\\src\\res\\snake.gif");
        snaked.setIcon(gifIcon);
        updates.add(snaked);

        JLabel chanceIcon = new JLabel("");
        chanceIcon.setFont(new Font("Zig", Font.ITALIC, 14));
        chanceIcon.setBounds(192, 285, 65, 65);

        updates.add(chanceIcon);

        JLabel chanceLabel = new JLabel("");
        chanceLabel.setFont(new Font("Zig", Font.ITALIC, 14));
        chanceLabel.setBounds(10, 298, 190, 32);
        updates.add(chanceLabel);
        
        JLabel congo = new JLabel("Welcome to Snakes & Ladders");
        congo.setFont(new Font("Zig", Font.ITALIC, 12));
        congo.setBounds(40,360,280,30);
        updates.add(congo);
        
        JLabel winPlayer = new JLabel("___________ Made by Rishav Parasar");
        winPlayer.setFont(new Font("Zig", Font.PLAIN, 9));
        winPlayer.setBounds(55,390,280,30);
        updates.add(winPlayer);

        dice.setBounds(0, 0, 85, 134);
        getContentPane().add(dice);

        JButton diceButton = new JButton("");
        diceButton.setBounds(10, 10, 61, 61);
        diceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        dice.setLayout(null);
        dice.add(diceButton);

        restartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                restartButton.setEnabled(false); // Disable restart button
                playButton.setEnabled(true); // Enable play button

                chanceLabel.setText("");
                chanceIcon.setIcon(null); // Remove current player
                panel.removeAll();
                panel.revalidate();
                panel.repaint(); // Remove all player buttons

                // Clear the board
                for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < 10; j++) {
                        boardButtons[i][j].setIcon(null);
                    }
                }

                // Reset player positions
                if (playerPositions != null) {
                    Arrays.fill(playerPositions, 0);
                }

                currentPlayer = 0; // Reset current player
            }
        });

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                JButton button = new JButton();
                boardButtons[i][j] = button;
                button.setOpaque(false);
                button.setContentAreaFilled(false);
                button.setBorderPainted(false); // making buttons transparent
                board.add(button);
            }
        }

        // dice functionality
        // initially 6
        ImageIcon one = new ImageIcon("G:\\workspace\\Ludo\\src\\res\\dice 1.png");
        ImageIcon two = new ImageIcon("G:\\workspace\\Ludo\\src\\res\\dice 2.png");
        ImageIcon three = new ImageIcon("G:\\workspace\\Ludo\\src\\res\\dice 3.png");
        ImageIcon four = new ImageIcon("G:\\workspace\\Ludo\\src\\res\\dice 4.png");
        ImageIcon five = new ImageIcon("G:\\workspace\\Ludo\\src\\res\\dice 5.png");
        ImageIcon six = new ImageIcon("G:\\workspace\\Ludo\\src\\res\\dice 6.png");

        ImageIcon[] diceIcon = {one, two, three, four, five, six};
        diceButton.setIcon(diceIcon[5]);
        diceButton.setOpaque(false);
        diceButton.setContentAreaFilled(false);
        diceButton.setBorderPainted(false); // making buttons transparent

        JButton rollButton = new JButton("Roll");
        rollButton.setFont(new Font("Zig", Font.ITALIC, 16));
        rollButton.setBounds(0, 67, 85, 43);
        rollButton.setOpaque(false);
        rollButton.setContentAreaFilled(false);
        rollButton.setBorderPainted(false); // making buttons transparent

        rollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	playSound("G:\\workspace\\Ludo\\src\\res\\diceRoll.wav"); // Play dice roll sound
                Timer timer = new Timer(ROLL_INTERVAL, null);
                timer.addActionListener(new ActionListener() {
                    int rollCount = 0;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int randomIndex = roll(diceIcon);
                        diceButton.setIcon(diceIcon[randomIndex]);

                        rollCount++;
                        if (rollCount * ROLL_INTERVAL >= ROLL_DURATION) {
                            timer.stop();
                            int roll = randomIndex + 1; // Dice roll result (1-6)
                            diceButton.setIcon(diceIcon[randomIndex]);

                            int newPosition = playerPositions[currentPlayer] + roll;
                            // Debugging
                            // System.out.println(newPosition);

                            if (newPosition == 100) {
                                // Player has won the game
                                congo.setFont(new Font("Zig", Font.ITALIC, 20));
                                congo.setText("Congratulations");
                                winPlayer.setFont(new Font("Zig", Font.PLAIN, 20));
                                winPlayer.setText("Player " + (currentPlayer + 1) + " Wins");

                                // Remove the roll button
                                dice.remove(rollButton);
                                playButton.setEnabled(true); // Enable play button
                                restartButton.setEnabled(false); // Disable restart button
                                return; // Exit the method as the player has won
                            } else if (newPosition < 100) {
                                // Clear the icon for the piece at the previous position
                                int[] previousPosition = previous.get(currentPlayer);
                                if (previousPosition != null) {
                                    int row = previousPosition[0];
                                    int col = previousPosition[1];
                                    boardButtons[row][col].setIcon(null);
                                }

                                // Create a transition timer for smooth movement
                                Timer transitionTimer = new Timer(200, null);
                                transitionTimer.addActionListener(new ActionListener() {
                                    int tempPosition = playerPositions[currentPlayer];

                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        // Move one step at a time
                                        if (tempPosition < newPosition) {
                                            tempPosition++;
                                            int[] tempPositionArray = oldnewForPosition(tempPosition);

                                            if (isValidPosition(tempPositionArray)) {
                                                int tempRow = tempPositionArray[0];
                                                int tempCol = tempPositionArray[1];
                                                boardButtons[tempRow][tempCol].setIcon(pieces[currentPlayer]);

                                                // Clear the previous step icon
                                                if (tempPosition > playerPositions[currentPlayer]) {
                                                    int[] prevTempPositionArray = oldnewForPosition(tempPosition - 1);
                                                    if (isValidPosition(prevTempPositionArray)) {
                                                        int prevTempRow = prevTempPositionArray[0];
                                                        int prevTempCol = prevTempPositionArray[1];
                                                        boardButtons[prevTempRow][prevTempCol].setIcon(null);
                                                    }
                                                }
                                            }
                                        } else {
                                            // Final position reached
                                            transitionTimer.stop();
                                            playerPositions[currentPlayer] = newPosition;

                                            // Update player position based on dice roll
                                            updatePlayerPosition(currentPlayer, newPosition);

                                            // Store the new position in the previous hashmap
                                            int[] newPositionArray = oldnew(currentPlayer);
                                            previous.put(currentPlayer, newPositionArray);

                                            // Move to the next player
                                            currentPlayer = (currentPlayer + 1) % players;

                                            // Update chanceLabel and chanceIcon
                                            chanceLabel.setText("Current Chance: Player " + (currentPlayer + 1));
                                            chanceIcon.setIcon(pieces[currentPlayer]);
                                        }
                                    }
                                });
                                transitionTimer.start();
                            }
                        }
                    }
                });
                timer.start();
            }
        });

        



        // piece movements
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                players = (int) playerCountComboBox.getSelectedItem();
                addPlayerButtons(panel, players);
                playButton.setEnabled(false); // Disable play button
                restartButton.setEnabled(true); // Enable restart button

                playerPositions = new int[players];
                currentPlayer = 0; // Reset current player

                chanceLabel.setText("Current Chance : ");
                chanceIcon.setIcon(pieces[currentPlayer]);
                dice.add(rollButton);

                // Initialize player positions
                Arrays.fill(playerPositions, 0);
                
                congo.setText(null);
            	winPlayer.setText(null);

            }
        });

    }

    private final JPanel dice = new JPanel();

    private void addPlayerButtons(JPanel panel, int players) {
        panel.removeAll();
        JButton[] buttons = new JButton[8];
        int[] positions = {455, 390, 325, 260, 195, 130, 65, 0};

        for (int i = 0; i < players; i++) {
            buttons[i] = new JButton();
            buttons[i].setBounds(0, positions[i], 55, 55);
            panel.add(buttons[i]);
            buttons[i].setOpaque(false);
            buttons[i].setContentAreaFilled(false);
            buttons[i].setBorderPainted(false); // making buttons transparent
            buttons[i].setIcon(pieces[i]);

        }

        panel.revalidate();
        panel.repaint();
    }

    private int roll(ImageIcon[] diceIcon) {
        Random random = new Random();
        int randomIndex = random.nextInt(diceIcon.length);
        return randomIndex;
    }
    
    
    private void updatePlayerPosition(int player, int newPosition) {
        // Clear the icon for the piece at the previous position
        int[] previousPosition = oldnew(player);
        if (previousPosition != null) {
            int row = previousPosition[0];
            int col = previousPosition[1];
            boardButtons[row][col].setIcon(null);
        }

        // Update the player's position considering snakes and ladders
        playerPositions[player] = newPosition;

        // Check for snakes and ladders
        for (int i = 0; i < B1_snakeStarts.length; i++) {
            if (playerPositions[player] == B1_snakeStarts[i]) {
            	//add snake sound
            	playSound("G:\\workspace\\Ludo\\src\\res\\hiss.wav"); // Play snake hiss sound
                playerPositions[player] = B1_snakeEnds[i];
                break;
            }
        }

        for (int i = 0; i < B1_ladderStarts.length; i++) {
            if (playerPositions[player] == B1_ladderStarts[i]) {
            	//add ladder sound
            	playSound("G:\\workspace\\Ludo\\src\\res\\ladder.wav"); // Play snake hiss sound
                playerPositions[player] = B1_ladderEnds[i];
                break;
            }
        }

        // Update the final position
        int[] output = oldnew(player);
        int newRow = output[0];
        int newCol = output[1];
        boardButtons[newRow][newCol].setIcon(pieces[player]);
    }

    
    private int[] oldnew (int player)
    {
    	//find tens digit
    	int n = playerPositions[player];
    	int[] output = new int[2];
    	
    	if(n == 20 || n == 40 || n == 60 || n == 80)
    	{
    		output[0] = 10 - (n/10);
    		output[1] = 0;
    		return output;
    	}
    	
    	if(n == 10 || n == 30 || n == 50 || n == 70 || n == 90)
    	{
    		output[0] = 10 - (n/10);
    		output[1] = 9;
    		return output;
    	}
    	
    	if((n/10) % 2 == 0)
    	{
    		output[0] = 9 - (n/10);
        	output[1] = (n%10) - 1;
    	}
    	else
    	{
    		output[0] = 9 - (n/10);
        	output[1] = 10 - (n%10) ;
    	}
    	return output;
    }
    
    private int[] oldnewForPosition(int position) {
        //find tens digit
        int n = position;
        int[] output = new int[2];

        if (n == 20 || n == 40 || n == 60 || n == 80) {
            output[0] = 10 - (n / 10);
            output[1] = 0;
            return output;
        }

        if (n == 10 || n == 30 || n == 50 || n == 70 || n == 90) {
            output[0] = 10 - (n / 10);
            output[1] = 9;
            return output;
        }

        if ((n / 10) % 2 == 0) {
            output[0] = 9 - (n / 10);
            output[1] = (n % 10) - 1;
        } else {
            output[0] = 9 - (n / 10);
            output[1] = 10 - (n % 10);
        }
        return output;
    }
    
    private boolean isValidPosition(int[] position) {
        return position[0] >= 0 && position[0] < 10 && position[1] >= 0 && position[1] < 10;
    }
    
    public void playSound(String soundFile) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundFile).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            ex.printStackTrace();
        }
    }

}
