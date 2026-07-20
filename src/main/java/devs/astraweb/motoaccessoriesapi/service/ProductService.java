package devs.astraweb.motoaccessoriesapi.service;

import devs.astraweb.motoaccessoriesapi.Dto.ProductRequest;
import devs.astraweb.motoaccessoriesapi.Dto.ProductResponse;
import devs.astraweb.motoaccessoriesapi.model.Category;
import devs.astraweb.motoaccessoriesapi.model.Product;
import devs.astraweb.motoaccessoriesapi.repository.CategoryRepository;
import devs.astraweb.motoaccessoriesapi.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final FileStorageService fileStorageService;

    public ProductService(ProductRepository productRepository,
                           CategoryRepository categoryRepository,
                           FileStorageService fileStorageService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.fileStorageService = fileStorageService;
    }

    public Page<ProductResponse> search(Long categoryId, String search, Pageable pageable) {
        return productRepository.search(categoryId, search, pageable)
                .map(ProductResponse::new);
    }

    public ProductResponse getById(Long id) {
        return new ProductResponse(findEntityById(id));
    }

    public ProductResponse create(ProductRequest request, MultipartFile image) {
        Category category = findCategoryById(request.getCategoryId());

        Product product = new Product();
        applyRequest(product, request, category);

        if (image != null && !image.isEmpty()) {
            product.setImageUrl(fileStorageService.store(image));
        }

        Product saved = productRepository.save(product);
        return new ProductResponse(saved);
    }

    public ProductResponse update(Long id, ProductRequest request, MultipartFile image) {
        Product product = findEntityById(id);
        Category category = findCategoryById(request.getCategoryId());
        applyRequest(product, request, category);

        // Only replace the image if a new one was actually uploaded - otherwise keep the existing one
        if (image != null && !image.isEmpty()) {
            String oldImageUrl = product.getImageUrl();
            product.setImageUrl(fileStorageService.store(image));
            fileStorageService.delete(oldImageUrl);
        }

        Product saved = productRepository.save(product);
        return new ProductResponse(saved);
    }

    public void delete(Long id) {
        Product product = findEntityById(id);
        fileStorageService.delete(product.getImageUrl());
        productRepository.delete(product);
    }

    private void applyRequest(Product product, ProductRequest request, Category category) {
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setBrand(request.getBrand());
        product.setCategory(category);
        product.setStockQuantity(request.getStockQuantity());
    }

    private Product findEntityById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));
    }

    private Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + categoryId));
    }
}

