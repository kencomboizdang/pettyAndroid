package dto;

import java.io.Serializable;

public class OrdersDTO implements Serializable {
    private String id;
    private String date;
    private float total;
    private String orderStatus;
    private String customerId;
    private String addressId;

    public OrdersDTO() {
    }

    public OrdersDTO(String id, String date, float total, String orderStatus, String customerId, String addressId) {
        this.id = id;
        this.date = date;
        this.total = total;
        this.orderStatus = orderStatus;
        this.customerId = customerId;
        this.addressId = addressId;
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

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }
}
