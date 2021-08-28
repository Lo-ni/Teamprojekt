package application.android.com.watchthewatchers.Core;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import application.android.com.watchthewatchers.Game.Player;
import application.android.com.watchthewatchers.IOFormat.CreateOsmNode;


/*
Der Service ist das Herz der App.
Er trackt die GPS position des mobilen Geräts und lädt Kameras von der Overpass API durch einen neuen Thread runter.
Falls der Nutzer sich einer Kamera auf einen Radius von <50 Meter nähert, startet die Vibration.
Beim Austritt (>50 Meter) stoppt die Vibration erneut.
Der Service führt zwei Listen, die "cameraList" mit allen Kameras und die "nearbyCameras" mit den Kameras in Umgebung.

*/

public class GPS_Service extends Service implements LocationListener{

    private static final String TAG = "GPS_Service";
    public LocationManager locationManager;
    private ArrayList<SurveillanceCam> cameraList;
    private ArrayList<SurveillanceCam> nearbyCameras;
    private Location lastDownloadedLocation;
    private Location lastNearbyListUpdateLocation;
    private Location currentLocation;
    private int time = 5000;
    Player player;



    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        super.onCreate();
        cameraList = new ArrayList<>();
        nearbyCameras = new ArrayList<>();
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, time,10,this);
        currentLocation = getLastKnownLocation();
        if(currentLocation != null){
            loadCamerasFromOverpassAPI(currentLocation);
        }

