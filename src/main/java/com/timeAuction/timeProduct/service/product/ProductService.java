package com.timeAuction.timeProduct.service.product;


import com.timeAuction.timeProduct.config.imageConfig.ImageHandler;
import com.timeAuction.timeProduct.entity.product.Product;
import com.timeAuction.timeProduct.entity.product.SaleStatus;
import com.timeAuction.timeProduct.entity.productThumbnail.ProductThumbnail;
import com.timeAuction.timeProduct.entity.user.User;
import com.timeAuction.timeProduct.exception.AlreadySoldProduct;
import com.timeAuction.timeProduct.exception.NotOwnProduct;
import com.timeAuction.timeProduct.exception.ProductNotFound;
import com.timeAuction.timeProduct.exception.UserNotFound;
import com.timeAuction.timeProduct.repository.product.ProductRepository;
import com.timeAuction.timeProduct.repository.user.UserRepository;
import com.timeAuction.timeProduct.request.product.ProductCreate;
import com.timeAuction.timeProduct.request.product.ProductEdit;
import com.timeAuction.timeProduct.response.product.ProductListResponse;
import com.timeAuction.timeProduct.response.product.ProductResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ImageHandler imageHandler;

    // 상품 생성하기
    @Transactional
    public Long createProduct(ProductCreate productCreate, String email , MultipartFile productThumbnail) {

        // user 가져오기 ( 경매인 )
        User seller = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾을 수 없음."));

        // 경매 썸네일 s3 + db에 저장.
        ProductThumbnail thumbnail = imageHandler.uploadProductThumbnail(productThumbnail);

        // 상품 DTO -> 상품 Entity
        // HTML에서 이미지 URL 추출
        List<String> imageUrls = imageHandler.extractImageUrlsFromHtml(productCreate.getDescription());

        // 이미지 URL로 S3에 업로드
        List<String> uploadedUrls = imageHandler.uploadProductImagesToS3(imageUrls);

        // product descritpion의 HTML 이미지 URL을 S3 URL로 교체
        String modifiedDescription = imageHandler.replaceImageUrlsInHtml(productCreate.getDescription(), imageUrls, uploadedUrls);
        productCreate.setDescription(modifiedDescription);

        Product newProduct = Product.createProduct(productCreate, seller.getId() , thumbnail);

        // 새로운 상품 db에 저장
        productRepository.save(newProduct);

        return newProduct.getId();
    }

    // 상품 1개 가져오기 ( read 1 )
    public ProductResponse getProduct(Long productId) {

        // 경매 가져오기
        return productRepository.getProduct(productId);
    }

    // 내 경매 모두 가져오기
    public ProductListResponse<List<ProductResponse>> getMyProductList(Long lastId, Integer limit , String userEmail){

        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UserNotFound());
        List<ProductResponse> myProductList = productRepository.getMyProductList(lastId, limit, user.getId());

        return new ProductListResponse<>(myProductList);
    }

    // 등록한 상품을 최신순으로 N개씩 가져오기 ( 기본 read 20 )
    public ProductListResponse<List<ProductResponse>> getRecentProducts(Long lastId, Integer limit) {

        // 등록한 경매 20개를 최신순으로 ( auctionResponse ) 객체에 담고 , 그걸 리스트 형태로 가져옴.
        List<ProductResponse> productResponseList = productRepository.getRecentProductList(lastId, limit);

        return new ProductListResponse<>(productResponseList);
    }

    // 상품 판매 완료
    @Transactional
    public void completeSale(Long productId, String userEmail) {
        // 상품 조회
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFound());

        // 유저 조회
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UserNotFound());

        // 유저가 상품의 판매자인지 확인
        if (!product.getSellerId().equals(user.getId())) {
            throw new NotOwnProduct();
        }

        // 판매 상태가 AVAILABLE인지 확인
        if (product.getSaleStatus() != SaleStatus.AVAILABLE) {
            throw new AlreadySoldProduct();
        }

        // 판매 완료 상태로 변경
        product.setSaleStatus(SaleStatus.SOLD);
        productRepository.save(product);
    }

    // 상품 변경
    @Transactional
    public void updateProduct(String userEmail , Long productId, ProductEdit updateDto, MultipartFile newImageFile) {

        log.info("updateDto={}",updateDto);

        // 상품 조회
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFound());

        // 본인 상품인지 확인
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UserNotFound());
        if(!product.getSellerId().equals(user.getId())){
            throw new RuntimeException("본인 상품만 변경 가능합니다.");
        }

        // path가 삭제된 기존 썸네일 경로 저장
        String extractKey = null;
        if (product.getProductThumbnail() != null) {
            String existingImagePath = product.getProductThumbnail().getImageUrl(); // 기존 이미지 경로
            extractKey = imageHandler.extractKeyFromUrl(existingImagePath); // path 삭제
        }

        // 새로운 썸네일 업로드된 경우
        if (newImageFile != null && !newImageFile.isEmpty() && !extractKey.equals(newImageFile)) {
            // 기존 이미지가 있을 경우 삭제
            imageHandler.deleteProductThumbnailFromS3(extractKey); // S3에서 기존 이미지 삭제

            // 새로운 이미지 S3에 업로드
            ProductThumbnail thumbnail = imageHandler.uploadProductThumbnail(newImageFile);
            product.setProductThumbnail(thumbnail); // 상품에 새로운 썸네일 설정
        }

        // 제목 업데이트
        if (updateDto.getTitle() != null && !updateDto.getTitle().equals(product.getTitle())) {
            product.setTitle(updateDto.getTitle());
        }

        // description 업데이트
        if (updateDto.getDescription() != null && !updateDto.getDescription().equals(product.getDescription())) {
            // 기존 설명에서 이미지 URL 추출
            List<String> existingImageUrls = imageHandler.extractImageUrlsFromHtml(product.getDescription());
            // 변경된 설명에서 이미지 URL 추출
            List<String> newImageUrls = imageHandler.extractImageUrlsFromHtml(updateDto.getDescription());

            // 변경된 이미지 URL 비교
            List<String> imagesToDelete = new ArrayList<>();

            // 기존 이미지가 있는 경우 비교 ( 기존 descritpion에 이미지가 있었다면 삭제)
            if (!existingImageUrls.isEmpty()) {
                for (String existingUrl : existingImageUrls) {
                    if (!newImageUrls.contains(existingUrl)) {
                        imagesToDelete.add(existingUrl); // 삭제할 이미지 URL 추가
                    }
                }

                // S3에서 기존 이미지 삭제
                for (String imageUrl : imagesToDelete) {
                    imageHandler.deleteProductDescriptionImageFromS3(imageUrl); // S3에서 이미지 삭제
                }
            }

            // 새로운 이미지 s3에 insert
            List<String> uploadedUrls = imageHandler.uploadProductImagesToS3(newImageUrls);

            // description HTML의 이미지 URL을 s3에 새로 insert된 이미지 URL로 변경
            String modifiedDescription = imageHandler.replaceImageUrlsInHtml(updateDto.getDescription(), newImageUrls, uploadedUrls);
            product.setDescription(modifiedDescription); // 상품 설명 업데이트
        }

        // 가격 업데이트
        if (updateDto.getPrice() != null && !updateDto.getPrice().equals(product.getPrice())) {
            product.setPrice(updateDto.getPrice());
        }

        // 변경된 상품 저장
        productRepository.save(product);
    }

    // 상품 삭제
    // 상품 삭제 메서드
    @Transactional
    public void deleteProduct(Long productId, String userEmail) {

        // 상품 조회
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFound());

        // 유저 조회
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UserNotFound());

        // 유저가 상품의 판매자인지 확인
        if (!product.getSellerId().equals(user.getId())) {
            throw new IllegalArgumentException("본인의 상품이 아닙니다.");
        }

        // 상품 삭제
        productRepository.delete(product);
    }

}
