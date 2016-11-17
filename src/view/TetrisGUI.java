/*
 * Tuan Huynh TCSS 305A - Tetris
 */

package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.AbstractAction;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import model.Board;

/**
 * Tetris's GUI.
 */
@SuppressWarnings("serial")
public class TetrisGUI extends JPanel implements Observer {

    /** Default time delay. */
    private static final int TIME_DELAY = 1000;

    /** Time to be minus from time delay. */
    private static final int DECREASE_DELAY = 150;

    /** smallest delay. */
    private static final int SMALLEST_DELAY = 50;

    /** The title of the window. */
    private static final String TITLE = "TCSS 305A - Tetris";

    /** Game over string. */
    private static final String GAME_OVER = "GAME OVER";

    /** Default width of the window. */
    private static final int DEFAULT_WIDTH = 395;

    /** Default height of the window. */
    private static final int DEFAULT_HEIGHT = 570;

    /** Default width of the left panel. */
    private static final int LEFT_PANEL_WIDTH = 120;

    /** Layout for the left panel. */
    private static final GridLayout LEFT_PANEL_LAYOUT = new GridLayout(3, 1);

    /** Default width of the board. */
    private static final int BOARD_WIDTH = 10;

    /** Default height of the board. */
    private static final int BOARD_HEIGHT = 20;

    /** Size of the tiles of the board and the preview box. */
    private static final int TILE_SIZE = 25;

    /** Font size for the "NEXT" label. */
    private static final int NEXT_FONT_SIZE = 25;

    /** Parent window of the main panel. */
    private final JFrame myGameWindow;

    /** The timer for animation. */
    private final Timer myTimer;

    /** Delay for the timer in milliseconds. */
    private int myDelayTime;

    /** Answer from JOptionPane. */
    private int myAnswer;

    /** The game board. */
    private Board myBoard;

    /** JPanel that displays the board. */
    private BoardPanel myBoardPanel;

    /** JPanel that displays the preview of the next piece. */
    private PreviewBoxPanel myPreviewPanel;

    /** JPanel that displays the preview of the score. */
    private ScorePanel myScorePanel;

    /** JPanel that displays the preview of the level. */
    private LevelPanel myLevelPanel;

    /** Boolean if the game is paused. */
    private boolean myPaused;

    /** Audio. */
    private Clip myClip;

    /** Menu bar. */
    private final JMenuBar myMainMenuBar;

