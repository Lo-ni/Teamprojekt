package application.android.com.watchthewatchers.Game;

public class Artefact {

    private String name;
    private String beschreibung;
    private int armor;
    private int price;
    private int income;

    Artefact(String name, String beschreibung, int value, int price, int income){
        this.name =name;
        this.beschreibung =beschreibung;
        this.armor =value;
        this.price = price;
        this.income = income;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public int getIncome()
    {
        return income;

    }








}
