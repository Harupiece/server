package com.example.onedaypiece.web.dto.request.categoryImage;

import com.example.onedaypiece.web.domain.challenge.CategoryName;
import lombok.Getter;

@Getter
public class CategoryImageRequestDto {
    String categoryImageUrl;
    CategoryName categoryName;
}
