import javax.swing.JButton;

public class Casa extends JButton{
    int x;
    int y;
    private boolean enable = true;

    Casa (int x, int y, boolean enable) {
        this.x = x;
        this.y = y;
        this.enable = enable;
    }

    public void setMyEnable( boolean enable) {
        this.enable = enable;
    }

    public boolean getMyEnable () {
        return enable;
    }
}