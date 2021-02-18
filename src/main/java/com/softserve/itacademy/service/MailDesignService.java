package com.softserve.itacademy.service;

import com.softserve.itacademy.service.implementation.MailDesignServiceImpl;

import java.util.Map;

public interface MailDesignService {
    void designAndQueue(String email, String topic, Map<String, Object> mailContext, MailDesignServiceImpl.MailType mailType);
}
