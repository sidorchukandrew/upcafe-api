package upcafe.dto.order;

public class OrderModifierDTO {

    private String id;
    private String name;
    private double price;

    public static class Builder {
        private final String id;
        private String name;
        private double price;

        public Builder(String id) {
            this.id = id;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder price(double price) {
            this.price = price;
            return this;
        }

        public OrderModifierDTO build() {
            return new OrderModifierDTO(this);
        }
    }

    public OrderModifierDTO() {
    }

    private OrderModifierDTO(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.price = builder.price;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
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
                ", name='" + name + "'" +
                ", price='" + price + "'" +
                "}";
    }
}