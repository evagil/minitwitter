package unrn.model;

public class Tweet {
    private final User autor;
    private final String texto;
    private final Tweet tweetOriginal;
    private int id;

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

    public boolean esAutor(User user) {
        return this.autor == user;
    }

    public boolean esRetweet() {
        return tweetOriginal != null;
    }

    public Tweet tweetDeOrigen() {
        return tweetOriginal;
    }

    // Getters para persistencia
    public int getId() {
        return id;
    }

    public int getAutorId() {
        return autor != null ? autor.getId() : 0;
    }

    public String getTexto() {
        return texto;
    }

    public void setId(int id) {
        this.id = id;
    }
}