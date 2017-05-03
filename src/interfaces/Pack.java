package interfaces;

import packs.Card;

import java.awt.*;
import java.io.Serializable;

/**
 * Základní rozhraní pro balíky kart.
 * @author Kiselevich Roman (xkisel00)
 * @author Ermak Aleksei (xermak00)
 */

public interface Pack extends Serializable {
    /**
     * Funkce vloží kartu na vrchol balíku podle pravidel hry.
     * @param card  karta, kterou je třeba vložit
     * @return      výsledek vkladání
     */
    public boolean put(Card card);

    /**
     * Funkce vloží kartu na vrchol balíku bez ohledu na pravidla hry.
     * @param card  karta, kterou je třeba vložit
     * @return      výsledek vkladání
     */
    public boolean push(Card card);

    /**
     * Funkce pokusí vložit kartu na vrchol balíku, ale nevloží jí úplně.
     * @param card  karta, kterou je třeba pokusit vložit
     * @return      výsledek pokusu
     */
    public boolean tryPut(Card card);

    /**
     * Funkce vrátí X souřadnice balíku.
     * @return  X souřadnice balíku
     */
    public int getX();

    /**
     * Funkce vrátí Y souřadnice balíku.
     * @return  X souřadnice balíku
     */
    public int getY();

    /**
     * Funkce odebere kartu z vrcholu balíku.
     * @return  odebraná karta
     */
    public Card pop();

    /**
     * Funkce vrátí počet karet v balíku.
     * @return  počet karet v balíku
     */
    public int size();

    /**
     * Funkce vrátí kartu ze zadaného indexu.
     * @param index  index karty v balíku
     * @return       karta ze zadaného indexu
     */
    public Card get(int index);

    /**
     * Funkce ukáže obrázek karty na vrcholu balíku.
     */
    public void showTopCard();

    /**
     * Funkce skryje obrazek karty na vrcholu balíku.
     */
    public void hideTopCard();

    /**
     * Funkce nastaví obrázky pro všechny karty v balíku pokud nejsou.
     * Jinak nastaví je na null.
     */
    public void turnImages();

    /**
     * Funkce vykresluje karty v balíku.
     * @param brush     "štěteček" pro vykreslení
     */
    public void draw(Graphics brush);
}
