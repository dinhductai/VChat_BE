package com.website.loveconnect.service.impl;

import com.website.loveconnect.dto.response.ListUserResponse;
import com.website.loveconnect.dto.response.UserResponse;
import com.website.loveconnect.enumpackage.AccountStatus;
import com.website.loveconnect.enumpackage.Gender;
import com.website.loveconnect.repository.UserRepository;
import com.website.loveconnect.service.UserService;
import jakarta.persistence.NoResultException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.ArrayList;
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
    public UserResponse getUserById(int idUser) {
        try {
            Object[] user = userRepository.getUserById(idUser);
            if (user.length == 0) {
                return null;
            }
            return UserResponse.builder()
                    .userId((Integer) user[0])
                    .photoUrl((String) user[1])
                    .fullName((String) user[2])
                    .email((String) user[3])
                    .gender(Gender.valueOf((String) user[4]))
                    .location((String) user[5])
                    .description((String) user[6])
                    //chuyển chuỗi thành list
                    .interestName(user[7] != null ?
                            Arrays.asList(((String) user[7]).split(", ")) : List.of())
                    .registrationDate((Timestamp) user[8])
                    .birthDate((Date) user[9])
                    .phoneNumber((String) user[10])
                    .accountStatus(AccountStatus.valueOf((String) user[11]))
                    .build();
        } catch (IllegalArgumentException e) {
            log.error("Tham số không hợp lệ: {}", e.getMessage());
            throw new RuntimeException("Tham số không hợp lệ: " + e.getMessage());
        }
        catch (DataAccessException e) {
            log.error("Lỗi truy vấn cơ sở dữ liệu: {}", e.getMessage());
            return null;
        }
        catch (NoResultException e){
            log.error("Lỗi không tìm thấy kết quả khi truy vấn: {}", e.getMessage());
            throw new RuntimeException("Lỗi không tìm thấy dữ liệu");
        }
        catch (Exception e) {
            log.error("Lỗi không xác định: {}", e.getMessage(), e);
            throw new RuntimeException("Đã xảy ra lỗi không xác định: " + e.getMessage());
        }
    }
}
