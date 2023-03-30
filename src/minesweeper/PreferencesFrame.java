package minesweeper;

public class PreferencesFrame extends WelcomeFrame{
    private GameFrame gameFrame;

    public PreferencesFrame(GameFrame gameFrame) {
        super(0);
        this.gameFrame = gameFrame;
    }

    @Override
    public boolean startGame() {
        boolean status = super.startGame();
        if(status) {
            gameFrame.dispose();
            dispose();
        }
        return status;
    }

    @Override
    public void changeLocaleRefresh() {
        PreferencesFrame preferencesFrame = new PreferencesFrame(gameFrame);
        preferencesFrame.setLocation(this.getLocation());
        preferencesFrame.setVisible(true);
    }
}
