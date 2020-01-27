package com.chrisgreenup.ordernchaos;

/*
 * Christopher Greenup
 * CSCI 4010-06
 * Order and Chaos with Original rules which allow 6 in a row to win
 * Tested on a Google Pixel 2
 */

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {

    private String[] players = {"Order", "Chaos"};
    private int counter = 1;

    private ArrayList<String>[][] board;
    private boolean inMainActivity = true;

    private boolean markerSelected;
    private boolean markerIsX;
    private boolean gameOver;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        board  = new ArrayList[6][6];
        gameOver = false;

        setUpMainButtons();

    }

    public void setUpMainButtons(){
        //Button setup 1
        findViewById(R.id.play_button).setOnClickListener(this);
        findViewById(R.id.game_info_button).setOnClickListener(this);
        findViewById(R.id.app_info_button).setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        if(!inMainActivity) {
            setContentView(R.layout.activity_main);
            inMainActivity = true;
            setUpMainButtons();
            markerSelected = false;
            counter = 1;

        }
        else {
            super.onBackPressed();
        }
    }

    //This is the onClick method for the activity_main.xml
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.game_info_button){

            setContentView(R.layout.activity_game_info);
            String url = "https://en.wikipedia.org/wiki/Order_and_Chaos";
            WebView wv = findViewById(R.id.InfoWebView);
            wv.loadUrl(url);
            wv.setWebViewClient(new WebViewClient());
        }
        else if(view.getId() == R.id.app_info_button){

            //Button setup 2
            setContentView(R.layout.activity_info);
            findViewById(R.id.retrn_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }
        else if(view.getId() == R.id.play_button){
            setContentView(R.layout.activity_playout);
            setupPlayout();
        }

        inMainActivity = false;
    }

    //Sets up all the buttons, images, etc for activity_playout.xml
    //Sorry for all of the repititious code
    void setupPlayout(){
        findViewById(R.id.space_0_0).setOnClickListener(new GameSpaceListener(0, 0));
        findViewById(R.id.space_0_1).setOnClickListener(new GameSpaceListener(0, 1));
        findViewById(R.id.space_0_2).setOnClickListener(new GameSpaceListener(0, 2));
        findViewById(R.id.space_0_3).setOnClickListener(new GameSpaceListener(0, 3));
        findViewById(R.id.space_0_4).setOnClickListener(new GameSpaceListener(0, 4));
        findViewById(R.id.space_0_5).setOnClickListener(new GameSpaceListener(0, 5));
        findViewById(R.id.space_1_0).setOnClickListener(new GameSpaceListener(1, 0));
        findViewById(R.id.space_1_1).setOnClickListener(new GameSpaceListener(1, 1));
        findViewById(R.id.space_1_2).setOnClickListener(new GameSpaceListener(1, 2));
        findViewById(R.id.space_1_3).setOnClickListener(new GameSpaceListener(1, 3));
        findViewById(R.id.space_1_4).setOnClickListener(new GameSpaceListener(1, 4));
        findViewById(R.id.space_1_5).setOnClickListener(new GameSpaceListener(1, 5));
        findViewById(R.id.space_2_0).setOnClickListener(new GameSpaceListener(2, 0));
        findViewById(R.id.space_2_1).setOnClickListener(new GameSpaceListener(2, 1));
        findViewById(R.id.space_2_2).setOnClickListener(new GameSpaceListener(2, 2));
        findViewById(R.id.space_2_3).setOnClickListener(new GameSpaceListener(2, 3));
        findViewById(R.id.space_2_4).setOnClickListener(new GameSpaceListener(2, 4));
        findViewById(R.id.space_2_5).setOnClickListener(new GameSpaceListener(2, 5));
        findViewById(R.id.space_3_0).setOnClickListener(new GameSpaceListener(3, 0));
        findViewById(R.id.space_3_1).setOnClickListener(new GameSpaceListener(3, 1));
        findViewById(R.id.space_3_2).setOnClickListener(new GameSpaceListener(3, 2));
        findViewById(R.id.space_3_3).setOnClickListener(new GameSpaceListener(3, 3));
        findViewById(R.id.space_3_4).setOnClickListener(new GameSpaceListener(3, 4));
        findViewById(R.id.space_3_5).setOnClickListener(new GameSpaceListener(3, 5));
        findViewById(R.id.space_4_0).setOnClickListener(new GameSpaceListener(4, 0));
        findViewById(R.id.space_4_1).setOnClickListener(new GameSpaceListener(4, 1));
        findViewById(R.id.space_4_2).setOnClickListener(new GameSpaceListener(4, 2));
        findViewById(R.id.space_4_3).setOnClickListener(new GameSpaceListener(4, 3));
        findViewById(R.id.space_4_4).setOnClickListener(new GameSpaceListener(4, 4));
        findViewById(R.id.space_4_5).setOnClickListener(new GameSpaceListener(4, 5));
        findViewById(R.id.space_5_0).setOnClickListener(new GameSpaceListener(5, 0));
        findViewById(R.id.space_5_1).setOnClickListener(new GameSpaceListener(5, 1));
        findViewById(R.id.space_5_2).setOnClickListener(new GameSpaceListener(5, 2));
        findViewById(R.id.space_5_3).setOnClickListener(new GameSpaceListener(5, 3));
        findViewById(R.id.space_5_4).setOnClickListener(new GameSpaceListener(5, 4));
        findViewById(R.id.space_5_5).setOnClickListener(new GameSpaceListener(5, 5));

        ((RadioGroup) findViewById(R.id.marker_radioGroup)).setOnCheckedChangeListener
                                                            (new markerRadioGroupListener());
        whoIsPlaying();
        gameOver = false;

        for (int i = 0; i < 6; i++){
            for (int j = 0; j < 6; j++){
                board[i][j] = new ArrayList<>();
                board[i][j].add("_");
            }
        }
    }

    //This method changes the text at the top of the playout to show who is playing
    void whoIsPlaying(){
        counter = (counter + 1) % 2;
        TextView tv = findViewById(R.id.player_textView);
        tv.setText("It's " + players[counter] + "'s turn!");
    }

    //Custom radiogroup listener
    class markerRadioGroupListener implements RadioGroup.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int clickedId) {
            if(radioGroup.getId() == R.id.marker_radioGroup) {
                if (clickedId == R.id.x_marker_radioButton) {

                    markerIsX = true;
                    //Log.i("TESTGame", "Radiobutton x");
                }
                else if (clickedId == R.id.o_marker_radioButton){
                    markerIsX = false;
                    //Log.i("TESTGame", "Radiobutton o");
                }
                markerSelected = true;
                //Log.i("TESTGame", "Marker is X = " + markerIsX);
            }
        }
    }

    //Button Setup 3
    //This listener is used to help determine the location of each marker on the board
    class GameSpaceListener implements View.OnClickListener{

        private int x;
        private int y;
        private String mark = "_";

        public GameSpaceListener(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void onClick(View view) {
            ImageButton btn = findViewById(view.getId());

            if (markerSelected == true && mark.equals("_") && !gameOver) {
                if(markerIsX) {
                    btn.setImageResource(R.drawable.cross);
                    board[x][y].set(0, "x");
                }
                else{
                    btn.setImageResource(R.drawable.circle);
                    board[x][y].set(0, "o");
                }

                mark = "no";

                RadioGroup rg = findViewById(R.id.marker_radioGroup);
                rg.clearCheck();
                markerIsX =false;
                markerSelected = false;
                whoIsPlaying();
                gameOver = (boardIsFull() || checkHorizontal() || checkVertical() ||
                        checkDiagonal1() || checkDiagonal2());
                if(gameOver){
                    TextView tv = findViewById(R.id.player_textView);
                    tv.setBackgroundColor(0xFFEECA99);
                }
                //Log.i("TESTWin", "Game Over = " + gameOver);
            }


            //debugBoard(x, y);
        }
    }

    public void debugBoard(int x, int y){
        Log.i("TESTGame", "X = " + x + " Y = " + y);
        Log.i("TESTGame", "Marker = " + board[x][y]);

        String string = "";
        for (int i = 0; i < 6; i++){
            for(int j = 0; j < 6; j++){
                string = string + board[j][i];
            }
            Log.i("TESTGame", string);
            string = "";
        }
    }

    public boolean boardIsFull(){
        for(int y = 0; y < 6; y++)
            for(int x = 0; x < 6; x++)
                if(board[x][y].toString().equals("[_]"))
                    return false;

        ((TextView)findViewById(R.id.player_textView)).setText("Chaos WINS!!!");
        return true;
    }

    public boolean checkHorizontal(){
        String lastMarker;
        String currentMarker;
        int numInARow;
        int ctr;

        for(int y = 0; y < 6; y++){
            for (int x = 0; x < 2; x++){
                numInARow = 0;
                currentMarker = board[x][y].toString();
                lastMarker = board[x][y].toString();
                ctr = x;
                //While the current mark matches the previously checked one,
                //and the x position is less than 6,
                //and neither mark is an empty space:
                //      then keep checking if
                while (currentMarker.equals(lastMarker) && ctr < 6 && (!(currentMarker.equals("[_]"))
                        && !(lastMarker.equals("[_]")))){

                    numInARow++;
                    if (numInARow < 5) {
                        ctr++;
                        lastMarker = currentMarker;
                        currentMarker = board[ctr][y].toString();
                    }
                    else{
                        Log.i("TESTWin","WIN CONDITION - HORIZONTAL " + numInARow);
                        ((TextView) findViewById(R.id.player_textView)).setText("Order WINS!!!");
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean checkVertical(){
        String lastMarker;
        String currentMarker;
        int numInARow;
        int ctr;

        for(int x = 0; x < 6; x++){
            for (int y = 0; y < 2; y++){
                numInARow = 0;
                currentMarker = board[x][y].toString();
                lastMarker = board[x][y].toString();
                ctr = y;
                //While the current mark matches the previously checked one,
                //and the x position is less than 6,
                //and neither mark is an empty space:
                //      then keep checking if
                while (currentMarker.equals(lastMarker) && ctr < 6 && (!(currentMarker.equals("[_]"))
                        && !(lastMarker.equals("[_]")))){

                    numInARow++;
                    if (numInARow < 5) {
                        ctr++;
                        lastMarker = currentMarker;
                        currentMarker = board[x][ctr].toString();
                    }
                    else{
                        Log.i("TESTWin","WIN CONDITION - VERTICAL " + numInARow);
                        ((TextView) findViewById(R.id.player_textView)).setText("Order WINS!!!");
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean checkDiagonal1(){
        String lastMarker;
        String currentMarker;
        int numInARow;
        int ctrX, ctrY;

        for(int y = 0; y < 2; y++){
            for (int x = 0; x < 2; x++){
                numInARow = 0;
                currentMarker = board[x][y].toString();
                lastMarker = board[x][y].toString();
                ctrX = x;
                ctrY = y;
                //While the current mark matches the previously checked one,
                //and the x position is less than 6,
                //and neither mark is an empty space:
                //      then keep checking if
                while (currentMarker.equals(lastMarker) && ctrX < 6  && ctrY < 6 && (!(currentMarker.equals("[_]"))
                        && !(lastMarker.equals("[_]")))){

                    numInARow++;
                    if (numInARow < 5) {
                        ctrX++;
                        ctrY++;
                        lastMarker = currentMarker;
                        if (ctrX < 6 && ctrY < 6)
                            currentMarker = board[ctrX][ctrY].toString();
                    }
                    else{
                        Log.i("TESTWin","WIN CONDITION - Diagonal \\ " + numInARow);
                        ((TextView) findViewById(R.id.player_textView)).setText("Order WINS!!!");
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean checkDiagonal2(){
        String lastMarker;
        String currentMarker;
        int numInARow;
        int ctrX, ctrY;

        for(int y = 0; y < 2; y++){
            for (int x = 5; x > 3; x--){
                numInARow = 0;
                currentMarker = board[x][y].toString();
                lastMarker = board[x][y].toString();
                ctrX = x;
                ctrY = y;
                //While the current mark matches the previously checked one,
                //and the x position is less than 6,
                //and neither mark is an empty space:
                //      then keep checking if
                while (currentMarker.equals(lastMarker) && (!(currentMarker.equals("[_]"))
                        && !(lastMarker.equals("[_]")))){
                    numInARow++;
                    if (numInARow < 5) {
                        ctrX--;
                        ctrY++;
                        lastMarker = currentMarker;
                        if(ctrX > -1 && ctrY < 6)
                            currentMarker = board[ctrX][ctrY].toString();
                    }
                    else{
                        Log.i("TESTWin","WIN CONDITION - Diagonal / " + numInARow);
                        ((TextView) findViewById(R.id.player_textView)).setText("Order WINS!!!");
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
