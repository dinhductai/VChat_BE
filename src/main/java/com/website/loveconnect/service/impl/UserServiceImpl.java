package com.website.loveconnect.service.impl;

//import com.cloudinary.utils.StringUtils;
import com.website.loveconnect.dto.request.UserCreateRequest;
import com.website.loveconnect.dto.request.UserUpdateRequest;
import com.website.loveconnect.dto.response.*;
import com.website.loveconnect.entity.*;
import com.website.loveconnect.enumpackage.AccountStatus;
        import com.website.loveconnect.enumpackage.RoleName;
import com.website.loveconnect.enumpackage.StatusReport;
import com.website.loveconnect.exception.*;
import com.website.loveconnect.mapper.UserInterestMapper;
import com.website.loveconnect.mapper.UserMapper;
import com.website.loveconnect.mapper.UserProfileMapper;
import com.website.loveconnect.mapper.UserRoleMapper;
import com.website.loveconnect.repository.*;
import com.website.loveconnect.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
        import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
        import org.springframework.stereotype.Service;
        import org.apache.commons.lang3.StringUtils;

        import java.sql.Timestamp;
import java.util.*;


@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class UserServiceImpl implements UserService {
    @PersistenceContext
    EntityManager entityManager;
    UserProfileMapper userProfileMapper;
    UserRepository userRepository;
    ModelMapper modelMapper;
    UserMapper userMapper;
    UserProfileRepository userProfileRepository;
    PhotoRepository photoRepository;
    UserRoleRepository userRoleRepository;
    RoleRepository roleRepository;
    InterestRepository interestRepository;
    UserInterestRepository userInterestRepository;
    UserInterestMapper userInterestMapper;
    UserRoleMapper userRoleMapper;
    ReportRepository reportRepository;
    //hàm lấy tất cả thông tin người dùng
    @Override
    public Page<ListUserResponse> getAllUser(int page, int size) {
        try {
            if (page < 0) { page = 0; }
            if (size < 1) { size = 10; }
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

        Report report = reportRepository.findFirstByReported(userById)
                .orElseThrow(()-> new ReportNotFoundException("Report not found"));
        userById.setAccountStatus(AccountStatus.BLOCKED);
        userRepository.save(userById);
        report.setStatusReport(StatusReport.RESOLVED);
        report.setReviewDate(new Timestamp(System.currentTimeMillis()));
        reportRepository.save(report);
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
    //CHƯA HOÀN THIỆN
    @Override
    public UserUpdateResponse updateUser(Integer idUser,UserUpdateRequest userRequest) {
        try{
            validateUserId(idUser);
            User user = userRepository.findById(idUser)
                    .orElseThrow(()-> new UserNotFoundException("User with id "+ idUser + " not found"));
            user.setPhoneNumber(userRequest.getPhoneNumber());
            user.setEmail(userRequest.getEmail());
            user.setAccountStatus(userRequest.getAccountStatus());

            Optional<UserProfile> userProfileOptional = userProfileRepository.findByUser_UserId(idUser);
            UserProfile userProfile = userProfileOptional
                    .orElseThrow(()->new UserNotFoundException("User with id "+ idUser + " not found"));
            userProfile.setFullName(userRequest.getFullName());
            userProfile.setBirthDate(userRequest.getBirthDate());
            userProfile.setGender(userRequest.getGender());
            userProfile.setLocation(userRequest.getLocation());
            userProfile.setDescription(userRequest.getDescription());

            userRepository.save(user);
            userProfileRepository.save(userProfile);


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
    public void deleteUser(Integer idUser) {
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
    public void createUser(UserCreateRequest userRequest) {
        // kiểm tra xem email hoặc password có null không,vì là thuộc tính bắt buộc
        if (StringUtils.isBlank(userRequest.getEmail()) || StringUtils.isBlank(userRequest.getPassword())) {
            log.error("Email or Password is blank in create user request");
            throw new IllegalArgumentException("Email or Password cannot be blank");
        }
        //kiểm tra xem có trùng email ai không
        boolean existingUser = false;
        existingUser = userRepository.existsByEmail(userRequest.getEmail());
        //đảm bảo email chưa tồn tại thì cho tạo tài khoản mới
        if(!existingUser) {
            try{
            //lưu user mới
            User newUser = userRepository.save(userMapper.toCreateNewUser(userRequest));
                //lưu thông tin user profile
            userProfileRepository.save(userProfileMapper.toCreateNewUserProfile(userRequest,newUser));
            //lấy danh sách các interest được chọn
            List<Interest> listInterest = interestRepository.getByInterestNameIn(userRequest.getInterestName());
            //lưu các interest vào user mới
            userInterestRepository.saveAll(userInterestMapper.toAttachUserInterest(listInterest,newUser));
            //lấy role USER trong database
            Role role = roleRepository.findByRoleName(RoleName.USER)
                    .orElseThrow(()-> new RoleNotFoundException("Not found role user"));
            //set role USER cho user mới
            userRoleRepository.save(userRoleMapper.toAttachUserRole(newUser,role));
            } catch (DataAccessException dae) {
                throw new DataAccessException("Failed to save new user");
            }
        }else throw new EmailAlreadyInUseException("Email was already in use");
    }

    //lấy danh sách người dùng bằng filter
    @Override
    public Page<ListUserResponse> getAllUserByFilters(String status, String gender, String sortType, String keyword, int page, int size) {
        try {
            if (page < 0) { page = 0; }
            if (size < 1) { size = 10; }
            Pageable pageable = PageRequest.of(page,size);
            Page<Tuple> listUserFindByFilters = userRepository.getAllUserByFilters(status,gender,sortType,keyword,pageable);
            if (listUserFindByFilters.isEmpty()) {
                return Page.empty();
            }
            //map dữ liệu chuyển từ tuple qua dto
            Page<ListUserResponse> mappedUserResponse = listUserFindByFilters.map(userMapper::toUserViewByFilters);
            return mappedUserResponse;
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
    public Page<UserSearchResponse> getAllUserByKeyword(String keyword, int page,int size) {
//        if (keyword == null || keyword.trim().isEmpty()) {
//            throw new IllegalArgumentException("Keyword cannot be empty or null");
//        }
        Pageable pageable = PageRequest.of(page,size);
        return userRepository.getAllUserByKeyword(keyword, pageable)
                .map(userMapper::toProfileDetailResponse);
    }

    @Override
    public Page<UserAndPhotosResponse> getAllUsersAndPhotos( int page, int size, Integer userId) {
        validateUserId(userId);
        try {
            User user =  userRepository.findById(userId)
                    .orElseThrow(()-> new UserNotFoundException("User with id "+ userId + " not found"));
            UserProfile userProfile = userProfileRepository.findByUser_UserId(userId)
                    .orElseThrow(()-> new UserNotFoundException("User with id "+ userId + " not found"));
            if(user != null) {
                Pageable pageable = PageRequest.of(page,size);
                Page<Tuple> listUserAndPhotos = userRepository.getAllUserAndPhotos(String.valueOf(userProfile.getLookingFor()),pageable);
                return listUserAndPhotos.map(userMapper::toUserAndPhotosResponse);
            }

        }catch (DataAccessException da){
            da.getMessage();
            throw new DataAccessException("Cannot access database");

        }
        return null;
    }

    @Override
    public Page<UserFriendResponse> getAllFriendsMatched(int page, int size, Integer userId) {
        try{
            boolean userExists = userRepository.existsById(userId);
            if(userExists) {
                Pageable pageable = PageRequest.of(page,size);
                return userRepository.getAllFriendMatched(userId,pageable)
                        .map(userMapper::toUserFriendResponse);
            }
            else{
                throw new UserNotFoundException("User with id "+ userId + " not found");
            }

        }catch (DataAccessException da){
            throw new DataAccessException("Cannot access database");
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Page<UserFriendResponse> getFiendsFriendsMatched(int page, int size, Integer userId) {
        try{
            boolean userExists = userRepository.existsById(userId);
            if(userExists) {
                Pageable pageable = PageRequest.of(page,size);
                return userRepository.getFriendsFriends(userId,pageable)
                        .map(userMapper::toUserFriendResponse);
            }
            else{
                throw new UserNotFoundException("User with id "+ userId + " not found");
            }

        }catch (DataAccessException da){
            throw new DataAccessException("Cannot access database");
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Page<UserFriendResponse> getRandomFriends(int page, int size, Integer userId) {
        try{
            boolean userExists = userRepository.existsById(userId);
            if(userExists) {
                Pageable pageable = PageRequest.of(page,size);
                return userRepository.getRandomFriends(userId,pageable)
                        .map(userMapper::toUserFriendResponse);
            }
            else{
                throw new UserNotFoundException("User with id "+ userId + " not found");
            }

        }catch (DataAccessException da){
            throw new DataAccessException("Cannot access database");
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Page<UserFriendResponse> getAllFriendsPending(int page, int size, Integer userId) {
        try{
            boolean userExists = userRepository.existsById(userId);
            if(userExists) {
                Pageable pageable = PageRequest.of(page,size);
                return userRepository.getAllFriendPending(userId,pageable)
                        .map(userMapper::toUserFriendResponse);
            }
            else{
                throw new UserNotFoundException("User with id "+ userId + " not found");
            }

        }catch (DataAccessException da){
            throw new DataAccessException("Cannot access database");
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }    }

    private void validateUserId(int idUser)  {
        if (idUser <= 0) {
            throw new IllegalArgumentException("User ID must be positive");
        }
    }
}
