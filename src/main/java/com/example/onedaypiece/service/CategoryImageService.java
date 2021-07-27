package com.example.onedaypiece.service;

import com.example.onedaypiece.web.domain.categoryImage.CategoryImage;
import com.example.onedaypiece.web.domain.categoryImage.CategoryImageRepository;
import com.example.onedaypiece.web.domain.challenge.CategoryName;
import com.example.onedaypiece.web.dto.request.categoryImage.CategoryImageRequestDto;
import com.example.onedaypiece.web.dto.response.category.CategoryImageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryImageService {

    private final CategoryImageRepository categoryImageRepository;

    public void postCategoryImage(CategoryImageRequestDto requestDto) {
        if (!categoryImageRepository.existsByCategoryImageUrl(requestDto.getCategoryImageUrl())) {
            categoryImageRepository.save(new CategoryImage(requestDto));
        }
    }

    public CategoryImageResponseDto getCategoryImage(CategoryName categoryName) {
        CategoryImageResponseDto categoryImageResponseDto = new CategoryImageResponseDto();
        categoryImageRepository.findAllByCategoryName(categoryName).forEach(
                value -> categoryImageResponseDto.addCategoryImageUrl(value.getCategoryImageUrl()));
        return categoryImageResponseDto;
    }
}
