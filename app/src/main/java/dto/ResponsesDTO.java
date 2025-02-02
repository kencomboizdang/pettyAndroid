package dto;

import java.io.Serializable;
import java.util.Date;

public class ResponsesDTO implements Serializable {
    private String id;
    private long date;
    private String title;
    private String content;
    private float rating;
    private String img;
    private String orderProductDetailId;

    public ResponsesDTO() {
    }

    public ResponsesDTO(String id, long date, String title, String content, float rating, String img, String orderProductDetailId) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.content = content;
        this.rating = rating;
        this.img = img;
        this.orderProductDetailId = orderProductDetailId;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }


    public String getOrderProductDetailId() {
        return orderProductDetailId;
    }

    public void setOrderProductDetailId(String orderProductDetailId) {
        this.orderProductDetailId = orderProductDetailId;
    }
}