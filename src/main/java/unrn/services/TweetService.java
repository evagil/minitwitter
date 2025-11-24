package unrn.services;

import unrn.dto.RetweetDetailsDto;
import unrn.model.Tweet;
import unrn.model.User;
import unrn.repository.TweetRepository;
import unrn.repository.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TweetService {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    public TweetService(TweetRepository tweetRepository, UserRepository userRepository) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
    }

    private User ensureUserExists(User user) {
        return userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public Tweet crearTweet(User user, String texto) {
        user = ensureUserExists(user);
        Tweet tweet = user.crearTweet(texto);
        return tweetRepository.save(tweet);
    }

    public Tweet hacerRetweet(User user, Tweet tweetOriginal, String comentario) {
        user = ensureUserExists(user);

        tweetOriginal = tweetRepository.findById(tweetOriginal.getId())
                .orElseThrow(() -> new RuntimeException("Tweet original no encontrado"));

        Tweet retweet = user.hacerRetweet(tweetOriginal, comentario);
        return tweetRepository.save(retweet);
    }

    public List<Tweet> obtenerTweetsDeUsuario(User user) {
        return tweetRepository.findByAutorOrderByCreatedAtDesc(user);
    }

    public Tweet buscarTweetPorId(int id) {
        return tweetRepository.findById(id).orElse(null);
    }

    public List<Tweet> obtenerTodosLosTweets(int offset, int limit) {
        Page<Tweet> page = tweetRepository.findTweetsOriginales(
                PageRequest.of(offset / limit, limit)
        );
        return page.getContent();
    }

    public int contarTweets() {
        return (int) tweetRepository.countTweetsOriginales();
    }

    public List<Tweet> obtenerTweetsDeUsuarioConPaginacion(User user, int offset, int limit) {
        Page<Tweet> page = tweetRepository.findByAutor(
                user,
                PageRequest.of(offset / limit, limit)
        );
        return page.getContent();
    }

    public List<RetweetDetailsDto> listarRetweets(int offset, int limit) {
        Page<Tweet> page = tweetRepository.findRetweets(PageRequest.of(offset / limit, limit));

        return page.stream()
                .map(retweet -> {
                    RetweetDetailsDto dto = new RetweetDetailsDto();
                    dto.setRetweetId(retweet.getId());

                    if (retweet.getAutor() != null) {
                        dto.setRetweeterId(retweet.getAutor().getId());
                        dto.setRetweeterUserName(retweet.getAutor().getUserName());
                    }

                    if (retweet.getTweetOriginal() != null) {
                        dto.setOriginalTweetId(retweet.getTweetOriginal().getId());
                        if (retweet.getTweetOriginal().getAutor() != null) {
                            dto.setOriginalAuthorId(retweet.getTweetOriginal().getAutor().getId());
                            dto.setOriginalAuthorUserName(retweet.getTweetOriginal().getAutor().getUserName());
                        }
                    }

                    dto.setComentario(retweet.getTexto());
                    return dto;
                })
                .toList();
    }
}
