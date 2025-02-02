package dto;

import java.io.Serializable;

public class OrderProductDetailsDTO implements Serializable {
    private String id;
    private long date;
    private int quantity;
    private float price;
    private String orderStatus;
    private String orderProductStoreId;
    private String productId;

    public OrderProductDetailsDTO() {
    }

    public OrderProductDetailsDTO(String id, long date, int quantity, float price, String orderStatus, String orderProductStoreId, String productId) {
        this.id = id;
        this.date = date;
        this.quantity = quantity;
        this.price = price;
        this.orderStatus = orderStatus;
        this.orderProductStoreId = orderProductStoreId;
        this.productId = productId;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }


    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderProductStoreId() {
        return orderProductStoreId;
    }

    public void setOrderProductStoreId(String orderProductStoreId) {
        this.orderProductStoreId = orderProductStoreId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
