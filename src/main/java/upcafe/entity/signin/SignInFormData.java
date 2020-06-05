package upcafe.entity.signin;

public class SignInFormData {

    private String username;
    private String password;

    public SignInFormData() {
    }

    public SignInFormData(String username, String password) {
        this.username = username;
        this.password = password;
    }

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

    @Override
    public String toString() {
        return "SignInFormData [username=" + username + ", password=" + password + "]";
    }

}
