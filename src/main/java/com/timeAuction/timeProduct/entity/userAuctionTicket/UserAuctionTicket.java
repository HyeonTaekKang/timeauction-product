package com.timeAuction.timeProduct.entity.userAuctionTicket;

import com.timeAuction.timeProduct.entity.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAuctionTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 누구의 경매권인지
    @OneToOne(mappedBy = "userAuctionTicket" , fetch = FetchType.LAZY , cascade = CascadeType.ALL, orphanRemoval = true)
    private User user;

    // 경매권 생성일
    private LocalDateTime startDateTime;

    // 경매권 만료일
    private LocalDateTime endDateTime;

    // 경매권 만료여부
    private boolean isExpired;

    @Builder
    public UserAuctionTicket(Long id, User user, LocalDateTime startDateTime, LocalDateTime endDateTime, boolean isExpired) {
        this.id = id;
        this.user = user;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.isExpired = isExpired;
    }
}
