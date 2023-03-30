package minesweeper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class LicenseDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JTextPane MITLicenseCopyrightCTextPane;

    private ResourceBundle resourceBundle;

    public LicenseDialog() {
        resourceBundle = Minesweeper.getResourceBundle();

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        setTitle(resourceBundle.getString("License"));

        buttonOK.addActionListener(e -> onOK());
    }

    private void onOK() {
        dispose();
    }
}
