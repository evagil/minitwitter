package unrn.services;

import unrn.model.Tweet;
import unrn.model.User;
import unrn.repository.TweetRepository;
import java.util.List;

public class TweetService {
    private final TweetRepository tweetRepository;

    public TweetService(TweetRepository tweetRepository) {
        this.tweetRepository = tweetRepository;
    }

    public Tweet crearTweet(User user, String texto) {
        Tweet tweet = user.crearTweet(texto);
        tweetRepository.guardar(tweet);
        return tweet;
    }

    public Tweet hacerRetweet(User user, Tweet tweetOriginal) {
        Tweet retweet = user.hacerRetweet(tweetOriginal);
        tweetRepository.guardar(retweet);
        return retweet;
    }

    public List<Tweet> obtenerTweetsDeUsuario(User user) {
        return tweetRepository.tweetsDeUsuario(user);
    }
}
