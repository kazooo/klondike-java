package packs;

import static interfaces.Constants.*;
import interfaces.Pack;

import java.awt.*;
import java.io.Serializable;
import java.util.*;

/**
 * Třída reprezentuje hrací plátno. Obsahuje v sobě všechny prvky hry.
 * @author Kiselevich Roman (xkisel00)
 * @author Ermak Aleksei (xermak00)
 */

public class GameBoard implements Serializable {

    private boolean win;
    private boolean pause;

    private TransferPack transfer;
    private SourcePack srcPack;
    private DestinationPack dstPack;
    private ArrayList<TargetPack> targetArea;
    private ArrayList<WorkingPack> workingArea;

    /**
     * Konstruktor pro plátno. Inicializuje seznamy s hracími sloupci
     * a nastavuje proměnné v původní stav.
     */
    public GameBoard(){

        win = false;
        pause = true;
        transfer = null;
        srcPack = new SourcePack();
        dstPack = new DestinationPack();
        targetArea = new ArrayList<TargetPack>();
        workingArea = new ArrayList<WorkingPack>();

        newGame();
    }

    /**
     * Funkce vytváří balík s kartami a rozmísťuje je do hracích sloupců.
     */
    public void newGame() {
        /* create card deck and mix cards in it */
        Stack<Card> cardDeck = KlondikeFactory.createStandartDeck();
        Collections.shuffle(cardDeck);

        /* distribute cards between packs */
        int x = INDENT*3;
        for (int i = 0; i < WORK_PACKS_COUNT; i++) {
            WorkingPack newPack = new WorkingPack(x, WORK_PACK_LEVEL);
            newPack.loadCards(cardDeck, i + 1);
            newPack.showTopCard();
            workingArea.add(newPack);
            x += WORK_PACK_STEP;
        }

        /* create target packs */
        x = TARG_PACK_BEG;
        for (Card.Color color : Card.Color.values()) {
            targetArea.add(new TargetPack(color, x, TARG_PACK_LEVEL));
            x += TARG_PACK_STEP;
        }

        /* move other cards to srcStacker */
        srcPack.loadCards(cardDeck);
    }

    /**
     * Funkce nastaví obrázky pro všechny karty v balíkach a sloupcích pokud není,
     * jinak nastaví je na null.
     */
    public void turnImages() {
        for (Pack pack : workingArea) {
            pack.turnImages();
        }
        for (Pack pack : targetArea) {
            pack.turnImages();
        }
        srcPack.turnImages();
        dstPack.turnImages();
    }

    /**
     * Funkce vyhledává kartu v balíku podle zadaných souřadnic.
     * @param stack     balík s kartami
     * @param x         X souřadnice hledané karty
     * @param y         Y souřadnice hledané karty
     * @return          nalezená karta v případě úspěchu, jinak null
     */
    public Card getCard(Pack stack, int x, int y) {
        Card card = null;
        for (int i = stack.size()-1; i >= 0; i--) {
            card = stack.get(i);
            if (card != null && card.isTurnedFaceUp() &&
                    (x >= card.getX() && x <= card.getX() + CARD_WIDTH) &&
                    (y >= card.getY() && y <= card.getY() + CARD_HEIGHT))
                break;
            else card = null;
        }
        return card;
    }

    /**
     * Funkce vyhledává balík na hracím plátně podle zadaných souřadnic.
     * @param x     X souřadnice hledaného balíku
     * @param y     Y souřadnice hledaného balíku
     * @return      balík na hracím plátně v případě úspěchu, jinak null.
     */
    public Pack getStack(int x, int y) {
        Pack pack = null;
        if ((x >= DST_STACK_X && x <= DST_STACK_X + CARD_WIDTH) &&
                (y >= DST_STACK_Y && y <= DST_STACK_Y + CARD_HEIGHT)) {
            pack = dstPack;
        }
        else if ((x >= SRC_STACK_X && x <= SRC_STACK_X + CARD_WIDTH) &&
                (y >= SRC_STACK_Y && y <= SRC_STACK_Y + CARD_HEIGHT)) {
            pack = srcPack;
        }
        else if (x >= TARG_PACK_BEG && (y >= TARG_PACK_LEVEL && y <= TARG_PACK_LEVEL + CARD_HEIGHT)){
            int xTar;
            for (int i = 0; i < TARG_PACKS_COUNT; i++) {
                xTar = targetArea.get(i).getX();
                if ((x >= xTar && x <= xTar + CARD_WIDTH))
                    pack = targetArea.get(i);
            }
        }
        else if (y >= WORK_PACK_LEVEL) {
            int xWork;
            for (int i = 0; i < WORK_PACKS_COUNT; i++) {
                xWork = workingArea.get(i).getX();
                if (x >= xWork && x <= xWork + CARD_WIDTH)
                    pack = workingArea.get(i);
            }
        }
        return pack;
    }

