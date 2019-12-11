package dto;

import java.io.Serializable;
import java.util.Date;

public class HistoriesDTO implements Serializable {

    private String id;
    private Date date;
    private String customersId;
    private String productsId;

    public HistoriesDTO() {
    }

    public HistoriesDTO(String id, Date date, String customersId, String productsId) {
        this.id = id;
        this.date = date;
        this.customersId = customersId;
        this.productsId = productsId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCustomersId() {
        return customersId;
    }

    public void setCustomersId(String customersId) {
        this.customersId = customersId;
    }

    public String getProductsId() {
        return productsId;
    }

    public void setProductsId(String productsId) {
        this.productsId = productsId;
    }
}
