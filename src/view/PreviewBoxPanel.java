/*
 * Tuan Huynh TCSS 305A - Tetris
 */

package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import model.Board;
import model.Point;
import model.TetrisPiece;

/**
 * A panel to display the next piece.
 * 
 */
@SuppressWarnings("serial")
public class PreviewBoxPanel extends JPanel implements Observer {

    /** Size of the preview box. */
    private static final int PREVIEW_BOX_SIZE = 4;

    /** Size of the tiles of the preview box. */
    private final int myTileSize;

    /** Width of the preview box. */
    private final int myBoxWidth;

    /** Height of the preview box. */
    private final int myBoxHeight;

    /** Next tetris piece. */
    private TetrisPiece myNextPiece;

    /**
     * Constructor.
     * 
     * @param theTileSize the size of the tiles of the preview box
     */
    public PreviewBoxPanel(final int theTileSize) {
        super();
        myTileSize = theTileSize;
        myBoxWidth = myTileSize * PREVIEW_BOX_SIZE;
        myBoxHeight = myTileSize * PREVIEW_BOX_SIZE;
    }

    /** {@inheritDoc} */
    @Override
    public void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);

        final Graphics2D g2d = (Graphics2D) theGraphics;

        // set origin of the preview box
        final int pBoxX = (this.getWidth() - myBoxWidth) / 2;
        final int pBoxY = (this.getWidth() - myBoxHeight) / 2;
        g2d.translate(pBoxX, pBoxY);

        // draw a black background for the preview box
        final Rectangle2D bg = new Rectangle2D.Double(0, 0, myBoxWidth, myBoxHeight);
        g2d.setColor(Color.BLACK);
        g2d.fill(bg);

        // draw the next tetris piece
        if (myNextPiece != null) {
            for (final Point point : myNextPiece.points()) {
                int x = myTileSize * point.x();
                int y = myTileSize * (myNextPiece.width() - point.y());

                // center the tetris piece horizontally if it is not an I or an
                // O piece
                if (myNextPiece != TetrisPiece.I && myNextPiece != TetrisPiece.O) {
                    x += myTileSize / 2;
                }

                // center the tetris piece vertically if it is an O or an I
                // piece
                if (myNextPiece == TetrisPiece.O) {
                    y += myTileSize;
                }
                else if (myNextPiece == TetrisPiece.I) {
                    y -= myTileSize / 2;
                }

                // fill in the tile
                g2d.setColor(myNextPiece.color());
                g2d.fill3DRect(x, y, myTileSize, myTileSize, true);
            }
        }

    }

    /** {@inheritDoc} */
    @Override
    public void update(final Observable theObj, final Object theArg) {
        if (theObj instanceof Board && theArg instanceof TetrisPiece) {
            // repaint the preview box
            final TetrisPiece nextPiece = (TetrisPiece) theArg;
            myNextPiece = nextPiece;
            repaint();
        }
    }
}
