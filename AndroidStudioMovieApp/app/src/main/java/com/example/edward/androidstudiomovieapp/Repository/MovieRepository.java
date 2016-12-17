package com.example.edward.androidstudiomovieapp.Repository;

import com.example.edward.androidstudiomovieapp.Model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieRepository
{
    private List<Movie> movieList;

    public MovieRepository()
    {

        movieList = new ArrayList<Movie>();

        movieList.add(new Movie(1,"Wardogs",4));
        movieList.add(new Movie(2,"X-MEN 3",6));
        movieList.add(new Movie(3,"Avengers",8));
    }

    public List<Movie> GetAllMovies()
    {
        return movieList;
    }

    public void AddMovie(String name,int rating)
    {
        movieList.add(new Movie(movieList.get(movieList.size()-1).getId() + 1,name,rating));
    }

    public void EditMovie(int id,String name,int rating)
    {
        for (Movie movie: movieList)
        {
            if(movie.getId() == id)
            {
                movieList.remove(movie);

                movieList.add(new Movie(id,name,rating));

                break;
            }
        }
    }

    public void DeleteMovie(int id)
    {
        for (Movie movie: movieList)
        {
            if(movie.getId() == id)
            {
                movieList.remove(movie);

                break;
            }
        }
    }



}
