package upcafe.dto.users;

import java.time.LocalDateTime;

public class CustomerDTO {
    
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String photoUrl;
    private LocalDateTime accountCreatedOn;

    public static class Builder {
        private final int id;
        private String firstName;
        private String lastName;
        private String email;
        private String photoUrl;
        private LocalDateTime accountCreatedOn;

        public Builder(int id) {
            this.id = id;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
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

        public CustomerDTO build() {
            return new CustomerDTO(this);
        }
    }

    public CustomerDTO(Builder builder) {
		this.id = builder.id;
		this.firstName = builder.firstName;
		this.lastName = builder.lastName;
		this.email = builder.email;
		this.photoUrl = builder.photoUrl;
		this.accountCreatedOn = builder.accountCreatedOn;
	}

    public CustomerDTO() { }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    @Override
    public String toString() {
        return "Customer [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
                + ", photoUrl=" + photoUrl + "]";
    }

}
