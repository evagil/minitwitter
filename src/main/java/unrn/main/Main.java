package unrn.main;

import unrn.controller.TweetController;
import unrn.services.TweetService;
import unrn.repository.TweetRepository;
import unrn.model.User;
import unrn.model.Tweet;

public class Main {
    public static void main(String[] args) {
        // Inicialización de dependencias
        TweetRepository tweetRepository = new TweetRepository();
        TweetService tweetService = new TweetService(tweetRepository);
        TweetController tweetController = new TweetController(tweetService);

        // Crear usuarios
        User user1 = new User("usuarioUno");
        User user2 = new User("usuarioDos");

        // Crear tweet
        Tweet tweet1 = tweetController.crearTweet(user1, "Hola mundo!");
        System.out.println("Tweet creado: " + tweet1);

        // Hacer retweet
        Tweet retweet = tweetController.hacerRetweet(user2, tweet1);
        System.out.println("Retweet creado: " + retweet);

        // Mostrar tweets de un usuario
        System.out.println("Tweets de usuarioUno: " + tweetController.obtenerTweetsDeUsuario(user1).size());
        System.out.println("Tweets de usuarioDos: " + tweetController.obtenerTweetsDeUsuario(user2).size());
    }
}
