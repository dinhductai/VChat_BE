package com.website.loveconnect.controller.admin;

import com.website.loveconnect.dto.request.MatchRequestDTO;
import com.website.loveconnect.dto.response.ApiResponse;
import com.website.loveconnect.dto.response.MatchResponse;
import com.website.loveconnect.enumpackage.MatchStatus;
import com.website.loveconnect.service.MatchService;
import com.website.loveconnect.service.impl.MatchServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminMatchController {
    MatchService matchService;

    // Tạo một Match mới
    @PostMapping("/matches/create")
    public ResponseEntity<ApiResponse<MatchResponse>> createMatch(@RequestBody MatchRequestDTO matchRequestDTO) {
        MatchResponse response = matchService.createMatch(matchRequestDTO);
        return ResponseEntity.ok(new ApiResponse<>(true,"Create new match successfully", response));
    }

    // Lấy danh sách Match của 1 người dùng
    @GetMapping("/matches/{userId}")
    public ResponseEntity<ApiResponse<List<MatchResponse>>> getMatchesByUser(@PathVariable Integer userId) {
        List<MatchResponse> matches = matchService.getMatchesByUser(userId);
        return ResponseEntity.ok(new ApiResponse<>(true,"Get matches successfully", matches));
    }

    // Cập nhật trạng thái Match
    @PutMapping("/matches/{matchId}/status")
    public ResponseEntity<ApiResponse<MatchResponse>> updateMatchStatus(
            @PathVariable Integer matchId,
            @RequestParam MatchStatus status) {
        MatchResponse response = matchService.updateMatchStatus(matchId, status);
        return ResponseEntity.ok(new ApiResponse<>(true,"Update match status successfully", response));
    }
}
