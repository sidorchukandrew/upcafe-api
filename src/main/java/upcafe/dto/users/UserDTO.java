package upcafe.dto.users;


import java.util.Set;

public class UserDTO {

    private int id;
    private String name;
    private String email;
    private String imageUrl;
    private Set<String> roles;

    public static class Builder {
        private int id;
        private String name;
        private final String email;
        private String imageUrl;
        private Set<String> roles;

        public Builder(String email) {
            this.email = email;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder roles(Set<String> roles) {
            this.roles = roles;
            return this;
        }

        public UserDTO build() {
            return new UserDTO(this);
        }
    }

    private UserDTO(Builder builder) {
        this.name = builder.name;
        this.email = builder.email;
        this.imageUrl = builder.imageUrl;
        this.id = builder.id;
        this.roles = builder.roles;
    }

    public UserDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", roles=" + roles +
                '}';
    }
}
