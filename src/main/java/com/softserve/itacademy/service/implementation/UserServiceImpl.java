package com.softserve.itacademy.service.implementation;

import static com.softserve.itacademy.config.Constance.USER_EMAIL_NOT_FOUND;
import static com.softserve.itacademy.config.Constance.USER_ID_NOT_FOUND;
import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.exception.FileProcessingException;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.exception.OperationNotAllowedException;
import com.softserve.itacademy.projection.IdNameTupleProjection;
import com.softserve.itacademy.projection.UserFullTinyProjection;
import com.softserve.itacademy.repository.UserRepository;
import com.softserve.itacademy.response.UserResponse;
import com.softserve.itacademy.service.InvitationService;
import com.softserve.itacademy.service.UserService;
import com.softserve.itacademy.service.converters.UserConverter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;
    private final InvitationService invitationService;

    public UserServiceImpl(UserRepository userRepository, UserConverter userConverter, PasswordEncoder passwordEncoder, InvitationService invitationService) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
        this.passwordEncoder = passwordEncoder;
        this.invitationService = invitationService;
    }

    @Override
    public UserFullTinyProjection findById(Integer id) {
        return userRepository.findProjectedById(id).orElseThrow(() -> new NotFoundException(USER_EMAIL_NOT_FOUND));
    }

    @Override
    public IdNameTupleProjection findUserNameById(Integer id) {
        return userRepository.findUserProjectedById(id).orElseThrow(() -> new NotFoundException(USER_EMAIL_NOT_FOUND));
    }

    @Override
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(userConverter::of).collect(Collectors.toList());
    }

    @Override
    public void updateDisabled(Integer id, Boolean disabled) {
        if (userRepository.updateDisabled(id, disabled) == 0) {
            throw new NotFoundException(USER_ID_NOT_FOUND);
        }
    }



    @Override
    public User getById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException(USER_ID_NOT_FOUND));
    }

    @Override
    public void updateProfileInfo(Integer id, String name, String email) {
        if (userRepository.updateProfileInfo(id, name, email) == 0)
            throw new NotFoundException(USER_ID_NOT_FOUND);
    }

    @Override
    public List<UserResponse> findByGroupId(Integer id) {
        return userRepository.findByGroupId(id).stream()
                .map(userConverter::of)
                .collect(Collectors.toList());
    }

    @Override
    public void changePass(Integer id, String oldPass, String newPass) {

        if (passwordEncoder.matches(oldPass, getById(id).getPassword())) {
            userRepository.updatePass(id, passwordEncoder.encode(newPass));
        } else throw new OperationNotAllowedException("wrong current password");
    }

    @Override
    public void deleteInvitation(Integer id, Integer invitationId) {
        invitationService.delete(invitationId);
        userRepository.deleteInvitation(id);
    }

    @Override
    public void createAvatar(MultipartFile file, Integer id) {
        Optional<User> user = userRepository.findById(id);
        try {
            if (user.isPresent()) {
                user.get().setAvatar(file.getBytes());
                userRepository.save(user.get());
            } else {
                throw new NotFoundException(USER_ID_NOT_FOUND);
            }
        } catch (IOException e) {
            throw new FileProcessingException("Cannot get bytes from avatar file for course");
        }
    }

    @Override
    public byte[] getAvatar(Integer id) {
        return userRepository.getAvatarById(id);
    }

    @Override
    public UserResponse getUserById(Integer id) {
        return userConverter.of(getById(id));
    }


}
