package devs.astraweb.motoaccessoriesapi.repository;

import devs.astraweb.motoaccessoriesapi.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // categoryId and search are both optional (nullable) - pass null to skip that filter.
    // JOIN FETCH loads the Category eagerly in the same query, avoiding a LazyInitializationException
    // if the session closes before ProductResponse touches product.getCategory() during serialization.
    @Query("""
            SELECT DISTINCT p FROM Product p
            JOIN FETCH p.category
            LEFT JOIN FETCH p.imageUrls
            WHERE (:categoryId IS NULL OR p.category.id = :categoryId)
            AND (:search IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')))
            """)
    Page<Product> search(@Param("categoryId") Long categoryId,
                         @Param("search") String search,
                         Pageable pageable);
}