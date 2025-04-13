package com.website.loveconnect.service.impl;

import com.website.loveconnect.dto.response.ProfileDetailResponse;
import com.website.loveconnect.entity.Interest;
import com.website.loveconnect.entity.User;
import com.website.loveconnect.entity.UserProfile;
import com.website.loveconnect.exception.UserNotFoundException;
import com.website.loveconnect.mapper.UserProfileMapper;
import com.website.loveconnect.repository.InterestRepository;
import com.website.loveconnect.repository.UserProfileRepository;
import com.website.loveconnect.repository.UserRepository;
import com.website.loveconnect.service.UserProfileService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class UserProfileServiceImpl implements UserProfileService {
    UserRepository userRepository;
    UserProfileRepository userProfileRepository;
    InterestRepository interestRepository;
    UserProfileMapper userProfileMapper;
    @Override
    public ProfileDetailResponse getProfileDetail(Integer idUser) {
        try {
            User user = userRepository.findById(idUser)
                    .orElseThrow(() -> new UserNotFoundException("User Not Found"));

            UserProfile userProfile = userProfileRepository.findByUser_UserId(user.getUserId())
                    .orElseThrow(() -> new UserNotFoundException("User Not Found"));

            List<Interest> listInterest = interestRepository.getAllInterest(user.getUserId());

            return userProfileMapper.toProfileDetailResponse(user,userProfile, listInterest);
        }
        catch (DataAccessException da){
            throw new com.website.loveconnect.exception.DataAccessException("Cannot access data");
        }
    }
}
