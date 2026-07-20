package devs.astraweb.motoaccessoriesapi.service;

import devs.astraweb.motoaccessoriesapi.Dto.CategoryRequest;
import devs.astraweb.motoaccessoriesapi.Dto.CategoryResponse;
import devs.astraweb.motoaccessoriesapi.model.Category;
import devs.astraweb.motoaccessoriesapi.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryResponse> getAll() {
        return categoryRepository.findAll().stream()
                .map(CategoryResponse::new)
                .collect(Collectors.toList());
    }

    public CategoryResponse getById(Long id) {
        Category category = findEntityById(id);
        return new CategoryResponse(category);
    }

    public CategoryResponse create(CategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("A category with this name already exists");
        }

        Category category = new Category(request.getName(), request.getSlug(), request.getIcon());
        Category saved = categoryRepository.save(category);
        return new CategoryResponse(saved);
    }

    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = findEntityById(id);
        category.setName(request.getName());
        category.setSlug(request.getSlug());
        category.setIcon(request.getIcon());
        Category saved = categoryRepository.save(category);
        return new CategoryResponse(saved);
    }

    public void delete(Long id) {
        Category category = findEntityById(id);
        categoryRepository.delete(category);
    }

    private Category findEntityById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + id));
    }
}
