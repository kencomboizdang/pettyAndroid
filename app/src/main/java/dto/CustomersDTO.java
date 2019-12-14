package dto;

import java.io.Serializable;
import java.util.Date;

public class CustomersDTO implements Serializable {

    private String id;
    private String name;
    private long dayOfBirth;
    private String email;
    private boolean gender;
    private boolean active;
    private String accountsId;

    public CustomersDTO() {
    }

    public CustomersDTO(String name, long dayOfBirth, String email, boolean gender, boolean active, String accountsId) {
        this.name = name;
        this.dayOfBirth = dayOfBirth;
        this.email = email;
        this.gender = gender;
        this.active = active;
        this.accountsId = accountsId;
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

    public long getDayOfBirth() {
        return dayOfBirth;
    }

    public void setDayOfBirth(long dayOfBirth) {
        this.dayOfBirth = dayOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getAccountsId() {
        return accountsId;
    }

    public void setAccountsId(String accountsId) {
        this.accountsId = accountsId;
    }
}
