package hakaton.haze.android.hackatonapp.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class Survey extends AppCompatActivity implements URLS
{

    private int leftImage = 1, rightImage = 2;
    private ImageView first, second;
    private TextView naslov;
    private ViewSwitcher vs;
    private HashMap<Integer, Picture> allpictures = new HashMap<Integer, Picture>();
    public static ArrayList<String> likes ;
    public static String user;
    static Context c ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_questions);

        c = this;
        first = (ImageView)findViewById(R.id.imageViewFirst);
        first.setSelected(true);
        second = (ImageView)findViewById(R.id.imageViewSecond);
        second.setSelected(true);
        naslov = (TextView) findViewById(R.id.textViewNaslov);
        vs = (ViewSwitcher) findViewById(R.id.viewSwitcher);
        vs.setSelected(true);

        fillHashMap();

        Animation a = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        vs.setInAnimation(a);
        Animation b = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
        vs.setOutAnimation(b);

        showImages();
    }

    public void showImages(){
        vs.showNext();
        first.setImageResource(allpictures.get(leftImage).getDrawable());
        first.setTag(leftImage);
        second.setImageResource(allpictures.get(rightImage).getDrawable());
        second.setTag(rightImage);

        first.setOnClickListener(imageClickListener);
        second.setOnClickListener(imageClickListener);
    }

    View.OnClickListener imageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int tag = Integer.parseInt(v.getTag().toString());
            System.out.println("User Like: " + allpictures.get(tag).getName());
            reportUserLikes(allpictures.get(tag).getName());

            if(rightImage != (allpictures.size())){
                leftImage++;
                leftImage++;
                rightImage++;
                rightImage++;
                showImages();
            }
            else{

                Toast.makeText(Survey.this, R.string.endSurvey, Toast.LENGTH_SHORT).show();

                Intent i = new Intent(Survey.this, MainMenuActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }

        }
    };

    public void reportUserLikes(String likeName){
        System.out.println("USER on click:" + user);
        BackgroundHTTP b = new BackgroundHTTP(this);
        b.execute(SERVER_URL_INDVLIKE, "indvlike", likeName, user);

    }

    public void fillHashMap(){

        allpictures.put(1, new Picture("Art", R.drawable.art));

        allpictures.put(2, new Picture("Food", R.drawable.food));

        allpictures.put(3, new Picture("Concert", R.drawable.concert));

        allpictures.put(4, new Picture("Renaissance", R.drawable.renaissance));

        allpictures.put(5, new Picture("LCS", R.drawable.lcs));

        allpictures.put(6, new Picture("Basketball", R.drawable.basketball));

        allpictures.put(7, new Picture("Meat", R.drawable.meat));

        allpictures.put(8, new Picture("Veggies", R.drawable.veggies));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_survey, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 1:
                    user = SignUp.emailFb;
                    System.out.println("USER survey 1:" + user);
                    break;
                case 2:
                    user = SignUp.username;
                    System.out.println("USER survey 2:" + user);
                    break;
                case 3:
                    user = SignUp.email.getText().toString();
                    System.out.println("USER survey 3:" + user);
                    break;
                case 4:
                    System.out.println("390218634yihkjwbnsfmdkf");
                    likes = SignUp.likesArray;
                    for(String s : SignUp.likesArray)
                    {
                        System.out.println(s);
                    }
                    postLikeArray();
                    break;
                case 5:
                    System.out.println("390218634yihkjwbnsfmdkf");
                    likes = SignUp.likes;
                    for(String s : SignUp.likes)
                    {
                        System.out.println(s);
                    }
                    postLikeArray();
                    break;
            }
        }
    };

    private static void postLikeArray() {

        Handler handler = new Handler(Looper.getMainLooper());
        final Runnable r = new Runnable() {
            public void run() {

                JSONArray jsArray = new JSONArray(likes);
                BackgroundHTTP b = new BackgroundHTTP(c, jsArray);

                b.execute(SERVER_URL_ARRLIKE, "arrlike");
            }
        };
        handler.postDelayed(r, 10000);

    }
}
