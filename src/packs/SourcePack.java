package packs;

import interfaces.Pack;

import java.awt.*;
import java.util.EmptyStackException;
import java.util.Stack;
import static interfaces.Constants.*;

/**
 * Třída reprezentuje balík pro ukládání zbylých kart.
 * Z balíku hráč nemůže vytahovat karty, jenom klikáním otevírat nové karty.
 * @author Kiselevich Roman (xkisel00)
 * @author Ermak Aleksei (xermak00)
 */

public class SourcePack implements Pack {

    private Stack<Card> stack;

    /**
     * Konstruktor pro balík. Inicializuje nový zásobník pro karty.
     */
    public SourcePack() {
        stack = new Stack<Card>();
    }

    /**
     * Funkce nahrává karty do balíku.
     * @param cardDeck      seznam karet
     */
    public void loadCards(Stack<Card> cardDeck) {
        Card card = null;
        while (!cardDeck.empty()) {
            card = cardDeck.pop();
            card.setPosition(SRC_STACK_X, SRC_STACK_Y);
            stack.push(card);
        }
    }

    @Override
    public void draw(Graphics brush) {

        if (stack.size() > 0)
            stack.get(stack.size()-1).draw(brush);

        else if (stack.size() == 0) {
            brush.setColor(Color.WHITE);
            brush.drawRect(SRC_STACK_X, SRC_STACK_Y, CARD_WIDTH, CARD_HEIGHT);

        }
    }

    @Override
    public boolean push(Card card) {
        card.setPosition(SRC_STACK_X, SRC_STACK_Y);
        stack.push(card);
        hideTopCard();
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
    public void hideTopCard() {
        Card card = stack.get(stack.size()-1);
        card.turnFaceDown();
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
    public void showTopCard() {

    }
}
