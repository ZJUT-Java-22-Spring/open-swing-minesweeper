package minesweeper;

import javax.swing.*;

import java.util.Locale;
import java.util.ResourceBundle;

import static javax.swing.JOptionPane.showMessageDialog;

/**
 * 开始菜单、选项页面所用的 JFrame。
 *
 * @author sichengchen
 */
public class WelcomeFrame extends JFrame {
    // GUI Elements
    private JTextField mineAmountField;
    private JButton startGameButton;
    private JButton easyButton;
    private JButton expertButton;
    private JButton mediumButton;
    private JPanel mainJPanel;
    private JPanel difficultyJPanel;
    private JButton masterButton;
    private JButton meijinButton;
    private JButton customizeButton;
    private JTextField colField;
    private JTextField rowField;
    private JLabel difficultyLabel;
    private JLabel customizeDifficultyLabel;
    private JLabel mineAmountLabel;
    private JLabel columnsLabel;
    private JLabel rowsLabel;
    private JLabel languageLabel;
    private JComboBox languageComboBox;

    // Variables
    private int totalMinesToSet = 40;
    private int rowsToSet = 16;
    private int columnsToSet = 16;

    private ResourceBundle resourceBundle;

    private String[] languageStrings;

    /**
     * 构造方法。
     */
    public WelcomeFrame(int type) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        resourceBundle = Minesweeper.getResourceBundle();

        Locale localeToSelectInComboBox = Minesweeper.getLocale();
        if(localeToSelectInComboBox == Locale.US) {
            languageComboBox.setSelectedIndex(1);
        } else if(localeToSelectInComboBox == Locale.SIMPLIFIED_CHINESE) {
            languageComboBox.setSelectedIndex(2);
        } else if(localeToSelectInComboBox == Locale.JAPAN) {
            languageComboBox.setSelectedIndex(3);
        } else if(localeToSelectInComboBox == Locale.TRADITIONAL_CHINESE) {
            languageComboBox.setSelectedIndex(4);
        } else if(localeToSelectInComboBox == Minesweeper.HONG_KONG) {
            languageComboBox.setSelectedIndex(5);
        } else if(localeToSelectInComboBox == Minesweeper.WU) {
            languageComboBox.setSelectedIndex(6);
        } else {
            languageComboBox.setSelectedIndex(0);
        }

        languageComboBox.addActionListener (e -> {
            String selectedItem = (String) languageComboBox.getSelectedItem();

            assert selectedItem != null;
            if(selectedItem.equals(languageStrings[0])) {
                Minesweeper.setLocale(Locale.getDefault());
            } else if (selectedItem.equals(languageStrings[1])) {
                Minesweeper.setLocale(Locale.US);
            } else if (selectedItem.equals(languageStrings[2])) {
                Minesweeper.setLocale(Locale.SIMPLIFIED_CHINESE);
            } else if (selectedItem.equals(languageStrings[3])) {
                Minesweeper.setLocale(Locale.JAPAN);
            } else if (selectedItem.equals(languageStrings[4])) {
                Minesweeper.setLocale(Locale.TRADITIONAL_CHINESE);
            } else if (selectedItem.equals(languageStrings[5])) {
                Minesweeper.setLocale(Minesweeper.HONG_KONG);
            } else if (selectedItem.equals(languageStrings[6])) {
                Minesweeper.setLocale(Minesweeper.WU);
            }

            changeLocaleRefresh();
            this.dispose();
        });

        if(type == 0) {
            setTitle(resourceBundle.getString("Preferences"));
        }else if(type == 1) {
            setTitle(resourceBundle.getString("Welcome"));
        }

        setSize(300,480);
        setLocationRelativeTo(null);

        easyButton.setText(resourceBundle.getString("Easy"));
        mediumButton.setText(resourceBundle.getString("Medium"));
        expertButton.setText(resourceBundle.getString("Expert"));
        masterButton.setText(resourceBundle.getString("Master"));
        meijinButton.setText(resourceBundle.getString("Meijin"));
        customizeButton.setText(resourceBundle.getString("Customize"));
        startGameButton.setText(resourceBundle.getString("StartGame"));
        difficultyLabel.setText(resourceBundle.getString("Difficulty"));
        customizeDifficultyLabel.setText(resourceBundle.getString("CustomizeDifficulty"));
        mineAmountLabel.setText(resourceBundle.getString("MineAmount"));
        columnsLabel.setText(resourceBundle.getString("Columns"));
        rowsLabel.setText(resourceBundle.getString("Rows"));
        languageLabel.setText(resourceBundle.getString("Language"));

