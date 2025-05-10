package nl.helvar.servicetickets.auth;

public class AuthDto {
    private String username;
    private String password;
    private Boolean tokenPersist;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isTokenPersist() { return tokenPersist; }
    public void setTokenPersist(boolean tokenPersist) { this.tokenPersist = tokenPersist; }
}
