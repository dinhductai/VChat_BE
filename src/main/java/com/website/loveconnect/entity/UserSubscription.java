package com.website.loveconnect.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.website.loveconnect.enumpackage.PaymentStatus;
import com.website.loveconnect.enumpackage.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Table(name = "user_subscriptions")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_id")
    private Integer subscriptionId;

    @Column(name = "start_date",nullable = false)
    private Timestamp startDate;

    @Column(name = "end_date",nullable = false)
    private Timestamp endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status",nullable = false)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_status")
    private SubscriptionStatus subscriptionStatus = SubscriptionStatus.ACTIVE;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "plan_id",nullable = false)
    private SubscriptionPlan subscriptionPlan;


}
