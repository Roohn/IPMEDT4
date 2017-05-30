package nl.exocare.ipmedt4;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ViewFlipper;

public class MainActivity extends AppCompatActivity {

    private ViewFlipper includeChange;
    private ImageView image;
    private int nextImage;

    //database???
    private String jouwBreuk;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    includeChange.setDisplayedChild(0);
                    return true;
                case R.id.navigation_dashboard:
                    includeChange.setDisplayedChild(1);
                    return true;
                case R.id.navigation_notifications:
                    includeChange.setDisplayedChild(2);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        includeChange = (ViewFlipper)findViewById(R.id.vf);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void imageClick(View v) {
        image = (ImageView) findViewById(v.getId());
        image.setDrawingCacheEnabled(true);
        image.setOnTouchListener(changeColorListener);
    }

    //on touch listener voor het vinden van de kleuren en aangeklikte bot
    private final View.OnTouchListener changeColorListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(final View v, MotionEvent ev) {

            final int action = ev.getAction();
            // get coords van touch
            final int evX = (int) ev.getX();
            final int evY = (int) ev.getY();

            ImageView imageView = (ImageView) findViewById (R.id.botzien);

            switch (action) {
                case MotionEvent.ACTION_UP :
                    int touchColor = getHotspotColor (R.id.botzienAreas, evX, evY);
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

    //het opzoeken van welke kleur aangeraakt wordt
    public int getHotspotColor (int hotspotId, int x, int y) {
        ImageView img = (ImageView) findViewById (hotspotId);
        img.setDrawingCacheEnabled(true);
        Bitmap hotspots = Bitmap.createBitmap(img.getDrawingCache());
        img.setDrawingCacheEnabled(false);
        return hotspots.getPixel(x, y);
    }
}
