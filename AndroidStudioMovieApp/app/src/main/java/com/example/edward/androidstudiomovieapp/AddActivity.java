package com.example.edward.androidstudiomovieapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.edward.androidstudiomovieapp.Model.Movie;
import com.example.edward.androidstudiomovieapp.Repository.LocalDBHandler;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddActivity extends AppCompatActivity
{
    private TextView tv;
    private RatingBar rb;
    private LocalDBHandler dbHandler;

    private Movie currentMovie;

    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        dbHandler = new LocalDBHandler(this,null,null,1);

        tv = (TextView) findViewById(R.id.addName);
        rb = (RatingBar) findViewById(R.id.addRating);

        userId = getIntent().getIntExtra("UserId",0);

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    public void SaveMovie(View view)
    {
        currentMovie = new Movie(1,tv.getText().toString(),Math.round(rb.getRating())*2);

        if(userId == -1)
        {
            dbHandler.AddMovie(currentMovie.getName(),currentMovie.getRating());

            Intent intent = new Intent(AddActivity.this,MainActivity.class);

            intent.putExtra("UserId",userId);

            startActivity(intent);
        }
        else
        {
            new SaveTheFeed().execute();
        }

    }

    class SaveTheFeed extends AsyncTask<Void,Void,Void>
    {

        String result = "";

        @Override
        protected void onPostExecute(Void aVoid)
        {
            Intent intent = new Intent(AddActivity.this,MainActivity.class);

            intent.putExtra("UserId",userId);

            startActivity(intent);
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            DefaultHttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());
            //Httpclient

            try
            {
                HttpPost request = new HttpPost("http://10.0.2.2:8081/addMovie");

                JSONObject jsonObject = new JSONObject();

                jsonObject.put("Name", currentMovie.getName());
                jsonObject.put("Rating", currentMovie.getRating());
                jsonObject.put("UserId", userId);

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
