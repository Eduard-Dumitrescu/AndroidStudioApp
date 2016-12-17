package com.example.edward.androidstudiomovieapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private ListView lv;
    private Button btnShow;
    private Button btnAdd;

    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = (Button) findViewById(R.id.buttonAddMovie);
        btnShow = (Button) findViewById(R.id.buttonShowMovies);


        LocalDBHandler db = new LocalDBHandler(this,null,null,1);

        userId = getIntent().getIntExtra("UserId",0);

        /*
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MovieListActivity.class));
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, AddActivity.class));
            }

        });*/

        /*
        lv = (ListView) findViewById(R.id.myList);

        List<String> myArray = new ArrayList<String>();

        myArray.add("trump");
        myArray.add("wins");
        myArray.add("fuckyeah");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    myArray
                );

        lv.setAdapter(arrayAdapter);*/

    }


    public void AddButtonClicked(View view)
    {
        Intent intent = new Intent(MainActivity.this,AddActivity.class);

        intent.putExtra("UserId",userId);

        startActivity(intent);
    }

    public void ShowMoviesButtonClicked(View view)
    {
        Intent intent = new Intent(MainActivity.this,MovieListActivity.class);

        intent.putExtra("UserId",userId);

        startActivity(intent);
    }


}
