package devs.astraweb.motoaccessoriesapi.Dto;

import devs.astraweb.motoaccessoriesapi.model.Order;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class CheckoutRequest {

    @NotBlank(message = "رقم التليفون مطلوب")
    @Pattern(regexp = "^01[0125][0-9]{8}$", message = "رقم التليفون غير صحيح، يجب أن يبدأ بـ 01 ويتكون من 11 رقم")
    private String phone;

    @NotBlank(message = "العنوان مطلوب")
    private String address;

    @NotNull(message = "طريقة الدفع مطلوبة")
    private Order.PaymentMethod paymentMethod;

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

    public Order.PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(Order.PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}