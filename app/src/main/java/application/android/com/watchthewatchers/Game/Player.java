package application.android.com.watchthewatchers.Game;

import java.util.ArrayList;

public class Player {

    private int money;
    private ArrayList<Artefact> artefactlist;
    private int totalArmor = 0;
    private Shop shop;
    private Score score;
    private Level level;


    public Player(Score score, int money, Shop shop, ArrayList<Artefact> currentItems) {
        this.money = money;
        this.artefactlist = currentItems;
        this.score = score;

    }

    public void updatePlayerStats()
    {
        upDateLevel(score);
        calculateArmor();
        getIncomefromAtrefacts();
    }


    //Berechnet Spielers gesamten Armor Wert anhand der Artefacte aus den SharedPrefs
    public void calculateArmor()
    {
        for(Artefact art : artefactlist){
            totalArmor += art.getArmor();
        }
    }

    //Level repräsentiert eine textliche Umschreibung des Aktuellen Score Werts
    public void upDateLevel(Score score)
    {
        String tempLvlString = level.getLevelFromScore(score.getScore());
    }

    //Spieler erhält durch Artefakte ein Einkommen wenn sich der Spieler mit dem Artefakt bei einer Kamera befindet
    public void getIncomefromAtrefacts()
    {
        for(Artefact art : artefactlist){
            money += art.getIncome();
        }
    }






}
