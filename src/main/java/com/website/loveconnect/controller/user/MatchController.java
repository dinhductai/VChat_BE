package com.website.loveconnect.controller.user;

import com.website.loveconnect.dto.response.ApiResponse;
import com.website.loveconnect.dto.response.MatchBySenderResponse;
import com.website.loveconnect.dto.response.MatchMatchIdResponse;
import com.website.loveconnect.service.MatchService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class MatchController {

    MatchService matchService;

    @GetMapping(value = "/matches")
    public ResponseEntity<ApiResponse<List<MatchBySenderResponse>>> getMatches(
            @RequestParam(name = "senderId") int senderId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(new ApiResponse<>(true,"Get all match by sender id successful",
                matchService.getAllMatchBySenderId(senderId, page, size)));
    }

    @GetMapping(value = "/matches/{matchId}")
    public ResponseEntity<ApiResponse<MatchMatchIdResponse>> getMatchBySenderId(
            @PathVariable int matchId){
        return ResponseEntity.ok(new ApiResponse<>(true,"Get match by match id successful",
                matchService.getMatchMatchId(matchId)));
    }
}
