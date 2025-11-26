package unrn.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    static final String ERROR_USERNAME_LONGITUD =
            "El nombre de usuario debe tener entre 5 y 25 caracteres";
    static final String ERROR_TWEET_LONGITUD =
            "El tweet debe tener entre 1 y 280 caracteres";
    static final String ERROR_RETWEET_PROPIO =
            "No puedes hacer retweet de tu propio tweet";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username", nullable = false, unique = true, length = 25)
    private String userName;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tweet> tweets = new ArrayList<>();

    // Campos no persistentes
    @Transient
    private String email;

    @Transient
    private String password;

    public User(String userName) {
        assertUserNameLongitud(userName);
        this.userName = userName;
    }

    public Tweet crearTweet(String texto) {
        assertTweetLongitud(texto);
        Tweet tweet = new Tweet(this, texto);
        tweets.add(tweet);
        return tweet;
    }

    public Tweet hacerRetweet(Tweet tweetOriginal) {
        assertNoRetweetPropio(tweetOriginal);
        Tweet retweet = Tweet.retweet(this, tweetOriginal);
        tweets.add(retweet);
        return retweet;
    }

    void eliminarTweets() {
        tweets.clear();
    }

    private void assertUserNameLongitud(String userName) {
        if (userName == null || userName.length() < 5 || userName.length() > 25) {
            throw new RuntimeException(ERROR_USERNAME_LONGITUD);
        }
    }

    private void assertTweetLongitud(String texto) {
        if (texto == null || texto.length() < 1 || texto.length() > 280) {
            throw new RuntimeException(ERROR_TWEET_LONGITUD);
        }
    }

    private void assertNoRetweetPropio(Tweet tweetOriginal) {
        if (tweetOriginal.esAutor(this)) {
            throw new RuntimeException(ERROR_RETWEET_PROPIO);
        }
    }
}
