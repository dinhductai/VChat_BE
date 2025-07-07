package com.website.loveconnect.service.impl;

import com.website.loveconnect.dto.request.ProfileDetailRequest;
import com.website.loveconnect.dto.response.ProfileDetailResponse;
import com.website.loveconnect.dto.response.UserNameAndProfileResponse;
import com.website.loveconnect.entity.Interest;
import com.website.loveconnect.entity.User;
import com.website.loveconnect.entity.UserProfile;
import com.website.loveconnect.exception.DataAccessException;
import com.website.loveconnect.exception.UserNotFoundException;
import com.website.loveconnect.mapper.UserInterestMapper;
import com.website.loveconnect.mapper.UserMapper;
import com.website.loveconnect.mapper.UserProfileMapper;
import com.website.loveconnect.repository.InterestRepository;
import com.website.loveconnect.repository.UserInterestRepository;
import com.website.loveconnect.repository.UserProfileRepository;
import com.website.loveconnect.repository.UserRepository;
import com.website.loveconnect.service.UserProfileService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class UserProfileServiceImpl implements UserProfileService {
    @PersistenceContext
    EntityManager entityManager;
    UserRepository userRepository;
    UserProfileRepository userProfileRepository;
    InterestRepository interestRepository;
    UserProfileMapper userProfileMapper;
    UserMapper userMapper;
    UserInterestMapper userInterestMapper;
    UserInterestRepository userInterestRepository;

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

    @Override
    public void updateProfileDetail(Integer idUser, ProfileDetailRequest profileDetailRequest) {
        try {
            User user = userRepository.findById(idUser)
                    .orElseThrow(() -> new UserNotFoundException("User Not Found"));

            UserProfile userProfile = userProfileRepository.findByUser_UserId(user.getUserId())
                    .orElseThrow(() -> new UserNotFoundException("User Not Found"));

            List<Interest> listInterest = interestRepository.getByInterestNameIn(profileDetailRequest.getInterestName());
            List<Integer> listInterestId = listInterest.stream().map(Interest::getInterestId).collect(Collectors.toList());
            userRepository.save(userMapper.toUpdateUserEmailAndPhoneNumber(user, profileDetailRequest));
            userProfileRepository.save(userProfileMapper.toUpdateUserProfile(userProfile, profileDetailRequest));
            userInterestRepository.insertUserInterestNotExist(idUser,listInterestId);
//        userInterestRepository.(userInterestMapper.toAttachUserInterest(listInterest,user));

        }
    catch (DataAccessException da){
        throw new com.website.loveconnect.exception.DataAccessException("Cannot access data");
    }
    }

    @Override
    public UserNameAndProfileResponse getUserNameAndProfile(Integer idUser) {
        try {
            return userProfileRepository.findUserNameAndProfileByUserId(idUser).orElseThrow(() -> new UserNotFoundException("User Not Found"));
        }catch (DataAccessException e){
            throw new DataAccessException("Cannot access database");
        }
    }
}
