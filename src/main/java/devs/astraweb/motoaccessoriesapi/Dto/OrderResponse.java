package devs.astraweb.motoaccessoriesapi.Dto;

import devs.astraweb.motoaccessoriesapi.model.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {

    private Long id;
    private BigDecimal totalAmount;
    private String status;
    private LocalDateTime createdAt;
    private String userName;
    private String userEmail;
    private String phone;
    private String address;
    private List<OrderItemResponse> items;

    public OrderResponse() {
    }

    public OrderResponse(Order order) {
        this.id = order.getId();
        this.totalAmount = order.getTotalAmount();
        this.status = order.getStatus().name();
        this.createdAt = order.getCreatedAt();
        this.userName = order.getUser().getName();
        this.userEmail = order.getUser().getEmail();
        this.phone = order.getPhone();
        this.address = order.getAddress();
        this.items = order.getItems().stream()
                .map(OrderItemResponse::new)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
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

    public List<OrderItemResponse> getItems() {
        return items;
    }

    public void setItems(List<OrderItemResponse> items) {
        this.items = items;
    }
}