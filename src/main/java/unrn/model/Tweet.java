package unrn.model;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.util.Date;

@Entity
@Table(name = "tweets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tweet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User autor;

    @Column(name = "texto", nullable = false, length = 280)
    private String texto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tweet_original_id")
    private Tweet tweetOriginal;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    public Tweet(User autor, String texto) {
        this.autor = autor;
        this.texto = texto;
        this.tweetOriginal = null;
    }

    private Tweet(User autor, Tweet tweetOriginal) {
        this.autor = autor;
        // Un retweet no tiene texto adicional, usa el texto del tweet original
        this.texto = tweetOriginal.texto;
        this.tweetOriginal = tweetOriginal;
    }

    // Para reconstrucción desde BD
    public Tweet(int id, User autor, String texto, Tweet tweetOriginal) {
        this.id = id;
        this.autor = autor;
        this.texto = texto;
        this.tweetOriginal = tweetOriginal;
    }

    public static Tweet retweet(User autor, Tweet tweetOriginal) {
        if (tweetOriginal == null) {
            throw new IllegalArgumentException("El tweet original no puede ser nulo");
        }
        return new Tweet(autor, tweetOriginal);
    }

    public boolean esAutor(User user) {
        if (this.autor == null || user == null)
            return false;

        // Caso entidades no persistidas aún
        if (this.autor.getId() == null || user.getId() == null)
            return this.autor == user;

        return this.autor.getId().equals(user.getId());
    }

    public boolean esRetweet() {
        return tweetOriginal != null;
    }

    public Tweet tweetDeOrigen() {
        return tweetOriginal;
    }
}

