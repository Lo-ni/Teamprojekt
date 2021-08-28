package application.android.com.watchthewatchers.Game;

import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;

import org.mapsforge.map.android.graphics.AndroidGraphicFactory;

public class Utils {

    public static Paint createPaint(int color, int strokeWidth, Style style) {
        Paint paint = AndroidGraphicFactory.INSTANCE.createPaint();
        paint.setColor(color);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(style);
        return paint;
    }


    private Utils() {
        throw new IllegalStateException();
    }

}
