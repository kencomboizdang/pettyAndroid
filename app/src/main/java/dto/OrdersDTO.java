package dto;

import java.io.Serializable;

public class OrdersDTO implements Serializable {
    private String id;
    private long date;
    private float total;
    private String orderStatus;
    private String customerId;
    private String addressId;

    public OrdersDTO() {
    }

    public OrdersDTO(String id, long date, float total, String orderStatus, String customerId, String addressId) {
        this.id = id;
        this.date = date;
        this.total = total;
        this.orderStatus = orderStatus;
        this.customerId = customerId;
        this.addressId = addressId;
    }

    public OrdersDTO(long date, float total, String orderStatus, String customerId, String addressId) {
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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
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
