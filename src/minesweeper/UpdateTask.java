package minesweeper;

import javax.swing.*;
import java.util.TimerTask;

public class UpdateTask extends TimerTask {
    private JLabel timeLabel;

    private static int minutes;
    private static double seconds;
    private String stringToUpdate;
    private String secondString;
    private boolean enabled;

    public UpdateTask() {
        minutes = 0;
        seconds = 0;
    }

    public void setTimeLabel(JLabel timeLabel) {
        this.timeLabel = timeLabel;
    }

    @Override
    public void run() {
        if(enabled) {
            seconds += 0.001;
            if(seconds >= 60) {
                seconds -= 60;
                minutes++;
            }

            secondString = Double.toString(seconds);
            stringToUpdate = minutes + ":" + secondString.substring(0, secondString.indexOf('.') + 4);

            timeLabel.setText(stringToUpdate);
        }
    }

    public static void clear() {
        minutes = 0;
        seconds = 0;
    }

    public void enable() {
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }

    public void manualUpdate() {
        timeLabel.setText("0:0.000");
    }

    public String getStringToUpdate() {
        return stringToUpdate;
    }
}
