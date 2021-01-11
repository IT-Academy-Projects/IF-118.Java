package com.softserve.itacademy.service;



import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.projection.IdNameTupleProjection;
import com.softserve.itacademy.projection.UserFullTinyProjection;
import org.springframework.transaction.annotation.Transactional;
import com.softserve.itacademy.response.UserResponse;
import java.util.List;


public interface UserService {

    void updateDisabled(Integer id, Boolean disabled);

    UserFullTinyProjection findById(Integer id);

    IdNameTupleProjection findUserNameById(Integer id);

    List<UserResponse> findAll();

    User getById(Integer id);

    void updateProfileInfo(Integer id, String name, String email);
}
