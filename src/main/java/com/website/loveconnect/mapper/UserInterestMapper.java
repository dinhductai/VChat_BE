package com.website.loveconnect.mapper;

import com.website.loveconnect.entity.Interest;
import com.website.loveconnect.entity.User;
import com.website.loveconnect.entity.UserInterest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class UserInterestMapper {

    //hàm set các interest vào cho một user
    public List<UserInterest> toAttachUserInterest(List<Interest> listInterest, User user) {
        List<UserInterest> listUserInterest = new ArrayList<>();
        for(Interest interest : listInterest){
            UserInterest userInterest = UserInterest.builder()
                    .interest(interest) //một trong những interest được truyền vào
                    .user(user) // lưu thẳng user mới tạo vào
                    .build();
            listUserInterest.add(userInterest);
        }
        return listUserInterest;
    }
    public UserInterest toAttachOneUserInterest(Interest interest, User user) {
            UserInterest userInterest = UserInterest.builder()
                    .interest(interest) //một trong những interest được truyền vào
                    .user(user) // lưu thẳng user mới tạo vào
                    .build();
        return userInterest;
    }
}
