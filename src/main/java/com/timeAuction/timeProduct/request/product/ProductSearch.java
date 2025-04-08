package com.timeAuction.timeProduct.request.product;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductSearch {
    private static final int MAX_SIZE = 2000;

    @Builder.Default
    private Integer page = 1;

    @Builder.Default
    private  Integer size = 20;

    public int getOffset(){
        return (Math.max(page,1)-1)* Math.min(size,MAX_SIZE);
    }

    public int getLimit(){ return Math.min(size , MAX_SIZE);}
}
