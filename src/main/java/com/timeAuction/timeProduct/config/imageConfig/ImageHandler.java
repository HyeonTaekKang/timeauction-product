package com.timeAuction.timeProduct.config.imageConfig;


import com.timeAuction.timeProduct.entity.productThumbnail.ProductThumbnail;
import com.timeAuction.timeProduct.exception.ImageUploadException;
import com.timeAuction.timeProduct.repository.productThumbnail.ProductThumbnailRepository;
import io.awspring.cloud.s3.S3Operations;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;


@Slf4j
@Component
@RequiredArgsConstructor
public class ImageHandler {

    @Value("${aws.product-thumbnail-bucket}")
    private String PRODUCT_THUMBNAIL_BUCKET_NAME;

    @Value("${aws.product-description-bucket-url}")
    private String PRODUCT_DESCRIPTION_BUCKET_URL;

    private final ProductThumbnailRepository productThumbnailRepository;
    private final S3Operations s3Operations;

    public List<String> extractImageUrlsFromHtml(String html) {
        List<String> imageUrls = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Elements images = doc.select("img");
        for (Element img : images) {
            String src = img.attr("src");
            imageUrls.add(src);
        }
        return imageUrls;
    }

    public String replaceImageUrlsInHtml(String html, List<String> imageUrls, List<String> uploadedUrls) {
        Document doc = Jsoup.parse(html);
        Elements images = doc.select("img");
        for (int i = 0; i < images.size(); i++) {
            Element img = images.get(i);
            String originalUrl = imageUrls.get(i);
            String uploadedUrl = uploadedUrls.get(i);
            img.attr("src", uploadedUrl);
        }
        return doc.html();
    }

    public static String extractExt(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex == -1) {
            return "";
        }
        return fileName.substring(dotIndex + 1);
    }

    private String generateUniqueFileName(String originalFileName) {
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        return "origin/" + UUID.randomUUID().toString() + extension;
    }

    // 상품 썸네일 저장
    public ProductThumbnail uploadProductThumbnail(MultipartFile productThumbnail) {
        try {
            // 썸네일의 진짜 이름
            String originalFileName = productThumbnail.getOriginalFilename();

            String fileName = generateUniqueFileName(productThumbnail.getOriginalFilename());

            // 원본 이미지 업로드
            s3Operations.upload(PRODUCT_THUMBNAIL_BUCKET_NAME, fileName, productThumbnail.getInputStream());

            // db에 이미지 정보 저장
            ProductThumbnail thumbnail = ProductThumbnail.builder()
                    .imageTitle(productThumbnail.getOriginalFilename())
                    .imageSize(productThumbnail.getSize())
                    .imageExtension(ProductThumbnail.extractExt(originalFileName))
                    .imageUrl(fileName)
                    .build();

            return productThumbnailRepository.save(thumbnail);
        } catch (IOException e) {
            throw new ImageUploadException();
        }
    }

    public List<String> uploadProductImagesToS3(List<String> imageUrls) {
        List<String> imageS3Urls = new ArrayList<>();
        for (String imageUrl : imageUrls) {
            String s3Url = uploadProductImageToS3(imageUrl);
            imageS3Urls.add(s3Url);
        }
        return imageS3Urls;
    }

    private String uploadProductImageToS3(String imageUrl) {
        try {
            byte[] imageBytes;
            String key;

            if (imageUrl.startsWith("data:")) {
                // data 프로토콜로 시작하는 경우, 이미지 데이터 추출
                String base64Image = imageUrl.split(",")[1];
                imageBytes = Base64.getDecoder().decode(base64Image);
                key = generateUniqueFileName("image.jpg"); // 파일명 생성
            } else {
                // 일반적인 URL인 경우, URL에서 이미지 데이터 추출
                URL url = new URL(imageUrl);
                imageBytes = ByteBuffer.wrap(url.openStream().readAllBytes()).array();
                key = generateUniqueFileName(imageUrl);
            }

            log.info("S3 upload 시작 - Bucket: {}, Key: {}", PRODUCT_DESCRIPTION_BUCKET_URL, key); // 로깅 추가
            s3Operations.upload(PRODUCT_DESCRIPTION_BUCKET_URL, key, new ByteArrayInputStream(imageBytes));
            log.info("S3 upload 완료 - Bucket: {}, Key: {}", PRODUCT_DESCRIPTION_BUCKET_URL, key); // 로깅 추가

            return PRODUCT_DESCRIPTION_BUCKET_URL + key;

        } catch (IOException e) {
            log.error("S3 upload 실패 - Bucket: {}, error: {}", PRODUCT_DESCRIPTION_BUCKET_URL, e); // 로깅 추가
            throw new RuntimeException("Failed to upload image", e);
        }
    }

    public void deleteProductThumbnailFromS3(String imageUrl) {
        String key = extractKeyFromUrl(imageUrl);
        s3Operations.deleteObject(PRODUCT_THUMBNAIL_BUCKET_NAME , key);
    }

    public void deleteProductDescriptionImageFromS3(String imageUrl) {
        String key = extractKeyFromUrl(imageUrl);
        s3Operations.deleteObject(PRODUCT_DESCRIPTION_BUCKET_URL , key);
    }

    public String extractKeyFromUrl(String imageUrl) {
        String[] parts = imageUrl.split("/");
        return parts[parts.length - 1]; // 마지막 부분이 파일 이름
    }
}

