package devs.astraweb.motoaccessoriesapi.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateProfileRequest {

    @NotBlank
    private String name;

    // Required only when changing the password - verified against the stored hash
    private String currentPassword;

    // Optional - if blank/null, the password is left unchanged
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String newPassword;

    public UpdateProfileRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}