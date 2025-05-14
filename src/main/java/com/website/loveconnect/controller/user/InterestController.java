package com.website.loveconnect.controller.user;


import com.website.loveconnect.dto.request.InterestDTO;
import com.website.loveconnect.dto.response.ApiResponse;
import com.website.loveconnect.entity.Interest;
import com.website.loveconnect.service.InterestService;
import com.website.loveconnect.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class InterestController {

    UserService userService;
    InterestService interestService;

    // Request Get all Interest
    @GetMapping("/{idUser}/interests")
    public ResponseEntity<ApiResponse<?>> getAllInterest(@PathVariable Integer idUser) {
            List<Interest> interests = interestService.getAllInterest(idUser);
            return ResponseEntity.ok(new ApiResponse<>(true,"Get interests successful", interests));
    }

    // Request Add one Interest
    @PostMapping("/{idUser}/interests/add")
    public ResponseEntity<ApiResponse<?>> addInterest(@Valid  @RequestBody InterestDTO interestDTO ,
                                                      @PathVariable Integer idUser) {
            interestService.addInterest(idUser , interestDTO);
            return ResponseEntity.ok(new ApiResponse<>(true,"Added interest successful", null));
    }

    // Request Update one Interest
    @PutMapping("/{idUser}/interests/update/{idInterest}")
    public ResponseEntity<ApiResponse<?>> updateInterest(@Valid @RequestBody InterestDTO interestDTO
            , @PathVariable Integer idUser
            , @PathVariable Integer idInterest) {
            interestService.updateInterest(idInterest, idUser, interestDTO);
            return ResponseEntity.ok(new ApiResponse<>(true,"Updated interest successful", null));

    }

    // Request Delete one Interest
    @DeleteMapping("/{idUser}/interests/delete/{idInterest}")
    public ResponseEntity<ApiResponse<?>> deleteInterest(@PathVariable Integer idUser ,
                                                         @PathVariable Integer idInterest) {
        try {
            log.info("Request SỬA sở thích vào danh sách của user có ID : {}" , idUser);
            interestService.deleterInterest(idUser , idInterest);
            return ResponseEntity.ok(new ApiResponse<>(true,"Deleted interest successful", null));
        }catch (Exception ex) {
            log.error("--->>> Không thể xóa được sở thích vì : {}" , ex.getMessage() , ex.getCause());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //lấy danh sách đầy đủ sở thích
    @GetMapping(value = "/interests-name")
    public ResponseEntity<ApiResponse<List<String>>> getInterestName() {
        return ResponseEntity.ok(new ApiResponse<>(true,"Get list interest name successful",
                interestService.findAllInterestName()));
    }
}
