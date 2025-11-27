package unrn.dto;

import lombok.Data;
import java.util.Date;

@Data
public class TweetDto {
    private Integer id;
    private String texto;
    private Date createdAt;
    private Integer autorId;
    private String autorUserName;
    private Integer tweetOriginalId;
    private String tweetOriginalAutor;
    private Date tweetOriginalCreatedAt;
}

