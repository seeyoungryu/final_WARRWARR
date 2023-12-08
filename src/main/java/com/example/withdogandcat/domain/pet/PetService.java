package com.example.withdogandcat.domain.pet;

import com.example.withdogandcat.domain.pet.dto.PetRequestDto;
import com.example.withdogandcat.domain.pet.dto.PetResponseDto;
import com.example.withdogandcat.domain.pet.entity.Pet;
import com.example.withdogandcat.domain.shop.dto.ShopRequestDto;
import com.example.withdogandcat.domain.shop.dto.ShopResponseDto;
import com.example.withdogandcat.domain.shop.entity.Shop;
import com.example.withdogandcat.global.config.s3.S3Upload; // 변경된 import
import com.example.withdogandcat.domain.user.entity.User;
import com.example.withdogandcat.domain.user.UserRepository;
import com.example.withdogandcat.global.dto.ApiResponseDto;
import com.example.withdogandcat.global.exception.CustomException;
import com.example.withdogandcat.global.exception.ErrorCode;
import com.example.withdogandcat.global.tool.LoginAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException; // 추가된 import
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PetService {

    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final S3Upload s3Upload;



    private static final String PETINFO_BUCKET = "petinfo-pet";                        //일단따로


    public ApiResponseDto<List<PetResponseDto>> getPetsByUserId(Long userId) {
        List<Pet> pets = petRepository.findByUser_UserId(userId);

        String message = pets.isEmpty() ? "등록된 왈왈이가 없습니다" : "왈왈이 목록 조회 성공";
        List<PetResponseDto> petDtos = pets.stream()
                .map(PetResponseDto::from)
                .collect(Collectors.toList());
        return new ApiResponseDto<>(message, petDtos);
    }



    //반려동물 등록
    @Transactional
    public PetResponseDto registerPet(PetRequestDto petRequestDto ,MultipartFile imageUrl,
                                      @LoginAccount User currentUser) throws IOException {
        String image = s3Upload.upload(imageUrl, PETINFO_BUCKET);
        Pet pet = Pet.of(petRequestDto, image, currentUser);
        petRepository.save(pet);
        return PetResponseDto.from(pet);
    }



    //반려동물 전체 조회
    @Transactional(readOnly = true)
    public List<PetResponseDto> getAllPets() {
        return petRepository.findAll().stream()
                .map(PetResponseDto::from).collect(Collectors.toList());
    }


    //반려동물 상세조회
    @Transactional(readOnly = true)
    public PetResponseDto getPet(Long userId, Long petId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.PET_NOT_FOUND));

        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new CustomException(ErrorCode.PET_NOT_FOUND));

        return PetResponseDto.from(pet);
    }



    //반려동물 삭제
    @Transactional
    public void deletePet(Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new CustomException(ErrorCode.PET_NOT_FOUND));

        petRepository.delete(pet);
    }

}