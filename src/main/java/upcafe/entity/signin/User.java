package upcafe.entity.signin;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "app_user")
public class User {
    private String firstName;
    private String lastName;

    @Id
    @Column(length = 40, name = "id")
    private String email;
    private String photoUrl;
    private LocalDateTime accountCreatedOn;

    @Column(name = "is_admin")
    private boolean staff;

    @Column(name = "is_staff")
    private boolean admin;

    public static class Builder {
        private String firstName;
        private String lastName;
        private final String email;
        private String photoUrl;
        private LocalDateTime accountCreatedOn;
        private boolean staff;
        private boolean admin;

        public Builder(String email) {
            this.email = email;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder photoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
            return this;
        }

        public Builder accountCreatedOn(LocalDateTime accountCreatedOn) {
            this.accountCreatedOn = accountCreatedOn;
            return this;
        }

        public Builder isStaff(boolean staff) {
            this.staff = staff;
            return this;
        }

        public Builder isAdmin(boolean admin) {
            this.admin = admin;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

    public User(Builder builder) {
		this.firstName = builder.firstName;
		this.lastName = builder.lastName;
		this.email = builder.email;
		this.photoUrl = builder.photoUrl;
        this.accountCreatedOn = builder.accountCreatedOn;
        this.staff = builder.staff;
        this.admin = builder.admin;
	}

    public User() { }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setDateAccountCreated(LocalDateTime accountCreatedOn) {
        this.accountCreatedOn = accountCreatedOn;
    }

    public LocalDateTime getAccountCreatedOn() {
        return accountCreatedOn;
    }

    public boolean isStaff() {
        return this.staff;
    }

    public boolean getStaff() {
        return this.staff;
    }

    public void setStaff(boolean staff) {
        this.staff = staff;
    }

    public boolean isAdmin() {
        return this.admin;
    }

    public boolean getAdmin() {
        return this.admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    @Override
    public String toString() {
        return "{" +  "firstName='" + firstName + "'" + ", lastName='" + lastName + "'"
                + ", email='" + email + "'" + ", photoUrl='" + photoUrl + "'" + ", accountCreatedOn='"
                + accountCreatedOn + "'" + ", staff='" + staff + "'" + ", admin='" + admin + "'" + "}";
    }
}