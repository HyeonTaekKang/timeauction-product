package com.timeAuction.timeProduct.repository.user;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import com.timeAuction.timeProduct.request.user.UserInfoResponse;
import lombok.RequiredArgsConstructor;

import static com.timeAuction.timeProduct.entity.user.QUser.user;
import static com.timeAuction.timeProduct.entity.userAuctionTicket.QUserAuctionTicket.userAuctionTicket;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    // 로그인한 유저의 정보 가져오기
    @Override
    public UserInfoResponse getUserInfoWithUserAuctionTicket(String email) {
        return jpaQueryFactory
                .select(Projections.constructor(UserInfoResponse.class,
                        user.id,
                        user.nickname,
                        userAuctionTicket.startDateTime,
                        userAuctionTicket.endDateTime
                ))
                .from(user)
                .where(user.email.eq(email))
                .leftJoin(user.userAuctionTicket , userAuctionTicket) // 만약 userAuctionTicket이 없으면 user만 가져오고 userAuctionticket은 null로 가져옴
                .fetchOne();
    }
}
