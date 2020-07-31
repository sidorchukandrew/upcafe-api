package upcafe.v2.models;

public class Customer {

    private String name;
    private String email;
    private String squareCustomerId;
    private String phoneNumber;
    private String userId;

    public Customer() { }

    public static class Builder {
        private String name;
        private String email;
        private String squareCustomerId;
        private String phoneNumber;
        private String userId;

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder squareCustomerId(String squareCustomerId) {
            this.squareCustomerId = squareCustomerId;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Customer build() {
            return new Customer(this);
        }
    }

    private Customer(Builder builder) {
        this.email = builder.email;
        this.name = builder.name;
        this.userId = builder.userId;
        this.squareCustomerId = builder.squareCustomerId;
        this.phoneNumber = builder.phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getSquareCustomerId() {
        return squareCustomerId;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", squareCustomerId='" + squareCustomerId + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
