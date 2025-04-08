package com.timeAuction.timeProduct.response.product;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ProductResponse {

    private Long id;

    private Long sellerId;

    // 판매자 닉네임
    private String sellerNickName;

    // 생성일자
    private LocalDateTime createdAt;

    // 상품 제목
    private String productTitle;

    // 상품 설명
    private String productDescription;

    // 상품 가격
    private Integer productPrice;

    private String productSaleStatus;

    private String thumbNailUrl;

    @Builder
    public ProductResponse(Long id, Long sellerId, String sellerNickName, LocalDateTime createdAt, String productTitle, String productDescription, Integer productPrice, String productSaleStatus, String thumbNailUrl) {
        this.id = id;
        this.sellerId = sellerId;
        this.sellerNickName = sellerNickName;
        this.createdAt = createdAt;
        this.productTitle = productTitle;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productSaleStatus = productSaleStatus;
        this.thumbNailUrl = thumbNailUrl;
    }
}
