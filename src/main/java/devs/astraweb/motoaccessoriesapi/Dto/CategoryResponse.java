package devs.astraweb.motoaccessoriesapi.Dto;

import devs.astraweb.motoaccessoriesapi.model.Category;

public class CategoryResponse {

    private Long id;
    private String name;
    private String slug;
    private String icon;

    public CategoryResponse() {
    }

    public CategoryResponse(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.slug = category.getSlug();
        this.icon = category.getIcon();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
