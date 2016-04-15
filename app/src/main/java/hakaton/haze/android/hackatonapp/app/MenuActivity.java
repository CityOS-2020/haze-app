package hakaton.haze.android.hackatonapp.app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;


public class MenuActivity extends ActionBarActivity {

    private Spinner dropdown;
    private String[] items;
    private static Context c;

    private static GoogleMap myMap;
    private static double lng,lat;
    private static double myLatitude,myLongitude;

    static Location myLocation;
    public static boolean running = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        c = this;
        Intent i = getIntent();


        //instanciranje mape
        myMap= ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        myMap.setMyLocationEnabled(true);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "If you want to log out, press Log out", Toast.LENGTH_LONG).show();
    }



    private static void addMyLocation()
    {
        if(myLocation!=null)
        {
            myLatitude = myLocation.getLatitude();
            myLongitude = myLocation.getLongitude();

            myMap.addMarker(new MarkerOptions().position(
                    new LatLng(myLatitude, myLongitude)).icon(
                    BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_AZURE)).title("MY POSITION"));

            calcDistance(myLatitude, myLongitude, lat, lng);
        }
        else
        {
            Toast.makeText(c, "This device does not support GPS", Toast.LENGTH_LONG).show();
        }
    }


    //calculating distance between two markers
    public static void calcDistance(double lat1, double lng1, double lat2 , double lng2)
    {
        int Radius=6371;//radius of earth in Km

        double diffLat = Math.toRadians(lat2-lat1);
        double diffLon = Math.toRadians(lng2-lng1);

        double a = Math.sin(diffLat/2) * Math.sin(diffLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(diffLon/2) * Math.sin(diffLon/2);

        double finalValue = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * finalValue;
        double kilometars = valueResult/1;

        DecimalFormat myFormat = new DecimalFormat("####");

        int distance = Integer.valueOf(myFormat.format(kilometars));
        System.out.println("KM  "+distance);
        Toast.makeText(c, "Distance to Your dog: " + distance, Toast.LENGTH_LONG).show();
    }
}
