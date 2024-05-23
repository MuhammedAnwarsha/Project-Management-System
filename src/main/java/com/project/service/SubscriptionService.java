package com.project.service;

import com.project.modal.PlanType;
import com.project.modal.Subscription;
import com.project.modal.User;

public interface SubscriptionService {

    Subscription createSubscription(User user);

    Subscription getUsersSubscription(Long userId)throws Exception;

    Subscription upgradeSubscription(Long userId, PlanType planType);

    boolean isValid(Subscription subscription);
}
