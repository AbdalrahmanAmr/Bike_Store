package devs.astraweb.motoaccessoriesapi.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class CheckoutRequest {

    @NotBlank(message = "رقم التليفون مطلوب")
    @Pattern(regexp = "^01[0125][0-9]{8}$", message = "رقم التليفون غير صحيح، يجب أن يبدأ بـ 01 ويتكون من 11 رقم")
    private String phone;

    @NotBlank(message = "العنوان مطلوب")
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