package com.website.loveconnect.controller.user;


import com.website.loveconnect.dto.request.InterestRequest;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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
    @GetMapping("/interests")
    public ResponseEntity<ApiResponse<?>> getAllInterest(@AuthenticationPrincipal Jwt jwt) {
            int idUser = Integer.parseInt(jwt.getSubject());
            List<Interest> interests = interestService.getAllInterest(idUser);
            return ResponseEntity.ok(new ApiResponse<>(true,"Get interests successful", interests));
    }

    // Request Add one Interest
    @PostMapping("/interests/add")
    public ResponseEntity<ApiResponse<?>> addInterest(@Valid  @RequestBody InterestRequest interestDTO ,
                                                      @AuthenticationPrincipal Jwt jwt) {
            int idUser = Integer.parseInt(jwt.getSubject());
            interestService.addInterest(idUser , interestDTO);
            return ResponseEntity.ok(new ApiResponse<>(true,"Added interest successful", null));
    }

    // Request Update one Interest
    @PutMapping("/interests/update/{idInterest}")
    public ResponseEntity<ApiResponse<?>> updateInterest(@Valid @RequestBody InterestRequest interestDTO
            , @AuthenticationPrincipal Jwt jwt
            , @PathVariable Integer idInterest) {
        int idUser = Integer.parseInt(jwt.getSubject());
            interestService.updateInterest(idInterest, idUser, interestDTO);
            return ResponseEntity.ok(new ApiResponse<>(true,"Updated interest successful", null));

    }

    // Request Delete one Interest
    @DeleteMapping("/interests/delete/{idInterest}")
    public ResponseEntity<ApiResponse<?>> deleteInterest(@AuthenticationPrincipal Jwt jwt,
                                                         @PathVariable Integer idInterest) {
             int idUser = Integer.parseInt(jwt.getSubject());
            interestService.deleterInterest(idUser , idInterest);
            return ResponseEntity.ok(new ApiResponse<>(true,"Deleted interest successful", null));
    }

    //lấy danh sách đầy đủ sở thích
    @GetMapping(value = "/interests-name")
    public ResponseEntity<ApiResponse<List<String>>> getInterestName() {
        return ResponseEntity.ok(new ApiResponse<>(true,"Get list interest name successful",
                interestService.findAllInterestName()));
    }
}
