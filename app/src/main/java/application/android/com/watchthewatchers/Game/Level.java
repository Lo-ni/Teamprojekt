package application.android.com.watchthewatchers.Game;

import android.util.Log;

public class Level {


    public static final String RAINER_DER_RAINE = ""; //987
    public static final String FROMME = "";           //610
    public static final String HALBASSI = "";           //377
    public static final String VOLLASSI = "";           //233
    public static final String SOZIALER_ABFALL = "";     //144
    public static final String AFD_WAEHLER = "";        //89
                                                        //54
                                                        //34
                                                        //21
                                                        //13
                                                        //8
                                                        //5
                                                        //3
                                                        //2
                                                        //1

    public String getLevelFromScore(int score){

        String level = "";
        if(score <=1000){
            level = RAINER_DER_RAINE;
        }
        if(score <= 987){
            level = FROMME;
        }
        if(score <= 610){
            level = HALBASSI;

        }
        if(score <= 377){
            level = HALBASSI;
        }
        if(score <= 233){
            level = HALBASSI;

        }
        if(score <= 144){
            level = HALBASSI;
        }
        if(score <= 89){

        }
        if(score <= 55){

        }
        if(score <= 34){

        }
        if(score <= 21){

        }
        if(score <= 13){

        }
        if(score <= 8){

        }
        if(score <= 5){

        }
        if(score <= 3){

        }
        if(score <= 2){

        }
        if(score <= 1){

        }

        return level;
    }





}
