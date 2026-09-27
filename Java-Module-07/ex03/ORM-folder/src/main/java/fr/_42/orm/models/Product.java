package fr._42.orm.models;

import fr._42.orm.annotations.OrmColumn;
import fr._42.orm.annotations.OrmColumnId;
import fr._42.orm.annotations.OrmEntity;

@OrmEntity(table = "product")
public class Product {
    @OrmColumnId
    private Long id;

    @OrmColumn(name = "product_name", length = 50)
    private String productName;

    @OrmColumn(name = "price")
    private Double price;

    public Product() { }
    public Product(String productName, Double price) {
        this.productName = productName;
        this.price = price;
    }

    public Long     getId()                                 { return id; }
    public void     setId(Long id)                          { this.id = id; }
    public String   getProduct_name()                       { return productName; }
    public void     setProduct_name(String productName)    { this.productName = productName; }
    public Double   getPrice()                              { return price; }
    public void     setPrice(Double price)                  { this.price = price; }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                '}';
    }
}
