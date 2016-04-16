package hakaton.haze.android.hackatonapp.app;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import org.w3c.dom.Text;

public class Survey extends AppCompatActivity
{

    private int leftImage = 0, rightImage = 1;
    private int firstXpos, secondXpos, orTextXpos;
    private ImageView first, second;
    private TextView naslov;
    private ViewSwitcher vs;
    private LinearLayout layout;
    private int images[] = {R.drawable.meat, R.drawable.veggies_br, R.drawable.password_icon_small, R.drawable.instagramicon};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_questions);

        first = (ImageView)findViewById(R.id.imageViewFirst);
        second = (ImageView)findViewById(R.id.imageViewSecond);
        naslov = (TextView) findViewById(R.id.textViewNaslov);
        vs = (ViewSwitcher) findViewById(R.id.viewSwitcher);
        layout = (LinearLayout) findViewById(R.id.holder);

        Animation a = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        vs.setInAnimation(a);
        Animation b = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
        vs.setOutAnimation(b);

        showImages();
    }

    public void showImages(){
        vs.showNext();
        first.setImageResource(images[leftImage]);
        second.setImageResource(images[rightImage]);

        first.setOnClickListener(imageClickListener);
        second.setOnClickListener(imageClickListener);
    }

    View.OnClickListener imageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            System.out.println("User likes: " + v.getResources().toString());

            if(rightImage != (images.length - 1)){
                leftImage++;
                leftImage++;
                rightImage++;
                rightImage++;
                showImages();
            }
            else{
                Intent i = new Intent(Survey.this, MainMenuActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }

        }
    };

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

    @Override
    public void onBackPressed(){

    }
}
