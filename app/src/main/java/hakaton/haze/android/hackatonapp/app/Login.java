package hakaton.haze.android.hackatonapp.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.facebook.appevents.AppEventsLogger;


public class Login extends FragmentActivity implements URLS
{
    EditText username;
    EditText password;
    public static Context c;
    public CheckBox rememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);




        setContentView(R.layout.activity_login);

        Intent is = getIntent();

        c = this;
        username = (EditText)findViewById(R.id.usernameText);
        password = (EditText)findViewById(R.id.passwordText);
        rememberMe = (CheckBox)findViewById(R.id.checkBox);
        rememberMe.setChecked(true);

        checkRememberMe();
    }



    private void checkRememberMe()
    {

    }



    @Override
    protected void onResume() {
        super.onResume();

        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

    }

    public void signUp(View v)
    {
        Intent i = new Intent(this, SignUp.class);
        startActivity(i);
    }

    //login with real server
    public void login(View v) throws Exception
    {

        String usernameString = username.getText().toString();
        String passwordString = password.getText().toString();

        if (usernameString.toString().equals("") || passwordString.toString().equals(""))
        {
            Toast.makeText(this, "ERROR! Some fields are empty...", Toast.LENGTH_LONG).show(); //every filed must be filled
        }
        else
        {
            BackgroundHTTP b = new BackgroundHTTP(this);
            b.execute(SERVER_URL_Login, "login", usernameString, passwordString);
            String response = b.get();
            if(response.equalsIgnoreCase("200")){
                Intent i = new Intent(this, MenuActivity.class);
                startActivity(i);
            }
        }
    }


}