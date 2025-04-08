package com.timeAuction.timeProduct.entity.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserIcon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "userIcon", fetch = FetchType.LAZY)
    private List<User> userList = new ArrayList<>();

    private String originalIconName; // 원래 아이콘 이름

    private String storageIconName; // db저장용 아이콘 이름

    private String iconUrl; // 아이콘 저장 위치 url

    @Builder
    public UserIcon(Long id, List<User> userList, String originalIconName, String storageIconName, String iconUrl) {
        this.id = id;
        this.userList = userList;
        this.originalIconName = originalIconName;
        this.storageIconName = storageIconName;
        this.iconUrl = iconUrl;
    }

    // "서버에 저장할 아이콘 이름" 생성 메서드
    public static String createStoreIconName(String originalIconName) {
        String ext = extractExt(originalIconName);
        return UUID.randomUUID() + "." + ext + originalIconName;
    }

    private static String extractExt(String originalIconName) {
        // test.png ->
        int pos = originalIconName.lastIndexOf(".");
        return originalIconName.substring(pos + 1);
    }
}