    /** Constructor. */
    public TetrisGUI() {
        super();
        myDelayTime = TIME_DELAY;
        // instantiate the final attributes
        myGameWindow = new JFrame();
        myTimer = new Timer(myDelayTime, new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theActionEvent) {
                myBoard.step();
            }
        });

        myMainMenuBar = new JMenuBar();
        // setup the window and its components
        setup();

        setUpAudio();

        // create a new game
        newGame();

        // start the timer and open the window
        myTimer.start();
        myGameWindow.setVisible(true);

    }

    /**
     * Sets up the window and the main panel.
     */
    private void setup() {
        // set the focus on this panel
        this.setFocusable(true);

        // add key listener
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(final KeyEvent theKeyEvent) {

                if (theKeyEvent.getKeyCode() == KeyEvent.VK_LEFT && !isPaused()) {
                    myBoard.left();
                    setSoundEffect();
                }
                else if (theKeyEvent.getKeyCode() == KeyEvent.VK_RIGHT && !isPaused()) {
                    myBoard.right();
                    setSoundEffect();
                }
                else if (theKeyEvent.getKeyCode() == KeyEvent.VK_DOWN && !isPaused()) {
                    myBoard.down();
                    setSoundEffect();
                }
                else if (theKeyEvent.getKeyCode() == KeyEvent.VK_SPACE && !isPaused()) {
                    myBoard.drop();
                }
                else if (theKeyEvent.getKeyCode() == KeyEvent.VK_SHIFT && !isPaused()) {
                    myBoard.rotateCW();
                    setSoundEffect();
                }
                else if (theKeyEvent.getKeyCode() == KeyEvent.VK_UP && !isPaused()) {
                    myBoard.rotateCCW();
                    setSoundEffect();
                }
                else if (theKeyEvent.getKeyCode() == KeyEvent.VK_P) {
                    pause();
                }
                else if (theKeyEvent.getKeyCode() == KeyEvent.VK_R) {
                    resume();
                }

            }
        });

        setGUI();
        // setUpAudio();

        // add this main panel to the gameWindow
        myGameWindow.add(this);

        // setup inner panels
        setupInnerPanels();

        // add menu bar
        addMenuBar();

        addMenuBar2();

    }

    /**
     * Set down array key sound effect.
     */
    private void setSoundEffect() {
        try {
            final AudioInputStream audioIn =
                            AudioSystem.getAudioInputStream(new File("his" + "ticks.wav"));
            final Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.loop(0);
        }
        catch (final IOException | LineUnavailableException
                        | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    private void setGUI() {
        // set attributes of the gameWindow
        myGameWindow.setTitle(TITLE);
        myGameWindow.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        myGameWindow.setResizable(false);
        myGameWindow.setLocationRelativeTo(null);
        myGameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Adds menu to the window.
     */
    private void addMenuBar() {

        final JMenu game = new JMenu("Game");
        game.setMnemonic(KeyEvent.VK_G);

        final JMenuItem newGame = new JMenuItem(new AbstractAction() {

            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                resume();
                newGame();
            }

        });
        newGame.setText("New Game");
        newGame.setMnemonic(KeyEvent.VK_N);
        game.add(newGame);

        final JMenuItem endGame = new JMenuItem(new AbstractAction() {

            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                pause();
                final int type = JOptionPane.YES_NO_OPTION;
                int answer = 0;
                answer = JOptionPane.showConfirmDialog(null,
                                                       "Game ended! Play again?"
                                                             + "\n (No to quit)",
                                                       GAME_OVER, type);
                if (answer == JOptionPane.YES_OPTION) {
                    resume();
                    newGame();
                    myScorePanel.repaint();
                }
                else {
                    myGameWindow.dispatchEvent(new WindowEvent(myGameWindow,
                                                               WindowEvent.WINDOW_CLOSING));
                }
            }

        });
        endGame.setText("End Game");
        endGame.setMnemonic(KeyEvent.VK_E);
        game.add(endGame);

        final JMenuItem quitGame = new JMenuItem(new AbstractAction() {

            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                myGameWindow.dispatchEvent(new WindowEvent(myGameWindow,
                                                           WindowEvent.WINDOW_CLOSING));
            }

        });
        quitGame.setText("Quit Game");
        quitGame.setMnemonic(KeyEvent.VK_Q);
        game.add(quitGame);

        myMainMenuBar.add(game);
    }

    /**
     * add more menu.
     */
    private void addMenuBar2() {
        final JMenu music = new JMenu("Music");
        music.setMnemonic(KeyEvent.VK_M);
        final JCheckBoxMenuItem rButton = new JCheckBoxMenuItem("Mute");
        rButton.setMnemonic(KeyEvent.VK_U);
        rButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                if (myClip.isRunning()) {
                    myClip.stop();
                }
                else {
                    myClip.start();
                }
            }

        });
        music.add(rButton);

        final JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);
        final JMenuItem controlsItem = new JMenuItem(new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent theActionEvent) {
                pause();
                final String helpMsg = "CONTROLS \n" + "\n" + "Move Left - Left Arrow Key \n"
                                       + "Move Right - Right Arrow Key \n"
                                       + "Move Down - Down Arrow Key \n" + "Drop - Spacebar \n"
                                       + "Rotate Clockwise - Shift Key \n"
                                       + "Rotate Counter Clockwise - Up Arrow Key \n"
                                       + "Pause - P Key\n" + "Resume - R Key";
                final int helpType = JOptionPane.INFORMATION_MESSAGE;
                JOptionPane.showMessageDialog(myGameWindow, helpMsg, "Help - Controls",
                                              helpType);
                resume();
            }
        });
        controlsItem.setText("Controls");
        controlsItem.setMnemonic(KeyEvent.VK_C);
        helpMenu.add(controlsItem);

        myMainMenuBar.add(music);
        myMainMenuBar.add(helpMenu);
        myGameWindow.setJMenuBar(myMainMenuBar);
    }

    /**
     * Sets up the inner panels.
     */
    private void setupInnerPanels() {

        // instantiate inner panels
        myBoardPanel = new BoardPanel(BOARD_HEIGHT, TILE_SIZE);
        myPreviewPanel = new PreviewBoxPanel(TILE_SIZE);
        myScorePanel = new ScorePanel(TILE_SIZE);
        myLevelPanel = new LevelPanel(TILE_SIZE, this);

        final JPanel leftPanel = new JPanel();
        final JPanel leftUpPanel = new JPanel();
        final JPanel leftCenterPanel = new JPanel();
        final JPanel leftDownPanel = new JPanel();

        leftUpPanel.setLayout(new BorderLayout());
        leftCenterPanel.setLayout(new BorderLayout());
        leftDownPanel.setLayout(new BorderLayout());

        // Score panel.
        final JLabel scoreLabel = new JLabel("SCORE");
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scoreLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, NEXT_FONT_SIZE));
        scoreLabel.setBackground(Color.WHITE);
        scoreLabel.setOpaque(true);

        leftUpPanel.add(scoreLabel, BorderLayout.NORTH);
        leftUpPanel.add(myScorePanel, BorderLayout.CENTER);
        leftUpPanel.setPreferredSize(new Dimension(LEFT_PANEL_WIDTH, DEFAULT_HEIGHT));

        // Level panel.
        final JLabel levelLabel = new JLabel("LEVEL");
        levelLabel.setHorizontalAlignment(SwingConstants.CENTER);
        levelLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, NEXT_FONT_SIZE));
        levelLabel.setBackground(Color.WHITE);
        levelLabel.setOpaque(true);

        leftCenterPanel.add(levelLabel, BorderLayout.NORTH);
        leftCenterPanel.add(myLevelPanel, BorderLayout.CENTER);
        leftCenterPanel.setPreferredSize(new Dimension(LEFT_PANEL_WIDTH, DEFAULT_HEIGHT));

        // Next piece panel.
        final JLabel nextLabel = new JLabel("NEXT");
        nextLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nextLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, NEXT_FONT_SIZE));
        nextLabel.setBackground(Color.WHITE);
        nextLabel.setOpaque(true);

        leftDownPanel.add(nextLabel, BorderLayout.NORTH);
        leftDownPanel.add(myPreviewPanel, BorderLayout.CENTER);
        leftDownPanel.setPreferredSize(new Dimension(LEFT_PANEL_WIDTH, DEFAULT_HEIGHT));

        // setup left panel
        leftPanel.setLayout(LEFT_PANEL_LAYOUT);
        leftPanel.add(leftUpPanel);
        leftPanel.add(leftCenterPanel);
        leftPanel.add(leftDownPanel);

        // setup colors;
        myBoardPanel.setBackground(Color.WHITE);
        myPreviewPanel.setOpaque(false);
        myScorePanel.setOpaque(false);
        myLevelPanel.setOpaque(false);
        leftUpPanel.setOpaque(false);
        leftCenterPanel.setOpaque(false);
        leftDownPanel.setOpaque(false);
        leftPanel.setBackground(Color.BLUE);

        // setup the layout
        this.setLayout(new BorderLayout());
        this.add(leftPanel, BorderLayout.WEST);
        this.add(myBoardPanel, BorderLayout.CENTER);
    }

    /**
     * Setting up audio.
     */
    private void setUpAudio() {

        try {
            final AudioInputStream audioIn =
                            AudioSystem.getAudioInputStream(new File("theme.wav"));
            myClip = AudioSystem.getClip();
            myClip.open(audioIn);
            myClip.loop(Clip.LOOP_CONTINUOUSLY);
        }
        catch (final IOException | LineUnavailableException
                        | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }

    }

    /**
     * Creates a new Tetris game.
     */
    private void newGame() {
        // create a new board
        // myDelayTime = TIME_DELAY;
        myBoard = new Board(BOARD_WIDTH, BOARD_HEIGHT);
        myBoard.addObserver(myBoardPanel);
        myBoard.addObserver(myPreviewPanel);
        myBoard.addObserver(myScorePanel);
        myBoard.addObserver(myLevelPanel);
        myBoard.addObserver(this);
        myBoard.clear();
        if (myClip.isRunning()) {
            myClip.close();
        }
        setUpAudio();
        myScorePanel.setScore(0);
        myLevelPanel.setLevel(0);
        myLevelPanel.repaint();
    }

    /**
     * Pauses the game.
     */
    private void pause() {
        // pause game by stopping the timer and setting the boolean myPaused to
        // true
        setPaused(true);
        myTimer.stop();
        if (myClip.isRunning()) {
            myClip.stop();
        }
    }

    /**
     * Resumes the game.
     */
    private void resume() {
        // resume game by starting the timer and setting the boolean myPaused to
        // false
        setPaused(false);
        myClip.start();
        myTimer.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final Observable theObj, final Object theArg) {
        if (theObj instanceof Board && theArg instanceof Board.GameStatus) {
            // ask to play again if game is over.
            final boolean gameOver = ((Board.GameStatus) theArg).isGameOver();
            if (gameOver) {
                pause();
                if (myClip.isRunning()) {
                    myClip.close();
                }
                final int type = JOptionPane.YES_NO_OPTION;

                myAnswer = JOptionPane.showConfirmDialog(this,
                                                         "You're bad!\nWould you "
                                                               + "like to play again?",
                                                         GAME_OVER, type);

                // create a new game if player chooses yes.
                if (myAnswer == JOptionPane.YES_OPTION) {
                    resume();
                    newGame();
                    myScorePanel.setScore(0);
                    myLevelPanel.setLevel(0);
                    myScorePanel.repaint();
                    myLevelPanel.repaint();
                }
                else {
                    myGameWindow.dispatchEvent(new WindowEvent(myGameWindow,
                                                               WindowEvent.WINDOW_CLOSING));
                }
            }
        }
    }

    /**
     * Checks if game is paused.
     *
     * @return true, if game is paused
     */
    public boolean isPaused() {
        return myPaused;
    }

    /**
     * Sets the if the game is paused.
     *
     * @param thePaused if the game is paused
     */
    public void setPaused(final boolean thePaused) {
        this.myPaused = thePaused;
    }

    /**
     * Reduce the delay when the level is increased.
     */
    public void increaseLevel() {

        if (myDelayTime > SMALLEST_DELAY) {
            myDelayTime -= DECREASE_DELAY;
        }
        myTimer.setDelay(myDelayTime);
        // System.out.print(myDelayTime);

    }

}
