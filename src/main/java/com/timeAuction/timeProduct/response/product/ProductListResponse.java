package com.timeAuction.timeProduct.response.product;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductListResponse<T> {
    private T data;
}
