package window;

import interfaces.Pack;
import packs.Card;
import packs.GameBoard;

import java.io.*;
import java.util.ArrayList;

/**
 * Pomocná třída pro uložení tahů.
 * @author Kiselevich Roman (xkisel00)
 * @author Ermak Aleksei (xermak00)
 */

public class Memory {

    private ArrayList<Pack> source;
    private ArrayList<Pack> destination;
    private ArrayList<Card> card;

    /**
     * Konstruktor pomocné třídy. Inicializuje zásobníky pro ukložení tahů.
     */
    public Memory() {
        source = new ArrayList<Pack>();
        destination = new ArrayList<Pack>();
        card = new ArrayList<Card>();
    }

    /**
     * Funkce ukládá tah.
     * @param src   balík, ze kterého vzali kartu
     * @param dst   balík, do kterého vložili kartu
     * @param c     karta, kterou přemistili
     */
    public void createUndo(Pack src, Pack dst, Card c) {
        source.add(src);
        destination.add(dst);
        card.add(c);
    }

    /**
     * Funkce děla operace undo.
     */
    public void back() {
        int last = source.size()-1;
        if (last == -1) return;
        Pack src = source.remove(last);
        Pack dst = destination.remove(last);
        Card c = card.remove(last);
        Card cardFromDst = null;
        ArrayList<Card> tmp = new ArrayList<Card>();
        src.hideTopCard();
        do {
            cardFromDst = dst.pop();
            tmp.add(cardFromDst);
        }while(c != cardFromDst);

        while (!tmp.isEmpty()) {
            src.push(tmp.remove(tmp.size()-1));
        }
    }

    /**
     * Funkce ukládá hru.
     * @param file          soubor, do kterého ukladáme hru
     * @param board         hra
     * @throws IOException  chyba při otevření souboru
     */
    public void saveGame(File file, GameBoard board) throws IOException {
        if (file == null) return;
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);

        board.turnImages();

        oos.writeObject(board);

        board.turnImages();

        oos.flush();
        oos.close();

        fos.flush();
        fos.close();
    }

    /**
     * Funkce načítává uloženou hru.
     * @param file                      soubor s uloženou hrou
     * @return                          uložená hra
     * @throws IOException              chyba při otevření souboru
     * @throws ClassNotFoundException   chyba při nenalezení třídy GameBoard
     */
    public GameBoard loadGame(File file) throws IOException, ClassNotFoundException {
        if (file == null) return  null;
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream oin = new ObjectInputStream(fis);
        GameBoard gameBoard = (GameBoard) oin.readObject();
        gameBoard.turnImages();
        return gameBoard;
    }
}
