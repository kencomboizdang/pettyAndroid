package dto;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class HistoriesDTO implements Comparable {

    private String id;
    private long date;
    private String customersId;
    private String productsId;

    public HistoriesDTO() {
    }

    public HistoriesDTO(String id, long date, String customersId, String productsId) {
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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
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


    @Override
    public int compareTo(Object o) {
        if (this.date - ((HistoriesDTO)o).date>0)
        return 1 ;
        return 0;
    }
}
