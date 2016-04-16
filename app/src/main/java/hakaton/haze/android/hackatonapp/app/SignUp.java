package hakaton.haze.android.hackatonapp.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.facebook.*;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;


public class SignUp extends Activity implements URLS
{

    private static final String API_URL = "https://api.instagram.com/v1";
    private static final String TAG = "InstagramAPI";

    private InstagramSession mSession;
    private EditText name, lastName, password, confirmPassword, email,gender;
    public static Context c;
    boolean rememberMeOption = false;
    private InstagramApp mApp;
    private HashMap<String, String> userInfoHashmap = new HashMap<String, String>();
    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == InstagramApp.WHAT_FINALIZE) {
                userInfoHashmap = mApp.getUserInfo();
            } else if (msg.what == InstagramApp.WHAT_FINALIZE) {
                Toast.makeText(SignUp.this, "Check your network.",
                        Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    });
    CallbackManager callbackManager;
    ArrayList<String> likes = new ArrayList<String>();
    LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {

            }
        });



        setContentView(R.layout.activity_sign_up);

        Intent i = getIntent();
        c = this;

        name = (EditText) findViewById(R.id.signUpName);
        lastName = (EditText) findViewById(R.id.signUpSurname);
        password = (EditText) findViewById(R.id.signUpPassword);
        confirmPassword = (EditText) findViewById(R.id.signUpConfirmPassword);
        email = (EditText) findViewById(R.id.signUpEmail);
        gender = (EditText) findViewById(R.id.editTextGender);

        mSession = new InstagramSession(this);
        mApp = new InstagramApp(this, InstagramAppData.CLIENT_ID,
                InstagramAppData.CLIENT_SECRET, InstagramAppData.CALLBACK_URL);
        mApp.setListener(new InstagramApp.OAuthAuthenticationListener() {

            @Override
            public void onSuccess() {
                mApp.fetchUserName(handler);
                URL url;
                try {

                    url = new URL(API_URL + "/users/" + mSession.getId()
                            + "/?access_token=" + mSession.getAccessToken());
                    getUserInformation(url);

                    /*String usrn = obj.getString("username");
                    String fullname = obj.getString("full_name");

                    String[] parts = fullname.split(" ");

                    postSignup(parts[0], parts[1], usrn, "null", "null", "null");*/


                    //tags
                    url = new URL(API_URL + "/tags/hakaton/media/recent?access_token=" + mSession.getAccessToken());
                    getTagInformation(url);

                    url = new URL(API_URL + "/tags/hakaton?access_token=" + mSession.getAccessToken());
                    getTagCount(url);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }


                startActivity(new Intent(SignUp.this, Survey.class));
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(SignUp.this, error, Toast.LENGTH_SHORT)
                        .show();
            }
        });

        loginButton = (LoginButton)findViewById(R.id.login_facebook_button);
        loginButton.setReadPermissions(Arrays.asList("user_likes","user_about_me","user_managed_groups",
                "user_tagged_places","user_location","user_birthday","user_friends","email",
                "public_profile","user_status","user_photos","user_games_activity"));


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getUserLikes();
                getUser();
                getUserGroups();
                getUserPhotos();
                getUserEmail(loginResult);


                String accessToken = loginResult.getAccessToken().getToken();
                Log.i("accessToken", accessToken);

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        System.out.println(response.toString());
                        //Bundle bFacebookData = getFacebookData(object);
                        JSONObject graphObject = response.getJSONObject();
                        getFacebookData(graphObject);
                        try {
                            String lastName = graphObject.getString("first_name");
                            String firstName = graphObject.getString("last_name");
                            String birthday = graphObject.getString("birthday");
                            String gender = graphObject.getString("gender");
                            String email = graphObject.getString("email");
                            System.out.println(lastName + firstName + birthday + gender+email);
                            JSONObject location = graphObject.getJSONObject("location");
                            String locationString = location.getString("name");
                            postSignup(firstName,lastName,email,gender,birthday,locationString);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
            });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id, first_name, last_name, email, gender, birthday, location");
            request.setParameters(parameters);
            request.executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }

    private void postSignup(String first, String last, String email, String gender, String birthday, String location)
    {
        try {
            BackgroundHTTP b = new BackgroundHTTP(this);
            b.execute(SERVER_URL_SignupFacebook, "signupFacebook", first, last, email, gender, birthday, location, getMyPhoneNumber());
            String response = b.get();
            Toast.makeText(this,"RESPONSE " +response,Toast.LENGTH_LONG).show();
            if(response.equals("201"))
            {
                finish();
                Intent i = new Intent(this, Survey.class);
                startActivity(i);
            }
        }catch(ExecutionException e)
        {
            e.printStackTrace();
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private Bundle getFacebookData(JSONObject object) {
        Bundle bundle =null;
        try {
            bundle = new Bundle();
            String id = object.getString("id");
            getUserEvents(id);
            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
                Log.i("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));
            if (object.has("birthday"))
                bundle.putString("birthday", object.getString("birthday"));
            if (object.has("location"))
                bundle.putString("location", object.getJSONObject("location").getString("name"));


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bundle;
    }

    private void getUserEvents(String id)
    {
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/"+id+"/events",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        System.out.println("Events:" + response.toString());
                    }
                }
        ).executeAsync();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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

    public String getMyPhoneNumber() {
        TelephonyManager mTelephonyMgr;
        mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if(!mTelephonyMgr.getLine1Number().toString().equals(""))
            return mTelephonyMgr.getLine1Number();
        else
            return "-1";
    }

    public void confirmSignUp(View v) throws ExecutionException, InterruptedException {
        BackgroundHTTP b = new BackgroundHTTP(this);
        b.execute(SERVER_URL_Signup, "signup", name.getText().toString(), lastName.getText().toString(),email.getText().toString(),
                password.getText().toString(), gender.getText().toString());
        String response = b.get();
        if(response.equals("201"))
        {
            finish();
            Intent i = new Intent(this, Survey.class);
            startActivity(i);
        }
    }

    private void getUserEmail(LoginResult loginResult)
    {
        GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject me, GraphResponse response) {
                System.out.println("Email " +response.toString());
            }
        }).executeAsync();
    }

    private void getUserPhotos()
    {
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/photos",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        System.out.println("Photos: " + response.toString());
                    }
                }
        ).executeAsync();
    }

    private void getUserGroups() {
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/groups",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        System.out.println("Groups: " + response.toString());
                    }
                }
        ).executeAsync();
    }

    private void getUserLikes()
    {
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "me/likes/",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        JSONObject graphObject = response.getJSONObject();
                        try {
                            JSONArray array = graphObject.getJSONArray("data");
                            for(int i =0; i < array.length(); i++) {
                                String json = array.get(i).toString();
                                JSONObject obj = new JSONObject(json);
                                System.out.println(obj.getString("name"));
                                likes.add(obj.getString("name"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }

    private void getUser()
    {
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        System.out.println(response.toString());
                        JSONObject graphObject = response.getJSONObject();
                        try {
                            String id = graphObject.getString("id");
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);


    }

    public void connectOrDisconnectUser(View v) {
        if (mApp.hasAccessToken()) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(
                    SignUp.this);
            builder.setMessage("Disconnect from Instagram?")
                    .setCancelable(false)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    mApp.resetAccessToken();
                                }
                            })
                    .setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.cancel();
                                }
                            });
            final AlertDialog alert = builder.create();
            alert.show();
        } else {
            mApp.authorize();
        }
    }

    public void getUserInformation(final URL url){
        new Thread() {
            @Override
            public void run() {
                try {

                    Log.d(TAG, "Opening URL " + url.toString());
                    HttpURLConnection urlConnection = (HttpURLConnection) url
                            .openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setDoInput(true);
                    urlConnection.connect();
                    String response = Utils.streamToString(urlConnection
                            .getInputStream());

                    JSONObject jObj = new JSONObject(response);
                    JSONObject obj = jObj.getJSONObject("data");

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void getTagInformation(final URL url){
        new Thread() {
            @Override
            public void run() {
                try {

                    Log.d(TAG, "Opening URL " + url.toString());
                    HttpURLConnection urlConnection = (HttpURLConnection) url
                            .openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setDoInput(true);
                    urlConnection.connect();
                    String response = Utils.streamToString(urlConnection
                            .getInputStream());

                    JSONObject jObj = new JSONObject(response);
                    JSONArray array = jObj.getJSONArray("data");
                    for(int i =0; i < array.length(); i++) {
                        String json = array.get(i).toString();
                       // System.out.println(json);
                        JSONObject o = array.getJSONObject(i);
                        JSONArray a = o.getJSONArray("tags");
                        for(int j =0;j < a.length(); j++) {
                            System.out.println(a.get(j).toString());
                        }
                    }


                    //System.out.println(tags.toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void getTagCount(final URL url){
        new Thread() {
            @Override
            public void run() {
                try {

                    Log.d(TAG, "Opening URL " + url.toString());
                    HttpURLConnection urlConnection = (HttpURLConnection) url
                            .openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setDoInput(true);
                    urlConnection.connect();
                    String response = Utils.streamToString(urlConnection
                            .getInputStream());

                    JSONObject jObj = new JSONObject(response);
                    JSONObject array = jObj.getJSONObject("data");
                    System.out.println(array.getString("media_count"));


                    //System.out.println(tags.toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
