package unrn.dto;

public class CreateTweetRequest {
    private int userId;
    private String texto;

    public CreateTweetRequest() {
    }

    public CreateTweetRequest(int userId, String texto) {
        this.userId = userId;
        this.texto = texto;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}

