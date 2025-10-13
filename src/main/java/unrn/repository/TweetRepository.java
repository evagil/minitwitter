package unrn.repository;

import unrn.model.Tweet;
import unrn.model.User;
import java.util.ArrayList;
import java.util.List;

public class TweetRepository {
    private final List<Tweet> tweets = new ArrayList<>();

    public void guardar(Tweet tweet) {
        tweets.add(tweet);
    }

    public List<Tweet> tweetsDeUsuario(User user) {
        List<Tweet> resultado = new ArrayList<>();
        for (Tweet t : tweets) {
            if (t.esAutor(user)) {
                resultado.add(t);
            }
        }
        return resultado;
    }
}
