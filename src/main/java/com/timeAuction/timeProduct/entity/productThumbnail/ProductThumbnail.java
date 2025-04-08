package com.timeAuction.timeProduct.entity.productThumbnail;

import com.timeAuction.timeProduct.entity.product.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductThumbnail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "productThumbnail", fetch = FetchType.LAZY)
    private Product product;

    private String imageTitle;

    private Long imageSize; // 이미지 사이즈

    private String imageExtension; // 이미지 확장자

    private String imageUrl;

    @Builder
    public ProductThumbnail(Long id, Product product, String imageTitle, Long imageSize, String imageExtension, String imageUrl) {
        this.id = id;
        this.product = product;
        this.imageTitle = imageTitle;
        this.imageSize = imageSize;
        this.imageExtension = imageExtension;
        this.imageUrl = imageUrl;
    }

    public static String extractExt(String  originalImageName) {
        // test.png ->
        int pos =  originalImageName.lastIndexOf(".");
        return  originalImageName.substring(pos + 1);
    }
}
