package hakaton.haze.android.hackatonapp.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by mateosokac on 09/07/15.
 */
public class BackgroundHTTP extends AsyncTask<String, Context, String>{
    String[] login = {"email", "password"};
    String[] indvLike = {"like", "user"};
    String[] event = {"like", "user"};

    String[] signup = {"name", "lastname","email", "password","gender"};
    String[] signupFacebook = {"name", "lastname", "email", "gender", "birthday", "location","phone_num"};

    JSONArray arr = null;
    JSONObject obj = null;

    Context c;
    ProgressDialog dialog;



    public BackgroundHTTP(Context c) {
        super();
        this.c = c;
    }

    public BackgroundHTTP(Context c, JSONArray arr) {
        super();
        this.c = c;
        this.arr = arr;
        String json = "{\"name\":" + "\"" + Survey.user + "\", \"tags\":" + arr.toString() + "}";
        System.out.println(json);
        try {
            obj = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(String... params) {

        String output = "null";

        try {

            URL url = new URL(params[0]);
            HttpURLConnection connection = prepareConnection(url);
            String urlParameters = "";

            if(arr == null) {
                String[] arr = whereToPost(params[1]);
                int i = 0;
                for (int j = 2; j < arr.length + 2; j++) {
                    urlParameters += arr[i++] + "=" + params[j] + "&";
                }
                writeToStream(connection, urlParameters);
            }
            else
            {
                connection.setDoInput(true);
                connection.setDoOutput(true);
                String msg = obj.toString();

                connection.connect();

                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                dStream.writeBytes(msg);

            }


            int responseCode = connection.getResponseCode();

            output = generateOutput(url, urlParameters, responseCode, connection);


        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return output;
    }

    private String generateOutput(URL url,String urlParameters, int responseCode, HttpURLConnection connection) throws IOException {
        final StringBuilder output = new StringBuilder("Request URL " + url);
        output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters);
        output.append(System.getProperty("line.separator")  + "Response Code " + responseCode);
        output.append(System.getProperty("line.separator")  + "Type " + "POST");
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line = "";
        StringBuilder responseOutput = new StringBuilder();
        //System.out.println("Returned from server: " + br.readLine());
        String response = br.readLine();
        while((line = br.readLine()) != null ) {
            responseOutput.append(line);
        }
        br.close();
        output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());
        return response;
    }

    private HttpURLConnection prepareConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
        if(arr != null) {
            connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
            connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
        }
        else
            connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");

        connection.setDoOutput(true);

        return connection;
    }

    private void writeToStream(HttpURLConnection connection, String urlParameters) throws IOException {

        DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
        dStream.writeBytes(urlParameters);
        dStream.flush();
        dStream.close();
    }

    private String[] whereToPost(String param)
    {
        if(param.equalsIgnoreCase("login"))
            return login;
        else if(param.equalsIgnoreCase("signup"))
            return signup;
        else if(param.equalsIgnoreCase("signupFacebook"))
            return signupFacebook;
        else if(param.equalsIgnoreCase("indvLike"))
            return indvLike;
        else if(param.equalsIgnoreCase("event"))
            return event;


        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(arr == null)
            dialog = ProgressDialog.show(c, "Loading...", "Please wait...", true);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(arr == null)
            dialog.dismiss();
    }

    @Override
    protected void onProgressUpdate(Context... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(String s) {
        super.onCancelled();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
}

