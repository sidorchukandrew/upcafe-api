package upcafe.dto.users;

import java.time.LocalDateTime;

public class UserDTO {
    
    private String firstName;
    private String lastName;
    private String email;
    private String photoUrl;
    private boolean staff;
    private boolean admin;

    public static class Builder {
        private String firstName;
        private String lastName;
        private final String email;
        private String photoUrl;
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


        public Builder isStaff(boolean staff) {
            this.staff = staff;
            return this;
        }

        public Builder isAdmin(boolean admin) {
            this.admin = admin;
            return this;
        }

        public UserDTO build() {
            return new UserDTO(this);
        }
    }

    public UserDTO(Builder builder) {
		this.firstName = builder.firstName;
		this.lastName = builder.lastName;
		this.email = builder.email;
		this.photoUrl = builder.photoUrl;
        this.staff = builder.staff;
        this.admin = builder.admin;
	}

    public UserDTO() { }

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
        return "{" +
            ", firstName='" + firstName + "'" +
            ", lastName='" + lastName + "'" +
            ", email='" + email + "'" +
            ", photoUrl='" + photoUrl + "'" +
            ", staff='" + staff + "'" +
            ", admin='" + admin + "'" +
            "}";
    }
    
}
