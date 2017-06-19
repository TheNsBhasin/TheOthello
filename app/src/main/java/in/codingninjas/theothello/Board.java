package in.codingninjas.theothello;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by nsbhasin on 19/06/17.
 */


public class Board extends Cell implements View.OnClickListener {

    Context context;

    private final static int N = 8;
    private Player player1 = new Player(COLOR.BLACK);
    private Player player2 = new Player(COLOR.WHITE);
    private Player currPlayer;

    private Cell[][] board;

    public Board(Context context) {
        super(context);

        this.context = context;
        setUpBoard();
    }

    public void resetBoard() {
        setUpBoard();
    }

    private void setUpBoard() {
        board = new Cell[N][N];
        MainActivity.rowLayout = new LinearLayout[N];
        MainActivity.mainLayout.removeAllViews();
        currPlayer = player1;

        for(int i = 0; i < N; i++) {
            MainActivity.rowLayout[i] = new LinearLayout(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0 , 1f);
            MainActivity.rowLayout[i].setLayoutParams(params);
            MainActivity.rowLayout[i].setOrientation(LinearLayout.HORIZONTAL);
            MainActivity.mainLayout.addView(MainActivity.rowLayout[i]);
        }

        for(int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                board[i][j] = new Cell(context);
                board[i][j].setBackground(ContextCompat.getDrawable(context, R.drawable.square));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
                params.setMargins(2,2,2,2);
                board[i][j].setLayoutParams(params);
                board[i][j].setPosition(i,j);
                board[i][j].setOnClickListener(this);
                board[i][j].setScaleType(ImageView.ScaleType.CENTER_CROP);
                MainActivity.rowLayout[i].addView(board[i][j]);
            }
        }

