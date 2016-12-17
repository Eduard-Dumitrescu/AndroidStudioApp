package com.example.edward.androidstudiomovieapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.edward.androidstudiomovieapp.Service.MovieServices;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity
{
    private EditText usernameInput;
    private EditText passwordInput;
    private TextView errorMessage;

    private String usernameText;
    private String passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameInput = (EditText) findViewById(R.id.userInput);
        passwordInput = (EditText) findViewById(R.id.passwordInput);
        errorMessage  = (TextView) findViewById(R.id.errorMessage);

        errorMessage.setVisibility(View.INVISIBLE);

    }


    public void Login(View view)
    {
        usernameText = usernameInput.getText().toString();
        passwordText = passwordInput.getText().toString();

        new SaveTheFeed().execute();

    }

    public void EnterOffline(View view)
    {
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        intent.putExtra("UserId",-1);
        startActivity(intent);
    }

    class SaveTheFeed extends AsyncTask<Void,Void,Void>
    {
        JSONObject json;

        String result = "";

        @Override
        protected void onPostExecute(Void aVoid)
        {
            try
            {
                json = new JSONObject(result);
                if(json.getBoolean("data"))
                {
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    intent.putExtra("UserId",json.getInt("UserId"));
                    startActivity(intent);
                }
                else
                {
                    errorMessage.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            DefaultHttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());
            //Httpclient

            try
            {
                HttpPost request = new HttpPost("http://10.0.2.2:8081/login");

                JSONObject jsonObject = new JSONObject();

                jsonObject.put("Username", usernameText);
                jsonObject.put("Password", passwordText);

                String message = jsonObject.toString();

                StringEntity params =new StringEntity(jsonObject.toString());
                request.addHeader("content-type", "application/json");
                request.setEntity(params);
                HttpResponse response = httpClient.execute(request);

                result = EntityUtils.toString(response.getEntity());

                // handle response here...
            }catch (Exception ex)
            {
                // handle exception here

            } finally
            {

            }
            return  null;
        }
    }


}
