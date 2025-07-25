package com.website.loveconnect.service;

import com.website.loveconnect.dto.request.ChatAIRequest;
import com.website.loveconnect.dto.response.ChatAIResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ChatAIService {
    List<ChatAIResponse> chat(String message, MultipartFile file,String conversationId, Integer userId) ;
}
