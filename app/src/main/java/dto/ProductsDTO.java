package dto;

import java.io.Serializable;
import java.util.Date;

public class ProductsDTO implements Serializable {
    private String id;
    private String name;
    private String description;
    private float price;
    private int quantity;
    private String size;
    private String img;
    private String origin;
    private String brand;
    private float netWeight;
    private String status;
    private long startDate;
    private long expiration;
    private String categoriesId;
    private String storeId;

    public ProductsDTO() {
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public ProductsDTO(String id, String name, String description, float price, int quantity, String size, String img, String origin, String brand, float netWeight, String status, long startDate, long expiration, String categoriesId, String storeId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.size = size;
        this.img = img;
        this.origin = origin;
        this.brand = brand;
        this.netWeight = netWeight;
        this.status = status;
        this.startDate = startDate;
        this.expiration = expiration;
        this.categoriesId = categoriesId;
        this.storeId = storeId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public float getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(float netWeight) {
        this.netWeight = netWeight;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    public String getCategoriesId() {
        return categoriesId;
    }

    public void setCategoriesId(String categoriesId) {
        this.categoriesId = categoriesId;
    }
}

