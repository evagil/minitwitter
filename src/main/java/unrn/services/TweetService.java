package unrn.services;

import unrn.model.Tweet;
import unrn.model.User;
import unrn.repository.TweetRepository;
import unrn.repository.UserRepository;
import java.util.List;

public class TweetService {
    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    public TweetService(TweetRepository tweetRepository, UserRepository userRepository) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
    }

    public Tweet crearTweet(User user, String texto) {
        // Si el usuario no existe en la base, lo guarda
        if (user.getId() == 0) {
            userRepository.guardar(user);
        }
        Tweet tweet = user.crearTweet(texto);
        tweetRepository.guardar(tweet);
        return tweet;
    }

    public Tweet hacerRetweet(User user, Tweet tweetOriginal) {
        if (user.getId() == 0) {
            userRepository.guardar(user);
        }
        Tweet retweet = user.hacerRetweet(tweetOriginal);
        tweetRepository.guardar(retweet);
        return retweet;
    }

    public List<Tweet> obtenerTweetsDeUsuario(User user) {
        return tweetRepository.tweetsDeUsuario(user);
    }
}
