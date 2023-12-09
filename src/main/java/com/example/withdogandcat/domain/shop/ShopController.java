package com.example.withdogandcat.domain.shop;

import com.example.withdogandcat.domain.shop.dto.ShopDetailResponseDto;
import com.example.withdogandcat.domain.shop.dto.ShopRequestDto;
import com.example.withdogandcat.domain.shop.dto.ShopResponseDto;
import com.example.withdogandcat.domain.shop.entity.ShopType;
import com.example.withdogandcat.domain.user.entity.User;
import com.example.withdogandcat.global.tool.ApiResponseDto;
import com.example.withdogandcat.global.tool.LoginAccount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shops")
public class ShopController {

    private final ShopService shopService;

    // 마이페이지 가게 조회
    @GetMapping("/mypage/{userId}")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<ApiResponseDto<List<ShopResponseDto>>> getShopsByCurrentUser(@PathVariable("userId") Long userId) {
        ApiResponseDto<List<ShopResponseDto>> response = shopService.getShopsByUserId(userId);
        return ResponseEntity.ok(response);
    }

    // 가게 등록
    @PostMapping("")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<ApiResponseDto<ShopResponseDto>> createShop(
            @ModelAttribute ShopRequestDto shopRequestDto,
            @RequestPart("imageUrl") List<MultipartFile> imageFiles,
            @LoginAccount User currentUser) throws IOException {

        ShopResponseDto createdShop = shopService.createShop(shopRequestDto, imageFiles, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDto<>("가게 등록 성공", createdShop));
    }

    // 가게 전체 조회
    @GetMapping("")
    public ResponseEntity<ApiResponseDto<List<ShopResponseDto>>> getAllShops() {
        ApiResponseDto<List<ShopResponseDto>> response = shopService.getAllShops();
        return ResponseEntity.ok(response);
    }

    // 가게 상세 조회
    @GetMapping("/{shopId}")
    public ResponseEntity<ApiResponseDto<ShopDetailResponseDto>> getShopDetails(@PathVariable("shopId") Long shopId) {
        ShopDetailResponseDto shopDetailResponseDto = shopService.getShopDetails(shopId);
        return ResponseEntity.ok(new ApiResponseDto<>("가게 상세 조회 성공", shopDetailResponseDto));
    }

    // 가게 수정
    @PutMapping("/{shopId}")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<ApiResponseDto<ShopResponseDto>> updateShop(
            @PathVariable("shopId") Long shopId,
            @ModelAttribute ShopRequestDto shopRequestDto,
            @RequestPart("imageUrl") List<MultipartFile> imageFiles,
            @LoginAccount User currentUser) throws IOException {

        ShopResponseDto updatedShop = shopService.updateShop(shopId, shopRequestDto, imageFiles, currentUser);
        return ResponseEntity.ok(new ApiResponseDto<>("가게 수정 성공", updatedShop));
    }

    // 가게 삭제
    @DeleteMapping("/{shopId}")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<ApiResponseDto<String>> deleteShop(@PathVariable Long shopId) {
        shopService.deleteShop(shopId);
        return ResponseEntity.ok(new ApiResponseDto<>("가게 삭제 완료", null));
    }
}
