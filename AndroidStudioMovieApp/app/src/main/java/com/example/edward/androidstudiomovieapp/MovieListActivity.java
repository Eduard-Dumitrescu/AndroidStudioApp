package com.example.edward.androidstudiomovieapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

public class MovieListActivity extends AppCompatActivity
{
    private ListView lv;

    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        userId = getIntent().getIntExtra("UserId",0);
        lv = (ListView) findViewById(R.id.mainMovieList);

        if(userId == -1)
        {
            MovieRepository repo = new MovieRepository();
            LocalDBHandler dbHandler = new LocalDBHandler(this,null,null,1);

            PopulateList(new ArrayList<Movie>(dbHandler.GetMovies()),userId);
        }
        else
        {
           new SaveTheFeed().execute();
        }





    }

    /*
    private Movie GetMovieAtPosition(int givenPosition)
    {
        return this.movieList.get(givenPosition);
    }*/

    private void PopulateList(final List<Movie> movieList, final int UserId)
    {
        List<String> movieNames = new ArrayList<String>();

        for (Movie movie : movieList)
        {
            movieNames.add(movie.getName());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                movieNames );

        lv.setAdapter(arrayAdapter);



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Intent intent = new Intent(MovieListActivity.this,MovieItemActivity.class);

                intent.putExtra("MovieId",movieList.get(i).getId());
                intent.putExtra("MovieName",movieList.get(i).getName());
                intent.putExtra("MovieRating",movieList.get(i).getRating());
                intent.putExtra("UserId",UserId);

                startActivity(intent);
            }

        });
    }

    class SaveTheFeed extends AsyncTask<Void,Void,Void>
    {

        String result = "";

        @Override
        protected void onPostExecute(Void aVoid)
        {

            //super.onPostExecute(aVoid);
            try
            {

                JSONArray jsonArr = new JSONArray(result);
                List<Movie> movieList = new ArrayList<Movie>();

                for (int i=0;i<jsonArr.length();i++)
                {
                    JSONObject obj = jsonArr.getJSONObject(i);
                    movieList.add(new Movie(obj.getInt("Id"),obj.getString("Name"),obj.getInt("Rating")));
                }

                PopulateList(movieList,userId);

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
                HttpPost request = new HttpPost("http://10.0.2.2:8081/movies");

                JSONObject jsonObject = new JSONObject();

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
