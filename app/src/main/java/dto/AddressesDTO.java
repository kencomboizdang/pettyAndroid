package dto;

import java.io.Serializable;

public class AddressesDTO implements Serializable {
    private String id;
    private String name;
    private String province;
    private String district;
    private String ward;
    private String detail;
    private String phone;
    private String customerId;

    public AddressesDTO() {
    }

    public AddressesDTO(String id, String name, String province, String district, String ward, String detail, String phone, String customerId) {
        this.id = id;
        this.name = name;
        this.province = province;
        this.district = district;
        this.ward = ward;
        this.detail = detail;
        this.phone = phone;
        this.customerId = customerId;
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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}

