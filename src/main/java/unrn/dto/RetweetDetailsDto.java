package unrn.dto;

import lombok.Data;

@Data
public class RetweetDetailsDto {
    private Integer retweetId;
    private Integer retweeterId;
    private String retweeterUserName;
    private Integer originalTweetId;
    private Integer originalAuthorId;
    private String originalAuthorUserName;
    private String comentario;
}

