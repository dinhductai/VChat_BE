package com.website.loveconnect.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "interests")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Interest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interest_id")
    private Integer interestId;

    @Column(name = "interest_name",nullable = false,unique = true)
    private String interestName;

    @Column(name = "category")
    private String category;

    @JsonIgnore
    @OneToMany(mappedBy = "interest", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UserInterest> userInterests;
}
