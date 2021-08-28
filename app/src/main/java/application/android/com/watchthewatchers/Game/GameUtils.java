package application.android.com.watchthewatchers.Game;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.Array;
import java.util.ArrayList;

import application.android.com.watchthewatchers.R;

import static android.content.Context.MODE_PRIVATE;

public class GameUtils {


    public static ArrayList<Artefact> getPlayerArtefactsFromSP(ArrayList<Artefact> shopArtefacts, Context ctx){

        ArrayList<Artefact> currentArtefacts = new ArrayList<>();
        SharedPreferences preferences = ctx.getSharedPreferences(ctx.getResources().getString(R.string.playerArtefactsPrefs), MODE_PRIVATE);

        for (Artefact art :shopArtefacts){

            if(!preferences.getString(art.getName(), "").isEmpty());{//if not empty
                currentArtefacts.add(art);
            }
        }

        return  currentArtefacts;

    }

    public static Score getScoreFromSP(Context ctx) {
        Score score = null;
        SharedPreferences preferences = ctx.getSharedPreferences(ctx.getResources().getString(R.string.playerScorePrefs), MODE_PRIVATE);
        String key = ctx.getResources().getString(R.string.playerScoreKey);
        int defaultValue = -1;
          if(preferences.getInt(key, defaultValue) != defaultValue){
               score = new Score(preferences.getInt(key, -1));
          }else{
              throw new NullPointerException();
          }

        return score;

    }

    public static int getMoneyFromSp(Context ctx) {
        SharedPreferences preferences = ctx.getSharedPreferences(ctx.getResources().getString(R.string.playerMoneyPrefs), MODE_PRIVATE);
        int money = -1;
        String key = ctx.getResources().getString(R.string.playerMoneyKey);
        int defaultValue = -1;

        if(preferences.getInt(key, defaultValue) != defaultValue){
            money = preferences.getInt(key, defaultValue);
        }else{
            throw new NullPointerException();
        }
        return money;
    }
}
