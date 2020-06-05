package upcafe.security.model;


import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
public class Role implements GrantedAuthority {

    @Id
    private int id;

    private String authority;

    public Role() {
    }

    public static class Builder {
        private int id;
        private final String authority;

        public Builder(String authority) {
            this.authority = authority;
        }

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Role build() {
            return new Role(this);
        }
    }

    private Role(Builder builder) {
        this.id = builder.id;
        this.authority = builder.authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public int getId() {
        return id;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", authority='" + authority + '\'' +
                '}';
    }
}
