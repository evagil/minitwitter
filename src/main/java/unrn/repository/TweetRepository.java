package unrn.repository;

import unrn.model.Tweet;
import unrn.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Integer> {
    
    List<Tweet> findByAutorOrderByCreatedAtDesc(User autor);
    
    @Query("SELECT t FROM Tweet t WHERE t.tweetOriginal IS NULL ORDER BY t.createdAt DESC")
    Page<Tweet> findTweetsOriginales(Pageable pageable);
    
    @Query("SELECT COUNT(t) FROM Tweet t WHERE t.tweetOriginal IS NULL")
    long countTweetsOriginales();
    
    void deleteByAutor(User autor);
}
