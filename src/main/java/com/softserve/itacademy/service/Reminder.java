package com.softserve.itacademy.service;

import com.softserve.itacademy.entity.User;

import java.util.List;

public interface Reminder {
    void remind (List<User> recipients);
}
