package application.android.com.watchthewatchers;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import application.android.com.watchthewatchers.Core.SurveillanceCam;


//Adapter to show the nearby Cameras in a Listview

public class CameraListAdapter extends BaseAdapter
{
    private ArrayList<SurveillanceCam> nearbyCameras;
    private Context context;
    private static final String TAG = "CameraListAdapter";
    ViewHolder viewHolder;


    public CameraListAdapter(ArrayList<SurveillanceCam> nearbyCameras, Context context){
        this.nearbyCameras = nearbyCameras;
        this.context = context;
    }
    @Override
    public int getCount() {
        return nearbyCameras.size();
    }

    @Override
    public SurveillanceCam getItem(int i) {
        return nearbyCameras.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.camera_listview_item, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        SurveillanceCam currentItem =  getItem(i);

        viewHolder.setDistance(currentItem.getDistance().intValue()+"m");
        viewHolder.setId("ID");

        return view;
    }

 class ViewHolder{

    public TextView id;
     private TextView distance;
     private ImageView iv;


      ViewHolder(View view) {
          id =  view.findViewById(R.id.camera_id);
       distance =  view.findViewById(R.id.camera_distance);
       iv =  view.findViewById(R.id.camera_icon);

       view.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
              Intent intent = new Intent(context, CameraDetailActivity.class);

               context.startActivity(intent);
           }
       });
      }
     public String getDistance() {
         return distance.getText().toString();
     }

     private void setDistance(String distance) {

        this.distance.setText(distance);
    }

    public void setId(String id) {
        this.id.setText(id);
    }
}

}
