package application.android.com.watchthewatchers.Core;

import android.location.Location;

import java.io.Serializable;
import java.util.HashMap;

/*
Diese Klasse definiert die SurveillanceCam Objekte
 */

public class SurveillanceCam implements Serializable{

    private HashMap<String, String> map;


    public enum WATCHER_TYPE {governmental, commercial, private_}
    public enum CAMERA_TYPE {dome, fixed}
    public enum TEAM_TYPE {beasts, goblins}
    private String operator;
    private String name;
    private String id;
//    private transient Location gpsPosition;
    private double latitude;
    private double longitude;
    private double distance;
    private WATCHER_TYPE watcher_type;
    private CAMERA_TYPE camera_type;

    //Team das die aktuelle Kamera übernommen hat
    private TEAM_TYPE camera_team_owner;

    //Spieler der die aktuelle Kamera übernommen hat
    private String camera_player_owner;

    public SurveillanceCam(){
    }

    public SurveillanceCam(String latitude, String longitude){
        map = new HashMap<>();
        Double latitudeD = Double.parseDouble(latitude);
        Double longitudeD = Double.parseDouble(longitude);
        this.latitude = latitudeD;
        this.longitude = longitudeD;
    }



    public String getCamera_type() {

        switch (camera_type) {
            case dome:
                return "dome";
            case fixed:
                return "fixed";
        }
        return "";
    }

    public void setCamera_type(CAMERA_TYPE camera_type) {
        this.camera_type = camera_type;
    }

    public String getWatcher_type() {

        switch (watcher_type) {
            case governmental:
                return "public";
            case commercial:
                return "outdoor";
            case private_:
                return "private";
        }
        return "";
    }

    public void setWatcher_type(WATCHER_TYPE watcher_type) {
        this.watcher_type = watcher_type;
    }


    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public  Location getGpsPosition() {
        Location targetLocation = new Location("");
        targetLocation.setLatitude(latitude);
        targetLocation.setLongitude(longitude);
        return targetLocation;
    }

    public void setGpsPosition(Location gpsPosition) {
       this.latitude =  gpsPosition.getLatitude();
        this.longitude =  gpsPosition.getLongitude();
    }

    public Double getDistance() {
        return distance;
    }

    void setDistance(double distance) {
        this.distance = distance;
    }

    public TEAM_TYPE getCamera_team_owner() {
        return camera_team_owner;
    }

    public void setCamera_team_owner(TEAM_TYPE camera_team_owner) {
        this.camera_team_owner = camera_team_owner;
    }

    public String getCamera_player_owner() {
        return camera_player_owner;
    }

    public void setCamera_player_owner(String camera_player_owner) {
        this.camera_player_owner = camera_player_owner;
    }

}
