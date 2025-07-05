package com.website.loveconnect.mapper;

import com.website.loveconnect.dto.response.PhotoStoryResponse;
import com.website.loveconnect.entity.Photo;
import jakarta.persistence.Tuple;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class PhotoMapper {
    public PhotoStoryResponse toPhotoStoryResponseList(Tuple tuple) {
        return PhotoStoryResponse.builder()
                .userId(tuple.get("userId", Integer.class))
                .fullName(tuple.get("fullName",String.class))
                .profileUrl(tuple.get("profileUrl",String.class))
                .listStoryPhoto(tuple.get("listStoryPhoto",String.class) != null ?
                        Arrays.asList(tuple.get("listStoryPhoto",String.class).split(","))
                        : Collections.emptyList())
                .listDateUpload(tuple.get("listDateUpload",String.class) != null ?
                        Arrays.asList(tuple.get("listDateUpload",String.class).split(","))
                        : Collections.emptyList())
                .build();

    }
}
