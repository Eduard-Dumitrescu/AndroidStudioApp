package com.example.edward.androidstudiomovieapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.edward.androidstudiomovieapp.Model.Movie;
import com.example.edward.androidstudiomovieapp.Repository.LocalDBHandler;
import com.example.edward.androidstudiomovieapp.Repository.MovieRepository;

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

public class MovieItemActivity extends AppCompatActivity
{
    private TextView tv;
    private RatingBar rb;

    private Movie currentMovie;
    private MovieRepository repo;
    private LocalDBHandler dbHandler;

    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_item);

        userId = getIntent().getIntExtra("UserId",0);

        currentMovie = new Movie(getIntent().getIntExtra("MovieId",0),getIntent().getStringExtra("MovieName"),getIntent().getIntExtra("MovieRating",0));

        tv = (TextView) findViewById(R.id.movieTextTitle);
        rb = (RatingBar) findViewById(R.id.ratingBar);

        tv.setText(currentMovie.getName());
        rb.setRating((float)currentMovie.getRating()/2);

        repo = new MovieRepository();
        dbHandler = new LocalDBHandler(this,null,null,1);

    }


    public void DeleteButtonClicked(View view)
    {
        if(userId == -1)
        {
            dbHandler.DeleteMovieById(currentMovie.getId());

            Intent intent = new Intent(MovieItemActivity.this,MovieListActivity.class);

            intent.putExtra("UserId",userId);

            startActivity(intent);
        }
        else
        {
            new SaveTheFeed().execute();
        }

    }

    public void EditButtonClicked(View view)
    {
        Intent intent = new Intent(MovieItemActivity.this,EditActivity.class);

        intent.putExtra("MovieId",currentMovie.getId());
        intent.putExtra("MovieName",currentMovie.getName());
        intent.putExtra("MovieRating",currentMovie.getRating());
        intent.putExtra("UserId",userId);

        startActivity(intent);
    }

    class SaveTheFeed extends AsyncTask<Void,Void,Void>
    {

        String result = "";

        @Override
        protected void onPostExecute(Void aVoid)
        {

            Intent intent = new Intent(MovieItemActivity.this,MovieListActivity.class);

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
                HttpPost request = new HttpPost("http://10.0.2.2:8081/deleteMovie");

                JSONObject jsonObject = new JSONObject();

                jsonObject.put("MovieId", currentMovie.getId());

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
