package minesweeper;

import javax.swing.*;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Minesweeper {
    private static ResourceBundle resourceBundle;
    private static Locale locale;
    public static final Locale HONG_KONG = new Locale("zh", "HK");
    public static final Locale WU = new Locale("wuu", "CN");
    public static int mineAmountConf, rowConf, colConf;

    public static UpdateTask updateTask;
    public static java.util.Timer timer;

    public static void main(String[] args) {
        mineAmountConf = 40;
        rowConf = 16;
        colConf = 16;
        locale = Locale.getDefault();
        updateTask = new UpdateTask();
        timer = new java.util.Timer(true);
        updateTask.disable();
        timer.scheduleAtFixedRate(updateTask, 0, 1);
        new WelcomeFrame(1).setVisible(true);
    }

    public static void setUpdateTaskTimeLabel(JLabel timeLabel) {
        updateTask.setTimeLabel(timeLabel);
    }

    public static Locale getLocale() {
        return locale;
    }

    public static ResourceBundle getResourceBundle() {
        try {
            resourceBundle = ResourceBundle.getBundle("res.Strings", locale);
        } catch(MissingResourceException e) {
            resourceBundle = ResourceBundle.getBundle("res.Strings");
        }
        return resourceBundle;
    }

    public static void setLocale(Locale locale) {
        Minesweeper.locale = locale;
    }
}
