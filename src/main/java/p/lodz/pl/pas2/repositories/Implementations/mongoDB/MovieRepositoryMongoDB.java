package p.lodz.pl.pas2.repositories.Implementations.mongoDB;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import p.lodz.pl.pas2.model.Movie;
import p.lodz.pl.pas2.repositories.AbstractMongoRepositoryConfig;
import p.lodz.pl.pas2.repositories.MovieRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class MovieRepositoryMongoDB implements MovieRepository {

    private final MongoCollection<Movie> movieMongoCollection;

    @Autowired
    public MovieRepositoryMongoDB(@NotNull AbstractMongoRepositoryConfig mongoRepo) {
        this.movieMongoCollection = mongoRepo.getDatabase().getCollection("movies", Movie.class);
    }

    @Override
    public Movie findMovie(UUID id) {
        return movieMongoCollection.find(Filters.eq("_id", id)).first();
    }

    @Override
    public List<Movie> findMovies() {
        return movieMongoCollection.find().into(new ArrayList<>());
    }

    @Override
    public Movie saveMovie(Movie movie) {
        movie.setId(UUID.randomUUID());
        movieMongoCollection.insertOne(movie);
        return movie;
    }

    @Override
    public Movie updateMovie(UUID id, Movie movie) {
        return movieMongoCollection.findOneAndUpdate(Filters.eq("_id", id), Updates.combine(
                Updates.set("title", movie.getTitle()),
                Updates.set("cost", movie.getCost())
        ));
    }

    @Override
    public boolean deleteMovie(UUID id) {
        Movie movie = movieMongoCollection.findOneAndDelete(Filters.eq("_id", id));
        return movie != null;
    }
}