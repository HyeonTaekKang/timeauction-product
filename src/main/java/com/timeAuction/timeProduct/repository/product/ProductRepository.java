package com.timeAuction.timeProduct.repository.product;


import com.timeAuction.timeProduct.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> , ProductRepositoryCustom {
    // 최신순으로 정렬하여 최대 10개 결과를 가져오는 메서드
    List<Product> findTop10ByTitleContainingOrderByCreatedAtDesc(String keyword);
}