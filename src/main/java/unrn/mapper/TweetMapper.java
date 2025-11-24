package unrn.mapper;

import unrn.dto.TweetDto;
import unrn.model.Tweet;

public class TweetMapper {

    public static TweetDto toDto(Tweet tweet) {
        TweetDto dto = new TweetDto();
        dto.setId(tweet.getId());
        dto.setTexto(tweet.getTexto());
        dto.setCreatedAt(tweet.getCreatedAt());
        if (tweet.getAutor() != null) {
            dto.setAutorId(tweet.getAutor().getId());
            dto.setAutorUserName(tweet.getAutor().getUserName());
        }
        if (tweet.getTweetOriginal() != null) {
            dto.setTweetOriginalId(tweet.getTweetOriginal().getId());
            if (tweet.getTweetOriginal().getAutor() != null) {
                dto.setTweetOriginalAutor(tweet.getTweetOriginal().getAutor().getUserName());
            }
        }
        return dto;
    }
}

