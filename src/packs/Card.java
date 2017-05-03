package packs;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/**
 * Třída reprezentující jednu kartu. Karta obsahuje informaci o své hodnotě (1 až 13) a barvě.
 * Tyto informace jsou nastaveny konstruktorem.
 * Hodnota 1 reprezentuje eso (ace), 11 až 13 postupně kluk (jack), královna (queen) a král (king).
 * Barvu definuje výčtový typ Color.
 * @author Ermak Aleksei (xermak00)
 * @author Kiselevich Roman (xkisel00)
 */

public class Card implements Serializable{
    /**
     * Výčtový typ reprezentující barvu karty.
     */
    public enum Color {
        CLUBS, DIAMONDS, HEARTS, SPADES;
        @Override
        public String toString() {
            switch (name()) {
                case "CLUBS": return "C";
                case "DIAMONDS": return "D";
                case "HEARTS": return "H";
                case "SPADES": return "S";
                default : return null;
            }
        }
    }

    private int value;
    private Card.Color color;
    private boolean faceUp;
    private int x;
    private int y;
    private Image img;

    /**
     * Konstruktor pro kartu. Nastavuje barvu a hodnotu karty.
     * Hodnota 1 reprezentuje eso (ace), 11 až 13 postupně kluk (jack), královna (queen) a král (king).
     * @param c         barva karty
     * @param value     hodnota karty
     */
    public Card(Card.Color c, int value) {
        this.value = value;
        this.color = c;
        this.faceUp = false;
        this.img = getImage(c, value);
    }

    /**
     * Funkce nastavuje pozice karty.
     * @param x     X souřadnice karty
     * @param y     Y souřadnice karty
     */
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Funkce nastaví obrázek pro kartu pokud není, jinak nastaví na null.
     */
    public void turnImage() {
        if (this.img != null)
            this.img = null;
        else {
            this.img = getImage(this.color, this.value);
        }
    }

    /**
     * Funkce získává obrázek pro kartu podle barvy a hodnoty.
     * @param col       barva karty
     * @param value     hodnota karty
     * @return          obrázek pro kartu
     */
    public Image getImage(Card.Color col, int value) {

        String imgName = null;
        if (this.faceUp)
            imgName = "" + col + value + ".png";
        else
            imgName = "back.png";
        String pathToImage = "lib/images/cards/" + imgName;
        Image returnImage = null;
        try {
            returnImage = ImageIO.read(new File(pathToImage));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnImage;
    }

    /**
     * Funkce vykresluje obrázek karty.
     * @param brush     "štěteček" pro vykreslení
     */
    public void draw(Graphics brush) {
        brush.drawImage(this.img, this.x, this.y,null);
    }

    /**
     * Funkce vrátí barvu karty.
     * @return      barva karty
     */
    public Card.Color color() {
        return this.color;
    }

    /**
     * Funkce vrátí hodnotu karty.
     * @return      hodnota karty
     */
    public int value() {
        return this.value;
    }

    /**
     * Funkce kontroluje je li karta obracená obrázkem nahoru.
     * @return      true jeli karta obracená obrázkem nahoru, jinak false
     */
    public boolean isTurnedFaceUp() {
        return this.faceUp;
    }

    /**
     * Funkce obrátí kartu obrázkem nahoru.
     */
    public void turnFaceUp() {
        if (!this.faceUp) {
            this.faceUp = true;
            this.img = getImage(this.color, this.value);
        }
    }

    /**
     * Funkce obrátí kartu obrázkem dolů.
     */
    public void turnFaceDown() {
        if (this.faceUp) {
            this.faceUp = false;
            this.img = getImage(this.color, this.value);
        }
    }


    /**
     * Funkce porovnává karty podle barvy a hodnoty.
     * @param c     karta, se kterou je třeba porovnat
     * @return      true jsouli karty stejné barvy a se stejnými hodnotami, jinak false
     */
    public boolean similarColorTo(Card c) {
        boolean areSimilar = false;
        switch (this.color) {
            case DIAMONDS:
            case HEARTS:
                if (c.color() == Card.Color.DIAMONDS ||
                        c.color() == Card.Color.HEARTS)
                    areSimilar = true;
                break;
            case CLUBS:
            case SPADES:
                if (c.color() == Card.Color.CLUBS ||
                        c.color() == Card.Color.SPADES)
                    areSimilar = true;
                break;
        }
        return areSimilar;
    }

    /**
     * Funkce vrátí X souřadnice karty.
     * @return      X souřadnice karty
     */
    public int getX() {
        return this.x;
    }

    /**
     * Funkce vrátí Y souřadnice karty.
     * @return      Y souřadnice karty
     */
    public int getY() {
        return this.y;
    }

    @Override
    public boolean equals(Object card) {
        if (card == null) return false;
        if (card == this) return true;
        if (!(card instanceof Card)) return false;
        Card eq_card = (Card) card;
        if ((this.value == eq_card.value()) && (this.color == eq_card.color())) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.color.hashCode();
        result = prime * result + this.value;
        return result;
    }

    @Override
    public String toString() {
        String range;
        switch (this.value) {
            case 1: range = "A"; break;
            case 11: range = "J"; break;
            case 12: range = "Q"; break;
            case 13: range = "K"; break;
            default: range = String.valueOf(this.value);
        }
        return range + "(" + String.valueOf(this.color) + ")";
    }
}
