package application.android.com.watchthewatchers;

import android.view.MotionEvent;
import org.mapsforge.map.android.rotation.RotateView;


public class rotateMultiTouch {

    private float rotationAngle = 0;

    public void rotate(MotionEvent event) {
        rotationAngle = rotationCalc(event);
//            RotateView rotateView = (RotateView) findViewById(R.id.rotateView);
//
//            rotateView.setHeading(rotateView.getHeading() - rotationAngle);
//            rotateView.postInvalidate();

//            https://github.com/mapsforge/mapsforge/blob/rotation/Applications/Android/Samples/src/main/java/org/mapsforge/applications/android/samples/RotateMapViewer.java

    }

    //calculates the rotation angle from a multi-touch gesture
    private float rotationCalc(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }




}
