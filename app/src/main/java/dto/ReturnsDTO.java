package dto;

import java.io.Serializable;

public class ReturnsDTO implements Serializable {
    private String id;
    private long date;
    private String title;
    private String reason;
    private String img;
    private String confirm;
    private String orderDetailId;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    public String getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(String orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public ReturnsDTO() {
    }

    public ReturnsDTO(String id, long date, String title, String reason, String img, String confirm, String orderDetailId) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.reason = reason;
        this.img = img;
        this.confirm = confirm;
        this.orderDetailId = orderDetailId;
    }
}
