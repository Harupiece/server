package com.example.onedaypiece.web.domain.categoryImage;

import com.example.onedaypiece.web.domain.challenge.CategoryName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryImageRepository extends JpaRepository<CategoryImage, Long> {
    Boolean existsByCategoryImageUrl(String categoryImageUrl);
    List<CategoryImage> findAllByCategoryName(CategoryName categoryName);
    void deleteByCategoryImageUrl(String imgUrl);
}
