package unrn.dto;

public class CreateRetweetRequest {
    private int userId;
    private String comentario; // Opcional: comentario del retweet

    public CreateRetweetRequest() {
    }

    public CreateRetweetRequest(int userId, String comentario) {
        this.userId = userId;
        this.comentario = comentario;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}

