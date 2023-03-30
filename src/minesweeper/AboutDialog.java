package minesweeper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class AboutDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;

    private ResourceBundle resourceBundle;

    public AboutDialog() {
        resourceBundle = Minesweeper.getResourceBundle();

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        setLocationRelativeTo(null);
        setTitle(resourceBundle.getString("About"));
        setSize(200,150);
        setResizable(false);

        buttonOK.addActionListener(e -> onOK());
    }

    private void onOK() {
        dispose();
    }
}
