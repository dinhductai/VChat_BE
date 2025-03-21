package com.website.loveconnect.service;

import com.website.loveconnect.dto.request.InterestDTO;
import com.website.loveconnect.entity.Interest;

import java.util.List;

public interface InterestService {
    // Method CRUD interest
    void addInterest(int idUser , InterestDTO interestDTO);
    void deleterInterest(int idUser ,int idInterest);
    void updateInterest(int  idInterest ,int idUser , InterestDTO interestDTO);
    List<Interest> getAllInterest(int idUser);
}
