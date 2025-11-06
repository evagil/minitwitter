package unrn.services;

import unrn.model.Tweet;
import unrn.model.User;
import unrn.repository.TweetRepository;
import unrn.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TweetService {
    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    public TweetService(TweetRepository tweetRepository, UserRepository userRepository) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
    }

    public Tweet crearTweet(User user, String texto) {
        // Si el usuario no existe en la base, lo guarda
        if (user.getId() == null || user.getId() == 0) {
            user = userRepository.save(user);
        } else {
            // Asegurar que el usuario está actualizado
            user = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        }
        Tweet tweet = user.crearTweet(texto);
        return tweetRepository.save(tweet);
    }

    public Tweet hacerRetweet(User user, Tweet tweetOriginal) {
        if (user.getId() == null || user.getId() == 0) {
            user = userRepository.save(user);
        } else {
            user = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        }
        // Asegurar que el tweet original existe
        tweetOriginal = tweetRepository.findById(tweetOriginal.getId())
            .orElseThrow(() -> new RuntimeException("Tweet original no encontrado"));
        Tweet retweet = user.hacerRetweet(tweetOriginal);
        return tweetRepository.save(retweet);
    }

    public Tweet hacerRetweetConComentario(User user, Tweet tweetOriginal, String comentario) {
        if (user.getId() == null || user.getId() == 0) {
            user = userRepository.save(user);
        } else {
            user = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        }
        // Asegurar que el tweet original existe
        tweetOriginal = tweetRepository.findById(tweetOriginal.getId())
            .orElseThrow(() -> new RuntimeException("Tweet original no encontrado"));
        Tweet retweet = user.hacerRetweetConComentario(tweetOriginal, comentario);
        return tweetRepository.save(retweet);
    }

    public List<Tweet> obtenerTweetsDeUsuario(User user) {
        return tweetRepository.findByAutorOrderByCreatedAtDesc(user);
    }

    public Tweet buscarTweetPorId(int id) {
        return tweetRepository.findById(id).orElse(null);
    }

    public List<Tweet> obtenerTodosLosTweets(int offset, int limit) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        Page<Tweet> page = tweetRepository.findTweetsOriginales(pageable);
        return page.getContent();
    }

    public int contarTweets() {
        return (int) tweetRepository.countTweetsOriginales();
    }

    public List<Tweet> obtenerTweetsDeUsuarioConPaginacion(User user, int offset, int limit) {
        List<Tweet> todos = tweetRepository.findByAutorOrderByCreatedAtDesc(user);
        int inicio = Math.min(offset, todos.size());
        int fin = Math.min(offset + limit, todos.size());
        if (inicio >= todos.size()) {
            return List.of();
        }
        return todos.subList(inicio, fin);
    }
}
