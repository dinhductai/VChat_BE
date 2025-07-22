package com.website.loveconnect.mapper;

import com.website.loveconnect.dto.response.PostResponse;
import com.website.loveconnect.dto.response.ReelResponse;
import jakarta.persistence.Tuple;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
@Component
public class PostMapper {

    public PostResponse toPostResponse(Tuple tuple) {
        // Lấy dữ liệu chuỗi các URL, sau đó tách ra list nếu cần
        String photoUrlsRaw = tuple.get("photosUrl", String.class);
        String videoUrlsRaw = tuple.get("videosUrl", String.class);

        List<String> photoUrls = (photoUrlsRaw != null && !photoUrlsRaw.isEmpty())
                ? Arrays.stream(photoUrlsRaw.split(",")).collect(Collectors.toList())
                : List.of();

        List<String> videoUrls = (videoUrlsRaw != null && !videoUrlsRaw.isEmpty())
                ? Arrays.stream(videoUrlsRaw.split(",")).collect(Collectors.toList())
                : List.of();

        return PostResponse.builder()
                .userId(tuple.get("userId", Integer.class))
                .fullName(tuple.get("fullName", String.class))
                .bio(tuple.get("bio", String.class))
                .phoneNumber(tuple.get("phoneNumber", String.class))
                .profilePicture(tuple.get("profilePicture", String.class))
                .postId(tuple.get("postId", Integer.class))
                .content(tuple.get("content", String.class))
                .uploadDate(tuple.get("uploadDate", Timestamp.class))
                .status(tuple.get("status", String.class))
                .isPublic(tuple.get("isPublic", Boolean.class))
                .photosUrl(photoUrls)
                .videosUrl(videoUrls)
                .build();
    }

    public ReelResponse toReelResponse(Tuple tuple) {
        return ReelResponse.builder()
                .userId(tuple.get("userId", Integer.class))
                .fullName(tuple.get("fullName", String.class))
                .bio(tuple.get("bio", String.class))
                .phoneNumber(tuple.get("phoneNumber", String.class))
                .profilePicture(tuple.get("profilePicture", String.class))
                .postId(tuple.get("postId", Integer.class))
                .content(tuple.get("content", String.class))
                .uploadDate(tuple.get("uploadDate", Timestamp.class))
                .status(tuple.get("status", String.class))
                .isPublic(tuple.get("isPublic", Boolean.class))
                .videosUrl(tuple.get("videosUrl",String.class))
                .build();
    }
}
