package window;

import static interfaces.Constants.*;
import packs.GameBoard;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Třída reprezentuje okno, které obsahuje hrací plátno.
 * @author Kiselevich Roman (xkisel00)
 * @author Ermak Aleksei (xermak00)
 */

public class GamePanel extends JPanel implements Runnable{

    private JFrame window;
    private GameBoard gameBoard;
    private Listener listener;
    private Memory past;
    private Checker checker;

    private Thread thread;
    private BufferedImage background;
    private BufferedImage canvas;
    private Graphics2D brush;

    private Button newGame;
    private Button continueGame;
    private Button saveGame;
    private Button loadGame;
    private Button splitGame;
    private Button exitGame;

    private Button undoButton;
    private Button menuButton;
    private Button checkButton;

    private int titleX;

    /**
     * Konstruktor okna. Inicializuje potřebná preoměnné a vytváří hrací menu.
     * @param wnd       hlavní okno, které obsahuje okna s hracími plátnami
     */
    public GamePanel(JFrame wnd) {

        super();

        setBackground("lib/images/backgrounds/background_1.jpg");
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setFocusable(true);
        this.requestFocus();

        window = wnd;
        past = new Memory();
        checker = new Checker(brush);
        listener = new Listener(this, past);

        this.addMouseListener(listener);
        this.addMouseMotionListener(listener);
        this.addKeyListener(listener);

        canvas = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        brush = (Graphics2D) canvas.getGraphics();
        brush.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        this.setLayout(null);
        createMenuButtons();
        createGameButtons();
        createMenu("START");
    }

    /**
     * Funkce nastavuje X souřadnice pro titulek.
     * @param g         "štěteček" pro vykreslení
     * @param title     titulek
     */
    private void setTitleX(Graphics g, String title) {
        titleX = SCREEN_WIDTH/2 - (int)g.getFontMetrics().getStringBounds(title, g).getWidth()/2;
    }

