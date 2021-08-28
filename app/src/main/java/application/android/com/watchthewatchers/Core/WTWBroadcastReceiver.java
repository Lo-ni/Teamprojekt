package application.android.com.watchthewatchers.Core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.layer.Layers;
import org.mapsforge.map.layer.overlay.Circle;

import java.util.ArrayList;

import application.android.com.watchthewatchers.Game.Utils;

public class WTWBroadcastReceiver extends BroadcastReceiver {

    ArrayList<SurveillanceCam> nearbyCameraList;
    Context context;
    MapView mapView;
    Location currentLocation;

    WTWBroadcastReceiver(Context context, ArrayList<SurveillanceCam> nearbyCameraList, MapView mapView){

        this.nearbyCameraList = nearbyCameraList;
        this.context = context;
        this.mapView = mapView;


    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "OnReceive BR Receiver ", Toast.LENGTH_SHORT).show();

        Bundle bundle = intent.getExtras();

        if(bundle.containsKey("location"))
        {
            Toast.makeText(context, "OnReceive location_update ", Toast.LENGTH_SHORT).show();
//            currentLocation = (Location)bundle.getSerializable("location");
//            addLocationOverlay(mapView);
        }


        if(bundle.containsKey("nearbyCameras"))
        {
              Toast.makeText(context, "OnReceive camera_update ", Toast.LENGTH_SHORT).show();
              ArrayList<SurveillanceCam> cams= (ArrayList<SurveillanceCam>)bundle.getSerializable("nearbyCameras");
              nearbyCameraList.clear();
              nearbyCameraList.addAll(cams);
              addNearbyCamerasOverlay(mapView);
        }



//        });
////        adapter.notifyDataSetChanged();
//
//
//        String location = intent.getStringExtra("location");
//        MainActivity.currentPosition = CreateOsmNode.createLocation(location);

        //tv.setText(intent.getExtras().get("coordinate")+"\n");
    }




    public void addNearbyCamerasOverlay(MapView mapView){

        Layers layers = mapView.getLayerManager().getLayers();

        ArrayList<LatLong>latLongList= new ArrayList<>();
        for(int i=0; i<nearbyCameraList.size();i++)
        {
            LatLong latLong = new LatLong(nearbyCameraList.get(i).getGpsPosition().getLatitude(),nearbyCameraList.get(i).getGpsPosition().getLongitude());
            latLongList.add(latLong);
        }

        //iterates over every Camera LatLongs and draws an Marker
        for(int i =0;i<latLongList.size();i++){
            Circle circle = new Circle(latLongList.get(i), 20, Utils.createPaint(
                    AndroidGraphicFactory.INSTANCE.createColor(Color.BLACK), 0,
                    Style.FILL), null)
                    {
                        //ontap
                    };
            layers.add(circle);
        }
    }

//    public void addLocationOverlay(MapView mapView)
//    {
//        Layers layers = mapView.getLayerManager().getLayers();
//        LatLong currentLatLong = new LatLong(currentLocation.getLatitude(), currentLocation.getLongitude());
//        Circle PositionMarker = new Circle(currentLatLong, 120, Utils.createPaint(
//                AndroidGraphicFactory.INSTANCE.createColor(Color.BLUE), 0,
//                Style.FILL), null) {};
//        layers.add(PositionMarker);
//    }

}
