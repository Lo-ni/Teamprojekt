package application.android.com.watchthewatchers;

import android.content.Context;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.rendertheme.InternalRenderTheme;

import java.io.File;

import application.android.com.watchthewatchers.Core.MainActivity;


public class MapViews {

    public static final String mapcacheID = "MAPCACHE";
    private static final String TAG = "Forgemap";
    public static String MAP_FILE = "map.map";



    public static void createMapView(final Context context, MapView mapView) {


        mapView.setClickable(true);
        mapView.getMapScaleBar().setVisible(true);
        mapView.setBuiltInZoomControls(true);

        /*
         *  Verhindert das überzeichnen der Tiles...
         */
        TileCache tileCache = AndroidUtil.createTileCache(context, mapcacheID,
                mapView.getModel().displayModel.getTileSize(), 1f, mapView.getModel().frameBufferModel.getOverdrawFactor());

        File file = context.getExternalFilesDir("");
        File k = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File newFile = new File(k.getPath() + "/" + MAP_FILE);
//        File newFile = new File(file.getPath() + "/" + MAP_FILE);

        //   Log.d(TAG,"exists: "+ newFile.exists());Log.d(TAG,"name: "+ newFile.getName())Log.d(TAG, newFile.getAbsolutePath())Log.d(TAG, "can write = "+newFile.canWrite()+"");Log.d(TAG, "can read = "+newFile.canRead()+"");

        MapDataStore mapDataStore = new MapFile(newFile);
        TileRendererLayer tileRendererLayer = new TileRendererLayer(tileCache, mapDataStore, mapView.getModel().mapViewPosition, AndroidGraphicFactory.INSTANCE);
        tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.OSMARENDER);

        //linking mapview with tileRenderer
        mapView.getLayerManager().getLayers().add(tileRendererLayer);

        mapView.setCenter(new LatLong(49.7447685, 6.6433435));
        mapView.setZoomLevel((byte) 12);
        }
}
