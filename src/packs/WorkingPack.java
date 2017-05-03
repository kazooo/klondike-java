package packs;

import static interfaces.Constants.*;
import interfaces.Pack;

import java.awt.*;
import java.util.EmptyStackException;
import java.util.Stack;

/**
 * Třída reprezentuje hrací balík.
 * @author Kiselevich Roman (xkisel00)
 * @author Ermak Aleksei (xermak00)
 */

public class WorkingPack implements Pack {

    private Stack<Card> stack;
    private int x;
    private int y;

    /**
     * Konstruktor pro balík. Inicializuje zásobník pro karty a nastavuje souřadnice balíku.
     * @param newX      X souřadnice balíku
     * @param newY      Y souřadnice balíku
     */
    public WorkingPack(int newX, int newY) {
        stack = new Stack<Card>();
        x = newX;
        y = newY;
    }

    @Override
    public void showTopCard() {
        if(!stack.isEmpty())
            stack.get(stack.size()-1).turnFaceUp();
    }

    @Override
    public void hideTopCard() {
        if (!stack.isEmpty())
            stack.get(stack.size()-1).turnFaceDown();
    }

    @Override
    public void turnImages() {
        for (Card c : stack) {
            c.turnImage();
        }
    }

    /**
     * Funkce nahrává karty do balíku.
     * @param cardDeck      zásobník karet, ze kterého mame nahrat karty
     * @param reqCount      počet karet, kterých je třeba nahrat
     */
    public void loadCards(Stack<Card> cardDeck, int reqCount) {
        Card card = null;
        for (int i = 0; i < reqCount; i++) {
            card = cardDeck.pop();
            card.setPosition(x, y);
            y += INDENT;
            stack.push(card);
        }
    }

    @Override
    public Card pop() {
        try {
            if (stack.size() == 1)
                y = WORK_PACK_LEVEL;
            else y -= INDENT;
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
    public boolean put(Card card) {
        /* working pack is empty -> on the top must lie a king */
        if (stack.isEmpty()) {
            /* king have a number 13 */
            if (card.value() == 13) {
                card.setPosition(x, y);
                y += INDENT;
                return stack.push(card) != null;
            }
        }
        /* on the top must lie a card with a number less than under it */
        else {
            Card topCard = stack.get(stack.size()-1);
            int topCardValue = topCard.value();
            if ((topCardValue == (card.value() + 1)) &&
                    !topCard.similarColorTo(card)) {
                card.setPosition(x, y);
                this.y += INDENT;
                return stack.push(card) != null;
            }
        }
        return false;
    }

    @Override
    public boolean push(Card card) {
        card.setPosition(x, y);
        y += INDENT;
        return stack.push(card) != null;
    }

    @Override
    public boolean tryPut(Card card) {
        if (stack.isEmpty()){
            if (card.value() == 13)
                return true;
        }
        else {
            Card topCard = stack.get(stack.size()-1);
            int topCardValue = topCard.value();
            if ((topCardValue == (card.value() + 1)) &&
                    !topCard.similarColorTo(card)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void draw(Graphics brush) {
        if (stack.isEmpty()) {
            brush.setColor(Color.WHITE);
            brush.drawRect(x, y, CARD_WIDTH, CARD_HEIGHT);
        }
        for (int i = 0; i <= stack.size()-1; i++) {
            stack.get(i).draw(brush);
        }
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public Card get(int index) {
        if (index < 0) return null;
        Card card = stack.get(index);
        if (card.isTurnedFaceUp())
            return card;
        else return null;
    }
}
