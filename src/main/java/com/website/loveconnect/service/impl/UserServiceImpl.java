package com.website.loveconnect.service.impl;

import com.website.loveconnect.dto.request.InterestDTO;
import com.website.loveconnect.dto.request.UserUpdateRequest;
import com.website.loveconnect.dto.response.ListUserResponse;
import com.website.loveconnect.dto.response.UserUpdateResponse;
import com.website.loveconnect.dto.response.UserViewResponse;
import com.website.loveconnect.entity.*;
import com.website.loveconnect.enumpackage.AccountStatus;
import com.website.loveconnect.exception.UserNotFoundException;
import com.website.loveconnect.mapper.UserMapper;
import com.website.loveconnect.repository.*;
import com.website.loveconnect.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class UserServiceImpl implements UserService {
    @PersistenceContext
    private EntityManager entityManager;
    UserRepository userRepository;
    ModelMapper modelMapper;
    UserMapper userMapper;
    UserProfileRepository userProfileRepository;
    PhotoRepository photoRepository;

    InterestRepository interestRepository;
    UserInterestRepository userInterestRepository;


    //hàm lấy tất cả thông tin người dùng
    @Override
    public Page<ListUserResponse> getAllUser(int page, int size) {
        try {
            if (page < 0) {
                page = 0;
            }
            if (size < 1) {
                size = 10;
            }
            //set thông số page
            Pageable pageable = PageRequest.of(page, size);
            Page<Object[]> listUserObject = userRepository.getAllUser(pageable);
            //map dữ liệu
            return listUserObject.map(obj -> ListUserResponse.builder()
                    .userId((Integer) obj[0])
                    .fullName((String) obj[1])
                    .email((String) obj[2])
                    .phone((String) obj[3])
                    .registrationDate((Timestamp) obj[4])
                    .accountStatus(AccountStatus.valueOf((String) obj[5]))
                    .build());

        } catch (IllegalArgumentException e) {
            log.error("Tham số không hợp lệ: {}", e.getMessage());
            throw new RuntimeException("Tham số không hợp lệ: " + e.getMessage());
        }
        catch (DataAccessException e) {
            log.error("Lỗi truy vấn cơ sở dữ liệu: {}", e.getMessage());
            throw new RuntimeException("Lỗi truy vấn cơ sở dữ liệu");
        }
        catch (Exception e) {
            log.error("Lỗi không xác định: {}", e.getMessage(), e);
            throw new RuntimeException("Đã xảy ra lỗi không xác định: " + e.getMessage());
        }
    }



    @Override
    public UserViewResponse getUserById(int idUser) {
        try {
            validateUserId(idUser);
            return Optional.ofNullable(userRepository.getUserById(idUser))
                    .map(tuple -> userMapper.toUserViewResponse(tuple))
                    .orElseThrow(() -> new UserNotFoundException("User with id "+ idUser + " not found"));

        } catch (NoResultException | EmptyResultDataAccessException e) { // bắt lỗi user ko tồn tại trước 404
            log.info("No result found for user ID {}: {}", idUser, e.getMessage());
            throw new UserNotFoundException("User with ID " + idUser + " not found");
        } catch (DataAccessException e) { // không thể truy cập database 500
            log.error("Database access error for user ID {}: {}", idUser, e.getMessage());
            throw new DataAccessException("Failed to access database: " + e.getMessage()) {
            };
        } catch (IllegalArgumentException e) {
            log.error("Invalid argument for user ID {}: {}", idUser, e.getMessage());
            throw new IllegalArgumentException("Invalid data format: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error for user ID {}: {}", idUser, e.getMessage());
            throw new RuntimeException("An unexpected error occurred while retrieving user");
        }
    }

    //hàm block người dùng bằng id
    @Override
    public void blockUser(int idUser) {
        if(idUser <= 0){
            log.error("Invalid user ID: {}", idUser);
            throw new IllegalArgumentException("User ID must be a positive integer");
        }
        User userById = userRepository.findById(idUser)
                .orElseThrow(()-> new UserNotFoundException("User with ID " + idUser + " not found"));
        if(userById.getAccountStatus() == AccountStatus.BLOCKED){
            log.info("User with ID {} is already blocked", idUser);
            return;
        }
        userById.setAccountStatus(AccountStatus.BLOCKED);
        userRepository.save(userById);
        log.info("User with ID {} has been blocked successfully", idUser);
    }

    //hàm gỡ block người dùng bằng id
    @Override
    public void unblockUser(int idUser) {
        if(idUser <= 0){
            log.error("Invalid user ID: {}", idUser);
            throw new IllegalArgumentException("User ID must be a positive integer");
        }
        User userById = userRepository.findById(idUser)
                .orElseThrow(()-> new UserNotFoundException("User with ID " + idUser + " not found"));
        if(userById.getAccountStatus() == AccountStatus.BLOCKED){
            userById.setAccountStatus(AccountStatus.ACTIVE);
            userRepository.save(userById);
            log.info("User with ID {} has been active successfully", idUser);
        }
        else{
            log.info("User with ID {} is already active", idUser);
        }
    }


    // hàm lấy thông tin người dùng bằng id để hiển thị lên giao diện sửa 1 người dùng
    @Override
    public UserUpdateResponse getUserUpdateById(int idUser) {
        try {
            validateUserId(idUser);
            return Optional.ofNullable(userRepository.getUserForUpdateById(idUser))
                    .map(tuple -> userMapper.toUserUpdateResponse(tuple))
                    .orElseThrow(() -> new UserNotFoundException("User with id "+ idUser + " not found"));

        } catch (NoResultException | EmptyResultDataAccessException e) { // bắt lỗi user ko tồn tại trước 404
            log.info("No result found for user ID {}: {}", idUser, e.getMessage());
            throw new UserNotFoundException("User with ID " + idUser + " not found");
        } catch (DataAccessException e) { // không thể truy cập database 500
            log.error("Database access error for user ID {}: {}", idUser, e.getMessage());
            throw new DataAccessException("Failed to access database: " + e.getMessage()) {
            };
        } catch (IllegalArgumentException e) {
            log.error("Invalid argument for user ID {}: {}", idUser, e.getMessage());
            throw new IllegalArgumentException("Invalid data format: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error for user ID {}: {}", idUser, e.getMessage());
            throw new RuntimeException("An unexpected error occurred while retrieving user");
        }
    }

    //hàm cập nhật thông tin người dùng
    @Override
    public UserUpdateResponse updateUser(Integer idUser,UserUpdateRequest userRequest) {
        try{
            validateUserId(idUser);
            User user = userRepository.findById(idUser)
                    .orElseThrow(()-> new UserNotFoundException("User with id "+ idUser + " not found"));
            user.setPhoneNumber(userRequest.getPhoneNumber());
            user.setEmail(userRequest.getEmail());
            user.setAccountStatus(userRequest.getAccountStatus());

            Optional<UserProfile> userProfileOptional = userProfileRepository.findByUser_Id(idUser);
            UserProfile userProfile = userProfileOptional.orElseThrow(()->new RuntimeException("User with id "+ idUser + " not found"));
            userProfile.setFullName(userRequest.getFullName());
            userProfile.setBirthDate(userRequest.getBirthDate());
            userProfile.setGender(userRequest.getGender());
            userProfile.setLocation(userRequest.getLocation());
            userProfile.setDescription(userRequest.getDescription());

            Optional<Photo> photoOptional = photoRepository.findOneByUserId(idUser);
            Photo photo = photoOptional.orElseThrow(()-> new RuntimeException("This user not have profile image"));
            photo.setPhotoUrl(userRequest.getPhotoUrl());

            userRepository.save(user);
            userProfileRepository.save(userProfile);
            photoRepository.save(photo);
            //chưa cập nhật dữ liệu ở interest

            //trả về dữ liệu mới cho giao diện
            //sử dụng mapper
            return userMapper.toUserUpdateResponseBuilder(idUser, userRequest);

        } catch (NoResultException | EmptyResultDataAccessException e) { // bắt lỗi user ko tồn tại trước    404
            log.info("No result found for user ID {}: {}", idUser, e.getMessage());
            throw new UserNotFoundException("User with ID " + idUser + " not found");
        } catch (DataAccessException e) { // không thể truy cập database 500
            log.error("Database access error for user ID {}: {}", idUser, e.getMessage());
            throw new DataAccessException("Failed to access database: " + e.getMessage()) {
            };
        } catch (IllegalArgumentException e) {
            log.error("Invalid argument for user ID {}: {}", idUser, e.getMessage());
            throw new IllegalArgumentException("Invalid data format: " + e.getMessage());
        }

    }

    @Override
    public void deleteUser(int idUser) {
        validateUserId(idUser);
        User user = userRepository.findById(idUser)
                .orElseThrow(()-> new UserNotFoundException("User with id "+ idUser + " not found"));
        if(user.getAccountStatus() != AccountStatus.DELETED){
            //chỉ đổi trạng thái tài khoản chứ ko xóa hoàn toàn
            user.setAccountStatus(AccountStatus.DELETED);
            userRepository.save(user);
        }else{
            log.info("User with Id {} is already blocked", idUser);
        }

    }

    @Override
    public void addInterest(int idUser, InterestDTO interestDTO) {
        try {
            User user = userRepository.findById(idUser).orElseThrow(() -> new UserNotFoundException("User with id "+ idUser + " not found"));

            Interest interest = Interest.builder()
                    .interestName(interestDTO.getInterestName())
                    .category(interestDTO.getCategory())
                    .build();

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
        User user = userRepository.findById(idUser).orElseThrow(() -> new UserNotFoundException("User with id "+ idUser + " not found"));
        Interest interest = interestRepository.findById(idInterest).orElseThrow(() -> new UserNotFoundException("Interest with id "+ idInterest + " not found"));
        interestRepository.delete(interest);

        log.info("Xóa thành công sở thích có idInterest : {}" , idInterest);
    }

    @Override
    public void updateInterest(int idInterest, int idUser, InterestDTO interestDTO) {
        try {
            User user = userRepository.findById(idUser).orElseThrow(() -> new UserNotFoundException("User with id "+ idUser + " not found"));
            // Xác định xem User có ID idUser có sở thích có ID idInterest không
            UserInterest ui = userInterestRepository.findUserInterestWithIdUserAndIdInterest(idInterest , idUser).orElseThrow(() -> new UserNotFoundException("Not found UserInterest need find !!! "));;
            Interest interest = interestRepository.findById(idInterest).orElseThrow(() -> new UserNotFoundException("Interest with id "+ idInterest + " not found"));
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
        List<Interest> interestList = interestRepository.getAllInterest(idUser);
        if (interestList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Người dùng chưa có sở thích nào.");
        }
        log.info("Danh sách sở thích đã được lấy thành công");
        return interestList;
    }

    private void validateUserId(int idUser) {
        if (idUser <= 0) {
            throw new IllegalArgumentException("User ID must be positive");
        }
    }
}
