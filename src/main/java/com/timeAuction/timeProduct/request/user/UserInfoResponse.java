package com.timeAuction.timeProduct.request.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class UserInfoResponse {

    private Long id;

    private String nickName; // 유저 닉네임

    // 경매권 생성일 ( 만약 경매권이 없을 시 null 로 표시됨 )
    private LocalDateTime startDateTime;

    // 경매권 종료일 ( 만약 경매권이 없을 시 null 로 표시됨 )
    private LocalDateTime endDateTime;

    @Builder
    public UserInfoResponse(Long id, String nickName, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.id = id;
        this.nickName = nickName;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    @Override
    public String toString() {
        return "UserInfoResponse{" +
                "id=" + id +
                ", nickName='" + nickName + '\'' +
                ", startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime +
                '}';
    }
}
