package packs;

import static interfaces.Constants.*;
import interfaces.Pack;

import java.awt.*;
import java.util.Stack;
import java.util.EmptyStackException;
import java.util.NoSuchElementException;

/**
 * Třída reprezentuje cílové políčko na hracím plátně.
 * @author Kiselevich Roman (xkisel00)
 * @author Ermak Aleksei (xermak00)
 */

public class TargetPack implements Pack {

    private Stack<Card> stack;
    private Card.Color color;
    private char symbol;
    private int x;
    private int y;

    /**
     * Konstruktor pro cílové políčko. Inicializuje zásobník pro karty,
     * barvu karet, které musí ležet na políčku, a souřadnice políčku.
     * @param newColor      barva karet, které musí ležet na políčku
     * @param newX          X souřadnice políčku
     * @param newY          Y souřadnice políčku
     */
    public TargetPack(Card.Color newColor, int newX, int newY) {
        stack = new Stack<Card>();
        color = newColor;
        symbol = getSymbol(newColor);
        x = newX;
        y = newY;
    }

    /**
     * Funkce vrátí symbol barvy pro cílové políčko.
     * @param color     barva políčku
     * @return          symbol barvy
     */
    private char getSymbol(Card.Color color) {
        switch (color) {
            case HEARTS: return '♡';
            case DIAMONDS: return '♢';
            case CLUBS: return '♧';
            case SPADES: return '♤';
            default: return 'n';
        }
    }

    @Override
    public boolean put(Card card) {
        int topCardValue;
        if (stack.isEmpty()) {
            topCardValue = 0;
        } else {
            topCardValue = stack.peek().value();
        }

        if (topCardValue == card.value() - 1 &&
                color == card.color())
        {
            card.setPosition(x, y);
            return stack.push(card) != null;
        }
        return false;
    }

    @Override
    public boolean push(Card card) {
        return false;
    }

    @Override
    public boolean tryPut(Card card) {
        int topCardValue;
        if (stack.isEmpty()) {
            topCardValue = 0;
        } else {
            topCardValue = stack.peek().value();
        }

        if (topCardValue == card.value() - 1 &&
                color == card.color())
            return true;
        return false;
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

    /**
     * Funkce vrátí kartu na vrcholu cílového políčka.
     * @return      karta na vrcholu cílového políčka
     */
    public Card get() {
        try {
            return stack.lastElement();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public Card get(int index) {
        try {
            return stack.get(index);
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public void showTopCard() {
        stack.get(stack.size()-1).turnFaceUp();
    }

    @Override
    public void hideTopCard() {
        stack.get(stack.size()-1).turnFaceDown();
    }

    @Override
    public void turnImages() {
        for (Card c : stack) {
            c.turnImage();
        }
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void draw(Graphics brush) {
        if (stack.size() > 0)
            stack.get(stack.size()-1).draw(brush);
        else {
            brush.setColor(Color.WHITE);
            brush.drawRect(x, y, CARD_WIDTH, CARD_HEIGHT);
            brush.setFont(new Font("Arial", Font.BOLD, 20));
            brush.drawString(String.valueOf(symbol), x+CARD_WIDTH/2-8, y+CARD_HEIGHT/2);
        }
    }
}
