package application.android.com.watchthewatchers.Game;

import application.android.com.watchthewatchers.Game.Artefact;
import application.android.com.watchthewatchers.Game.InvalidFormatException;

public class ArtefaktUtils {

/*
    Reads Artefacts from File with Structure:
    <ItemName>$<Beschreibung>$<Value>
 */
    public static Artefact readInString(String s) throws InvalidFormatException {

        try {
            String[] substrings = s.split("\\$");

            String name = substrings[0].substring(1, substrings[0].length()-2);
            String description = substrings[1].substring(1, substrings[1].length()-1);
            String value = substrings[2].substring(1, substrings[2].length()-1);
            String money = substrings[2].substring(1, substrings[2].length()-1);
//            String money = substrings[2].substring(1, substrings[2].length()-1);


            System.out.println("name: " + name);
            System.out.println("description: " + description);
            System.out.println("armor: " + value);
//            System.out.println("income": " + income);

//todo init income correctly
		return new Artefact(name, description, Integer.parseInt(value), Integer.parseInt(money),0);

        }
        catch(Exception e)
        {
            throw new InvalidFormatException();
        }
    }


}
