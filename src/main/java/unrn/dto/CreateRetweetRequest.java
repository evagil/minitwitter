package unrn.dto;

public class CreateRetweetRequest {
    private int userId;
    private int tweetId;
    private String comentario;

    public CreateRetweetRequest() {
    }

    public CreateRetweetRequest(int userId, int tweetId, String comentario) {
        this.userId = userId;
        this.tweetId = tweetId;
        this.comentario = comentario;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTweetId() {
        return tweetId;
    }

    public void setTweetId(int tweetId) {
        this.tweetId = tweetId;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}

