public class Product {
    private String name;
    private double quantity;
    private String country;
    private String brand;

    public String getName() {
        return this.name;
    }

    public void setName(String productName) {
        this.name = productName;
    }

    public double getQuantity() {
        return this.quantity;
    }

    public void setQuantity(double quantityAmount) {
        this.quantity = quantityAmount;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String countryOfOrigin) {
        this.country = countryOfOrigin;
    }

    public String getBrand() {
        return this.brand;
    }

    public void setBrand(String brandName) {
        this.brand = brandName;
    }

    @Override
    public String toString() {
        return "name=" + name + ", quantity=" + quantity + ", country=" + country + ", brand=" + brand;
    }
}
