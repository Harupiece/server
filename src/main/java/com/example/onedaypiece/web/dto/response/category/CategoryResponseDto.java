package com.example.onedaypiece.web.dto.response.category;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class CategoryResponseDto {

    private List<String> categoryImageUrl;

    public void addCategoryImageUrl(String categoryImageUrl) {
        this.categoryImageUrl.add(categoryImageUrl);
    }
}
