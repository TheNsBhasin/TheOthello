package in.codingninjas.theothello;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Board board;

    public static LinearLayout mainLayout;
    public static LinearLayout rowLayout[];
    public static TextView blackScore;
    public static TextView whiteScore;
    public static ImageView blackTurn;
    public static ImageView whiteTurn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        blackScore = (TextView) findViewById(R.id.blackScore);
        whiteScore = (TextView) findViewById(R.id.whiteScore);
        blackTurn = (ImageView) findViewById(R.id.blackTurn);
        whiteTurn = (ImageView) findViewById(R.id.whiteTurn);

        board = new Board(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.newGame) {
            board.resetBoard();
        }
        return true;
    }
}
