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

    @OrmColumn (name = "brand_name", length = 50)
    private String brandName;

    @OrmColumn(name = "price")
    private Double price;

    public Product() { }
    public Product(String productName, String brandName, Double price) {
        this.productName = productName;
        this.brandName = brandName;
        this.price = price;
    }

    public Long     getId()                                 { return id; }
    public void     setId(Long id)                          { this.id = id; }
    public String   getProductName()                        { return productName; }
    public void     setProductName(String productName)      { this.productName = productName; }
    public String   getBrandName()                          { return brandName; }
    public void     setBrandName(String brandName)          { this.brandName = brandName; }
    public Double   getPrice()                              { return price; }
    public void     setPrice(Double price)                  { this.price = price; }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", brandName='" + brandName + '\'' +
                ", price=" + price +
                '}';
    }
}
