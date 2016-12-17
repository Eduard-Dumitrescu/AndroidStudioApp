package com.example.edward.androidstudiomovieapp.Repository;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.edward.androidstudiomovieapp.Model.Movie;

import java.util.ArrayList;
import java.util.List;

public class LocalDBHandler extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "MovieApp.db";

    private static final String TABLE_Movies = "Movies";
    private static final String COLUMN_ID = "Id";
    private static final String COLUMN_NAME = "Name";
    private static final String COLUMN_RATING = "Rating";

    public LocalDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        String query = "CREATE TABLE " + TABLE_Movies + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_RATING + " INTEGER " +
                ");";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_Movies);
        onCreate(sqLiteDatabase);
    }

    public List<Movie> GetMovies()
    {
        List<Movie> movieList = new ArrayList<Movie>();

        SQLiteDatabase db = getWritableDatabase();
        String querry = "SELECT * FROM " + TABLE_Movies + ";";

        Cursor c = db.rawQuery(querry,null);

        c.moveToFirst();

        while (!c.isAfterLast())
        {
            if(c.getString(c.getColumnIndex(COLUMN_NAME)) != null)
            {
                int id = c.getInt(c.getColumnIndex(COLUMN_ID));
                String name = c.getString(c.getColumnIndex(COLUMN_NAME));
                int rating  = c.getInt(c.getColumnIndex(COLUMN_RATING));

                movieList.add(new Movie(id,name,rating));
            }
            c.moveToNext();
        }

        db.close();

        return movieList;
    }

    public void AddMovie(String name,int rating)
    {
        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME,name);
        values.put(COLUMN_RATING,rating);

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_Movies,null,values);
        db.close();
    }

    public void AddMovie(Movie movie)
    {
        AddMovie(movie.getName(),movie.getRating());
    }

    public void DeleteMovieById(int id)
    {

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM  " + TABLE_Movies + " WHERE " + COLUMN_ID + "=" + id + ";");

    }

    public void UpdateMovieById(int id,String name,int rating)
    {
        String query = "UPDATE " + TABLE_Movies + " SET " +
                COLUMN_NAME + "=\'" + name +"\', " +
                COLUMN_RATING + "=" + rating + " WHERE " +
                COLUMN_ID + "=" + id + ";";

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);

    }

    public void UpdateMovieById(Movie movie)
    {
        UpdateMovieById(movie.getId(),movie.getName(),movie.getRating());
    }

    public String databaseToString()
    {
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String querry = "SELECT * FROM " + TABLE_Movies;

        Cursor c = db.rawQuery(querry,null);

        c.moveToFirst();

        while (!c.isAfterLast())
        {
            if(c.getString(c.getColumnIndex(COLUMN_NAME)) != null)
            {
                dbString += c.getString(c.getColumnIndex(COLUMN_ID));
                dbString += ",";
                dbString += c.getString(c.getColumnIndex(COLUMN_NAME));
                dbString += ",";
                dbString += c.getString(c.getColumnIndex(COLUMN_RATING));
                dbString += "@";
            }
            c.moveToNext();
        }

        db.close();
        return dbString;

    }


}
