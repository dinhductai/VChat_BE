package com.website.loveconnect.service;

import com.website.loveconnect.dto.request.ChatAIRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ChatAIService {
    String chat(String message, MultipartFile file);
}
