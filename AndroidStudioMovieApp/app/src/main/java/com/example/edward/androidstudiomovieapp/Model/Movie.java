package com.example.edward.androidstudiomovieapp.Model;


public class Movie
{
    private int id;
    private String name;
    private int rating;

    public Movie() {}

    public Movie(int id, String name, int rating)
    {
        this.id = id;
        this.name = name;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Movie)) return false;

        Movie movie = (Movie) o;

        if (id != movie.id) return false;
        if (rating != movie.rating) return false;
        return name.equals(movie.name);

    }

    @Override
    public String toString()
    {
        return "Movie{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", rating=" + rating +
                '}';
    }
}
