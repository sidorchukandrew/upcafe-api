package upcafe.model.catalog;

public class VariationData {

	private String name;
	private double variationPrice;
	private boolean stocked;
	private String variationId;
	private String variationImageUrl;

	public String getName() {
		return name;
	}

	public void setName(String nameOfVariation) {
		this.name = nameOfVariation;
	}

	public double getVariationPrice() {
		return variationPrice;
	}

	public void setVariationPrice(double variationPrice) {
		this.variationPrice = variationPrice;
	}

	public boolean isStocked() {
		return stocked;
	}

	public void setStocked(boolean stocked) {
		this.stocked = stocked;
	}

	public String getVariationId() {
		return variationId;
	}

	public void setVariationId(String variationId) {
		this.variationId = variationId;
	}

	public String getVariationImageUrl() {
		return variationImageUrl;
	}

	public void setVariationImageUrl(String variationImageUrl) {
		this.variationImageUrl = variationImageUrl;
	}

	@Override
	public String toString() {
		return "\n\t\tVariationData [name=" + name + ", variationPrice=" + variationPrice + ", stocked="
				+ stocked + ", variationId=" + variationId + ", variationImageUrl=" + variationImageUrl + "]";
	}
}
