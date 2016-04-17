package hakaton.haze.android.hackatonapp.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by mateosokac on 17/04/16.
 */
public class ListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    public static List<Event> eventinfoSort;
    public int cnt = 0;

    public ListAdapter(Activity activity, List<Event> movieItems2) {
        this.activity = activity;
        this.eventinfoSort = movieItems2;
    }

    @Override
    public int getCount() {
        return eventinfoSort.size();
    }

    @Override
    public Object getItem(int location) {
        return eventinfoSort.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);

        RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.itemlayout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, MenuActivity.class);
                i.putExtra("int", position );
                activity.startActivity(i);
            }
        });

        TextView name = (TextView) convertView.findViewById(R.id.name);
        name.setSelected(true);
        TextView city = (TextView) convertView.findViewById(R.id.city);
        city.setSelected(true);
        TextView description = (TextView) convertView.findViewById(R.id.description);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        TextView time = (TextView) convertView.findViewById(R.id.time);

        String ename = eventinfoSort.get(position).getEventName();
        String ecity = eventinfoSort.get(position).getCity();
        String edescription = eventinfoSort.get(position).getDesc();
        String edate = eventinfoSort.get(position).getDate();
        String etime = eventinfoSort.get(position).getTime();

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

        return convertView;
    }

}
