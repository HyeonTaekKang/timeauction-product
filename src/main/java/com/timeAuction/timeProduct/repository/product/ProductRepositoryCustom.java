package com.timeAuction.timeProduct.repository.product;



import com.timeAuction.timeProduct.response.product.ProductResponse;

import java.util.List;

public interface ProductRepositoryCustom {

    // 등록한 상품 1개 가져오기
    ProductResponse getProduct(Long productId);

    // 등록한 상품 최신순으로 20개씩 가져오기
    List<ProductResponse> getRecentProductList(Long lastId, Integer limit);

    // 내 상품 모두 가져오기
    List<ProductResponse> getMyProductList (Long lastId, Integer limit ,Long userId );
}
