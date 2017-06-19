package in.codingninjas.theothello;

import android.content.Context;
import android.widget.ImageButton;

/**
 * Created by nsbhasin on 19/06/17.
 */

public class Cell extends android.support.v7.widget.AppCompatImageButton{

    private Player player = new Player();

    private int x,y;

    public Cell(Context context) {
        super(context);
    }

    public void setPlayer(final Player player) {
        this.player = player;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getXPos() {
        return x;
    }

    public int getYPos() {
        return y;
    }

    public Player getPlayer() {
        return player;
    }

    public void setColor(COLOR color) {
        player.setColor(color);
    }

    public COLOR getColor() {
        return player.getColor();
    }

}
