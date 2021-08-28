package application.android.com.watchthewatchers.Core;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.view.MapView;

import java.util.ArrayList;

import application.android.com.watchthewatchers.AddNewCameraActivity;
import application.android.com.watchthewatchers.CameraListAdapter;
import application.android.com.watchthewatchers.MapViews;
import application.android.com.watchthewatchers.OptionsMenu.PreferenceActivity;
import application.android.com.watchthewatchers.R;


public class MainActivity extends AppCompatActivity{

    private static final String TAG = "MainActivity";
    public static final int PERMISSIONS_REQUEST_LOCATION = 99;
    public static String RUNTIME;
    public Button startServiceBtn;
    public Button stopServiceBtn;
    public TextView tv;
    private BroadcastReceiver broadcastReceiver;
    private ArrayList<SurveillanceCam> nearbyCameraList;

    private Intent intent;
    CameraListAdapter adapter;
    public static Location currentPosition;
    public MapView mapView;


    /*

    Zeigt eine Listview mit Kameras in der Umgebung an.
    Über das Auge Symbol kann der Service gestartet werden
    Über das + Symbol können Kameras erstellt und hochgeladen werden.
     */


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

//        ListView listView= findViewById(R.id.camera_listview);
        nearbyCameraList = new ArrayList<>();
//        adapter = new CameraListAdapter(nearbyCameraList, this);
//        listView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        intent = new Intent(this, AddNewCameraActivity.class);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);

            }
        });

//        final Intent impotent = new Intent(this, Forgemap.class);

//        Button button = findViewById(R.id.butti);
//                button.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                    startActivity(impotent);
//                    }
//                });
        if(!runtimePermissions()){
            enableButtons();
        }
        loadDataFromSharedPreferences();

        AndroidGraphicFactory.createInstance(getApplication());
        mapView  = findViewById(R.id.map);
        MapViews.createMapView(this, mapView);

//        addNearbyCamerasOverlay(mapView.getLayerManager().getLayers());



    }


    private void loadDataFromSharedPreferences() {


        SharedPreferences prefs = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        prefs.getInt("score",0);

    }

    private  boolean runtimePermissions() {

        if(Build.VERSION.SDK_INT >=23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},PERMISSIONS_REQUEST_LOCATION);
            return true; //permission wurde erteilt
        }
        return false;
    }

    public void enableButtons() {

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableButtons();
                } else {
                    runtimePermissions();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        SharedPreferences sp = getSharedPreferences("preferences",MODE_PRIVATE);
        boolean h = sp.getBoolean("bla", false);

        super.onResume();

        //Register BroadcastReceiver
        if(broadcastReceiver == null){

            broadcastReceiver = new WTWBroadcastReceiver(this, nearbyCameraList, mapView);
        }
        registerReceiver(broadcastReceiver, new IntentFilter("broadcast"));
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }

        mapView.destroyAll();
        AndroidGraphicFactory.clearResourceMemoryCache();
//        store data before closing the app (score usw)
//        SharedPreferences prefs = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs.edit();

        //Hashbildung des Scores um Manipulation vorzubeugen
//            MessageDigest hash = MessageDigest.getInstance("SHA1","");
//            int scoreValue = 0;
//            byte[] b = new byte[1];
//            b[0] = (byte) scoreValue;
//            byte[] hashValue = hash.digest(b);

            //save Hash



//        editor.putString("key", "value");
//        editor.commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        Log.d(TAG, "Menu size: "+menu.size()+"");
        menu.findItem(R.id.unwatch).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                final Intent intent = new Intent(MainActivity.this, GPS_Service.class);
                if (menuItem.getIcon().getConstantState().equals(
                        getResources().getDrawable(R.drawable.baseline_remove_red_eye_black_18dp).getConstantState())){
                    menuItem.setIcon(R.drawable.baseline_visibility_off_black_48dp);
                    //stop Service
                    Toast.makeText(MainActivity.this, "Suche nach Kameras beendet", Toast.LENGTH_SHORT).show();
                    stopService(intent);
                } else {
                    menuItem.setIcon(R.drawable.baseline_remove_red_eye_black_18dp);
                    //start Service
                    Toast.makeText(MainActivity.this, "Suche nach Kameras gestartet", Toast.LENGTH_SHORT).show();
                    startService(intent);
               }
                return true;
            }
        });

        menu.findItem(R.id.action_settings).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                Toast.makeText(MainActivity.this, "Preferences", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent();
                intent1.setClass(MainActivity.this, PreferenceActivity.class);
                startActivity(intent1);
                return true;
            }
        });


        menu.findItem(R.id.refresh).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

//                Intent intent = new Intent(MainActivity.this, Forgemap.class);
//                startActivity(intent);

                return  true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
