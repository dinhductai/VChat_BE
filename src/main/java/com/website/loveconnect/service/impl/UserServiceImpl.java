package com.website.loveconnect.service.impl;

import com.website.loveconnect.dto.response.ListUserResponse;
import com.website.loveconnect.dto.response.UserResponse;
import com.website.loveconnect.enumpackage.AccountStatus;
import com.website.loveconnect.enumpackage.Gender;
import com.website.loveconnect.repository.UserRepository;
import com.website.loveconnect.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    
    UserRepository userRepository;
    ModelMapper modelMapper;

    @Override
    public Page<ListUserResponse> getAllUser(int page, int size) {
        try {
            if (page < 0) {
                page = 0;
            }
            if (size < 1) {
                size = 10;
            }
            //set so lieu page
            Pageable pageable = PageRequest.of(page, size);
            Page<Object[]> listUserObject = userRepository.getAllUser(pageable);
            //map du lieu
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
    public UserResponse getUserById(int idUser) {
        Object userById = userRepository.getUserById(idUser);
        if(userById == null) return null;
        else {
            Object[] row = (Object[]) userById;

            return UserResponse.builder()
                    .userId(((Number) row[0]).intValue())  // user_id
                    .photoUrl((String) row[1])         // photo_url
                    .fullName((String) row[2])         // full_name
                    .email((String) row[3])            // email
                    .gender(Gender.valueOf((String) row[4])) // Chuyển String → Enum Gender
                    .location((String) row[5])         // location
                    .description((String) row[6])      // description
                    .interestName(row[7] != null ? Arrays.asList(((String) row[7]).split(", ")) : List.of()) // Chuyển chuỗi → List<String>
                    .registrationDate((Timestamp) row[8]) // registration_date
                    .birthDate((Date) row[9])          // birthdate
                    .phoneNumber((String) row[10])     // phone_number
                    .accountStatus(AccountStatus.valueOf((String) row[11])) // Chuyển String → Enum AccountStatus
                    .build();
        }
    }
}
