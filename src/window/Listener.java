package window;

import interfaces.Constants;
import interfaces.Pack;
import packs.Card;
import packs.GameBoard;

import java.awt.event.*;

/**
 * Třída reprezentuje herního posluhače.
 * @author Kiselevich Roman (xkisel00)
 * @author Ermak Aleksei (xermak00)
 */

public class Listener implements Constants, MouseListener, MouseMotionListener, KeyListener {

    private GamePanel gamePanel;
    private GameBoard gameBoard;
    private Memory past;
    private Pack prevPack;
    private Card card;

    private int mouseX;
    private int mouseY;

    /**
     * Konstruktor posluchače.
     * @param gp        okno s hracím plátnem
     * @param pst       pomocná třída pro uložení tahů
     */
    public Listener(GamePanel gp, Memory pst) {
        card = null;
        past = pst;
        gamePanel = gp;
    }

    public void setGameBoard(GameBoard gb) {
        gameBoard = gb;
    }

    /**
     * Funkce získává souřadnice kurzoru myši.
     * @param mouseEvent    děj myši
     */
    private void getCoordinates(MouseEvent mouseEvent) {
        mouseX = mouseEvent.getX();
        mouseY = mouseEvent.getY();
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if (gameBoard == null || gameBoard.isPaused()) return;
        getCoordinates(mouseEvent);
        if ((mouseX >= SRC_STACK_X && mouseX <= SRC_STACK_X + CARD_WIDTH) &&
                (mouseY >= SRC_STACK_Y && mouseY <= SRC_STACK_Y + CARD_HEIGHT)) {
            Pack src = gameBoard.getStack(mouseX, mouseY);
            Pack dst = gameBoard.getStack(DST_STACK_X, DST_STACK_Y);
            Card c = src.pop();
            if (c != null) {
                dst.push(c);
                past.createUndo(src, dst, c);
            } else {
                while (dst.size() > 0)
                    src.push(dst.pop());
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        if (gameBoard == null || gameBoard.isPaused()) return;
        getCoordinates(mouseEvent);
        Pack pack = gameBoard.getStack(mouseX, mouseY);
        if (pack != null) {
            Card c = gameBoard.getCard(pack, mouseX, mouseY);
            if (c != null) {
                prevPack = pack;
                card = c;
                gameBoard.createTransferPack(pack, c);
                gameBoard.draggedPack(mouseX, mouseY);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        if (gameBoard == null || gameBoard.isPaused()) return;
        getCoordinates(mouseEvent);
        Pack pack = gameBoard.getStack(mouseX, mouseY);
        if (pack != null && gameBoard.transferPack(pack)){
            past.createUndo(prevPack, pack, card);
            prevPack.showTopCard();
        }
        else gameBoard.returnPack(prevPack);
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        gamePanel.setFocus();
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        if (gameBoard == null || gameBoard.isPaused()) return;
        getCoordinates(mouseEvent);
        gameBoard.draggedPack(mouseX, mouseY);
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {

    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        int key = keyEvent.getKeyChar();
        if (key == 27 && gameBoard != null) {
            if (gamePanel.buttonsAreShowing())
                gamePanel.hideButtons();
            else
                gamePanel.showButtons();
        }
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {

    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }
}