        setContentPane(mainJPanel);

        mineAmountField.setText(String.valueOf(Minesweeper.mineAmountConf));
        mineAmountField.setEnabled(false);
        colField.setText(String.valueOf(Minesweeper.colConf));
        colField.setEnabled(false);
        rowField.setText(String.valueOf(Minesweeper.rowConf));
        rowField.setEnabled(false);

        //mediumButton.setEnabled(false);

        startGameButton.addActionListener(e -> {
            boolean status = startGame();
            if(status) {
                dispose();
            }
        });

        mediumButton.addActionListener(e -> {
            setTextFieldsDisable();
            //mediumButton.setEnabled(false);
            Minesweeper.mineAmountConf = 40;
            Minesweeper.colConf = 16;
            Minesweeper.rowConf = 16;
            setConfStrings();
        });

        easyButton.addActionListener(e -> {
            setTextFieldsDisable();
            //easyButton.setEnabled(false);
            Minesweeper.mineAmountConf = 10;
            Minesweeper.colConf = 9;
            Minesweeper.rowConf = 9;
            setConfStrings();
        });

        expertButton.addActionListener(e -> {
            setTextFieldsDisable();
            //expertButton.setEnabled(false);
            Minesweeper.mineAmountConf = 100;
            Minesweeper.colConf = 30;
            Minesweeper.rowConf = 16;
            setConfStrings();
        });

        masterButton.addActionListener(e -> {
            setTextFieldsDisable();
            //masterButton.setEnabled(false);
            Minesweeper.mineAmountConf = 200;
            Minesweeper.colConf = 50;
            Minesweeper.rowConf = 24;
            setConfStrings();
        });

        meijinButton.addActionListener(e -> {
            setTextFieldsDisable();
            //meijinButton.setEnabled(false);
            Minesweeper.mineAmountConf = 300;
            Minesweeper.colConf = 60;
            Minesweeper.rowConf = 40;
            setConfStrings();
        });

        customizeButton.addActionListener(e -> {
            //setTextFieldsDisable();
            mineAmountField.setEnabled(true);
            //customizeButton.setEnabled(false);
            colField.setEnabled(true);
            rowField.setEnabled(true);
        });
    }

    private void setTextFieldsDisable() {
        mineAmountField.setEnabled(false);
        colField.setEnabled(false);
        rowField.setEnabled(false);
    }

    private void setConfStrings() {
        mineAmountField.setText(String.valueOf(Minesweeper.mineAmountConf));
        colField.setText(String.valueOf(Minesweeper.colConf));
        rowField.setText(String.valueOf(Minesweeper.rowConf));
    }

    public boolean startGame() {
        try {
            totalMinesToSet = Integer.parseInt(mineAmountField.getText());
            rowsToSet = Integer.parseInt(rowField.getText());
            columnsToSet = Integer.parseInt(colField.getText());
        } catch (NumberFormatException e) {
            showMessageDialog(null,
                    resourceBundle.getString("InvalidAmount"),
                    resourceBundle.getString("Warning"),
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if(totalMinesToSet < 0 || rowsToSet < 0 || columnsToSet < 0) {
            showMessageDialog(null,
                    resourceBundle.getString("InvalidAmount"),
                    resourceBundle.getString("Warning"),
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if(totalMinesToSet >= rowsToSet * columnsToSet) {
            showMessageDialog(null,
                    resourceBundle.getString("TooManyMines"),
                    resourceBundle.getString("Warning"),
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        int newHeightToSet = rowsToSet * Board.CELL_SIZE + 140;
        int newWidthToSet = Math.max(columnsToSet * Board.CELL_SIZE + 20, 200);
        GameFrame gameFrame = new GameFrame(newWidthToSet, newHeightToSet, totalMinesToSet, rowsToSet, columnsToSet);
        gameFrame.setVisible(true);
        return true;
    }

    public void changeLocaleRefresh() {
        WelcomeFrame welcomeFrame = new WelcomeFrame(1);
        welcomeFrame.setLocation(this.getLocation());
        welcomeFrame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        languageStrings = new String[]{"Default", "English (US)", "简体中文 (中国大陆)", "日本語", "繁體中文 (台灣)", "繁體中文 (香港)", "吴语"};
        languageComboBox = new JComboBox(languageStrings);
    }
}
