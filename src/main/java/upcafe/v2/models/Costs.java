package upcafe.v2.models;

public class Costs {

    private double totalAmount;
    private double taxAmount;
    private double orderAmount;

    private Costs(Builder builder) {
        this.totalAmount = builder.totalAmount;
        this.taxAmount = builder.taxAmount;
        this.orderAmount = this.totalAmount - this.taxAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public double getOrderAmount() {
        return orderAmount;
    }


    public static class Builder {
        private double totalAmount;
        private double taxAmount;

        public Builder totalAmount(double totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public Builder taxAmount(double taxAmount) {
            this.taxAmount = taxAmount;
            return this;
        }

        public Costs build() {
            return new Costs(this);
        }
    }

    @Override
    public String toString() {
        return "Costs{" +
                "totalAmount=" + totalAmount +
                ", taxAmount=" + taxAmount +
                ", orderAmount=" + orderAmount +
                '}';
    }
}
