package com.timeAuction.timeProduct.repository.user;


import com.timeAuction.timeProduct.request.user.UserInfoResponse;

public interface UserRepositoryCustom {

    UserInfoResponse getUserInfoWithUserAuctionTicket(String email);
}
