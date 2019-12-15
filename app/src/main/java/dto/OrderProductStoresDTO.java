package dto;

import java.io.Serializable;

public class OrderProductStoresDTO implements Serializable {
    private String id;
    private long date;
    private float total;
    private String orderStatus;
    private String storeId;
    private String orderId;


    public OrderProductStoresDTO(String id, long date, float total, String orderStatus, String storeId, String orderId) {
        this.id = id;
        this.date = date;
        this.total = total;
        this.orderStatus = orderStatus;
        this.storeId = storeId;
        this.orderId = orderId;
    }

    public OrderProductStoresDTO() {
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

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

}