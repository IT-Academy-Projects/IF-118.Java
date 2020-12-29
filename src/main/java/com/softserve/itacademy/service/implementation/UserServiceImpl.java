package com.softserve.itacademy.service.implementation;

import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.request.UserRequest;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.repository.UserRepository;
import com.softserve.itacademy.response.UserResponse;
import com.softserve.itacademy.service.UserService;
import com.softserve.itacademy.service.converters.UserConverter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;

    @Override
    public UserResponse findById(Integer id) {
        return userConverter.convertToDto(userRepository.findById(id).orElseThrow(NotFoundException::new));
    }

    @Override
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(userConverter::convertToDto).collect(Collectors.toList());
    }

    @Override
    public void updateDisabled(Integer id, boolean disabled) {
        if (userRepository.updateDisabled(id, disabled) == 0) {
            throw new NotFoundException();
        }
    }

    private User getById(Integer id) {
        return userRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public void updateProfileInfo(Integer id, String name, String email) {
        if (userRepository.updateProfileInfo(id, name, email) == 0) throw new NotFoundException();
    }
}
