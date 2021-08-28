package application.android.com.watchthewatchers.Game;

import java.util.ArrayList;

public class Score {

    private int score;
//    ArrayList<Long> timestamps; //Um auszuschließen das man ständig Punkte verlier
// TODO Lohnt sich hier eine eigene Klasse?, könnten den Score auch einfach in der Playerklasse behandeln

    public Score(int scoreValue){
        score = scoreValue;
    }


    public void updateScore(){
        //get DamageValue from Player class (static?)
        //update Score abhängig vom schaden den man bekommt. (Rüstung)

    }
    public int getScore()
    {
        return score;
    }






}
