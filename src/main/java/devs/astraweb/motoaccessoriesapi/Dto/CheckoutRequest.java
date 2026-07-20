package devs.astraweb.motoaccessoriesapi.Dto;

import jakarta.validation.constraints.NotBlank;

public class CheckoutRequest {

    @NotBlank
    private String phone;

    @NotBlank
    private String address;

    public CheckoutRequest() {
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}