    /**
     * Funkce nastavuje obrázek pozadí.
     * @param pathToBackground      cesta k obrázku
     */
    public void setBackground(String pathToBackground) {
        try {
            this.background = ImageIO.read(new File(pathToBackground));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Funkce spouští hru.
     */
    public void startGame() {
        this.thread = new Thread(this);
        this.thread.start();
        createMenu("PAUSE");
    }

    @Override
    public void run() {
        do {
            brush.drawImage(background, 0, 0,null);
            if (gameBoard.win()) {
                endGame();
                return;
            }
            else if (!gameBoard.isPaused()) {
                gameBoard.draw(brush);
                checker.draw(brush);
            }
            else {
                brush.setColor(Color.WHITE);
                brush.setFont(new Font("Arial", Font.BOLD, 40));
                setTitleX(brush, "Klondike");
                brush.drawString("Klondike", titleX, SCREEN_HEIGHT/4);
            }
            gameDraw();

            try { Thread.sleep(33); }
            catch (InterruptedException e) { e.printStackTrace(); }

        } while (true);
    }

    /**
     * Funkce ukončuje hru, vyvolává menu.
     */
    private void endGame() {

        listener.setGameBoard(null);

        createMenu("END");
        gameBoard.setPause();
        hideButtons();
        showMenu();

        brush.setColor(Color.WHITE);
        brush.setFont(new Font("Arial", Font.BOLD, 40));
        setTitleX(brush, "You win!!!");
        brush.drawString("You win!!!", titleX, SCREEN_HEIGHT/3);
        gameDraw();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, null);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        setTitleX(g, "Klondike");
        g.drawString("Klondike", titleX, SCREEN_HEIGHT/3);
    }

    /**
     * Funkce vykresluje celé hrací plátno.
     */
    private void gameDraw() {
        Graphics graphics = this.getGraphics();
        graphics.drawImage(canvas, 0, 0, this);
        graphics.dispose();
    }

    /**
     * Funkce rozděluje hlavní okno do 4 oken s hracímí plátnami.
     */
    private void splitScreen() {
        GameWindow.splitted = true;
        if (gameBoard != null)
            createMenu("PAUSE");
        else
            createMenu("START");
        window.setLayout(new GridLayout(2, 2));
        window.getContentPane().add(new GamePanel(window));
        window.getContentPane().add(new GamePanel(window));
        window.getContentPane().add(new GamePanel(window));
        window.pack();
        window.revalidate();
    }

    /**
     * Funkce určuje je-li menu zobrazeno.
     * @return      true, pokud je menu zobrazeno, jinak false
     */
    public boolean buttonsAreShowing() {
        return newGame.isShowing();
    }

    public void showMenu() {
        newGame.setVisible(true);
        continueGame.setVisible(true);
        loadGame.setVisible(true);
        saveGame.setVisible(true);
        splitGame.setVisible(true);
        exitGame.setVisible(true);
    }

    /**
     * Funkce skryvá menu.
     */
    public void hideMenu() {
        newGame.setVisible(false);
        continueGame.setVisible(false);
        loadGame.setVisible(false);
        saveGame.setVisible(false);
        splitGame.setVisible(false);
        exitGame.setVisible(false);
    }

    /**
     * Funkce skryvá tlačitka hry.
     */
    public void hideButtons() {
        undoButton.setVisible(false);
        menuButton.setVisible(false);
        checkButton.setVisible(false);
    }

    /**
     * Funkce zobrazuje tlačitka hry.
     */
    public void showButtons() {
        undoButton.setVisible(true);
        menuButton.setVisible(true);
        checkButton.setVisible(true);
    }

    /**
     * Funkce nastavuje fokus na okno s hracím plátnem.
     */
    public void setFocus() {
        this.requestFocus();
    }

    /**
     * Funkce vytváří hrací menu.
     * @param mode      řežim menu
     */
    private void createMenu(String mode) {
        this.removeAll();
        int y = MENU_Y;
        if (mode == "PAUSE")
            y = SCREEN_HEIGHT/3;
        newGame.setBounds(MENU_X, y, BUTTON_WIDTH, BUTTON_HEIGHT);
        y += 35;
        this.add(newGame);
        switch (mode) {
            case "START":
                loadGame.setBounds(MENU_X, y, BUTTON_WIDTH, BUTTON_HEIGHT);
                this.add(loadGame);
                y += 35;
                break;
            case "PAUSE":
                continueGame.setBounds(MENU_X, y, BUTTON_WIDTH, BUTTON_HEIGHT);
                this.add(continueGame);
                y += 35;
                saveGame.setBounds(MENU_X, y, BUTTON_WIDTH, BUTTON_HEIGHT);
                this.add(saveGame);
                y += 35;
                loadGame.setBounds(MENU_X, y, BUTTON_WIDTH, BUTTON_HEIGHT);
                this.add(loadGame);
                y += 35;
                break;
            case "END":
                loadGame.setBounds(MENU_X, y, BUTTON_WIDTH, BUTTON_HEIGHT);
                this.add(loadGame);
                y += 35;
                break;
        }
        if (!GameWindow.splitted){
            splitGame.setBounds(MENU_X, y, BUTTON_WIDTH, BUTTON_HEIGHT);
            y += 35;
            this.add(splitGame);
        }
        exitGame.setBounds(MENU_X, y, BUTTON_WIDTH, BUTTON_HEIGHT);
        this.add(exitGame);

        undoButton.setVisible(false);
        this.add(undoButton);
        menuButton.setVisible(false);
        this.add(menuButton);
        checkButton.setVisible(false);
        this.add(checkButton);

        this.revalidate();
    }

    /**
     * Funkce vytváří tlačitka menu.
     */
    private void createMenuButtons() {

        newGame = new Button("new game");
        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                gameBoard = new GameBoard();
                listener.setGameBoard(gameBoard);
                startGame();
                showButtons();
                hideMenu();
                gameBoard.setPause();
                setFocus();
            }
        });

        continueGame = new Button("continue game");
        continueGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                showButtons();
                hideMenu();
                gameBoard.setPause();
                setFocus();
            }
        });

        saveGame = new Button("save game");
        saveGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                saveGame();

                hideMenu();
                showButtons();

                setFocus();
                gameBoard.setPause();
            }
        });

        loadGame = new Button("load game");
        loadGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                GameBoard getting = loadGame();
                if (getting == null) return;
                else gameBoard = getting;
                listener.setGameBoard(gameBoard);
                startGame();
                hideMenu();
                showButtons();
                gameBoard.setPause();
                setFocus();
            }
        });

        splitGame = new Button("split screen");
        splitGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                splitScreen();
                setFocus();
            }
        });

        exitGame = new Button("exit");
        exitGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });
    }

    /**
     * Funkce vytváří hrací tlačitka.
     */
    private void createGameButtons() {
        undoButton = new Button("undo");
        undoButton.setBounds(UNDO_BUT_X, UNDO_BUT_Y+BUTTON_HEIGHT, 60, BUTTON_HEIGHT);
        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                past.back();
            }
        });

        menuButton = new Button("menu");
        menuButton.setBounds(UNDO_BUT_X, UNDO_BUT_Y, 60, BUTTON_HEIGHT);
        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                hideButtons();
                showMenu();
                gameBoard.setPause();
            }
        });

        checkButton = new Button("check");
        checkButton.setBounds(UNDO_BUT_X, UNDO_BUT_Y+BUTTON_HEIGHT*2, 60, BUTTON_HEIGHT);
        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                checker.check(gameBoard);
            }
        });
    }

    /**
     * Funkce načítává uloženou hru.
     * @return      uložená hra
     */
    private GameBoard loadGame() {
        File dir = new File("../Klondike/examples");
        JFileChooser dialog = new JFileChooser(dir);
        dialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
        dialog.setDialogType(JFileChooser.OPEN_DIALOG);
        dialog.setMultiSelectionEnabled(false);

        FileNameExtensionFilter filter = new FileNameExtensionFilter(null, "klondike");
        dialog.setFileFilter(filter);
        dialog.setFileView(new FileView() {
            @Override
            public Boolean isTraversable(File file) {
                return dir.equals(file);
            }
        });

        File file = null;
        int result = dialog.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION)
            file = dialog.getSelectedFile();

        try {
            return past.loadGame(file);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Funkce ukládá hru.
     */
    private void saveGame() {
        File dir = new File("../Klondike/examples");
        JFileChooser dialog = new JFileChooser(dir);
        dialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
        dialog.setDialogType(JFileChooser.SAVE_DIALOG);
        dialog.setMultiSelectionEnabled(false);

        FileNameExtensionFilter filter = new FileNameExtensionFilter(null, "klondike");
        dialog.setFileFilter(filter);
        dialog.setFileView(new FileView() {
            @Override
            public Boolean isTraversable(File file) {
                return dir.equals(file);
            }
        });

        File file = null;
        int result = dialog.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            file = dialog.getSelectedFile();
            String path = file.getAbsolutePath();
            String name = file.getName();
            if (!name.endsWith(".klondike"))
                file = new File(path + ".klondike");
        }

        try {
            past.saveGame(file, gameBoard);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
