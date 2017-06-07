package nl.exocare.ipmedt4;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class BeenActivity extends AppCompatActivity {
    private ImageView image;

    //opslaan in database???
    private String jouwBreuk;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gebroken);
    }

    /**
     * methode als er op het poppetje gedrukt wordt
     *
     * @param v
     */
    public void imageClick(View v) {
        image = (ImageView) findViewById(v.getId());
        image.setDrawingCacheEnabled(true);
        image.setOnTouchListener(changeColorListener);
    }

    /**
     * on touch listener voor het vinden van de kleuren en het aangeklikte bot
     *
     */
    private final View.OnTouchListener changeColorListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(final View v, MotionEvent ev) {

            final int action = ev.getAction();
            // get coords van touch
            final int evX = (int) ev.getX();
            final int evY = (int) ev.getY();

            ImageView imageView = (ImageView) findViewById(R.id.botzien);

            switch (action) {
                case MotionEvent.ACTION_UP :
                    int touchColor = getHotspotColor(R.id.botzienAreas, evX, evY);
                    ColorTool ct = new ColorTool();
                    int tolerance = 25;
                    if (ct.closeMatch (Color.RED, touchColor, tolerance)) {
                        jouwBreuk = "bovenbeen";
                        imageView.setImageResource(R.drawable.botzienbovenbeen);
                    } else if (ct.closeMatch (Color.BLUE, touchColor, tolerance)) {
                        jouwBreuk = "knie";
                        imageView.setImageResource(R.drawable.botzienknie);
                    } else if (ct.closeMatch (Color.GREEN, touchColor, tolerance)) {
                        jouwBreuk = "onderbeen";
                        imageView.setImageResource(R.drawable.botzienonderbeen);
                    } else if (ct.closeMatch (Color.YELLOW, touchColor, tolerance)) {
                        jouwBreuk = "voet";
                        imageView.setImageResource(R.drawable.botzienvoet);
                    }
                    break;
            } // end switch
            return true;
        }
    };

    /**
     * het opzoeken van welke kleur aangeraakt wordt
     *
     * @param hotspotId
     * @param x
     * @param y
     * @return
     */
    public int getHotspotColor (int hotspotId, int x, int y) {
        ImageView img = (ImageView) findViewById (hotspotId);
        img.setDrawingCacheEnabled(true);
        Bitmap hotspots = Bitmap.createBitmap(img.getDrawingCache());
        img.setDrawingCacheEnabled(false);
        return hotspots.getPixel(x, y);
    }
}
