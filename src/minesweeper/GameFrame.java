package minesweeper;

import javax.swing.*;

import java.awt.*;
import java.util.ResourceBundle;

public class GameFrame extends JFrame {
    // Menu Bar GUI Elements
    private JMenuBar menuBar;
    private JMenu optionMenu;
    private JMenuItem preferenceSubMenu;
    private JMenuItem exitSubMenu;
    private JMenuItem newGameSubMenu;
    private JMenu aboutMenu;
    private JMenuItem licenseSubMenu;
    private JMenuItem aboutSubMenu;
    private JMenuItem restartSubMenu;

    // GUI Elements
    private JPanel rootPanel;
    private JLabel statusLabel;
    private JLabel timeLabel;
    private JLabel pictureLabel;
    private Board boardPanel;
    private JPanel statusPanel;
    private JPanel statusLabelPanel;
    private JPanel timeLabelPanel;
    private JPanel pictureLabelPanel;

    private int totalMines, rows, columns;

    private ResourceBundle resourceBundle;

    public GameFrame(int width, int height, int totalMines, int rows, int columns) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        resourceBundle = Minesweeper.getResourceBundle();

        setMinimumSize(new Dimension(width, height));
        setPreferredSize(new Dimension(width, height));
        setContentPane(rootPanel);

        boardPanel.setTotalMines(totalMines);
        boardPanel.setRows(rows);
        boardPanel.setColumns(columns);
        boardPanel.setSize(columns * Board.CELL_SIZE, rows * Board.CELL_SIZE);
        boardPanel.setLayout(new GridLayout(rows, columns));
        boardPanel.gameInitialize();

        menuBar = new JMenuBar();
        optionMenu = new JMenu(resourceBundle.getString("Option"));
        restartSubMenu = new JMenuItem(resourceBundle.getString("Restart"));
        optionMenu.add(restartSubMenu);
        newGameSubMenu = new JMenuItem(resourceBundle.getString("NewGame"));
        optionMenu.add(newGameSubMenu);
        preferenceSubMenu = new JMenuItem(resourceBundle.getString("Preferences"));
        optionMenu.add(preferenceSubMenu);
        exitSubMenu = new JMenuItem(resourceBundle.getString("Exit"));
        menuBar.add(optionMenu);
        optionMenu.add(exitSubMenu);
        aboutMenu = new JMenu(resourceBundle.getString("About"));
        licenseSubMenu = new JMenuItem(resourceBundle.getString("License"));
        aboutMenu.add(licenseSubMenu);
        aboutSubMenu = new JMenuItem(resourceBundle.getString("About"));
        aboutMenu.add(aboutSubMenu);
        menuBar.add(aboutMenu);
        setJMenuBar(menuBar);

        setLocationRelativeTo(null);
        setTitle(resourceBundle.getString("SoftwareTitle"));
        pack();

        restartSubMenu.addActionListener(e -> {
            boardPanel.restartGame();
            boardPanel.redrawImages();
        });

        newGameSubMenu.addActionListener(e -> {
            boardPanel.setNewGame();
            boardPanel.redrawImages();
        });

        preferenceSubMenu.addActionListener(e -> {
            PreferencesFrame preferencesFrame = new PreferencesFrame(this);
            preferencesFrame.setVisible(true);
        });

        exitSubMenu.addActionListener(e -> dispose());

        licenseSubMenu.addActionListener(e -> {
            LicenseDialog dialog = new LicenseDialog();
            dialog.pack();
            dialog.setSize(400, 600);
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
            dialog.setResizable(false);
        });

        aboutSubMenu.addActionListener(e -> {
            AboutDialog aboutDialog = new AboutDialog();
            aboutDialog.setVisible(true);
        });

    }

    private void createUIComponents() {
        timeLabel = new JLabel("0:0.000");
        statusLabel = new JLabel("Status");
        pictureLabel = new JLabel();
        boardPanel = new Board(statusLabel, timeLabel, pictureLabel);
    }
}
