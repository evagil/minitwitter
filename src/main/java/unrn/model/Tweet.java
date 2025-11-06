package unrn.model;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

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

    @Column(name = "created_at", updatable = false, insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date createdAt;

    public Tweet(User autor, String texto) {
        this.autor = autor;
        this.texto = texto;
        this.tweetOriginal = null;
    }

    private Tweet(User autor, Tweet tweetOriginal) {
        this.autor = autor;
        this.texto = tweetOriginal.texto;
        this.tweetOriginal = tweetOriginal;
    }

    // Constructor para retweet con comentario
    private Tweet(User autor, Tweet tweetOriginal, String comentario) {
        this.autor = autor;
        this.texto = comentario != null && !comentario.trim().isEmpty() 
            ? comentario 
            : tweetOriginal.texto;
        this.tweetOriginal = tweetOriginal;
    }

    // Constructor para reconstruir desde BD
    public Tweet(int id, User autor, String texto, Tweet tweetOriginal) {
        this.id = id;
        this.autor = autor;
        this.texto = texto;
        this.tweetOriginal = tweetOriginal;
    }

    public static Tweet retweet(User autor, Tweet tweetOriginal) {
        return new Tweet(autor, tweetOriginal);
    }

    public static Tweet retweetConComentario(User autor, Tweet tweetOriginal, String comentario) {
        return new Tweet(autor, tweetOriginal, comentario);
    }

    public boolean esAutor(User user) {
        return this.autor != null && this.autor.getId().equals(user.getId());
    }

    public boolean esRetweet() {
        return tweetOriginal != null;
    }

    public Tweet tweetDeOrigen() {
        return tweetOriginal;
    }

    public int getAutorId() {
        return autor != null ? autor.getId() : 0;
    }
}
