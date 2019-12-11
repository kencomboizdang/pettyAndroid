package dto;

import java.io.Serializable;
import java.util.Date;

public class StoresDTO implements Serializable {
    private String id;
    private String name;
    private long date;
    private String description;
    private String logoImg;
    private String province;
    private String district;
    private String ward;
    private String detail;
    private String phone;
    private String email;
    private boolean active;
    private String postalCode;
    private float positionX;
    private float positionY;
    private String accountId;

    public StoresDTO(String id, String name, long date, String description, String logoImg, String province, String district, String ward, String detail, String phone, String email, boolean active, String postalCode, float positionX, float positionY, String accountId) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.description = description;
        this.logoImg = logoImg;
        this.province = province;
        this.district = district;
        this.ward = ward;
        this.detail = detail;
        this.phone = phone;
        this.email = email;
        this.active = active;
        this.postalCode = postalCode;
        this.positionX = positionX;
        this.positionY = positionY;
        this.accountId = accountId;
    }

    public StoresDTO() {

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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogoImg() {
        return logoImg;
    }

    public void setLogoImg(String logoImg) {
        this.logoImg = logoImg;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public float getPositionX() {
        return positionX;
    }

    public void setPositionX(float positionX) {
        this.positionX = positionX;
    }

    public float getPositionY() {
        return positionY;
    }

    public void setPositionY(float positionY) {
        this.positionY = positionY;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
