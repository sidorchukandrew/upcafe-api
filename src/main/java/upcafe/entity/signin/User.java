package upcafe.entity.signin;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonFormat;

import upcafe.security.model.Role;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @Column(unique = true)
    private String email;

    private String imageUrl;
    
    @JsonFormat(pattern = "EEE MMM dd yyyy HH:mm")
    private LocalDateTime accountCreatedOn;
    private String provider;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    public static class Builder {
        private String name;
        private final String email;
        private String imageUrl;
        private LocalDateTime accountCreatedOn;
        private String provider;
        private int id;
        private Set<Role> roles;

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

        public Builder accountCreatedOn(LocalDateTime accountCreatedOn) {
            this.accountCreatedOn = accountCreatedOn;
            return this;
        }

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder provider(String provider) {
            this.provider = provider;
            return this;
        }

        public Builder roles(Set<Role> roles) {
            this.roles = roles;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

    private User(Builder builder) {
        this.name = builder.name;
        this.email = builder.email;
        this.imageUrl = builder.imageUrl;
        this.accountCreatedOn = builder.accountCreatedOn;
        this.provider = builder.provider;
        this.id = builder.id;
        this.roles = builder.roles;
    }

    public User() {
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

    public LocalDateTime getAccountCreatedOn() {
        return accountCreatedOn;
    }

    public void setAccountCreatedOn(LocalDateTime accountCreatedOn) {
        this.accountCreatedOn = accountCreatedOn;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", accountCreatedOn=" + accountCreatedOn +
                ", provider='" + provider + '\'' +
                ", roles=" + roles +
                '}';
    }
}