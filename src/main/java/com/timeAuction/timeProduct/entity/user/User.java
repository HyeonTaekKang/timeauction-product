package com.timeAuction.timeProduct.entity.user;


import com.timeAuction.timeProduct.entity.userAuctionTicket.UserAuctionTicket;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        indexes = {
                @Index(name = "IDX_USER_EMAIL", columnList = "email")
        }
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long id;

    // 이메일  ====> 아이디
    private String email;

    // 패스워드
    private String password;

    // 이름
    private String userName;

    // 닉네임
    private String nickname;

    // 유저 role
    @Enumerated(value = EnumType.STRING)
    private UserRole userRole;

    // 유저 아이콘
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userIcon_id")
    private UserIcon userIcon;

    // 경매권
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userAuctionTicket_id")
    private UserAuctionTicket userAuctionTicket;

    @Builder
    public User(Long id, String email, String password, String userName, String nickname, UserRole userRole, UserIcon userIcon, UserAuctionTicket userAuctionTicket) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.nickname = nickname;
        this.userRole = userRole;
        this.userIcon = userIcon;
        this.userAuctionTicket = userAuctionTicket;
    }

    public void setUserAuctionTicket(UserAuctionTicket userAuctionTicket) {
        this.userAuctionTicket = userAuctionTicket;
    }
}

