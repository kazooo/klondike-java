package packs;

import interfaces.Pack;
import static interfaces.Constants.*;

import java.awt.*;
import java.util.EmptyStackException;
import java.util.Stack;

/**
 * Třída reprezentuje balík pro ukládání zbylých kart.
 * Z balíku hráč může vytahovat karty, ale vrátit zpět nemůže.
 * @author Kiselevich Roman (xkisel00)
 * @author Ermak Aleksei (xermak00)
 */

public class DestinationPack implements Pack {

    private Stack<Card> stack;

    /**
     * Konstruktor pro balík. Inicializuje nový zásobník pro karty.
     */
    public DestinationPack() {
        stack = new Stack<Card>();
    }

    @Override
    public void draw(Graphics brush) {

        if (stack.size() > 0)
            stack.get(stack.size()-1).draw(brush);

        else {
            brush.setColor(Color.WHITE);
            brush.drawRect(DST_STACK_X, DST_STACK_Y, CARD_WIDTH, CARD_HEIGHT);
        }
    }

    @Override
    public boolean push(Card card) {
        card.setPosition(DST_STACK_X, DST_STACK_Y);
        stack.push(card);
        showTopCard();
        return true;
    }

    @Override
    public boolean tryPut(Card card) {
        return false;
    }

    @Override
    public int getX() {
        return 0;
    }

    @Override
    public int getY() {
        return 0;
    }

    @Override
    public Card pop() {
        try {
            return stack.pop();
        } catch (EmptyStackException e) {
            return null;
        }
    }

    @Override
    public int size() {
        return stack.size();
    }

    @Override
    public Card get(int index) {
        return stack.get(index);
    }

    @Override
    public void showTopCard() {
        if (stack.size()-1 >= 0) {
            Card card = stack.get(stack.size()-1);
            card.turnFaceUp();
        }
    }

    @Override
    public void turnImages() {
        for (Card c : stack) {
            c.turnImage();
        }
    }

    @Override
    public boolean put(Card card) {
        return false;
    }

    @Override
    public void hideTopCard() {

    }
}
