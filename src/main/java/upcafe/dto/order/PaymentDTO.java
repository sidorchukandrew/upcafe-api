package upcafe.dto.order;

public class PaymentDTO {

    private String id;
    private String orderId;
    private String nonce;
    private double price;

    public static class Builder {
        private String id;
        private final String orderId;
        private String nonce;
        private double price;

        public Builder(String orderId) {
            this.orderId = orderId;
        }


        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder nonce(String nonce) {
            this.nonce = nonce;
            return this;
        }

        public Builder price(double price) {
            this.price = price;
            return this;
        }

        public PaymentDTO build() {
            return new PaymentDTO(this);
        }
    }

    public PaymentDTO() {

    }

    public PaymentDTO(Builder builder) {
        this.orderId = builder.orderId;
        this.id = builder.id;
        this.nonce = builder.nonce;
        this.price = builder.price;
    }


    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return this.orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getNonce() {
        return this.nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + id + "'" +
                ", orderId='" + orderId + "'" +
                ", nonce='" + nonce + "'" +
                ", price='" + price + "'" +
                "}";
    }

}