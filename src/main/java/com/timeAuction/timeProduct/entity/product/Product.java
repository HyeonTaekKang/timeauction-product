package com.timeAuction.timeProduct.entity.product;


import com.timeAuction.timeProduct.entity.baseEntity.BaseEntity;
import com.timeAuction.timeProduct.entity.productThumbnail.ProductThumbnail;
import com.timeAuction.timeProduct.request.product.ProductCreate;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="product_id")
    private Long id;

    // 판매자 id
    @Column(nullable = false)
    private Long sellerId;

    // 상품 제목
    @Column(nullable = false)
    private String title;

    // 상품 설명
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    // 고용 가격
    private Integer price;

    // 판매 여부
    @Enumerated(EnumType.STRING)
    private SaleStatus saleStatus;

    // 상품 썸네일
    @OneToOne(fetch = FetchType.LAZY ,  cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "productThumbnail_id")
    private ProductThumbnail productThumbnail;

    @Builder
    public Product(Long id, Long sellerId, String title, String description, Integer price, SaleStatus saleStatus, ProductThumbnail productThumbnail) {
        this.id = id;
        this.sellerId = sellerId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.saleStatus = saleStatus;
        this.productThumbnail = productThumbnail;
    }

    // product(상품) 생성 메서드 ( request 받은 Create DTO를 entity로 변환하는 메서드 )
    public static Product createProduct(ProductCreate productCreate, Long sellerId , ProductThumbnail productThumbnail ) {
        Product product = new Product();

        // seller 연관관계 맵핑
        product.sellerId = sellerId;

        product.title = productCreate.getTitle();

        product.description = productCreate.getDescription();

        product.price = productCreate.getPrice();

        product.saleStatus = SaleStatus.AVAILABLE;

        product.productThumbnail = productThumbnail;

        return product;
    }

    // 판매 상태 변경 ( 판매 -> 판매완료 )
    public void setSaleStatus(SaleStatus saleStatus) {
        this.saleStatus = saleStatus;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setProductThumbnail(ProductThumbnail productThumbnail) {
        this.productThumbnail = productThumbnail;
    }
}
