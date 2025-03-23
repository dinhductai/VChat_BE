package com.website.loveconnect.dto.request;

import lombok.Data;

@Data
public class MatchRequestDTO {
    private Integer senderId;
    private Integer receiverId;
}