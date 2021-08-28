package application.android.com.watchthewatchers.Core;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import application.android.com.watchthewatchers.IOFormat.XmlToSurveillanceCamParser;


/*

Dieser AsyncTask lädt die Kameras von der Overpass API runter und parst diese zu SurveillanceCam Objekten.


 */

public class RequestTask extends AsyncTask<Object, Object, ArrayList<SurveillanceCam>> {

    private static final String TAG = "RequestTask";
    Context context;
    RefreshList refreshList;
    ArrayList<SurveillanceCam> cameraList;

    @Override
    protected ArrayList<SurveillanceCam> doInBackground(Object... objects) {
        Log.d(TAG, "startAsyncTask");

        cameraList = (ArrayList<SurveillanceCam>) objects[0];
        String requestedURL = (String) objects[1];
        this.context = (Context) objects[2];
        refreshList = (RefreshList) objects[3];


        Log.d(TAG, "req URL" + requestedURL);

        try {
            URL url = new URL(requestedURL);
            Log.d(TAG, "url:" + url);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            try {
                if(conn.getResponseCode() == HttpsURLConnection.HTTP_OK){

                    InputStream in = conn.getInputStream();
                    BufferedInputStream bis = new BufferedInputStream(in);
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                    //We create an array of bytes
                    byte[] data = new byte[100];
                    int current = 0;

                    while((current = bis.read(data,0,data.length)) != -1){
                        buffer.write(data,0,current);
                    }

                    File file = new File(context.getFilesDir(), "jopo.osm");

                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(buffer.toByteArray());

                    bis.close();
                    fos.close();

                    try {
                        return XmlToSurveillanceCamParser.parseDocument(file);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                else {
                    Log.e(TAG, "error");
                    //response = "FAILED"; // See documentation for more info on response handling
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onPostExecute(ArrayList<SurveillanceCam> result) {
        super.onPostExecute(result);
        Log.d(TAG, "result: "+ result.size());
        cameraList.addAll(result);
        refreshList.refreshListe();
        this.onCancelled();

    }

    @Override
    protected void onCancelled(ArrayList<SurveillanceCam> surveillanceCams) {
        super.onCancelled(surveillanceCams);
        Toast.makeText(context, "Thread wurde beendet.", Toast.LENGTH_SHORT).show();
    }


}