//        Shop shop = new Shop(this);
//        Score score = GameUtils.getScoreFromSP(this);
//        int money = GameUtils.getMoneyFromSp(this);
//        ArrayList<Artefact> artefacts = GameUtils.getPlayerArtefactsFromSP(shop.getArtefacts(), this);
//        player = new Player(score, money,shop,artefacts);


    }

    @Override //Wird aufgerufen falls Service schon gestartet ist
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        Location location = new Location("");
        location.setLatitude(49.7447685);
        location.setLongitude(6.6433435);
        currentLocation = location;

        loadCamerasFromOverpassAPI(location);

        return START_STICKY;
    }

    private void broadcastNearbyCameras(Location location) {
        Intent i = new Intent("broadcast");
        ArrayList<SurveillanceCam> sendingCams = (ArrayList<SurveillanceCam>) nearbyCameras.clone();

        for(SurveillanceCam cam : sendingCams){
            cam.setDistance(CreateOsmNode.round(cam.getDistance(), 0));
            cam.setCamera_type(SurveillanceCam.CAMERA_TYPE.dome);
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable("nearbyCameras", sendingCams);
        i.putExtras(bundle);
        sendBroadcast(i);
    }

    private void broadcastLocation(Location location){
        Intent i = new Intent("broadcast");
        Bundle bundle = new Bundle();
        bundle.putString("location",location.getLatitude()+","+location.getLongitude());
        i.putExtras(bundle);
        sendBroadcast(i);
    }




    @SuppressLint("MissingPermission")
    private void requestLocationUpdates(int time) {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, time,1,this);
    }

    private void recalculateNearbyList(Location location) {
        try {
            nearbyCameras.clear();
            float distance;
            for (SurveillanceCam cam:cameraList) {
                if(cam.getGpsPosition().distanceTo(location)<70000){
                    distance  = cam.getGpsPosition().distanceTo(location);
                    cam.setDistance(distance);
                    nearbyCameras.add(cam);
                }
            }
            lastNearbyListUpdateLocation = location;
        }
        catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
    }


    private Location getLastKnownLocation() {
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Log.d(TAG, provider.toString());
            @SuppressLint("MissingPermission") Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }

        if(bestLocation == null)
        {
        SharedPreferences sharedPrefs = getSharedPreferences("location", MODE_PRIVATE);
        String prefsData;
            prefsData = sharedPrefs.getString("location_save","null");
//            bestLocation = CreateOsmNode.createLocation(prefsData); //TODO: hmm
        }
        return bestLocation;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(locationManager != null){
            locationManager.removeUpdates(this);
        }
        if(currentLocation != null){
            SharedPreferences prefs = getSharedPreferences("location", MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = prefs.edit();
            String latitude = currentLocation.getLatitude()+ "";
            String longitude = currentLocation.getLongitude()+ "";
            prefsEditor.putString("location_save", latitude+","+longitude);
            prefsEditor.commit();
        }
    }



    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }

    private void loadCamerasFromOverpassAPI(final Location location) {
        currentLocation = location;
//        String url = "http://www.overpass-api.de/api/xapi?node[man_made=surveillance]";
        String url = "http://185.233.107.115/request_cams";
        String[] co = locationtesting(location);
        String coordinatesString = "[bbox="+co[0]+ "," + co[1]+"," + co[2] +"," + co[3] + "]";
        RefreshList refreshList = new RefreshList() {
            @Override
            public void refreshListe() {
                recalculateNearbyList(location);
                lastDownloadedLocation = location;
                onLocationChanged(location);
                Toast.makeText(getApplicationContext(), "refresh Liste", Toast.LENGTH_SHORT).show();
                broadcastNearbyCameras(location);
            }
        };
        new RequestTask().execute(cameraList, url.concat(coordinatesString), this, refreshList);
    }


    private String[] locationtesting(Location location){
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        int radius = 50000;
        // 6378000 Size of the Earth (in meters)
        double longitudeD = (Math.asin(radius / (6378000 * Math.cos(Math.PI*latitude/180))))*180/Math.PI;
        double latitudeD = (Math.asin((double)radius / (double)6378000))*180/Math.PI;

        double longitudeMin = longitude-(longitudeD);
        double latitudeMax = latitude+(latitudeD);
        double longitudeMax = longitude+(longitudeD);
        double latitudeMin = latitude-(latitudeD);
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols().getInstance();
        otherSymbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("#.#####",otherSymbols);
        String[] coordinateArray = {df.format(longitudeMin), df.format(latitudeMin), df.format(longitudeMax), df.format(latitudeMax)};
        return coordinateArray;
    }

    @Override
    public void onLocationChanged(Location location) {

        currentLocation = location;
        Log.d(TAG, "currentLocation: " + currentLocation);
        Log.d(TAG, "lastDownloadedLocation: " + lastDownloadedLocation);
        Log.d(TAG, "lastnearbyUploadedLocation: " + lastNearbyListUpdateLocation);
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            try {
                //Alle 30km neue Kamera Files anfragen
                if (currentLocation.distanceTo(lastDownloadedLocation) > 30000) {
                    loadCamerasFromOverpassAPI(currentLocation);
                    }
                }
            catch (NullPointerException e){
                loadCamerasFromOverpassAPI(currentLocation);
            }
                //Alle 500Meter werden neue Kameras von der MainListe in die NearbyListe aufgenommen
                //Nur Kameras im Umkreis von <1000 Metern werden in onLocationChanged berücksichtigt um Ressourcen zu sparen
        try{
                if(lastNearbyListUpdateLocation.distanceTo(currentLocation)>5000) {
                    recalculateNearbyList(currentLocation);
                    broadcastNearbyCameras(location);
                }
            }catch (NullPointerException e){
                e.printStackTrace();
        }


            broadcastLocation(location);

        broadcastNearbyCameras(location); // Only for Debugging))


        //Falls Kameras in der Nähe (40Meter) sind wird Benachrichtigt.
            //Todo: Möglichkeit einzustellen ab welcher Reichweite man benachrichtigt werden soll
            //TODO: Falls Alert gemuted wird, kann die Zeit wieder höher gedreht werden
            for (SurveillanceCam cam:nearbyCameras) {
                if(cam.getGpsPosition().distanceTo(currentLocation)<50){
                    requestLocationUpdates(1000);
                    long[] pattern = {0, 500, 5000};
                    v.vibrate(pattern, 0);
                }
                else{
                    requestLocationUpdates(10000);
                    v.cancel();
                }
            }
        }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {
        Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}
