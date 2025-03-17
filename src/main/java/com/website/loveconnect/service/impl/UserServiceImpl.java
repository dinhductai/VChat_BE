package com.website.loveconnect.service.impl;

import com.website.loveconnect.dto.response.UserResponse;
import com.website.loveconnect.enumpackage.AccountStatus;
import com.website.loveconnect.repository.UserRepository;
import com.website.loveconnect.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    
    UserRepository userRepository;
    ModelMapper modelMapper;

    @Override
    public Page<UserResponse> getAllUser(int page, int size) {
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
            return listUserObject.map(obj -> UserResponse.builder()
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
}
