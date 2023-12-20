package com.example.withdogandcat.domain.shop;

import com.example.withdogandcat.domain.shop.dto.ShopDetailResponseDto;
import com.example.withdogandcat.domain.shop.dto.ShopRequestDto;
import com.example.withdogandcat.domain.shop.dto.ShopResponseDto;
import com.example.withdogandcat.domain.shop.entity.ShopType;
import com.example.withdogandcat.domain.user.entity.User;
import com.example.withdogandcat.global.common.BaseResponse;
import com.example.withdogandcat.global.common.LoginAccount;
import com.example.withdogandcat.global.exception.BaseResponseStatus;
import com.example.withdogandcat.global.security.impl.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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

    @GetMapping("/mypage")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<BaseResponse<List<ShopResponseDto>>> getShopsByCurrentUser(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User currentUser = userDetails.getUser();
        List<ShopResponseDto> shops = shopService.getShopsByCurrentUser(currentUser).getResult();
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", shops));
    }

    @PostMapping("")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<BaseResponse<ShopResponseDto>> createShop(
            @ModelAttribute ShopRequestDto shopRequestDto,
            @RequestPart("imageUrl") List<MultipartFile> imageFiles,
            @LoginAccount User currentUser) throws IOException {

        ShopResponseDto createdShop = shopService.createShop(shopRequestDto, imageFiles, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", createdShop));
    }

    @GetMapping("")
    public ResponseEntity<BaseResponse<List<ShopResponseDto>>> getAllShops() {
        List<ShopResponseDto> shops = shopService.getAllShops().getResult();
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", shops));
    }

    @GetMapping("/{shopId}")
    public ResponseEntity<BaseResponse<ShopDetailResponseDto>> getShopDetails(@PathVariable("shopId") Long shopId) {
        ShopDetailResponseDto shopDetailResponseDto = shopService.getShopDetails(shopId).getResult();
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", shopDetailResponseDto));
    }

    @PutMapping("/{shopId}")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<BaseResponse<ShopResponseDto>> updateShop(
            @PathVariable("shopId") Long shopId,
            @ModelAttribute ShopRequestDto shopRequestDto,
            @RequestParam(value = "imageUrl", required = false) List<MultipartFile> imageFiles,
            @LoginAccount User currentUser) throws IOException {

        ShopResponseDto updatedShop = shopService.updateShop(shopId, shopRequestDto, imageFiles, currentUser).getResult();
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", updatedShop));
    }

    @DeleteMapping("/{shopId}")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<BaseResponse<Void>> deleteShop(@PathVariable Long shopId) {
        shopService.deleteShop(shopId);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", null));
    }

    @GetMapping("/category/{shopType}")
    public ResponseEntity<BaseResponse<List<ShopResponseDto>>> getShopsByCategory(
            @PathVariable("shopType") ShopType shopType) {
        List<ShopResponseDto> shops = shopService.getShopsByCategory(shopType).getResult();
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", shops));
    }
}
