package hakaton.haze.android.hackatonapp.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class EventList extends AppCompatActivity implements URLS{

    ListView list;
    ListAdapter adapter;
    List<Event> l;
    boolean sort = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        Intent i = getIntent();
        l = new ArrayList<Event>();
        if(!sort)
            prepareData();
        else
            prepareDataSort();

        list = (ListView)findViewById(R.id.list);
        adapter = new ListAdapter(this, l);
        list.setAdapter(adapter);

    }

    private void prepareData() {
        BackgroundHTTP backgroundHTTP = new BackgroundHTTP(this);
        backgroundHTTP.execute(SERVER_URL_EVENT,"event", "", "");
        try {
            JSONObject obj = new JSONObject(backgroundHTTP.get());
            JSONArray arr =obj.getJSONArray("data");
            for(int i = 0; i< arr.length(); i++) {
                JSONObject o = new JSONObject(arr.get(i).toString());
                l.add(new Event(o.getString("eventName"), o.getString("category"), o.getString("description"), o.getString("eventDate"),
                        o.getString("eventTime"), o.getDouble("latitude"), o.getDouble("longitude"), o.getString("cityName"),
                        o.getString("keyword1"), o.getString("keyword2"), o.getString("keyword3")));


            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void prepareDataSort() {
        BackgroundHTTP backgroundHTTP = new BackgroundHTTP(this);
        backgroundHTTP.execute(SERVER_URL_EVENT_Sort,"event", " " ,Survey.user );

        try {
            JSONObject obj = new JSONObject(backgroundHTTP.get());
            JSONArray arr =obj.getJSONArray("data");
            for(int i = 0; i< arr.length(); i++)
            {
                JSONObject o = new JSONObject(arr.get(i).toString());
                    l.add(new Event(o.getString("eventName"), o.getString("category"), o.getString("description"), o.getString("eventDate"),
                            o.getString("eventTime"), o.getDouble("latitude"), o.getDouble("longitude"), o.getString("cityName"),
                            o.getString("keyword1"), o.getString("keyword2"), o.getString("keyword3")));

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            if(sort)
                sort =  false;
            else
                sort = true;

            list.invalidate();
            list.invalidateViews();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
