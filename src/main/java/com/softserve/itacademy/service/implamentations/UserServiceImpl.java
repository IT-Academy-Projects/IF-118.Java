package com.softserve.itacademy.service.implamentations;

import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.dto.UserDto;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.repository.UserRepository;
import com.softserve.itacademy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDto> findAll() {
        return userRepository.findAll().stream().map(UserDto::create).collect(Collectors.toList());
    }


    @Override
    public void updateDisabled(Integer id, Boolean disabled) {
        User user = getById(id);
        user.setDisabled(disabled);
        userRepository.save(user);
    }

    @Override
    public User findById(Integer id) {
        return getById(id);
    }

    private User getById(Integer id) {
        return userRepository.findById(id).orElseThrow(NotFoundException::new);
    }
}
