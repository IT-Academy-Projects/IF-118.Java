package com.softserve.itacademy.service;



import com.softserve.itacademy.entity.User;
import org.springframework.transaction.annotation.Transactional;
import com.softserve.itacademy.response.UserResponse;
import java.util.List;


public interface UserService {

    void updateDisabled(Integer id, Boolean disabled);

    @Transactional
    void addUser(User user);

    UserResponse findById(Integer id);

    List<UserResponse> findAll();

    User getById(Integer id);

    void updateProfileInfo(Integer id, String name, String email);

}
