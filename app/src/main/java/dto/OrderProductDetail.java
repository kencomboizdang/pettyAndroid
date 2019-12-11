package dto;

import java.io.Serializable;
import java.util.Date;

public class OrderProductDetail implements Serializable {
    private String id;
    private String date;
    private int quantity;
    private float price;
    private String orderStatus;
    private String orderId;
    private String productId;

    public OrderProductDetail() {
    }

    public OrderProductDetail(String id, String date, int quantity, float price, String orderStatus, String orderId, String productId) {
        this.id = id;
        this.date = date;
        this.quantity = quantity;
        this.price = price;
        this.orderStatus = orderStatus;
        this.orderId = orderId;
        this.productId = productId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
