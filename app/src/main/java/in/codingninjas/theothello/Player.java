package in.codingninjas.theothello;

/**
 * Created by nsbhasin on 19/06/17.
 */

public class Player {

    private COLOR color;

    public Player() {
        this.color = COLOR.EMPTY;
    }

    public Player(COLOR color) {
        this.color = color;
    }

    public COLOR getColor() {
        return color;
    }

    public void setColor(COLOR color) {
        this.color = color;
    }
}
