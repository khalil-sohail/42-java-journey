package fr._42.models;

import java.util.Objects;

public class Product {
    private Long    identifier;
    private Double  price;
    private String  name;

    public Product(Long identifier, Double price, String name) {
        this.identifier = identifier;
        this.price = price;
        this.name = name;
    }

    public Long  getIdentifier() { return identifier; }
    public Double   getPrice()      { return price; }
    public String   getName()       { return name; }

    public void setIdentifier(Long identifier)   { this.identifier = identifier; }
    public void setPrice(Double price)              { this.price = price; }
    public void setName(String name)                { this.name = name; }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Product product = (Product) obj;

        return Objects.equals(identifier, product.identifier)
                && Objects.equals(name, product.name)
                && Objects.equals(price, product.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, name, price);
    }

    @Override
    public String toString() {
        return "Product{" +
                "identifier=" + identifier +
                ", price=" + price +
                ", name='" + name + '\'' +
                '}';
    }
}

