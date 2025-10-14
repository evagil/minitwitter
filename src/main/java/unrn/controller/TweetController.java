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

    public Tweet crearTweet(String userName, String texto) {
        User user = new User(userName);
        return tweetService.crearTweet(user, texto);
    }

    public Tweet hacerRetweet(String userName, Tweet tweetOriginal) {
        User user = new User(userName);
        return tweetService.hacerRetweet(user, tweetOriginal);
    }

    public List<Tweet> obtenerTweetsDeUsuario(String userName) {
        User user = new User(userName);
        return tweetService.obtenerTweetsDeUsuario(user);
    }
}
