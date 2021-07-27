package com.example.onedaypiece.web.domain.categoryImage;

import com.example.onedaypiece.web.domain.challenge.CategoryName;
import com.example.onedaypiece.web.domain.common.Timestamped;
import com.example.onedaypiece.web.dto.request.categoryImage.CategoryImageRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class CategoryImage extends Timestamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long categoryImageId;

    @Column
    private CategoryName categoryName;

    @Column
    private String categoryImageUrl;

    public CategoryImage(CategoryImageRequestDto requestDto) {
        this.categoryName = requestDto.getCategoryName();
        this.categoryImageUrl = requestDto.getCategoryImageUrl();
    }
}