    /**
     * Funkce vytváří balík pro přenos kart mezí ostatními baliky,
     * a také naplňuje ho kartami, které chce hráč vzít.
     * @param fromPack      balík, ze kterého bereme karty
     * @param fromCard      karta, až po kterou naplňuje výsledný balík
     */
    public void createTransferPack(Pack fromPack, Card fromCard) {
        if (win || pause) return;
        transfer = new TransferPack(fromCard.getX(), fromCard.getY());
        Card card = null;
        do {
            card = fromPack.pop();
            transfer.put(card);
        } while (card != fromCard);
    }

    /**
     * Funkce ukládá karty z balíku pro přenos kart do hracího balíku.
     * @param toPack    hrací balík
     * @return          true, pokud se podařilo přemístit všechny karty, jinak false
     */
    public boolean transferPack(Pack toPack) {
        if (transfer == null) return false;
        if (toPack instanceof TargetPack && transfer.size() > 1)
            return false;

        while (transfer.size() > 0) {
            Card card = transfer.pop();
            if (!toPack.put(card)){
                transfer.put(card);
                return false;
            }
        }
        transfer = null;
        return true;
    }

    /**
     * Funkce vrátí karty z balíku pro přenos kart do hracího balíku odkud oni byly odebrány.
     * @param toPack    původní balík, odkud karty byly odebrány.
     */
    public void returnPack(Pack toPack) {
        if (transfer == null) return;
        Card card;
        while (transfer.size() > 0) {
            card = transfer.pop();
            toPack.push(card);
        }
        return;
    }

    /**
     * Funkce vyměňuje souřadnice každé karty za nové, které odpovídají souřadnicím kurzoru myši.
     * @param x     X souřadnice kurzoru myši
     * @param y     Y souřadnice kurzoru myši
     */
    public void draggedPack(int x, int y) {
        if (transfer == null) return;
        for (int i = transfer.size()-1; i >= 0 ; i--) {
            Card card = transfer.get(i);
            card.setPosition(x-CARD_WIDTH/2, y-CARD_HEIGHT/2+(INDENT * i));
        }
    }

    /**
     * Funkce zjišťuje vyhral-li hráč.
     * @return      true, pokud hráč vyhral, jinak false
     */
    private boolean gameWin() {
        boolean victory = true;
        for (TargetPack target : targetArea) {
            if (target.get() == null) {
                victory = false;
                break;
            }
            if (target.get().value() != 13)
                victory = false;
        }
        return victory;
    }

    /**
     * Funkce nastaví pauzu ve hře pokud není nastavená, jinak spustí hru.
     */
    public void setPause() {
        if (pause) pause = false;
        else pause = true;
    }

    /**
     * Funkce zjišťuje nastavená-li hra na pauzu.
     * @return      true, pokud hra nastavená na pauzu, jinak false
     */
    public boolean isPaused() {
        return pause;
    }

    /**
     * Funkce vrátí stav hry.
     * @return      true, pokud hráč vyhral hru, jinak false
     */
    public boolean win() { return win; }

    /**
     * Funkce vykresluje všechny prvky hracího plátna.
     * @param brush     "štěteček" pro vykreslení
     */
    public void draw(Graphics brush) {
        if (gameWin()) {
            win = true;
        }
        for (int i = 0; i < WORK_PACKS_COUNT; i++) {
              workingArea.get(i).draw(brush);
        }
        for (int i = 0; i < TARG_PACKS_COUNT; i++) {
            targetArea.get(i).draw(brush);
        }
        srcPack.draw(brush);
        dstPack.draw(brush);
        if (transfer != null)
            transfer.draw(brush);
    }

    /**
     * Funkce vrátí seznam hracích balíků.
     * @return      seznam hracích balíků
     */
    public ArrayList<WorkingPack> getWorkingPacks() {
        return workingArea;
    }

    /**
     * Funkce vrátí seznam cílových políčků.
     * @return      seznam cílových políčků.
     */
    public ArrayList<TargetPack> getTargetPacks() {
        return targetArea;
    }
}
