package devs.astraweb.motoaccessoriesapi.Dto;

import jakarta.validation.constraints.NotBlank;

public class CategoryRequest {

    @NotBlank
    private String name;

    private String slug;

    private String icon;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
