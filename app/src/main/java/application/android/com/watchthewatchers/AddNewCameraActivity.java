package application.android.com.watchthewatchers;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import application.android.com.watchthewatchers.Core.MainActivity;
import application.android.com.watchthewatchers.Core.SurveillanceCam;
import application.android.com.watchthewatchers.IOFormat.CreateOsmNode;
import application.android.com.watchthewatchers.OSMConnection.LoginActivity;



/*
    Diese Klasse ist zuständig für das Erstellen von Kameras.
    Mit dem Klick auf den "Create Camera" Button wird die Login Activity gestartet.
    Diese übernimmt die den Verbindungsaufbau zum OSM Server über OAuth, und lädt die erstellte Kamera hoch.
    Dazu wurde die lib "android-oauth-handler" erweitert und angepasst.

    Beim Hochladen kommt aktuell noch zu einem Syntax Fehler, der noch behoben werden muss.

 */


public class AddNewCameraActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "AddNewCameraActivity";
    LinearLayout lin_governmental;
    LinearLayout lin_commercial;
    LinearLayout lin_private;
    ImageView cameraDome;
    ImageView cameraFixed;
    LinearLayout current_gps_container;
    static SurveillanceCam scam;
    EditText nameField;
    EditText operatorField ;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_surveillance_camera);


        nameField = findViewById(R.id.name);
        operatorField = findViewById(R.id.operator);

        scam = new SurveillanceCam();

        lin_governmental = findViewById(R.id.watchertype_governmental);
        lin_governmental.setOnClickListener(this);

        lin_commercial = findViewById(R.id.watchertype_commercial);
        lin_commercial.setOnClickListener(this);

        lin_private = findViewById(R.id.watchertype_private);
        lin_private.setOnClickListener(this);

        cameraDome = findViewById(R.id.view_camera_dome);
        cameraDome.setOnClickListener(this);

        cameraFixed = findViewById(R.id.view_camera_fixed);
        cameraFixed.setOnClickListener(this);

        current_gps_container= findViewById(R.id.current_gps_location_container);
        current_gps_container.setOnClickListener(this);
    }

    //On Button click Method
    public void openOsmConnection(View view) {

        CreateOsmNode.createChangesetFile(getApplicationContext());
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }


    public static SurveillanceCam getCam(){
     return scam;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){

            /*******  Type of Watcher    *********/
            case R.id.watchertype_governmental:
                view.setBackgroundColor(Color.LTGRAY);
                lin_commercial.setBackgroundColor(Color.WHITE);
                lin_private.setBackgroundColor(Color.WHITE);
                scam.setWatcher_type(SurveillanceCam.WATCHER_TYPE.governmental);
                break;
            case R.id.watchertype_commercial:
                view.setBackgroundColor(Color.LTGRAY);
                lin_governmental.setBackgroundColor(Color.WHITE);
                lin_private.setBackgroundColor(Color.WHITE);
                scam.setWatcher_type(SurveillanceCam.WATCHER_TYPE.commercial);

                break;

            case R.id.watchertype_private:
                view.setBackgroundColor(Color.LTGRAY);
                lin_governmental.setBackgroundColor(Color.WHITE);
                lin_commercial.setBackgroundColor(Color.WHITE);
                scam.setWatcher_type(SurveillanceCam.WATCHER_TYPE.private_);
                break;

            /*******  Type of Camera    *********/

            case R.id.view_camera_dome:
                view.setBackgroundColor(Color.LTGRAY);
                cameraFixed.setBackgroundColor(Color.WHITE);
                scam.setCamera_type(SurveillanceCam.CAMERA_TYPE.dome);
                break;

            case R.id.view_camera_fixed:
                view.setBackgroundColor(Color.LTGRAY);
                cameraDome.setBackgroundColor(Color.WHITE);
                scam.setCamera_type(SurveillanceCam.CAMERA_TYPE.fixed);
                break;

            case R.id.current_gps_location_container:
               TextView tv =  findViewById(R.id.current_gps_location_tv);
               try{
                   String s = "Latitude: " + MainActivity.currentPosition.getLatitude() + ", Longitude: " + MainActivity.currentPosition.getLongitude();
                   tv.setText(s);
                   scam.setGpsPosition(MainActivity.currentPosition);
               }catch (NullPointerException e){
                   Toast.makeText(this, "Start Service first", Toast.LENGTH_SHORT).show();
               }
                break;



        }
    }

}




