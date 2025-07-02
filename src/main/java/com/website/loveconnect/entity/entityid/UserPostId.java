package com.website.loveconnect.entity.entityid;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPostId implements Serializable {
    private Integer user;
    private Integer post;
}
