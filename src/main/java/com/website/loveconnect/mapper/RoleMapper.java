//package com.website.loveconnect.mapper;
//
//import com.website.loveconnect.dto.response.RoleUserResponse;
//import jakarta.persistence.Tuple;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Component
//public class RoleMapper {
//    public List<RoleUserResponse> toRoleUserResponse(List<Tuple> listRoleUser) {
//        List<RoleUserResponse> userRoleDTOMapped = listRoleUser.stream()
//                .map(tuple -> new RoleUserResponse(
//                        tuple.get("roleName", String.class)
//                )).toList();
//        return userRoleDTOMapped;
//    }
//}
