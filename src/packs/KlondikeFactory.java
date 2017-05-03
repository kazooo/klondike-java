package packs;

import java.util.Stack;
import java.util.stream.IntStream;

/**
 * Třída reprezentuje rozhraní pro přípravu hracího plátna.
 * @author Kiselevich Roman (xkisel00)
 * @author Ermak Aleksei (xermak00)
 */

public class KlondikeFactory {
    /**
     * Funkce vytváří novou kartu.
     * @param color     barva nové karty
     * @param value     hodnota nové karty
     * @return          nová karta, pokud hodnota je v rozmezí od 1 do 13, jinak null
     */
    public static Card createCard(Card.Color color, int value) {
        if (value > 13 || value < 1) {
            return null;
        }
        return new Card(color, value);
    }

    /**
     * Funkce vytváří standardní balík s 54 kartami.
     * @return      standardní balík s 54 kartami
     */
    public static Stack<Card> createStandartDeck() {
        Stack<Card> standartDeck = new Stack<Card>();
        for (Card.Color color : Card.Color.values()) {
            final int maxCardValue = 13;
            IntStream.rangeClosed(1, maxCardValue).forEach(val -> {
                standartDeck.push(new Card(color, val));
            });
        }
        return standartDeck;
    }
}