        placePlayer(3,4);
        nextTurn();
        placePlayer(3,3);
        nextTurn();
        placePlayer(4,3);
        nextTurn();
        placePlayer(4,4);
        nextTurn();

    }

    private void nextTurn() {
        updateScoreboard();
        this.currPlayer = (this.currPlayer == player1)? player2 : player1;
        if (currPlayer == player1) {
            MainActivity.blackTurn.setVisibility(VISIBLE);
            MainActivity.whiteTurn.setVisibility(INVISIBLE);
        } else if(currPlayer == player2) {
            MainActivity.whiteTurn.setVisibility(VISIBLE);
            MainActivity.blackTurn.setVisibility(INVISIBLE);
        }
        showMoves();
    }

    private Cell getCellAt(int x, int y) {
        return board[x][y];
    }

    public void placePlayer(int row, int col) {

        if (board[row][col].getColor() == COLOR.EMPTY) {
            if (currPlayer.getColor() == COLOR.BLACK) {
                board[row][col].setColor(COLOR.BLACK);
                getCellAt(row,col).setImageResource(R.drawable.black);
            } else if (currPlayer.getColor() == COLOR.WHITE) {
                board[row][col].setColor(COLOR.WHITE);
                getCellAt(row,col).setImageResource(R.drawable.white);
            }
        }
    }

    @Override
    public void onClick(View view) {
        Cell cell = (Cell) view;
        int X = cell.getXPos();
        int Y = cell.getYPos();
        Log.d("onClick", X + " " + Y);
        checkAction(X,Y);
    }

    private void checkAction(int row, int col) {

        if(!checkEnd() && movesLeft()) {
            if(checkMove(row,col)) {
                Log.d("checkAction", "Move possible at " + row + " " + col);
                flip(row, col);
                placePlayer(row, col);

                if (!checkEnd()) {
                    nextTurn();
                    if(!movesLeft()) {
                        Toast.makeText(context,"Pass",Toast.LENGTH_SHORT).show();
                        nextTurn();
                        if(!movesLeft()) {
                            showWinMessage();
                        }
                    }
                }
            }
            if (checkEnd()) {
                showWinMessage();
            }
        } else if (checkEnd()) {
            showWinMessage();
        }
    }

    private void showWinMessage() {
        updateScoreboard();
        MainActivity.blackTurn.setVisibility(VISIBLE);
        MainActivity.whiteTurn.setVisibility(VISIBLE);
        if(count(COLOR.BLACK) > count(COLOR.WHITE)) {
            Toast.makeText(context, "Black Wins !!" , Toast.LENGTH_LONG).show();
        } else if (count(COLOR.BLACK) < count(COLOR.WHITE)) {
            Toast.makeText(context, "White Wins !!" , Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Match Draw!" , Toast.LENGTH_LONG).show();
        }
    }

    public boolean checkMove(int row, int col) {
        boolean flippable = false;

        if(board[row][col].getColor() != COLOR.EMPTY) {
            return flippable;
        }

        for (int dirRow = -1; dirRow < 2; dirRow++) {
            for (int dirCol = -1; dirCol < 2; dirCol++) {

                if(dirRow == 0 && dirCol == 0) {
                    continue;
                }

                int newRow = row + dirRow;
                int newCol = col + dirCol;

                if (newRow >= 0 && newRow <= 7 && newCol >= 0 && newCol <= 7) {

                    COLOR oppoColor =
                            this.currPlayer.getColor() == COLOR.WHITE ? COLOR.BLACK : COLOR.WHITE;
                    if (board[newRow][newCol].getColor() == oppoColor) {
                        for (int range = 1; range < 8; range++) {

                            int nRow = row + range * dirRow;
                            int nCol = col + range * dirCol;

                            if(nRow < 0 || nRow > 7 || nCol < 0 || nCol > 7) {
                                continue;
                            }

                            if(board[nRow][nCol].getColor() == COLOR.EMPTY) {
                                break;
                            }

                            if(board[nRow][nCol].getColor() == this.currPlayer.getColor()) {
                                flippable = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return flippable;
    }

    public void flip(int row, int col) {

        for(int dirRow = -1; dirRow < 2; dirRow++) {
            for(int dirCol = -1; dirCol < 2; dirCol++) {

                if(dirRow == 0 && dirCol == 0)
                    continue;

                int newRow = row + dirRow;
                int newCol = col + dirCol;

                if(newRow >= 0 && newRow <= 7 && newCol >= 0 && newCol <= 7) {

                    COLOR oppositeColor = this.currPlayer.getColor() == COLOR.WHITE ? COLOR.BLACK : COLOR.WHITE;
                    if(board[newRow][newCol].getColor() == oppositeColor) {
                        for(int range = 0; range < 8; range++) {

                            int nRow = row + range * dirRow;
                            int nCol = col + range * dirCol;

                            if(nRow < 0 || nRow > 7 || nCol < 0 || nCol > 7)
                                continue;

                            if(board[nRow][nCol].getColor() == this.currPlayer.getColor()) {
                                boolean canFlip = true;
                                for (int dist = 1; dist < range; dist++) {

                                    int testRow = row + dist * dirRow;
                                    int testCol = col + dist * dirCol;

                                    if (board[testRow][testCol].getColor() != oppositeColor) {
                                        canFlip = false;
                                    }
                                }

                                if(canFlip) {
                                    for(int flipDist = 1; flipDist < range; flipDist++) {

                                        int finalRow = row + flipDist * dirRow;
                                        int finalCol = col + flipDist * dirCol;

                                        if(board[finalRow][finalCol].getColor() == oppositeColor) {
                                            board[finalRow][finalCol].setPlayer(this.currPlayer);
                                            if(currPlayer.getColor() == COLOR.BLACK)
                                                board[finalRow][finalCol].setImageResource(R.drawable.black);
                                            else if (currPlayer.getColor() == COLOR.WHITE)
                                                board[finalRow][finalCol].setImageResource(R.drawable.white);
                                            Log.d("flip", "Flipped at " + finalRow + " " + finalCol);
                                        }
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean checkEnd() {
        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                if (board[row][col].getColor() == COLOR.EMPTY) {
                    Log.d("checkEnd", "returns false");
                    return false;
                }
            }
        }
        Log.d("checkEnd", "returns true");
        return true;
    }

    private void showMoves() {

        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                if (checkMove(row, col)) {
                    if(currPlayer.getColor() == COLOR.BLACK) {
                        board[row][col].setImageResource(R.drawable.blackdot);
                    }
                    else if (currPlayer.getColor() == COLOR.WHITE) {
                        board[row][col].setImageResource(R.drawable.whitedot);
                    }
                }
                else if(board[row][col].getColor()==COLOR.EMPTY)
                    board[row][col].setImageResource(R.drawable.transparent);
            }
        }
    }

    private boolean movesLeft() {

        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                if (checkMove(row, col)) {
                    Log.d("moveLeft", "returns true for " + row + " " + col);
                    return true;
                }
            }
        }
        Log.d("moveLeft", "returns false");
        return false;
    }

    public int count(COLOR color) {
        int num = 0;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if(board[row][col].getColor() == color) {
                    num++;
                }
            }
        }
        return num;
    }

    private void updateScoreboard() {
        MainActivity.blackScore.setText("" + count(COLOR.BLACK));
        MainActivity.whiteScore.setText("" + count(COLOR.WHITE));
    }

}
