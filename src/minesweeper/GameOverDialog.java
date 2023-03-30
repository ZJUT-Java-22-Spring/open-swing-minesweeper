package minesweeper;

import javax.swing.*;
import java.awt.event.*;
import java.util.ResourceBundle;

public class GameOverDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton cancelButton;
    private JButton restartButton;
    private JButton newGameButton;
    private JLabel timeLabel;

    private ResourceBundle resourceBundle;
    private Board gameBoard;

    public GameOverDialog(Board gameBoard, boolean isWin, String timeString) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setLocationRelativeTo(null);
        setSize(350,200);

        resourceBundle = Minesweeper.getResourceBundle();
        this.gameBoard = gameBoard;

        timeLabel.setText(resourceBundle.getString("Time") + " " + timeString + " " + resourceBundle.getString("Second"));

        cancelButton.setText(resourceBundle.getString("Cancel"));
        restartButton.setText(resourceBundle.getString("Restart"));
        newGameButton.setText(resourceBundle.getString("NewGame"));

        cancelButton.addActionListener(e -> onCancel());
        restartButton.addActionListener(e -> onRestart());
        newGameButton.addActionListener(e -> onNewGame());

        if(isWin) {
            setTitle(resourceBundle.getString("Win"));
        } else {
            setTitle(resourceBundle.getString("Lost"));
        }

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onCancel() {
        dispose();
    }

    private void onRestart() {
        gameBoard.restartGame();
        gameBoard.redrawImages();
        dispose();
    }

    private void onNewGame() {
        gameBoard.setNewGame();
        gameBoard.redrawImages();
        dispose();
    }
}
