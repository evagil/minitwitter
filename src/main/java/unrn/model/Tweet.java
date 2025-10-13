package unrn.model;

public class Tweet {
    private final User autor;
    private final String texto;
    private final Tweet tweetOriginal;

    public Tweet(User autor, String texto) {
        this.autor = autor;
        this.texto = texto;
        this.tweetOriginal = null;
    }

    private Tweet(User autor, Tweet tweetOriginal) {
        this.autor = autor;
        this.texto = tweetOriginal.texto;
        this.tweetOriginal = tweetOriginal;
    }

    public static Tweet retweet(User autor, Tweet tweetOriginal) {
        return new Tweet(autor, tweetOriginal);
    }

    boolean esAutor(User user) {
        return this.autor == user;
    }

    public boolean esRetweet() {
        return tweetOriginal != null;
    }

    public Tweet tweetDeOrigen() {
        return tweetOriginal;
    }
}