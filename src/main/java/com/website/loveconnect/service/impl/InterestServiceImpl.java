package com.website.loveconnect.service.impl;

import com.website.loveconnect.dto.request.InterestRequest;
import com.website.loveconnect.entity.Interest;
import com.website.loveconnect.entity.User;
import com.website.loveconnect.entity.UserInterest;
import com.website.loveconnect.exception.InterestNotFoundException;
import com.website.loveconnect.exception.UserNotFoundException;
import com.website.loveconnect.mapper.UserMapper;
import com.website.loveconnect.repository.InterestRepository;
import com.website.loveconnect.repository.UserInterestRepository;
import com.website.loveconnect.repository.UserRepository;
import com.website.loveconnect.service.InterestService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class InterestServiceImpl implements InterestService {
    @PersistenceContext
    private EntityManager entityManager;
    UserRepository userRepository;
    ModelMapper modelMapper;
    UserMapper userMapper;
    InterestRepository interestRepository;
    UserInterestRepository userInterestRepository;
    @Override
    public void addInterest(int idUser, InterestRequest interestDTO) {
        try {
            User user = userRepository.findById(idUser)
                    .orElseThrow(() -> new UserNotFoundException("User with id "+ idUser + " not found"));

            Interest interest = interestRepository.findByInterestName(interestDTO.getInterestName())
                    .orElseThrow(()->new InterestNotFoundException("Interest not found"));

            UserInterest ui = UserInterest.builder()
                    .user(user)
                    .interest(interest)
                    .build();

            interestRepository.save(interest);
            userInterestRepository.save(ui);

            log.info("Sở thích đã được thêm thành công");
        } catch (Exception e) {
            log.error("Không thể thêm được sở thích vì {}" , e.getMessage() , e.getCause());
        }
    }

    @Override
    public void deleterInterest(int idUser, int idInterest) {
        userRepository.findById(idUser)
                .orElseThrow(() -> new UserNotFoundException("User with id "+ idUser + " not found"));
        interestRepository.findById(idInterest)
                .orElseThrow(() -> new InterestNotFoundException("Interest with id "+ idInterest + " not found"));

        // Delete record has idUser and idInterest
        UserInterest userInterest = userInterestRepository.findUserInterestWithIdUserAndIdInterest(idInterest , idUser);
        userInterestRepository.delete(userInterest);

        log.info("Xóa thành công sở thích có idInterest : {}" , idInterest);
    }

    @Override
    public void updateInterest(int idInterest, int idUser, InterestRequest interestDTO) {
        try {
            userRepository.findById(idUser)
                    .orElseThrow(() -> new UserNotFoundException("User with id "+ idUser + " not found"));
            // Xác định xem User có ID idUser có sở thích có ID idInterest không
            UserInterest ui = userInterestRepository.findUserInterestWithIdUserAndIdInterest(idInterest , idUser);
//                    .orElseThrow(() -> new UserNotFoundException("Not found UserInterest need find !!! "));;
            Interest interest = interestRepository.findById(idInterest)
                    .orElseThrow(() -> new UserNotFoundException("Interest with id "+ idInterest + " not found"));
            interest.setInterestName(interestDTO.getInterestName());
            interest.setCategory(interestDTO.getCategory());

            interestRepository.save(interest);
            log.info("Interest has been updated successfully , idInterest = {}" , idInterest);
        } catch (Exception e) {
            log.error("Không thể sửa được sở thích vì {}" , e.getMessage() , e.getCause());
        }
    }

    @Override
    public List<Interest> getAllInterest(int idUser) {
        if( idUser <= 0) {
            throw new UserNotFoundException("User with id "+ idUser + " not found");
        }
        List<Interest> interestList = interestRepository.getAllInterest(idUser);

        return interestList;
    }

    @Override
    public List<String> findAllInterestName() {
        try {
            return interestRepository.findAllInterestName();

        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , e.getMessage() , e.getCause());
        }
    }

    private void validateUserId(int idUser) {
        if (idUser <= 0) {
            throw new IllegalArgumentException("User ID must be positive");
        }
    }
}
