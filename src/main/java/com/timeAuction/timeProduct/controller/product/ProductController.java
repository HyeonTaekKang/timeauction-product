package com.timeAuction.timeProduct.controller.product;


import com.timeAuction.timeProduct.request.product.ProductCreate;
import com.timeAuction.timeProduct.request.product.ProductEdit;
import com.timeAuction.timeProduct.response.product.ProductListResponse;
import com.timeAuction.timeProduct.response.product.ProductResponse;
import com.timeAuction.timeProduct.service.product.ProductService;
import com.timeAuction.timeProduct.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product") // 기본 경로 설정
public class ProductController {

    private final ProductService productService;
    private final UserService userService;

    private final CognitoIdentityProviderClient cognitoIdentityProviderClient;

    // 새로운 상품 등록
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Long createProduct(
            @RequestPart(value = "productCreate") ProductCreate productCreate,
            @RequestHeader("Authorization") String authHeader,
            @RequestPart(value = "productThumbnail", required = false) MultipartFile productThumbnail

    ) {
        String email = userService.getUserEmail(authHeader);

        Long itemId = productService.createProduct(productCreate, email, productThumbnail);
        return itemId;
    }

    // 상품 1개 가져오기 ( 상품 상세 페이지 )
    @GetMapping("/{productId}")
    public ProductResponse getProduct(@PathVariable Long productId) {
        return productService.getProduct(productId);
    }

    // 본인 상품 모두 가져오기
    @GetMapping("/my-products")
    public ProductListResponse<List<ProductResponse>> getMyProductList(
            @RequestParam(required = false) Long lastId,
            @RequestParam(required = false) Integer limit,
            @RequestHeader("Authorization") String authHeader
    ) {
        // 로그인한 사용자 이메일 가져오기
        String email = userService.getUserEmail(authHeader);
        return productService.getMyProductList(lastId, limit, email);
    }

    // 상품 최신순으로 N개 가져오기
    @GetMapping("/recents")
    public ProductListResponse<List<ProductResponse>> getRecentProducts(@RequestParam(required = false) Long lastId, @RequestParam(required = false) Integer limit) {
        return productService.getRecentProducts(lastId, limit);
    }

    // 상품 변경하기
    @PutMapping("/{productId}")
    public ResponseEntity<Void> updateProduct(
            @PathVariable Long productId,
            @RequestPart(value = "productEdit") ProductEdit updateDto,
            @RequestPart(value = "productThumbnail" , required = false) MultipartFile newImageFile,
            @RequestHeader("Authorization") String authHeader) {

        String email = userService.getUserEmail(authHeader);
        productService.updateProduct(email, productId, updateDto, newImageFile);
        return ResponseEntity.ok().build();
    }

    // 상품 판매 완료
    @PostMapping("/{id}/completeSale")
    public ResponseEntity<Void> completeSale(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        String email = userService.getUserEmail(authHeader);
        productService.completeSale(id, email);
        return ResponseEntity.ok().build();
    }


    // 상품 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        String email = userService.getUserEmail(authHeader);
        productService.deleteProduct(id, email);
        return ResponseEntity.noContent().build(); // 204 No Content 응답
    }
}
