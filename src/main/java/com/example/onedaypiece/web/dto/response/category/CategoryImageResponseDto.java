package com.example.onedaypiece.web.dto.response.category;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class CategoryImageResponseDto {

    private final List<String> categoryImageUrl = new ArrayList<>();

    public void addCategoryImageUrl(String categoryImageUrl) {
        this.categoryImageUrl.add(categoryImageUrl);
    }
}
