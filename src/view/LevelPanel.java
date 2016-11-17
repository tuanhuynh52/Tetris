/*
 /*
 * Tuan Huynh TCSS 305A - Tetris
 */


package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import model.Board;

/**
 * Level panel.
 * 
 */
@SuppressWarnings("serial")
public class LevelPanel extends JPanel implements Observer {

    /** The value that decides the current level of the game. */
    private static final int LEVEL_CONST = 5;

    /** Font size for level. */
    private static final int FONT_SIZE = 40;

    /** Size of the level panel. */
    private static final int PANEL_SIZE = 4;

    /** two digit. */
    private static final int DOUBLE_DIGIT = 10;

    /** width. */
    private static final int WIDTH_GAP = 20;

    /** height. */
    private static final int HEIGHT_GAP = 10;

    /** Empty string. */
    private static final String EMPTY_S = "";

    /** Width of the level panel. */
    private final int myPanelWidth;

    /** Height of the level panel. */
    private final int myPanelHeight;

    /** Current level of the game. */
    private int myLevel;

    /** Current score of the game. */
    private int myScore;

    /** tetris. */
    private final TetrisGUI myTetrisGUI;

    /**
     * Constructor.
     *
     * @param theTileSize the size of the tiles of the level panel
     * @param theTetrisGUI tetris gui
     */
    public LevelPanel(final int theTileSize, final TetrisGUI theTetrisGUI) {
        super();
        this.myTetrisGUI = theTetrisGUI;
        myPanelWidth = theTileSize * PANEL_SIZE;
        myPanelHeight = theTileSize * PANEL_SIZE;

    }

    /**
     * Set the level.
     * 
     * @param theLevel the level.
     */
    public void setLevel(final int theLevel) {
        myLevel = theLevel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);

        final Graphics2D g2d = (Graphics2D) theGraphics;

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                             RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // set origin of the level panel
        final int pPanelX = (this.getWidth() - myPanelWidth) / 2;
        final int pPanelY = (this.getWidth() - myPanelHeight) / 2;
        g2d.translate(pPanelX, pPanelY);

        // draw a black background for the level panel
        final Rectangle2D bg = new Rectangle2D.Double(0, 0, myPanelWidth, myPanelHeight);
        g2d.setColor(Color.BLACK);
        g2d.fill(bg);

        // Changing the color and the font size
        g2d.setColor(Color.YELLOW);
        g2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, FONT_SIZE));

        final int tempValue = myLevel;

        myLevel = myScore / LEVEL_CONST;
        // Drawing the string to the panel
        if (myLevel < DOUBLE_DIGIT) {
            g2d.drawString("0" + myLevel, myPanelWidth / 2 - WIDTH_GAP,
                           myPanelHeight / 2 + HEIGHT_GAP);
        }
        else {
            g2d.drawString(EMPTY_S + myLevel, myPanelWidth / 2 - WIDTH_GAP,
                           myPanelHeight / 2 + HEIGHT_GAP);
        }

        if (tempValue < myLevel) {
            myTetrisGUI.increaseLevel();
            // System.out.println("Increasing level");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final Observable theObj, final Object theArg) {
        if (theObj instanceof Board && theArg instanceof Board.CompletedLines) {

            final int lines = ((Board.CompletedLines) theArg).getCompletedLines();
            myScore += lines;
        }
        repaint();
    }

    /**
     * Get level.
     * 
     * @return level.
     */
    public int getLevel() {
        return myLevel;
    }

}
