package com.example.edward.androidstudiomovieapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import org.json.JSONObject;

public class EditActivity extends AppCompatActivity
{
    private TextView tv;
    private RatingBar rb;
    private Movie currentMovie;
    private Movie updatedMovie;
    private LocalDBHandler dbHandler;

    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        currentMovie = new Movie(getIntent().getIntExtra("MovieId",0),getIntent().getStringExtra("MovieName"),getIntent().getIntExtra("MovieRating",0));

        userId = getIntent().getIntExtra("UserId",0);


        tv = (TextView) findViewById(R.id.editName);
        rb = (RatingBar) findViewById(R.id.addRating);

        tv.setText(currentMovie.getName());
        rb.setRating((float)currentMovie.getRating()/2);


    }

    public void SaveMovie(View view)
    {
        if(userId == -1)
        {
            dbHandler = new LocalDBHandler(this,null,null,1);

            dbHandler.UpdateMovieById(currentMovie.getId(),tv.getText().toString(),Math.round(rb.getRating())*2);

            Intent intent = new Intent(EditActivity.this,MovieListActivity.class);

            intent.putExtra("UserId",userId);

            startActivity(intent);
        }
        else
        {
            updatedMovie = new Movie(currentMovie.getId(),tv.getText().toString(),Math.round(rb.getRating())*2);

            new SaveTheFeed().execute();
        }


    }

    class SaveTheFeed extends AsyncTask<Void,Void,Void>
    {

        String result = "";

        @Override
        protected void onPostExecute(Void aVoid)
        {
            Intent intent = new Intent(EditActivity.this,MovieListActivity.class);

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
                HttpPost request = new HttpPost("http://10.0.2.2:8081/updateMovie");

                JSONObject jsonObject = new JSONObject();

                jsonObject.put("Name", updatedMovie.getName());
                jsonObject.put("Rating", updatedMovie.getRating());
                jsonObject.put("Id", updatedMovie.getId());

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
