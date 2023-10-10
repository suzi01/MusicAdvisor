package advisor.Model;

public class UserDTO {

    private String accessCode;

    private String token;

    public UserDTO() {
    }

    public String getAccessCode() {
        return accessCode;
    }

    public String getToken() {
        return token;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
