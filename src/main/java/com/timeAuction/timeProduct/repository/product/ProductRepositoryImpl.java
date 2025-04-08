package com.timeAuction.timeProduct.repository.product;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import com.timeAuction.timeProduct.response.product.ProductResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.timeAuction.timeProduct.entity.product.QProduct.product;
import static com.timeAuction.timeProduct.entity.productThumbnail.QProductThumbnail.productThumbnail;
import static com.timeAuction.timeProduct.entity.user.QUser.user;


@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    // 가져올 product 갯수
    private static final Integer PAGE_SIZE = 4;

    // product id로 상품 1개 가져오기
    @Override
    public ProductResponse getProduct(Long productId) {
        return jpaQueryFactory
                .select(Projections.constructor(ProductResponse.class,
                        product.id,
                        product.sellerId,
                        user.nickname,
                        product.createdAt,
                        product.title,
                        product.description,
                        product.price,
                        product.saleStatus.stringValue(),
                        productThumbnail.imageUrl
                ))
                .from(product)
                .where(product.id.eq(productId))
                .innerJoin(user).on(product.sellerId.eq(user.id))
                .innerJoin(product.productThumbnail , productThumbnail)
                .fetchOne();
    }

    // 내 상품 모두 가져오기
    @Override
    public List<ProductResponse> getMyProductList (Long lastId, Integer limit ,Long userId ){
        Long realLastId = (lastId == null) ? Long.MAX_VALUE : lastId;
        int realLimit = (limit == null) ? PAGE_SIZE : limit;

        return jpaQueryFactory
                .select(Projections.constructor(ProductResponse.class,
                        product.id,
                        product.sellerId,
                        user.nickname,
                        product.createdAt,
                        product.title,
                        product.description,
                        product.price,
                        product.saleStatus.stringValue(),
                        productThumbnail.imageUrl
                ))
                .from(product)
                .innerJoin(user).on(product.sellerId.eq(user.id))
                .innerJoin(product.productThumbnail , productThumbnail)
                .where((product.id.lt(realLastId)).and(product.sellerId.eq(userId)))
                .orderBy(product.id.desc())
                .limit(realLimit)
                .fetch();
    }

    // 등록한 경매 최신순으로 N개씩 가져오기
    @Override
    public List<ProductResponse> getRecentProductList(Long lastId, Integer limit) {

        Long realLastId = (lastId == null) ? Long.MAX_VALUE : lastId;
        int realLimit = (limit == null) ? PAGE_SIZE : limit;

        return jpaQueryFactory
                .select(Projections.constructor(ProductResponse.class,
                        product.id,
                        product.sellerId,
                        user.nickname,
                        product.createdAt,
                        product.title,
                        product.description,
                        product.price,
                        product.saleStatus.stringValue(),
                        productThumbnail.imageUrl
                ))
                .from(product)
                .innerJoin(user).on(product.sellerId.eq(user.id))
                .innerJoin(product.productThumbnail , productThumbnail)
                .where(product.id.lt(realLastId))
                .orderBy(product.id.desc())
                .limit(realLimit)
                .fetch();
    }
}
