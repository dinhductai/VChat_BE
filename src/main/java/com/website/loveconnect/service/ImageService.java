package com.website.loveconnect.service;

import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;

public interface ImageService {
    String saveImageProfile(MultipartFile file, String userEmail) throws IOException;
}
