package com.example.onedaypiece.web.controller;

import com.example.onedaypiece.service.CategoryImageService;
import com.example.onedaypiece.web.domain.challenge.CategoryName;
import com.example.onedaypiece.web.dto.request.categoryImage.CategoryImageRequestDto;
import com.example.onedaypiece.web.dto.response.category.CategoryImageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class CategoryImageController {

    private final CategoryImageService categoryImageService;

    /**
     * 1. 카테고리 이미지 등록
     */
    @PostMapping("/api/category-image")
    public void postCategoryImage(@RequestBody CategoryImageRequestDto requestDto) {
        categoryImageService.postCategoryImage(requestDto);
    }

    /**
     * 2. 카테고리 이미지 조회
     */
    @GetMapping("/api/category-image/{categoryName}")
    public CategoryImageResponseDto getCategoryImage(@PathVariable CategoryName categoryName) {
        return categoryImageService.getCategoryImage(categoryName);
    }

    /**
     * 3. 카테고리 이미지 조회
     */
    @DeleteMapping("/api/category-image/{imgUrl}")
    public void deleteCategoryImage(@PathVariable String imgUrl) {
        categoryImageService.deleteCategoryImage(imgUrl);
    }
}
