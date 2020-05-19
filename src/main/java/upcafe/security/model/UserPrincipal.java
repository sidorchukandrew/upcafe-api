package upcafe.security.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import upcafe.entity.signin.User;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class UserPrincipal implements OAuth2User, UserDetails {

    private int id;
    private String email;
    private String password;
    private Set<Role> roles;
    private Map<String, Object> attributes;

    public static UserPrincipal create(User user) {
        return new UserPrincipal(user.getId(), user.getEmail(), user.getRoles());
    }

    public static UserPrincipal create(User user, Map<String, Object> attributes) {
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        userPrincipal.setAttributes(attributes);

        return userPrincipal;
    }

    private UserPrincipal(int id, String email, Set<Role> roles) {
        this.id = id;
        this.email = email;
        this.roles = roles;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getName() {
        return email;
    }

    public int getId() {
        return id;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return "UserPrincipal{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                ", attributes=" + attributes +
                '}';
    }
}
