package com.example.withdogandcat.domain.shop.entity;

import com.example.withdogandcat.domain.shop.dto.ShopRequestDto;
import com.example.withdogandcat.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "shops")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shopId;

    private String shopName;
    private String shopTime;
    private String shopTel;
    private String shopAddress;
    private String shopDescribe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private ShopType shopType;

    private String imageUrl;

    @Builder
    private Shop(String shopName, String shopTime, String shopTel,
                 String shopAddress, ShopType shopType,
                 String shopDescribe, String imageUrl, User user) {
        this.shopName = shopName;
        this.shopTime = shopTime;
        this.shopTel = shopTel;
        this.shopAddress = shopAddress;
        this.shopType = shopType;
        this.shopDescribe = shopDescribe;
        this.imageUrl = imageUrl;
        this.user = user;

    }

    public static Shop of(ShopRequestDto shopRequestDto, String imageUrl, User user) {
        return Shop.builder()
                .shopName(shopRequestDto.getShopName())
                .shopTime(shopRequestDto.getShopTime())
                .shopTel(shopRequestDto.getShopTel())
                .shopAddress(shopRequestDto.getShopAddress())
                .shopType(shopRequestDto.getShopType())
                .shopDescribe(shopRequestDto.getShopDescribe())
                .imageUrl(imageUrl)
                .user(user)
                .build();
    }

    public void update(ShopRequestDto shopRequestDto, String imageUrl) {
        this.shopName = shopRequestDto.getShopName();
        this.shopTime = shopRequestDto.getShopTime();
        this.shopTel = shopRequestDto.getShopTel();
        this.shopAddress = shopRequestDto.getShopAddress();
        this.shopType = shopRequestDto.getShopType();
        this.shopDescribe = shopRequestDto.getShopDescribe();
        this.imageUrl = imageUrl;
    }
}