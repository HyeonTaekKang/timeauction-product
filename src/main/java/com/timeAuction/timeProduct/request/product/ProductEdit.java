package com.timeAuction.timeProduct.request.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductEdit {

    // 제목
    @NotBlank(message = "제목을 작성해주세요!")
    private String title;

    // 설명
    @NotNull(message = "설명을 작성해주세요!")
    private String description;

    // 가격
    @NotNull(message = "가격을 설정해주세요!")
    private Integer price;

    @Builder
    public ProductEdit(String title, String description, Integer price) {
        this.title = title;
        this.description = description;
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
