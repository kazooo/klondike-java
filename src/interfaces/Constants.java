package interfaces;

import java.awt.*;

/**
 * Základní konstanty používané v programu.
 * @author Kiselevich Roman (xkisel00)
 * @author Ermak Aleksei (xermak00)
 */

public interface Constants {

    final static int SCREEN_WIDTH = ((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth()-50) / 2;
    final static int SCREEN_HEIGHT = ((int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()-50) / 2;

    final static int CARD_WIDTH = 71;
    final static int CARD_HEIGHT = 95;
    final static int INDENT = 20;

    final static int WORK_PACKS_COUNT = 7;
    final static int TARG_PACKS_COUNT = 4;

    final static int WORK_PACK_STEP = SCREEN_WIDTH / 8;
    final static int WORK_PACK_LEVEL = SCREEN_HEIGHT / 2 - 50;

    final static int TARG_PACK_BEG = SCREEN_WIDTH - (CARD_WIDTH + INDENT) * TARG_PACKS_COUNT;
    final static int TARG_PACK_STEP = CARD_WIDTH + INDENT;
    final static int TARG_PACK_LEVEL = INDENT;

    final static int SRC_STACK_X = INDENT;
    final static int SRC_STACK_Y = INDENT;
    final static int DST_STACK_X = SRC_STACK_X + CARD_WIDTH + INDENT;
    final static int DST_STACK_Y = SRC_STACK_Y;

    final static int BUTTON_WIDTH = 150;
    final static int BUTTON_HEIGHT = 30;

    final static int MENU_X = (SCREEN_WIDTH / 2) - (BUTTON_WIDTH / 2);
    final static int MENU_Y = (SCREEN_HEIGHT / 2);

    final static int UNDO_BUT_X = DST_STACK_X + CARD_WIDTH + (TARG_PACK_BEG - (DST_STACK_X + CARD_WIDTH))/4;
    final static int UNDO_BUT_Y = DST_STACK_Y + 3;

}
