package unrn.controller;

import unrn.services.TweetService;
import unrn.model.Tweet;
import unrn.model.User;
import java.util.List;

public class TweetController {
    private final TweetService tweetService;

    public TweetController(TweetService tweetService) {
        this.tweetService = tweetService;
    }

    public Tweet crearTweet(User user, String texto) {
        return tweetService.crearTweet(user, texto);
    }

    public Tweet hacerRetweet(User user, Tweet tweetOriginal) {
        return tweetService.hacerRetweet(user, tweetOriginal);
    }

    public List<Tweet> obtenerTweetsDeUsuario(User user) {
        return tweetService.obtenerTweetsDeUsuario(user);
    }
}
