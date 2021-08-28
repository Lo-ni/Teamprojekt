package application.android.com.watchthewatchers.Game;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Shop {

    ArrayList<Artefact> artefakte;

    public Shop(Context ctx){

        //read Artefakte aus Datei

        File file = new File("Artefakte.txt");
            artefakte = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            while(reader.readLine() != null){
                Artefact artefact =  ArtefaktUtils.readInString(reader.readLine());
                artefakte.add(artefact);

            }

        } catch (java.io.IOException | InvalidFormatException e) {
            e.printStackTrace();
        }


    }

    public void buy(){

    }
    public void sell(){

    }

    public ArrayList<Artefact> getArtefacts() {
        return this.artefakte;
    }
}
