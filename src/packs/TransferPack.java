package packs;

import interfaces.Pack;

import java.awt.*;
import java.util.ArrayList;

/**
 * Třída reprezentuje balík pro přenos karet.
 * @author Kiselevich Roman (xkisel00)
 * @author Ermak Aleksei (xermak00)
 */

public class TransferPack implements Pack {

    private ArrayList<Card> list;
    private int x;
    private int y;

    /**
     * Konstruktor pro balík. Nastavuje souřadnice balíku.
     * @param newX	X souřadnice balíku
     * @param newY	Y souřadnice balíku
     */
    public TransferPack(int newX, int newY) {
        list = new ArrayList<Card>();
        x = newX;
        y = newY;
    }

    @Override
    public boolean put(Card card) {
        card.setPosition(x, y);
        list.add(0, card);
        return true;
    }

    @Override
    public boolean push(Card card) {
        return false;
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
        return list.remove(0);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public Card get(int index) {
        return list.get(index);
    }

    @Override
    public void showTopCard() {

    }

    @Override
    public void hideTopCard() {

    }

    @Override
    public void turnImages() {

    }

    @Override
    public void draw(Graphics brush) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).draw(brush);
        }
    }
}
