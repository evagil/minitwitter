package unrn.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    static final String ERROR_USERNAME_DUPLICADO = "El nombre de usuario ya existe";
    static final String ERROR_USERNAME_LONGITUD = "El nombre de usuario debe tener entre 5 y 25 caracteres";
    static final String ERROR_TWEET_LONGITUD = "El tweet debe tener entre 1 y 280 caracteres";
    static final String ERROR_RETWEET_PROPIO = "No puedes hacer retweet de tu propio tweet";

    private final String userName;
    private final List<Tweet> tweets;
    private int id;

    public User(String userName) {
        assertUserNameLongitud(userName);
        this.userName = userName;
        this.tweets = new ArrayList<>();
    }

    public Tweet crearTweet(String texto) {
        assertTweetLongitud(texto);
        Tweet tweet = new Tweet(this, texto);
        tweets.add(tweet);
        return tweet;
    }

    public Tweet hacerRetweet(Tweet tweetOriginal) {
        assertNoRetweetPropio(tweetOriginal);
        Tweet retweet = Tweet.retweet(this, tweetOriginal);
        tweets.add(retweet);
        return retweet;
    }

    void eliminarTweets() {
        tweets.clear();
    }

    // Getter y setter para persistencia
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private void assertUserNameLongitud(String userName) {
        if (userName == null || userName.length() < 5 || userName.length() > 25) {
            throw new RuntimeException(ERROR_USERNAME_LONGITUD);
        }
    }

    private void assertTweetLongitud(String texto) {
        if (texto == null || texto.length() < 1 || texto.length() > 280) {
            throw new RuntimeException(ERROR_TWEET_LONGITUD);
        }
    }

    private void assertNoRetweetPropio(Tweet tweetOriginal) {
        if (tweetOriginal.esAutor(this)) {
            throw new RuntimeException(ERROR_RETWEET_PROPIO);
        }
    }
}