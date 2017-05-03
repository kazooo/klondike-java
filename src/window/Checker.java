package window;

import static interfaces.Constants.*;

import interfaces.Pack;
import packs.Card;
import packs.GameBoard;
import packs.TargetPack;
import packs.WorkingPack;

import java.awt.*;
import java.util.ArrayList;

/**
 * Pomocná třída pro kontrolu možných tahů.
 * @author Kiselevich Roman (xkisel00)
 * @author Ermak Aleksei (xermak00)
 */

public class Checker {

    private Graphics2D brush;
    private ArrayList<WorkingPack> workingPacks;
    private ArrayList<TargetPack> targetPacks;

    private Card srcCard;
    private Card dstCard;
    private int srcCount;

    private long begin;
    private long interval;

    /**
     * Konstruktor pro pomocnou třídu.
     * @param br    "štěteček" pro vyčlenění možného tahu
     */
    public Checker(Graphics2D br) {
        brush = br;
        srcCard = null;
        dstCard = null;
        srcCount = -1;
        interval = 1000000 * 5000;
    }

    /**
     * Funkce vyhledává možný tah.
     * @param gb    hrací plátno
     */
    public void check(GameBoard gb) {

        workingPacks = gb.getWorkingPacks();
        targetPacks = gb.getTargetPacks();

        /* check working packs */
        WorkingPack wPack = null;
        for (int i = 0; i < WORK_PACKS_COUNT; i++) {
            wPack = workingPacks.get(i);
            if (findPlaceInWorkingPacks(wPack, i)) return;
        }
        /* check target packs */
        TargetPack tPack = null;
        for (int i = 0; i < TARG_PACKS_COUNT; i++) {
            tPack = targetPacks.get(i);
            if (findPlaceInTargetPacks(tPack)) return;;
        }

    }

    /**
     * Funkce výhledává možný tah ve hracích balíkach.
     * @param pack          hhrací balík, pro karty kterého výhledáváme možný tah
     * @param packNumber    číslo balíku
     * @return              true, pokud funkce našla tah, jinak false
     */
    private boolean findPlaceInWorkingPacks(Pack pack, int packNumber) {
        int size = pack.size()-1;
        if (size < 0) return false;
        Card card = pack.get(size);
        Card dstCard;
        /* check each card in pack */
        while ((size-- >= 0) && (card != null)) {
            /* check it for each pack in packsList */
            for (int i = 0; i < WORK_PACKS_COUNT; i++) {
                /* the same pack -> skip it */
                if (i == packNumber) continue;
                Pack other = workingPacks.get(i);
                /* only top card */
                if (other.tryPut(card)) {  /* found!!! */
                    dstCard = other.get(other.size()-1);
                    if (dstCard == null) {
                        dstCard = new Card(Card.Color.DIAMONDS, 0);
                        dstCard.setPosition(other.getX(), other.getY());
                    }
                    saveData(card, cardsCount(pack, card), dstCard);
                    return true;
                }
            }
            card = pack.get(size);
        }
        return false;
    }

    /**
     * Funkce výhledává možný tah v cílových políčkach.
     * @param pack      cílové políčko
     * @return          true, pokud funkce našla tah, jinak false
     */
    private boolean findPlaceInTargetPacks(TargetPack pack) {
        WorkingPack wPack;
        Card card;
        Card dstCard;
        int size;
        /* check putting into working pack */
        for (int i = 0; i < WORK_PACKS_COUNT; i++) {
            wPack = workingPacks.get(i);
            size = pack.size()-1;
            if (size < 0) continue;
            card = pack.get(size);
            if (wPack.tryPut(card)) {
                dstCard = wPack.get(wPack.size()-1);
                if (dstCard == null) {
                    dstCard = new Card(Card.Color.DIAMONDS, 0);
                    dstCard.setPosition(wPack.getX(), wPack.getY());
                }
                saveData(card, 0, dstCard);
                return true;
            }
        }
        /* check putting into target pack */
        for (int i = 0; i < WORK_PACKS_COUNT; i++) {
            wPack = workingPacks.get(i);
            size = wPack.size()-1;
            if (size < 0) continue;
            card = wPack.get(size);
            if (pack.tryPut(card)) {
                dstCard = pack.get(pack.size()-1);
                if (dstCard == null) {
                    dstCard = new Card(Card.Color.DIAMONDS, 0);
                    dstCard.setPosition(pack.getX(), pack.getY());
                }
                saveData(card, 0, dstCard);
                return true;
            }
        }
        return false;
    }

    /**
     * Funkce vyčlenuje balíky, mezí kterými je možně provest tah.
     * @param brush     "štěteček" pro vyčlenění
     */
    public void draw(Graphics2D brush) {
        if (srcCount < 0 || srcCard == null || dstCard == null) return;
        long diff = System.nanoTime();
        if ((diff - begin) >= interval) {
            begin = 0;
            srcCount = -1;
            srcCard = null;
            dstCard = null;
            return;
        }
        brush.setColor(Color.YELLOW);
        brush.drawRect(srcCard.getX(), srcCard.getY(), CARD_WIDTH, CARD_HEIGHT+(srcCount*INDENT));
        brush.drawRect(dstCard.getX(), dstCard.getY(), CARD_WIDTH, CARD_HEIGHT);
    }

    /**
     * Funkce schraňuje informace o balíkach.
     * @param srcCard   karta z balíku, kterou je možně přenest
     * @param srcCount  počet karet, kterých je možně přenest
     * @param dstCard   karta z balíku, do kterého je možně kartu přenest
     */
    private void saveData(Card srcCard, int srcCount, Card dstCard) {
        this.srcCount = srcCount;
        this.dstCard = dstCard;
        this.srcCard = srcCard;
        begin = System.nanoTime();
    }

    /**
     * Funkce počítá kolik karet je třeba přenest.
     * @param pack      balík, ve kterém funkce počítá
     * @param card      karta, až do kterou funkce počítá
     * @return          počet karet, kterých je třeba přenest
     */
    private int cardsCount(Pack pack, Card card) {
        int count = 0;
        int size = pack.size()-1;
        while (pack.get(size--) != card) {
            count++;
        }
        return count;
    }
}
