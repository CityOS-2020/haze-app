package hakaton.haze.android.hackatonapp.app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;


public class MenuActivity extends ActionBarActivity {
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
        int position = i.getIntExtra("int", 0);
        MarkerOptions markerOptions = new MarkerOptions();
        LatLng latLng = new LatLng(ListAdapter.eventinfo.get(position).getLng(),ListAdapter.eventinfo.get(position).getLat());
        markerOptions.position(latLng);
        markerOptions.title(ListAdapter.eventinfo.get(position).getEventName());

        //instanciranje mape
        myMap= ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        myMap.setMyLocationEnabled(true);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        addMyLocation();

        myMap.addMarker(markerOptions);
        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                Float.parseFloat(ListAdapter.eventinfo.get(position).getLng()+""),Float.parseFloat(ListAdapter.eventinfo.get(position).getLat()+"")), 7));



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        TextView name = (TextView)findViewById(R.id.name);
        name.setSelected(true);
        TextView city = (TextView) findViewById(R.id.city);
        city.setSelected(true);
        TextView description = (TextView) findViewById(R.id.description);
        TextView date = (TextView) findViewById(R.id.date);
        TextView time = (TextView)findViewById(R.id.time);

        String ename = ListAdapter.eventinfo.get(position).getEventName();
        String ecity = ListAdapter.eventinfo.get(position).getCity();
        String edescription = ListAdapter.eventinfo.get(position).getDesc();
        String edate = ListAdapter.eventinfo.get(position).getDate();
        String etime = ListAdapter.eventinfo.get(position).getTime();


        if(!ename.equalsIgnoreCase("null"))
            name.setText(ename);

        if(!ecity.equalsIgnoreCase("null"))
            city.setText(ecity);

        if(!edescription.equalsIgnoreCase("null"))
            description.setText(edescription);

        if(!edate.equalsIgnoreCase("null"))
            date.setText(edate);

        if(!etime.equalsIgnoreCase("null"))
            time.setText(etime);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
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
