package unrn.dto;

import java.time.LocalDateTime;

public class TweetDTO {
    private int id;
    private int userId;
    private String userName;
    private String texto;
    private LocalDateTime createdAt;
    private boolean esRetweet;
    private TweetDTO tweetOriginal;
    private Integer userIdRetweet;
    private String userNameRetweet;
    private LocalDateTime createdAtRetweet;

    public TweetDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isEsRetweet() {
        return esRetweet;
    }

    public void setEsRetweet(boolean esRetweet) {
        this.esRetweet = esRetweet;
    }

    public TweetDTO getTweetOriginal() {
        return tweetOriginal;
    }

    public void setTweetOriginal(TweetDTO tweetOriginal) {
        this.tweetOriginal = tweetOriginal;
    }

    public Integer getUserIdRetweet() {
        return userIdRetweet;
    }

    public void setUserIdRetweet(Integer userIdRetweet) {
        this.userIdRetweet = userIdRetweet;
    }

    public String getUserNameRetweet() {
        return userNameRetweet;
    }

    public void setUserNameRetweet(String userNameRetweet) {
        this.userNameRetweet = userNameRetweet;
    }

    public LocalDateTime getCreatedAtRetweet() {
        return createdAtRetweet;
    }

    public void setCreatedAtRetweet(LocalDateTime createdAtRetweet) {
        this.createdAtRetweet = createdAtRetweet;
    }
}

