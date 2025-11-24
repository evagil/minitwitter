package unrn.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import unrn.model.Tweet;
import unrn.model.User;

import java.util.List;

public interface TweetRepository extends JpaRepository<Tweet, Integer> {

    // Tweets de un usuario, ordenados por fecha desc
    List<Tweet> findByAutorOrderByCreatedAtDesc(User autor);

    // Paginación completa por autor
    Page<Tweet> findByAutor(User autor, Pageable pageable);

    // Tweets originales (no retweets)
    @Query("SELECT t FROM Tweet t WHERE t.tweetOriginal IS NULL ORDER BY t.createdAt DESC")
    Page<Tweet> findTweetsOriginales(Pageable pageable);

    // Contar solo tweets originales
    @Query("SELECT COUNT(t) FROM Tweet t WHERE t.tweetOriginal IS NULL")
    long countTweetsOriginales();

    @Query("SELECT t FROM Tweet t WHERE t.tweetOriginal IS NOT NULL ORDER BY t.createdAt DESC")
    Page<Tweet> findRetweets(Pageable pageable);
}

